# Logger

 Provides simple WAL REST API for logging celebrities.<br/>
 This is intended for demonstrational purposes. See **not implemented** section.
 
## Requirements

 - JDK 8
 - Docker (version 17.03.1-ce)
 - Docker-Compose (version 1.11.2)

## Instructions

| Command                            | Description                          |
| ---------------------------------- | ------------------------------------ |
| `./gradlew build`                  | Build + Tests                        |
| `./docker-compose up --build`      | Start **crawler** and **logger**     |
| `./docker-compose scale crawler=5` | Scale **crawler** containers to 5    |

## Not Implemented

* Security
* Hypermedia
* Paging
* Proper infrastructure (separation of concerns, modularization etc...)
* More complex test cases (and dedicated test suite for REST API)
* More refined configuration of frame-works in use
* Use of key value store for better performance
* More refined docker/docker-compose configuration
