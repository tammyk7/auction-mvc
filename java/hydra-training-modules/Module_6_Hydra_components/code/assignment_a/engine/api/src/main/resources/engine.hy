metadata = {
  "java.package": "com.weareadaptive.assignment_a.engine"
}

import { UserService } from "user-service.hy"

cluster Engine = {
    services: {
        UserService
    }
}
