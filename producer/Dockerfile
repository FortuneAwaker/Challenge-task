FROM openjdk:11
ADD target/producer-0.0.1-SNAPSHOT.jar producer.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "producer.jar"]
