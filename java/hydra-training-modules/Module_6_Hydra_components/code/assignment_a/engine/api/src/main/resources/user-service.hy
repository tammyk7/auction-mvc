metadata = {
  "java.package": "com.weareadaptive.assignment_a.engine"
  "format.type.test": "allocated.Allocated[Name]"
}

type Username = char[30]
type Age = int8

table UserRecord = {
    primary key username: Username
    age: Age
}

database EngineDatabase = {
    UserRecord
}

type CreateUserRequest = {
    username: Username
    age: Age
}

type CreateUserResponse = {
}

service UserService = {
    createUser(CreateUserRequest): CreateUserResponse
}
