FROM openjdk:11
EXPOSE 8001
ADD ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
