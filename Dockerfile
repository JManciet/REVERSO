FROM openjdk:18-alpine

WORKDIR /app

RUN apk add --no-cache openjdk8-jre

CMD ["java", "-jar", "reverso.jar"]