//
// This script expects to be running in the context of a Pull Request trigger so that it can communicate with the
//  user via the Pull Request, Comments and Labels.
//
module.exports = async ({context, core, github}) => {
    const label = context.payload.label.name.toLowerCase();

    //TODO remove this
    console.log('----- Context');
    console.log(JSON.stringify(context, null, 2));

    const status = await github.repos.getCombinedStatusForRef({
        ...context.repo,
        ref: context.payload.pull_request.head.sha
    }).then(status => {
        if (status.data.statuses) {
            return status.data.statuses.filter(status => {
                return status.context === 'Container Image Published'
            });
        }
        return null;
    });

    //TODO remove this
    console.log('----- Combined Statuses');
    console.log(JSON.stringify(status, null, 2));

    if (!status || status.length === 0) {
        await postNoContainerStatus(context, github);
    } else if (status.length === 1) {
        await postDeploymentComment(context, core, github, label, status[0]);
    } else {
        // Assuming first the last is the latest, this should not happen in practice as this is for a single commit
        await postDeploymentComment(context, core, github, label, status[status.length - 1]);
    }
}

async function postDeploymentComment(context, core, github, label, status) {
    const containerParts = status.description.split(' ')
        , containerRegistry = containerParts[1]
        , containerImageTag = containerParts[0]
        , environmentRegexResult = /deploy to (.*)/.exec(label)
    ;

    core.setOutput('container_image_tag', containerImageTag);
    core.setOutput('container_registry', containerRegistry);

    const commentBody = `
👋 @${context.actor}, request for ${label} received.

Starting Deployment:
  * Container: _${containerParts[0]}_
  * Environment: _${environmentRegexResult[1]}_
`
    await github.issues.createComment({
        issue_number: context.issue.number,
        owner: context.repo.owner,
        repo: context.repo.repo,
        body: commentBody,
    });
}

async function postNoContainerStatus(context, github) {
    await github.issues.createComment({
        issue_number: context.issue.number,
        owner: context.repo.owner,
        repo: context.repo.repo,
        body: `⚠️ Failed to trigger environment deployment request as missing container status check on commit`,
    });
}
