**Run**
- Server `TERM=dumb HOST=<host> PORT=<port> AT_MOST_ONCE=<0|1> PACKET_LOSS_RATE=<0.0-1.0> ./gradlew -q server`. Default config:
  ```
  HOST=127.0.0.1 
  PORT=12740 
  AT_MOST_ONCE=0
  PACKET_LOSS_RATE=0.0
  ```
- Client `TERM=dumb CLIENT_HOST=<host> CLIENT_PORT=<port> SERVER_HOST=<host> SERVER_PORT<port> TIMEOUT_SEC=<seconds> MAX_RETRIES=<retries>  ./gradlew -q client`. Default config:
  ```
  CLIENT_HOST=127.0.0.1
  CLIENT_PORT=12741
  SERVER_HOST=127.0.0.1
  SERVER_PORT=12740
  TIMEOUT_SEC=5
  MAX_RETRIES=5
  ```
- Client 1 `TERM=dumb CLIENT_PORT=12741 ./gradlew -q client`
- Client 2 `TERM=dumb CLIENT_PORT=12742 ./gradlew -q client`
- Test `./gradlew test`
