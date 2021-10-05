# forum-backend
A REST API implemented with the Spring Boot framework. It uses an embedded Apache Tomcat web server provided by Spring Boot as the back-end web server. The Tomcat server also connects to a MongoDB Atlas cluster.

## Setup

To run the application locally, you will need JDK version 11 installed on your machine. You may also want to install the Eclipse IDE, as it will be referenced directly in this readme. However, if you don't have the Eclipse IDE installed, then you can simply compile and run the application from your command line.

1. Clone the repository.

    `$ git clone https://github.com/mistyau/forum-backend.git`

2. Open the Eclipse IDE and open the project folder.

3. Create an environment variable. From the Eclipse menu, select `Run > Run Configurations`. Then select `Java Application > DemoApplication > Environment`. Click on `New...` and create a new environment variable with the name `SECRET_KEY`.

4. Configure the database. Under the `config` package, select `MongoConfig.java` and enter your database settings. Under the `getDatabaseName()` method, replace the return value with your database name string. Likewise, under the `mongoClient()` method, replace the connection string with your MongoDB connection string.

5. Run the application. Navigate to `DemoApplication.java` and right-click on it. Select `Run As > Java Application`. The application should now be running locally on your machine.


## Deploying to AWS Elastic Beanstalk

To deploy this app, you will need the following:
- an AWS account
- the Elastic Beanstalk CLI (EB CLI) installed on your machine

1. Open the EB CLI and change directories into the project folder.

2. Configure the project directory. Run the following command:

    `$ eb init`

    When prompted, select Java for the platform and Coretto 11 for the platform branch.
3. Change the server port. In the Eclipse IDE, or the file editor of your choice, navigate to `demo > src > main > resources > application.properties`. Uncomment the line `server.port=5000`.

4. Build the project and create the executable jar file. In the Eclipse IDE menu, select `Run > Run Configurations`. Then, select `Maven Build > demo` and create a new run configuration. Under `Goals` enter `clean install`. Check `Skip Tests`. Apply the settings and run the application. On success, you should see a new jar file at `demo > target > demo-0.0.1-SNAPSHOT.jar`.

5. Add the jar file path to `.elasticbeanstalk/config.yml`. The `config.yml` file should have the following entry:

    ```
    deploy:
        artifact: demo/target/demo-0.0.1-SNAPSHOT.jar
    ```

6. You are now ready to create your AWS environment. Navigate back to the EB CLI and execute the following command:

    `$ eb create -s`

    *The `-s` option instructs AWS to create a single instance.*

    The application should now be deployed to AWS Elastic Beanstalk. However, because `SECRET_KEY` is not yet defined in the environment, JWTs created by the application will not be properly signed. The next steps detail how to set environment properties for the newly created environment.

7. To create the environment variable `SECRET_KEY`, run the following command in the EB CLI:

    `$ eb setenv key=value`

    where `key` is `SECRET_KEY` and `value` is your secret key.
    
    The application should now be properly configured and deployed to AWS Elastic Beanstalk.