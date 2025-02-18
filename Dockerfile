FROM openjdk:21-jdk

ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD
ARG REDIS_HOST
ARG REDIS_PORT
ARG CLOVA_AUTHORIZATION
ARG OPENAI_AUTHORIZATION

ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} misik-api.jar

ENV db_url=${DB_URL} \
  db_username=${DB_USERNAME} \
  db_password=${DB_PASSWORD} \
  redis_host=${REDIS_HOST} \
  redis_port=${REDIS_PORT} \
  clova_authorization=${CLOVA_AUTHORIZATION} \
  openai_authorization=${OPENAI_AUTHORIZATION}

ENTRYPOINT java -jar misik-api.jar \
  --spring.datasource.url=${db_url} \
  --spring.datasource.username=${db_username} \
  --spring.datasource.password=${db_password} \
  --netx.host=${redis_host} \
  --netx.port=${redis_port} \
  --me.misik.chatbot.clova.authorization=${clova_authorization} \
  --me.misik.chatbot.openai.authorization=${openai_authorization}
