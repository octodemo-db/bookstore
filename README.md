# GitHub Demo Bookstore

This is a simple Maven project that builds a WAR that contains a simple bookstore application. The application is able 
to be built into a container and then available to be deployed to the Cloud or any other container hosting platform.

The Workflow files in this repository provide the following features:

* Pull Requests code is built and tested using Maven and a Docker container published
* Code QL scanning performed on each push
* Ability to deploy a `review` environment to a Cloud Environment using labels:
    - `Deploy to test`
    - `Deploy to qa`
    - `Deploy to staging`
* Cloud Review Environments are destroyed unpon closing of the PR
* Any commit to the main branch will result in the `prod` web application updated the latest container build


## Running the Web Application locally

have Maven and a JDK installed, or inside a container that has Maven and JDK installed.


### Developing and running locally:
The following command will build the WAR package and then run up a Jetty process to serve the WAR file on http://localhost:8080

```bash
$ mvn package jetty:run
```
