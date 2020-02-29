# Sample CRUD App

## Quick Start with Docker

Create a network that is called `crud-app`:
```bash
docker network create crud-app
```

Start a standalone MongoDB instance:
```
docker volume create --name mongodb_data
docker run -p 27017:27017 -d --network=crud-app \
            --name mongodb \
            -v mongodb_data:/data/db \
            --restart=on-failure \
            mongo:4.0.2
```

Start [backend](./backend):
```
docker run -d --network=crud-app \
            --name backend \
            -e MONGODB_URI="mongodb://mongodb:27017/sample-app" \
            -e JAVA_OPTS="-Dspring.profiles.active=deployment -Dserver.port=80 -Xms125m -Xmx250m" \
            --restart=on-failure \
            cunal/demo-backend:v0.0.1
```

Start [gateway](./gateway):
```bash
docker run -p 9091:80 -d --network=crud-app \
            --name gateway \
            -e SIMPLE_BACKEND_SERVICE="http://backend" \
            -e JAVA_OPTS="-Dspring.profiles.active=deployment -Dserver.port=80 -Xms125m -Xmx250m" \
            --restart=on-failure \
            cunal/demo-gateway:v0.0.2
```

Start [frontend](./frontend):

```bash
docker run -p 5000:5000 -d --network=crud-app \
            --name frontend \
            -e REACT_APP_BACKEND_URI=http://localhost:9091/api/simple-backend \
            --restart=on-failure \
            cunal/demo-frontend:v0.0.1
```

Check everything is working properly:

```
docker ps
CONTAINER ID        IMAGE                        COMMAND                  CREATED             STATUS              PORTS                      NAMES
61c5bc5197f3        cunal/demo-frontend:v0.0.1   "docker-entrypoint.s…"   5 seconds ago       Up 4 seconds        0.0.0.0:5000->5000/tcp     frontend
209ae289eded        cunal/demo-gateway:v0.0.2    "/bin/sh -c 'java ${…"   9 seconds ago       Up 8 seconds        0.0.0.0:9091->80/tcp       gateway
6e014775e3c9        cunal/demo-backend:v0.0.1    "/bin/sh -c 'java ${…"   13 seconds ago      Up 12 seconds                                  backend
da5c84243c7a        mongo:4.0.2                  "docker-entrypoint.s…"   19 seconds ago      Up 18 seconds       0.0.0.0:27017->27017/tcp   mongodb
```

You can access to the application at `http://localhost:5000`.
