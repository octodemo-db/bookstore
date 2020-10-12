[![Board Status](https://dev.azure.com/octoorg/f9fecbbc-d2ce-41aa-b745-de61141f55a7/8574cca6-9065-4912-bc64-e8d3797b02f5/_apis/work/boardbadge/43b17eff-7feb-4d15-96d7-063fcf3d49d3)](https://dev.azure.com/octoorg/f9fecbbc-d2ce-41aa-b745-de61141f55a7/_boards/board/t/8574cca6-9065-4912-bc64-e8d3797b02f5/Microsoft.RequirementCategory)
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



