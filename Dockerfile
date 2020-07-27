ARG jdkVersion=14
FROM adoptopenjdk:${jdkVersion}-openj9 AS template
WORKDIR /usr/local/src
COPY . .
RUN sh gradlew bootJar


FROM adoptopenjdk:14-openj9
COPY --from=template /usr/local/src/build/dist/template-java-spring-1.0.0-SNAPSHOT.jar /app.jar
CMD ["-jar", "/app.jar"]
