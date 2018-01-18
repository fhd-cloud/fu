# FileUpload Service

## SpringBoot RESTful API for File upload service

## Installation
#### With Maven
Run `mvn clean install` to download all the dependencies and install locally

#### Without Maven
Run `mvnw clean install` to download all the dependencies and install locally


## Running the Application
### In Windows
Run `start.bat` to start the REST server

### In OSX
Run `start.sh` to start the REST server

### REST API Endpoints

#### List of available files
`GET http://localhost:8080/api/files`

#### Get a particular file
`GET http://localhost:8080/api/files/:fileName`

#### Upload new file
`POST http://localhost:8080/api/files`