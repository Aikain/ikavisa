FROM maven:3-jdk-10 as build

WORKDIR /code

COPY pom.xml /code/pom.xml
COPY src /code/src

RUN mvn package


FROM openjdk:10-jre
COPY --from=build /code/target/app.jar .

CMD ["java", "-jar", "./app.jar"]