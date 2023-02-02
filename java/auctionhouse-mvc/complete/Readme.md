## Docker

Run docker

## Manage Database

To run in terminal and be able to exit with Ctrl+C:

```shell
docker compose -f src/main/resources/docker-compose-db.yml up 
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

Spring security is enabled for the project, with a custom authentication provider implemented to respond to UsernamePasswordAuthenticationToken authentication requests. 
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


