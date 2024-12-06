# Analytics Service

# Stack used :

* [PostgreSQL](https://www.postgresql.org/) – main DB
* [Redis](https://redis.io/) – as cache and pub/sub interface
* [Liquibase](https://www.liquibase.org/) – for DB migration and schemas
* [Gradle](https://gradle.org/) – as application builder
* [Lombok](https://projectlombok.org/) – to exclude boiler-plate code
* [MapStruct](https://mapstruct.org/) – for mapping
* Also integrated:
* [CI]()
* [Checkstyle]()
* [Swagger]()

## Service to store events from others services
## As subscriber with Redis, can store and get analytics by types to see who and when done the action
## Such as : profile view, post published, post like, task completed etc.
    