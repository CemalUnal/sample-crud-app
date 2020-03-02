## Gateway

- Netflix Zuul is used as api gateway. Zuul is the API Gateway that sits in front of microservices, handles the requests from clients and routes them to the backend services. Api gateway provides some utilities such as:

- Path Based Routing
- Rate Limiting
- Authentication and Security
- API Monitoring

Api gateway configuration yaml file can be found [here](./src/main/resources/application.yml).

## Development

#### Prerequisites
* JDK 1.8+
* Maven 3+
* docker 19.03.2+

Before running the gateway, you need to have Redis up and running. It is used for rate limit data storage. You can easily get a redis running in your development environment by executing the following command;

```bash
docker volume create --name redis_data
docker run -p 6379:6379 -d --network=crud-app \
            --name redis \
            -v redis_data:/data \
            --restart=on-failure \
            redis:5.0.6
```

After that execute the following command in your terminal;

```bash
$ mvn clean install && mvn spring-boot:run
```

## Testing the Rate Limiting
Please keep in mind that while we applying rate limiting for our APIs, we should not restrict different users to perform more than 4 requests total at the same time unless one of them do not exceed the limit. In order to achieve it, we can simply add `type: user`
to the [gateway configuration file](./src/main/resources/application.yml).

```
default-policy-list:
  - limit: 4
    refresh-interval: 1
    type:
      - user
```

Then we can test whether this configuration is working or not:

```bash
bash rate-limit-test.sh
```

Rate limit is set to 10 in one second [here](./src/main/resources/application.yml). We can change it and set it to the desired number.

Please notice that all requests are performed against the api gateway, not directly to the backend services.

## Tracing
`spring-cloud-starter-zipkin` is used to send spans to Zipkin over HTTP. Following maven dependency is added to the [gateway](gateway/pom.xml).
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

Add following lines to the [gateway configuration](./src/main/resources/application.yml):

```
zipkin:
    base-url: http://localhost:9411
```

After that you can run Jaeger to visualize spans:

```bash
docker run -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  -p 9411:9411 \
  jaegertracing/all-in-one:1.12
```

Then you can visit `http://localhost:16686/search` to view the spans.
