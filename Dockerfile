FROM openjdk:14
ADD target/e-learning.jar e-learning.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "e-learning.jar"]