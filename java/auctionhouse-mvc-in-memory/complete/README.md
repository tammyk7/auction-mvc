# AuctionHouse MVC In Memory - complete version

## Security

Spring security is enabled for the project, with a custom authentication provider implemented to respond to UsernamePasswordAuthenticationToken authentication requests.
See `security` package in the project for details.

!!! Note that this is a very basic implementation (not be reused in any "real projects").
See valid Jwt token definition here https://jwt.io/introduction.

### Call to application endpoints

As configured in the SecurityConfiguration.java class and in the controllers, you need to provide a valid token in the `Authorization` header to call the endpoints.

According to the implementation done in this project, the expected format is `Bearer username:password`.
The username and password are validated against the data defined here `src/main/java/com/weareadaptive/auction/configuration/ApplicationInit.java.

Call the users endpoints with an `Authorization` header = `Bearer ADMIN:adminpassword`.
You can create the users you need to call the other endpoints that required other roles.
