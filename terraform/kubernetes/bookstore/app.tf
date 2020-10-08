resource "kubernetes_service" "app" {
  metadata {
    name      = var.app_name
    namespace = var.app_namespace
  }

  spec {
    selector = {
      app = var.app_name
    }

    type = "LoadBalancer"

    port {
      name        = "http"
      port        = 80
      protocol    = "TCP"
      target_port = var.app_port
    }
  }
}

resource "kubernetes_deployment" "app" {
  metadata {
    name      = var.app_name
    namespace = var.app_namespace
    labels = {
      app = var.app_name
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = var.app_name
      }
    }

    template {
      metadata {
        labels = {
          app = var.app_name
        }
      }

      spec {
        container {
          name  = "bookstore"
          image = "eu.gcr.io/octodemo-db-291120/octodemo-db-bookstore:1.0.0-peter-murray-patch-1-3264e6e7-SNAPSHOT"
          image_pull_policy = "IfNotPresent"

          env {
            name  = "DATABASE_URL"
            #TODO port from database service and name?
            value = "jdbc:postgresql://bookstore-db:5432/${var.database_name}"
          }

          env {
            name = "DATABASE_USER"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.database.metadata.0.name
                key  = "user"
              }
            }
          }

          env {
            name = "DATABASE_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.database.metadata.0.name
                key  = "password"
              }
            }
          }

          # resources {
          #   limits {
          #     cpu    = "0.5"
          #     memory = "512Mi"
          #   }
          #   requests {
          #     cpu    = "250m"
          #     memory = "50Mi"
          #   }
          # }

          # liveness_probe {
          #   http_get {
          #     path = "/status"
          #     port = 8080
          #   }
          #   initial_delay_seconds = 5
          #   period_seconds        = 10
          # }
        }

        restart_policy                   = "Always"
        termination_grace_period_seconds = 5
      }
    }
  }
  depends_on = [kubernetes_service.database]
}

output app_service_ip {
  value = tostring(kubernetes_service.app.load_balancer_ingress.0.ip)
  description = "Application Service IP Address"
}

output app_service_name {
  value = kubernetes_service.app.metadata.0.name
  description = "Application Service name"
}