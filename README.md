# Student Enrollment System

This is a SpringBoot web API based appilication that provides student enrollment features.

## How to setup the application

- After the application have been cloned on your machine
- Ensure you open the application via an IDE that supports springboot dashboard
- Setup your local database (Postgres) to match the configuration provided in the application.properties file (src\main\resources\application.properties)
- Open the project and in your terminal run ".\mvnw spring-boot:run" to start the application
- The dafault port for the application is port 8084, which can be modified in the application.properties file.

## API DOCS

- Swagger was used to document the available APIs
- It can be accessed via http://localhost:8084/swagger-ui.html , if no changes were made to the application.properties file.
- All endpoints are properly document in the swagger ui.

## Test

- The test resources are available in "./src/test/java/com/flexisaf/sas/"
- run "./mvnw test" in your terminal to run the test cases.

## General Details

- PostgresDb is the database used in this application
- No authentication was provided in this application, it wasn't a requirement, therefore all endpoints are not authenticated.
- The application have a cron job that sends birthday messages to all students in the system on their birthdays.
