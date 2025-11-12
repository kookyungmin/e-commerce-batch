# E-Commerce-Batch

Spring Batch를 활용한 이커머스 대용량 처리 시스템

<!-- prettier-ignore-start -->
![SpringBoot](https://shields.io/badge/springboot-black?logo=springboot&style=for-the-badge%22)
![SpringBatch](https://shields.io/badge/springbatch-black?logo=springboot&style=for-the-badge%22)
![Docker](https://shields.io/badge/docker-black?logo=docker&style=for-the-badge%22)
![Postgresql](https://shields.io/badge/postgresql-black?logo=postgresql&style=for-the-badge%22)
<!-- prettier-ignore-end -->

### System Requirements

- [java] 21
- [springboot] 3.5.7
- [springbatch] 
- [docker] 20.10.12
- [postgresql] 16.10

## Local 실행 환경
### Postgresql start
`/bin/docker-compose-up.sh`

### Postgresql stop
`/bin/docker-compose-down.sh`

### Spring Boot application 실행
`./gradlew bootRun`

## docker 실행 상태에서 DB 접근
### Postgresql
`docker exec -it ecb-postgres bash` \
`psql -U local -d ecb`
