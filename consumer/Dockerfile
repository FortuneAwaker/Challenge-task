FROM openjdk:11
ADD target/consumer-0.0.1-SNAPSHOT.jar consumer.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "consumer.jar"]
