# Gav-Graph-Management

This project is built with Spring Boot 2 associated with Swagger 2, so when you run it, you can clearly find the rest APIs via the Swagger-UI.
 To do so, you only need to start the project and click this [link](http://localhost:9091) when you are useing `dev` configuration.


## Configuration
Before you run this project, please find [application.properties](src/main/resources/application.properties) to select whether you want to run with `dev` or `prod` environment.
`dev` is using <font color='red'>**neo4j bolt drive**</font> while prod environment is using **neo4j http drive**.

After you decided using which driver, you should configure your `username` and `password` corresponding your local database setting.
 
 ## Create Demo Data
 The [demo.cypher](src/main/resources/demo.cypher) contains several simple nodes and relationships of the Artifact entity.
 
## How to run
Simply find the [GavgraphApplication.java](src/main/java/uk/ac/newcastle/redhat/gavgraph/GavgraphApplication.java), and run the main method.
Or if you are using Itellij Idea, you can easily run it using the shortcut shift + F10 to run it.

