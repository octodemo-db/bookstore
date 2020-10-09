provider kubernetes {
  version = "=1.13.2"
}

variable "app_name" {
  default = "bookstore"
}

variable "namespace" {
  default = "bookstore-terraform"
}

resource "kubernetes_namespace" "bookstore" {
  metadata {
    name = var.namespace
    # annotations {
    #   name = "bookstore"
    # }
  }
}

module "bookstore" {
  source = "./bookstore"

  app_namespace = kubernetes_namespace.bookstore.metadata.0.name
  app_name      = var.app_name
  app_port      = 8080

  #TODO container, and version
}

output application_url {
  value       = "http://${module.bookstore.app_service_ip}"
  description = "Application Service URL"
}

output app_service_name {
  value       = module.bookstore.app_service_name
  description = "Application Service name"
}