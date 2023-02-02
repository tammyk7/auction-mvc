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