FROM openjdk:11
WORKDIR /butterflyzhu
COPY ./target/task-joy-0.0.1-SNAPSHOT.jar ./app.jar
CMD ["java", "-jar", "app.jar"]