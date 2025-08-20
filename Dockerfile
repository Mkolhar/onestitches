# Multi-stage build for backend and frontend
FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /backend
COPY backend/pom.xml .
RUN mvn -q -pl order -am dependency:go-offline
COPY backend/ .
RUN mvn -q -pl order -am package -DskipTests

FROM node:20-alpine AS frontend-build
WORKDIR /frontend
COPY frontend/package.json frontend/tsconfig.json frontend/next.config.js ./
RUN npm ci --silent || npm install --silent
COPY frontend/ ./
RUN npm run build

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=backend-build /backend/order/target/*.jar app.jar
COPY --from=frontend-build /frontend/.next ./frontend
ENV JAVA_OPTS="-Xms512m -Xmx512m"
CMD ["sh","-c","java $JAVA_OPTS -jar app.jar"]
