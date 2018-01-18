# fileUpload

## SpringBoot RESTful API for File upload service

## Installation
### With Maven
`mvn clean install`

### Without Maven
`mvnw clean install`


## Running the Application
### run below script (use start.bat / start.sh depends on your OS)
### In Windows
`start.bat`

### In OSX
`start.sh`

### REST API Endpoints

#### List of available files
`GET http://localhost:8080/api/files`

#### Get a particular file
`GET http://localhost:8080/api/files/:fileName`

#### Upload new file
`POST http://localhost:8080/api/files`