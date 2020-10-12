//
// This script expects to be running in the context of a Pull Request trigger so that it can communicate with the
//  user via the Pull Request, Comments and Labels.
//
module.exports = async ({context, core, github}) => {
    const label = context.payload.label.name.toLowerCase();

    //TODO remove this
    console.log('----- Context');
    console.log(JSON.stringify(context, null, 2));

    const statuses = await github.repos.getCombinedStatusForRef({
        ...context.repo,
        ref: context.payload.pull_request.head.sha
    }).then(status => {
        if (status.data.statuses) {
            const statusToMatch = [
                'Container Image Published - App',
                'Container Image Published - Database',
            ]

            return status.data.statuses.filter(status => {
                return statusToMatch.indexOf(status.context) > -1;
            });
        }
        return null;
    });

    //TODO remove this
    console.log('----- Combined Statuses');
    console.log(JSON.stringify(statuses, null, 2));

    if (!statuses || statuses.length === 0) {
        await postNoContainerStatus(context, github);
    } else if (statuses.length === 2) {
        await postDeploymentComment(context, core, github, label, statuses);
    } else {
        // Assuming first the last is the latest, this should not happen in practice as this is for a single commit
        await postTooManyContainerStatus(context, github, statuses);
    }
}

// Extract the necessary details from the statuses to be able to create a deployment
async function postDeploymentComment(context, core, github, label, statuses) {
    const environmentRegexResult = /deploy to (.*)/.exec(label)
        , containers = []
    ;

    statuses.forEach(status => {
        const nameParts = status.context.split(' - ')
            , containerType = nameParts[1].toLowerCase()
            , containerParts = status.description.split(':')
            , image = containerParts[0]
            , version = containerParts[1]
            ;
        
        containers.push(`Container ${containerType}: _${image}_:__${version}__`);
        core.setOutput(`${containerType}_container_image`, image);
        core.setOutput(`${containerType}_container_version`, version);
    });

    const commentBody = `
👋 Request for ${label} received...

Starting Deployment of:
* ${containers.join('\n')}
* Environment: _${environmentRegexResult[1]}_
`
    await github.issues.createComment({
        ...context.repo,
        issue_number: context.issue.number,
        body: commentBody,
    });
}

async function postNoContainerStatus(context, github) {
    await github.issues.createComment({
        ... context.repo,
        issue_number: context.issue.number,
        body: `⚠️ Failed to trigger environment deployment request as missing container status check on commit`,
    });
}

async function postTooManyContainerStatus(context, github, statuses) {
    await github.issues.createComment({
        ... context.repo,
        issue_number: context.issue.number,
        body: `⚠️ Failed to trigger environment deployment request found too many statuses on the commit, expected two but got:\n${JSON.stringify(statuses)}`,
    });
}