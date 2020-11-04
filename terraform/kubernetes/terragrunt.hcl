remote_state {
    backend         = "gcs"

    generate = {
        path        = "backend.generated.tf"
        if_exists   = "overwrite_terragrunt"
    }

    config = {
        bucket      = "octodemo-db-bookstore"
        prefix      = "kube-azure-${get_env("TF_VAR_ENVIRONMENT", "integration")}.terraform.state"
    }
}
