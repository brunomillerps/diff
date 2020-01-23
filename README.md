
<img src="https://github.com/brunomillerps/diff/blob/master/img/Diff-Files.svg">

# JSON File Comparator
> Compare 2 [Base64](https://en.wikipedia.org/wiki/Base64) encoded JSON data

Provides through a set of API Rest the functionality that allows comparing 2 JSON files, 
being formatted in base64 when submitting them.

# Table of Content
  
- [Documentation](#documentation)
- [Tools and Libraries](#tools-and-libraries)
- [Code Quality](#code-quality)
- [Improvement List](#improvement-list)

---

# Documentation
<img src="https://avatars3.githubusercontent.com/u/16343502?v=3&s=200" width="80px">

You are going to find an [Open API Specification](https://github.com/OAI/OpenAPI-Specification) (OAS)
 document at `./swagger/api.json` folder, so that you can check the engine of this app APIs.
 
## API Endpoints


| Endpoint            | Method   | Summary             | Request                   | Response              |
|:------------------  | :------: | :-------------------| :------------------------:| :---------:           |
| /v1/diff/{id}/left  |  `POST`  | Persists left side  |  application/octet-stream |     204               |
| /v1/diff/{id}/right |  `POST`  | Persists right side |  application/octet-stream |     204               |
| /v1/diff/{id}       |  `POST`  | Operates diffs      |  -                        | 200> application/json | 

# Tools and Libraries
> The technology stack used at this project to accomplish needs

* Java 8
* Spring Boot and affiliate projects (Data and MVC)
* MongoDB
* Docker Compose (for local mongodb running)
* Jackson 2 & [json-patch](https://github.com/fge/json-patch.git)
* [Diff Utils library](https://java-diff-utils.github.io/java-diff-utils/)

# Features

In this system, we have two ways of comparing the JSON files. The API `POST /v1/diff/{id}` also contains a query parameter `diffType` to tell the application how to perform the diff. They are:
- `JSON_PATCH` comparison. [RFC 6902](https://tools.ietf.org/html/rfc6902)
	- That will show a set of operations (if files differ) which may be: add, remove, replace, move, copy and test, and its affected path.
- `MYER` algorithm: An O(ND) Difference Algorithm by Eugene W. Myers[[1](http://blog.robertelder.org/diff-algorithm/#myers-paper)]. 

## Example
> You can encode or decode examples here https://www.base64encode.org/, https://www.base64decode.org/.

Given the following encoded JSON

**Left side**
```
ewogICJtZW51IjogewogICAgImlkIjogImZpbGUiLAogICAgInZhbHVlIjogIkZpbGUiLAogICAgInBvcHVwIjogewogICAgICAibWVudWl0ZW0iOiBbCiAgICAgICAgewogICAgICAgICAgInZhbHVlIjogIk5ldyIsCiAgICAgICAgICAib25jbGljayI6ICJDcmVhdGVOZXdEb2MoKSIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICJ2YWx1ZSI6ICJPcGVuIiwKICAgICAgICAgICJvbmNsaWNrIjogIk9wZW5Eb2MoKSIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICJ2YWx1ZSI6ICJDbG9zZSIsCiAgICAgICAgICAib25jbGljayI6ICJDbG9zZURvYygpIgogICAgICAgIH0KICAgICAgXQogICAgfQogIH0KfQo=
```

**Right side**
```
ewogICJtZW51IjogewogICAgImlkIjogImZpbGUiLAogICAgInZhbHVlIjogIkZpbGUiLAogICAgInBvcHVwIjogewogICAgICAibWVudWl0ZW0iOiBbCiAgICAgICAgewogICAgICAgICAgInZhbHVlIjogIk5ldyIsCiAgICAgICAgICAib25jbGljayI6ICJDcmVhdGVEb2MoKSIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICJ2YWx1ZSI6ICJDbG9zZSIsCiAgICAgICAgICAib25jbGljayI6ICJDbG9zZURvYygpIgogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgInZhbHVlIjogIkFib3V0IiwKICAgICAgICAgICJvbmNsaWNrIjogIkFib3V0RG9jWCgpIgogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgInZhbHVlIjogIk9wZW4iLAogICAgICAgICAgIm9uY2xpY2siOiAiT3BlbkRvYygpIgogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgInZhbHVlIjogIlJlbGVhc2UgTm90ZXMiLAogICAgICAgICAgIm9uY2xpY2siOiAiT3BlblJlbGVhc2VOb3RlcygpIgogICAgICAgIH0KICAgICAgXQogICAgfQogIH0KfQ==
```

### JSON Patch

When calling the API like this: `http://<host>:<port>/api/v1/diff/{id}?diffType=JSON_PATCH`

API respose body would be like

```json
{
    "data": [
        {
            "op": "replace",
            "path": "/menu/popup/menuitem/0/onclick",
            "value": "CreateDoc()"
        },
        {
            "op": "replace",
            "path": "/menu/popup/menuitem/1/onclick",
            "value": "CloseDoc()"
        },
        {
            "op": "replace",
            "path": "/menu/popup/menuitem/1/value",
            "value": "Close"
        },
        {
            "op": "replace",
            "path": "/menu/popup/menuitem/2/onclick",
            "value": "AboutDocX()"
        },
        {
            "op": "replace",
            "path": "/menu/popup/menuitem/2/value",
            "value": "About"
        },
        {
            "op": "add",
            "path": "/menu/popup/menuitem/-",
            "value": {
                "value": "Open",
                "onclick": "OpenDoc()"
            }
        },
        {
            "op": "add",
            "path": "/menu/popup/menuitem/-",
            "value": {
                "value": "Release Notes",
                "onclick": "OpenReleaseNotes()"
            }
        }
    ],
    "matched": false
}
```

### Meyr's Algorithm
When calling the API like this: `http://<host>:<port>/api/v1/diff/{id}?diffType=MEYR`

API respose body would be like

```json
{
    "data": "--- json-base64-left.json\n+++ json-base64-right.json\n@@ -9,1 +9,1 @@\n-          \"onclick\": \"CreateNewDoc()\"\n+          \"onclick\": \"CreateDoc()\"\n@@ -12,0 +12,8 @@\n+          \"value\": \"Close\",\n+          \"onclick\": \"CloseDoc()\"\n+        },\n+        {\n+          \"value\": \"About\",\n+          \"onclick\": \"AboutDocX()\"\n+        },\n+        {\n@@ -16,2 +24,2 @@\n-          \"value\": \"Close\",\n-          \"onclick\": \"CloseDoc()\"\n+          \"value\": \"Release Notes\",\n+          \"onclick\": \"OpenReleaseNotes()\"",
    "matched": false
}
```

Pre formated `data`:
```diff
--- json-base64-left.json
+++ json-base64-right.json
@@ -9,1 +9,1 @@
-          \"onclick\": \"CreateNewDoc()\"
+          \"onclick\": \"CreateDoc()\"
@@ -12,0 +12,8 @@
+          \"value\": \"Close\",
+          \"onclick\": \"CloseDoc()\"
+        },
+        {
+          \"value\": \"About\",
+          \"onclick\": \"AboutDocX()\"
+        },
+        {
@@ -16,2 +24,2 @@
-          \"value\": \"Close\",
-          \"onclick\": \"CloseDoc()\"
+          \"value\": \"Release Notes\",
+          \"onclick\": \"OpenReleaseNotes()\"
```


# Code Quality
## Tests

Test coverage is near to 100% of cover. Until June 29th it has *93%* of classes and *82%* of lines covered.

<img src="https://github.com/brunomillerps/diff/blob/master/img/test-coverage2019-06-29.png">

## Continuous Inspection
Code quality is provided by [SonarQube](https://www.sonarqube.org/). [Check the report out](https://sonarcloud.io/dashboard?id=brunomillerps_diff).

# Improvement List

* Fix SonarQube issues
* Make it containerized
* Appropriate separation of Unit and Integration Test. [ref. doc.](https://www.petrikainulainen.net/programming/maven/integration-testing-with-maven/)
* Create code coverage reports for Unit and Integration tests with the JaCoCo Maven Plugin. [ref. doc.](https://www.petrikainulainen.net/programming/maven/creating-code-coverage-reports-for-unit-and-integration-tests-with-the-jacoco-maven-plugin/)
* Fix SonarQubeConverage report ( sonar report is not reflecting the actual result of tests)
* Replace Jackson comparator `json-patch` to Tobias Warneke's `applying patches` [schema](https://github.com/java-diff-utils)
* Continuous Integration with [Travis CI](https://travis-ci.org)
* Revisit the project structure to match a consolidated architecture (Hexagonal, Onion, DDD or Clean Architecture)
* README Improvement with badges to code quality and tests for better visualization.

1..2...3
