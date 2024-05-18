FROM openjdk:19
ADD target/*.jar app.jar
RUN chmod 777 app.jar
ENTRYPOINT ["java","-jar","app.jar"]