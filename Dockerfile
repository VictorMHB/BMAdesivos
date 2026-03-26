FROM maven:latest

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

ENTRYPOINT ["java", "-jar", "target/BMAdesivos-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080