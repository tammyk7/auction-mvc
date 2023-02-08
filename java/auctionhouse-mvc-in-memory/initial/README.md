# AuctionHouse MVC In Memory - initial version

## Building
Application build is performed with Gradle.

To build source code, run in a terminal (it will also execute the tests):

> ./gradlew clean build

The build generates the application executable jar in build/libs.

## Running
To run the application, run in a terminal:

> ./gradlew bootRun


## Security

It is disabled for now, to enable, see instructions on top of `AuctionHouseMvcApplication.java` and `SecurityConfiguration.java`
Tests are disabled since security is disabled for now

Once enabled, security for this project uses a custom authentication provider implemented to respond to UsernamePasswordAuthenticationToken authentication requests.
The username and password are validated against the data defined here `src/main/java/com/weareadaptive/auction/configuration/ApplicationInit.java`.
See `security` package in the project for details.

!!! Note that this is a very basic implementation (not be reused in any "real projects").
See valid Jwt token definition here https://jwt.io/introduction.

### Call to application endpoints

When security is enabled, you need to provide a valid token in the `Authorization` header to call the endpoints.

According to the implementation done in this project, the expected format is `Bearer username:password`.
The username and password are validated against the data defined here `src/main/java/com/weareadaptive/auction/configuration/ApplicationInit.java`.

Call the users endpoints with an `Authorization` header = `Bearer ADMIN:adminpassword`.
You can create the users you need to call the other endpoints that required other roles.
