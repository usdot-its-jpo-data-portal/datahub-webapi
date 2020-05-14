# datahub-webapi
DataHub Web API to consume data from  DataHub ElasticSearch storage system.

## Usage
Once the application is running on a configured port it is required to submit a GET request to retrieve a list of datasets or individual dataset or a POST request to search for **words** or **phrases**.

The following endpoints were defined to request for data:

### DataAssets

To list a set of datasets.

  - Method: GET
  - URL: http://[host:port]/api/v1/dataassets?sortby=lastUpdate&sortorder=desc&limit=10

To get a data assets by it's id.

  - Method: GET
  -- URL: http://[host:port]/api/v1/dataassets/{dhId}


### Search

 - Method: POST
 - URL: http://[host:port]/api/v1/search
 - Content-Type: application/json
 - Payload (sample)
```json
  {
    "term" : "Test",
    "phrase" : false,
    "limit" : 10
  }
```

#### cURL

```bash
curl 'http://example.com:3006/api/v1/search' -i -X POST \
    -H 'Content-Type: application/json' \
    -d '{
  "term" : "Test",
  "phrase" : false,
  "limit" : 10
}'
```

### Configuration - Engagement Popup

To list a set of Engagement Popups.

  - Method: GET
  - URL: http://[host:port]/api/v1/configurations/engagementpopups


### Response
The response object provides a general response information and the actual data is associated with the "result" property.

#### Data Asset List
Resonse sample
```json
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "timestamp" : "2020-03-26T17:26:34Z",
  "status" : "OK",
  "code" : 200,
  "path" : "http://localhost",
  "verb" : "GET",
  "traceid" : "20200326172634138",
  "result" : [ {
    "id" : "test:1234",
    "name" : "Different Approaches to Disseminating Traveler Information",
    "description" : "Source: Provided by ITS DataHub through the National Transportation Library.",
    "accessLevel" : "Public",
    "lastUpdate" : "2020-03-26T17:26:34.138+0000",
    "tags" : [ "Definitions", "Human factors", "Information dissemination" ],
    "sourceUrl" : "https://source.example.com/view/test/1234",
    "dhId" : "s1-test:1234",
    "dhLastUpdate" : "2020-03-26T17:26:34.138+0000",
    "dhSourceName" : "s1",
    "metrics" : {
      "downloadsTotal" : 5,
      "pageViewsLastMonth" : 15,
      "pageViewsTotal" : 25
    }
  } ]
}
```

#### Data Asset by ID

Resonse sample where the "result" is an individual document.
```json
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "timestamp" : "2019-10-30T21:44:42Z",
  "status" : "OK",
  "code" : 200,
  "path" : "http://localhost",
  "verb" : "GET",
  "traceid" : "20191030214442965",
  "result" : {
    "id" : "test:1234",
    "name" : "Different Approaches to Disseminating Traveler Information",
    "description" : "Source: Provided by ITS DataHub through the National Transportation Library.",
    "accessLevel" : "Public",
    "lastUpdate" : "2019-10-30T21:44:42.965+0000",
    "tags" : [ "Definitions", "Human factors", "Information dissemination" ],
    "sourceUrl" : "https://source.example.com/view/test/1234",
    "dhId" : "s1-test:1234",
    "dhLastUpdate" : "2019-10-30T21:44:42.965+0000",
    "dhSourceName" : "s1"
  }
}
```

#### Search

The top level of the response contains a highlevel information about the transaction, the next level under the "result" property contains the Response wrapper that includes the requested information and the "result" data.

```json
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "timestamp" : "2020-03-26T17:34:17Z",
  "status" : "OK",
  "code" : 200,
  "path" : "http://localhost",
  "verb" : "POST",
  "traceid" : "20200326173417998",
  "result" : {
    "searchRequest" : {
      "term" : "Test",
      "phrase" : false,
      "limit" : 10
    },
    "numHits" : 1,
    "maxScore" : 1.0,
    "result" : [ {
      "id" : "id:1234",
      "name" : "SampleDataAsset",
      "description" : "Description of the data asset",
      "accessLevel" : "Public",
      "lastUpdate" : "2020-03-26T17:34:17.998+0000",
      "tags" : [ "Sample tag number one", "Sample tag number two", "Sample tag number three" ],
      "sourceUrl" : "http://testing.com/id:1234",
      "dhId" : "intId",
      "dhLastUpdate" : "2020-03-26T17:34:17.997+0000",
      "dhSourceName" : "source",
      "esScore" : 1.0,
      "related" : [ {
        "id" : "585e203c4bf7b9ff12966fd9697b87cb",
        "name" : "related1-name",
        "url" : "http://related.item.com/id=585e203c4bf7b9ff12966fd9697b87cb"
      }, {
        "id" : "7f3bac27fc81d39ffa8ede58b39c8fb6",
        "name" : "related2-name",
        "url" : "http://related.item.com/id=7f3bac27fc81d39ffa8ede58b39c8fb6"
      } ],
      "metrics" : {
        "downloadsTotal" : 5,
        "pageViewsLastMonth" : 15,
        "pageViewsTotal" : 25
      }
    } ]
  }
}
```

