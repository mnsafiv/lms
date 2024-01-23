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
```sh
http://localhost:8080/v1/author/create			                    POST
http://localhost:8080/v1/author/{id}      		                  PUT
http://localhost:8080/v1/author/page{?}    		                  GET

http://localhost:8080/v1/book/create			                      POST
http://localhost:8080/v1/book/{id}/update			                  PUT
http://localhost:8080/v1/book/{id}				                      GET
http://localhost:8080/v1/book/page{n}			                      GET
http://localhost:8080/v1/book/page{n]/available		              GET
http://localhost:8080/v1/book/author/{id}/page{n}		            GET
http://localhost:8080/v1/book/author/3/page1/available	        GET
http://localhost:8080/v1/book/genre/{id}/page{n}		            GET
http://localhost:8080/v1/book/genre/{id}/page{n}/available	    GET

http://localhost:8080/v1/genre/create			                      POST
http://localhost:8080/v1/genre/{id}			                        PUT
http://localhost:8080/v1/genre/page{n}		                      GET

http://localhost:8080/v1/bookreg/reserve			                  POST
http://localhost:8080/v1/bookreg/{id}/feedback                  POST
http://localhost:8080/v1/bookreg/{id}/confirm		                POST
http://localhost:8080/v1/bookreg/{id}/cancel			              POST
http://localhost:8080/v1/bookreg/{id}/return			              POST
http://localhost:8080/v1/bookreg/page{n}				                GET
```
