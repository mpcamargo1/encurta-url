FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build

# pom.xml do núcleo
COPY pom.xml .

COPY nucleo ./nucleo
COPY encurta-url ./encurta-url
COPY qrcode ./qrcode

RUN mvn clean package -pl qrcode -am -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /build/qrcode/target/*.jar qrcode.jar

ENTRYPOINT ["java", "-jar", "qrcode.jar"]
