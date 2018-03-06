**Run**
- Server `HOST=<host> PORT=<port> AT_MOST_ONCE=<0|1> ./gradlew server`. Default config: 
  ```
  HOST=127.0.0.1 
  PORT=12740 
  AT_MOST_ONCE=0
  ```
- Client `TERM=dumb ./gradlew -q client`
- Test `./gradlew test`
