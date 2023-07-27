# Assignment A: broadcasting events

In this assignment, we have a cluster (defined in `engine.hy`) that implements the hydra service `UserService`
(defined in `user-service.hy`). This service only has one method:

`createUser(CreateUserRequest): CreateUserResponse`

which creates a persistent UserRecord (in the Module 7 we will cover Hydra Persistence,
but we provide a working implementation of this service) and returns a CreateUserResponse.

For this assignment, you will have to:

- Create two components that connect to the cluster:
  - an imperative cluster client `admin`
  - a web-gateway `trading`
- the `admin` cluster client must expose a REST POST endpoint to create new users that accepts a body with a `username` and `age` fields. The admin gateway will send a `CreateUserRequest` (with username and age fields in it) to the cluster UserService method’s `createUser`.
- the `admin` gateway must reply to the HTTP request with a 201 CREATED only after receiving the CreateUserResponse from the cluster. The user must log to the console when the CreateUserResponse is received by the admin gateway.
- the `admin` gateway must reply to the create user HTTP request with a 400 BAD REQUEST and an informative error message (”invalid age, must be 18 or above”) if the `age` is below 18, and in this case it must not send the CreateUserRequest to the cluster.
- the `trading` gateway will receive a `UserCreatedEvent` every time a new user is created. The user must log to the console when the UserCreatedEvent is received by the trading gateway.

Write the Hydra files defining both `admin` and `trading` components, as well as any hydra services and types (under its respective `api/src/main/resources` folder).
You have to write the Java classes that implement the `admin` and `trading` behaviour described above.
