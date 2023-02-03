# AuctionHouse MVC - initial version

This initial version of this project was generated using [Spring Boot Initializr](https://start.spring.io/) 

It contains the following features
- A Postgresl database (see docker-compose to run it)
- Flyway enabled with initial script `src/main/resources/db/migration/V1__auction_user.sql`
- Security: see package `security` + userService, userRepository and required table in the db. 
It is disabled for now, to enable, see instructions on top of `AuctionhouseMvcApplication.java` and `SecurityConfiguration.java`
- Tests setup to use postresql testContainers + tests for security (partially disabled since security is disabled for now)
- Checkstyle plugin (in pom.xml)


## Docker

In order to successfully start the application you need to have a running database.
This is achieved by using docker (and docker-compose) that will run a postgresl db.

#### Pre-requisite

Run docker

#### Manage Database

To run in terminal and be able to exit with Ctrl+C:

```shell
docker compose -f docker/docker-compose-db.yml up 
```

To run in background:
```shell
docker compose -f src/main/resources/docker-compose-db.yml up -d
```

To stop database:
```shell
docker compose -f src/main/resources/docker-compose-db.yml down
```

## Security

To enable security, see instructions on top of `AuctionhouseMvcApplication.java` and `SecurityConfiguration.java`.

Once enabled, security for this project uses a custom authentication provider implemented to respond to UsernamePasswordAuthenticationToken authentication requests.
See `security` package in the project for details.

!!! Note that this is a very basic implementation (not be reused in any "real projects").
See valid Jwt token definition here https://jwt.io/introduction.

### Call to application endpoints

As specify in the SecurityConfiguration.java class and in the controllers, you need to provide a valid token to call the endpoints.

According to the implementation done in this project, the expected format is `Bearer username:password`.
The username and password are validated against the data present in the db.

When you start the application, the database is setup and an admin user is created (see the script `src/main/resources/db/migration/V1__Create_user.sql`)

Call the users endpoints with an `Authorization` header = `Bearer ADMIN:adminpassword`.
You can create the users you need to call the other endpoints that required other roles.
