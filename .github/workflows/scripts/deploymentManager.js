module.exports = class DeploymentManager {

    constructor(context, github) {
        this.context = context;
        this.github = github;
    }

    async activateDeployment(deploymentId) {
        const github = this.github
            , context = this.context
            ;

        const deployment = await github.repos.getDeployment({
            ...context.repo,
            deployment_id: deploymentId
        }).then(resp => {
            return resp.data;
        });

        // Activate the deployment
        await github.repos.createDeploymentStatus({
            ...context.repo,
            deployment_id: deployment.id,
            state: 'success'
        });

        // Get all deployment for the specified environment
        const allDeployments = await github.paginate('GET /repos/:owner/:repo/deployments', {
            ...context.repo,
            environment: deployment.environment
        });

        // Inactivate any previous environments
        const promises = [];
        allDeployments.forEach(deployment => {

            // If this a previous deployment, check to see it is inactive and if not, deactivate it.
            if (deployment.id !== deploymentId) {

                promises.push(
                    github.repos.listDeploymentStatuses({
                        ...context.repo, 
                        deployment_id: deployment.id
                    })
                    .then(statuses => {
                        const currentStatus = statuses.data[0].state;

                        if (currentStatus !== 'inactive') {
                            return github.repos.createDeploymentStatus({
                                ...context.repo,
                                deployment_id: deployment.id,
                                state: 'inactive',
                                mediaType: { previews: ['flash', 'ant-man'] }
                            });
                        }
                    })
                );
            }
        });

        await Promise.all(promises);
    }
}