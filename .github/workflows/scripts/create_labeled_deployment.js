//
// Creates a Deployment using the provided data in a PR and labels associated with it
//
const LABEL_TO_ENVIRONMENT = {
    'Deploy to QA': 'qa',
    'Deploy to Staging': 'staging',
    'Deploy to Test': 'test'
};

module.exports = async (context, github, containerRegistry, containerImage) => {
    const label = context.payload.label.name
        , head = context.payload.pull_request.head.ref //Branch name PR created from
        , isProduction = false
        , environment = LABEL_TO_ENVIRONMENT[label] || 'test'
    ;

    if (!containerRegistry) {
        throw new Error(`Container Registry was not provided.`)
    }

    if (!containerImage) {
        throw new Error(`Container Image was not provided.`)
    }

    // A deployment payload from the PR and Label data
    const deploymentPayload = {
        container_registry: containerRegistry,
        container_image: containerImage,
        environment: environment,
        ref: context.ref,
    };

    //TODO remove
    console.log(JSON.stringify(deploymentPayload, null, 2));

    await github.repos.createDeployment({
        owner: context.repo.owner,
        repo: context.repo.repo,
        ref: head,
        auto_merge: false,
        required_contexts: [],
        payload: JSON.stringify(deploymentPayload),
        environment: environment,
        description: `Deploy Pull Request to ${environment}`,
        transient_environment: !isProduction,
        production_environment: isProduction,
        mediaType: { previews: ["flash-preview", "ant-man"] }
    });
}