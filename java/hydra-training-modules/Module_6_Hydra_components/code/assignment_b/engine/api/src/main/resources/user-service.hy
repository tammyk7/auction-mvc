metadata = {
  "java.package": "com.weareadaptive.assignment_b.engine"
  "format.type.test": "allocated.Allocated[Name]"
}

import { Username Age } from "common.hy"

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

type UserEvent =
    | UserCreated of Username

service UserService = {
    createUser(CreateUserRequest): CreateUserResponse

    userEvents: UserEvent stream
}
