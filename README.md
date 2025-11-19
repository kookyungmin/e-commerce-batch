# E-Commerce-Batch

Spring Batch를 활용한 이커머스 대용량 처리 시스템

<!-- prettier-ignore-start -->
![SpringBoot](https://shields.io/badge/springboot-black?logo=springboot&style=for-the-badge%22)
![SpringBatch](https://shields.io/badge/springbatch-black?logo=springboot&style=for-the-badge%22)
![Docker](https://shields.io/badge/docker-black?logo=docker&style=for-the-badge%22)
![Postgresql](https://shields.io/badge/postgresql-black?logo=postgresql&style=for-the-badge%22)
![Prometheus](https://shields.io/badge/prometheus-black?logo=prometheus&style=for-the-badge%22)
![Grafana](https://shields.io/badge/grafana-black?logo=grafana&style=for-the-badge%22)
<!-- prettier-ignore-end -->

### System Requirements

- [java] 21
- [springboot] 3.3.4
- [springbatch] 3.3.4
- [docker] 20.10.12
- [postgresql] 16.10
- [prometheus] 2.7.2
- [pushgateway] 0.6.0
- [grafana] 6.0.2

## Local 실행 환경

### Postgresql start

`/bin/docker-compose-up.sh`

### Postgresql stop

`/bin/docker-compose-down.sh`

### Spring Boot application 실행

`./gradlew bootRun`

`--spring.boot.job.names=productUploadJob inputFilePath=batch/data/random_product.csv,java.lang.String,false`

`--spring.batch.job.name=productDownloadJob outputFilePath=batch/data/download_product.csv,java.lang.String,false gridSize=8,java.lang.Integer,false`

## docker 실행 상태에서 DB 접근

### Postgresql

`docker exec -it ecb-postgres bash` \
`psql -U local -d ecb`

## pushgateway, prometheus, grafana 접근

### pushgateway

`http://locahost:19091`

### prometheus

`http://localhost:19090`

### grafana

`http://localhost:13000`