**Run**
- Server `TERM=dumb HOST=<host> PORT=<port> AT_MOST_ONCE=<0|1> ./gradlew -q server`. Default config:
  ```
  HOST=127.0.0.1 
  PORT=12740 
  AT_MOST_ONCE=0
  ```
- Client 1 `TERM=dumb CLIENT_PORT=12741 ./gradlew -q client`
- Client 2 `TERM=dumb CLIENT_PORT=12742 ./gradlew -q client`
- Test `./gradlew test`
