variable "app_namespace" {
  type = string
}

variable "app_name" {
  default = "bookstore"
  type = string
}

variable "app_port" {
  default = 8080
  type = number
}

variable "database_name" {
  default = "bookstore"
  type = string
}

variable "database_port" {
  default = 5432
  type = number
}