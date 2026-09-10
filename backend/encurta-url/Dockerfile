FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# pom.xml do núcleo
COPY pom.xml .

COPY nucleo ./nucleo
COPY encurta-url ./encurta-url
COPY qrcode ./qrcode

RUN mvn clean package -pl encurta-url -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /build/encurta-url/target/*.jar encurta-url.jar

ENTRYPOINT ["java", "-jar", "encurta-url.jar"]
