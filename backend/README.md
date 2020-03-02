#  **Backend**

## Prerequisites

- JDK 11+
- Maven 3+
- MongoDB 4.0.2+

## Running

### Create Docker Network

```bash
docker network create backend-network
```

### Running MongoDB with Docker
You can easily start a standalone MongoDB instance by executing the following command:

```bash
docker volume create mongodb_backend_volume
docker run -p 27017:27017 -d --network=backend-network \
            --name mongodb \
            -v mongodb_backend_volume:/data/db \
            --restart=on-failure \
            mongo:4.0.2
```

### Running Backend with Maven
Execute the following command to start Backend:

```bash
mvn spring-boot:run
```

(OPTIONAL) You can define multiple profiles under the [src/main/resources](./src/main/resources) directory. For example:
  - `application-deployment.properties` for deployment profile
  - `application.properties` for default profile

After defining multiple profiles, you can set the active profile when running:

```bash
mvn spring-boot:run -Dspring.profiles.active=deployment
```

Swagger UI will be available at `http://localhost:8090/swagger-ui.html`. 

### Running Backend with Docker
Build docker image:

```bash
docker build -t backend .
```

Run newly built image:

```bash
docker run -it --rm -p 8090:80 \
        --network=backend-network \
        -e SERVER_PORT="80" \
        -e JAVA_OPTS="-Dspring.profiles.active=deployment -Xms128m -Xmx256m" \
        -e MONGODB_URI="mongodb://mongodb:27017/mongo" \
        --name backend \
        backend
```

Swagger UI will be available at `http://localhost:8090/swagger-ui.html`. 
