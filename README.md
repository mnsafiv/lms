```sh
swagger
localhost:8080/swagger-ui/index.html#/
```
```sh
localhost:8080/register -register
localhost:8080/auth -token
localhost:8080/v1/book/create     -required jwt
localhost:8080/v1/genre/create    -required jwt
localhost:8080/v1/author/create   -required jwt
localhost:8080/v1/bookreg/**      -required jwt
localhost:8080/v1/**              -permit all
```
