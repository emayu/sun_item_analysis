# sun_item_analysis
SUN tool for analyze item responses

This tool is build for being used in the Sistema de Ubicación y Nivelación -SUN- at the Universidad de San Carlos -USAC-

## Build

`mvnw clean install`

The Maven build will also build the Angular client and package it in the rest project.

## Run web service & angular ui
Now the angular project is packaged and built inside rest jar

`java -jar rest-services\target\sun_analysis_item_rest-0.0.1-SNAPSHOT.jar`

### access to web ui project
GO to http://localhost:8080

## Run UI in development mode
If you already build the project Maven has already downloaded node and executed `npm install` and `npm build`,
or in the parent folder run for Maven to download node
```
mvnw --projects ui clean package
```

Now you can start a local server, first move in the project ui folder and then use the cmd script to launch it
```
cd ui
npm.cmd start
```

this will launch a development server go to http://localhost:4200/

### Angular CLI
You can use the two scripts provided `ng` and `npm` these scripts will use the local installation
```
cd ui
npm version
```
```
ng version
```

## Requirements
JDK 21