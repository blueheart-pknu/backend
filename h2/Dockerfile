# Base image for Java
FROM bellsoft/liberica-openjdk-alpine:17

# Set working directory
WORKDIR /h2

# Copy H2 JAR file
COPY h2-2.3.232.jar h2.jar

# Expose H2 console and JDBC ports
EXPOSE 1521 81

# Run H2 database
ENTRYPOINT ["java", "-cp", "h2.jar", "org.h2.tools.Server", "-web", "-webAllowOthers", "-tcp", "-tcpAllowOthers", "-tcpPort", "1521", "-ifNotExists"]