#### Configuration Engagement Popup

The response object provides a general response information and the actual data is associated with the "result" property, in this case will contains an array of the available Engagement Popups.

```json
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "timestamp" : "2020-05-14T18:27:28Z",
  "status" : "OK",
  "code" : 200,
  "path" : "http://localhost",
  "verb" : "GET",
  "traceid" : "20200514182728965",
  "result" : [ {
    "id" : "71b8984d-fdb1-4f6b-8163-2f380090b298",
    "name" : "Engagement Popup 0",
    "description" : "Description-0",
    "lastModified" : "2020-05-14T18:27:28.964+0000",
    "content" : "<h1>Content-0</h1>",
    "controlsColor" : "white",
    "controlsShadow" : "black",
    "isActive" : true
  }, {
    "id" : "2d7b43a2-e204-4276-ba80-fe4e9dda1335",
    "name" : "Engagement Popup 1",
    "description" : "Description-1",
    "lastModified" : "2020-05-14T18:27:28.964+0000",
    "content" : "<h1>Content-1</h1>",
    "controlsColor" : "white",
    "controlsShadow" : "black",
    "isActive" : true
  } ]
}
```


The following status codes are possible to have as part of a response.

- 200 : The request was successfull and there is data available.
- 204 : The request was successfull but there is no data available.
- 400 : The request is bad and does not contains the required information.
- 500 : Internal server error will be provided in case of errors.


## Configuration
The API requires the following environment variables

|Name   |Required   |Default   |Description|
|--|--|--|----|
|datahub.webapi.es.host|mandatory||Sets the host of the target ElasticSearch|
|datahub.webapi.es.port|mandatory||Sets the port that the target ElasticSearch is using.|
|datahub.webapi.es.scheme|mandatory||Sets the protocol scheme used by the target ElasticSearch (http or https)|
|codehub.ui.url.endpoint|mandatory||Sets the CodeHub target URL ( http://[host]:[port] )|
|codehub.ui.url.questring|optional|/#/project-details?id=|CodeHub query string for the project information page.|
|datahub.webapi.es.index|optional|dataassets|Set the Index name to be used as main source of the data.|
|datahub.webapi.es.related|optional|related|Set the Index name to be used as source of related CodeHub information|
|server.servlet.context-path|optional|/api|Set the DataHub Web API context path|
|server.port|optional|3006|Sets the DataHub Web API listening port|
|datahub.webapi.configurations.index|optional|configurations|Set the Index that stores the configuration.|
|datahub.webapi.configurations.default|optional|datahub-default-configuration|Id of the document that has the configurations information.|


## Installation
The API is a Java application and can be executed updating the values of the following command template.

```bash
sh -c java -Djava.security.egd=file:/dev/./urandom -jar /datahub-webapi-1.3.0.jar"
```
It is important to setup the environment variables before to execute the application.

## File Manifest
* src/main : Contains the source code
* src/test : Contains the unit testing code.
* Dockerfile: Docker image definition file


## Development setup
> The API was developed using [Spring Tool Suite 4](https://spring.io/tools/) that is base on [Eclipse](https://www.eclipse.org/ide/)

1. Install and open Spring Tool Suit
2. Configure the required environment variables
3. Debug/Run as Spring Boot application, after this step the application will be running and ready to receive request.

## Docker Support
A [Docker](https://www.docker.com/) image can be build with the next command line.
```bash
  docker build -t datahub-webapi:latest .
```

The following command with the correct values for the environment variable will start a Docker container.
```bash
docker run -p 3006:3006 --rm \
-e "server.port=3006" \
-e "datahub.webapi.es.host=[HOST]" \
-e "datahub.webapi.es.port=[PORT]" \
-e "datahub.webapi.es.scheme=[SCHEME]" \
-e "codehub.ui.url.endpoint=[CODEHUB-WEBHOST]" \
-e "JAVA_OPTS=-Xmx512M -Xms512M" \
-t -i datahub-webapi:latest
```


## Release History
* 1.0.0
  * Initial version
* 1.1.0
  * Add related CodeHub entries to the search result.
* 1.2.0
  * Adding Metrics to DataAssets
* 1.3.0
  * Support for Configurations - Engagement Popups


## Contact information
Joe Doe : X@Y

Distributed under XYZ license. See *LICENSE* for more information

## Contributing
1. Fork it (https://github.com/usdot-its-jpo-data-portal/datahub-webapi/fork)
2. Create your feature branch (git checkout -b feature/fooBar)
3. Commit your changes (git commit -am 'Add some fooBar')
4. Push to the branch (git push origin feature/fooBar)
5. Create a new Pull Request

## Known Bugs
*

## Credits and Acknowledgment
Thank you to the Department of Transportation for funding to develop this project.

## CODE.GOV Registration Info
* __Agency:__ DOT
* __Short Description:__ WebAPI to interface ITS DataHub ElasticSearch.
* __Status:__ Beta
* __Tags:__ DataHub, DOT, Spring Boot, Java, ElasticSearch
* __Labor Hours:__
* __Contact Name:__
* __Contact Phone:__
