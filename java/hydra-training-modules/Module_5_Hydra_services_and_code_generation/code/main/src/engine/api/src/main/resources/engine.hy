metadata = {
  "java.package": "com.weareadaptive.echo.engine"
}

import { EchoService } from "echo-service.hy"

cluster Engine = {
    services: {
        EchoService
    }
}
