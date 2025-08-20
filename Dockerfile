# Multi-stage build for backend and frontend

FROM gradle:8.4-jdk17 AS backend-build
WORKDIR /backend
COPY backend/ ./
RUN gradle order:bootJar inventory:bootJar --no-daemon

FROM node:20-alpine AS frontend-build
WORKDIR /frontend
COPY frontend/package.json frontend/tsconfig.json frontend/next.config.js ./
RUN npm ci --silent || npm install --silent
COPY frontend/ ./
RUN npm run build

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=backend-build /backend/order/build/libs/order-*.jar order.jar
COPY --from=backend-build /backend/inventory/build/libs/inventory-*.jar inventory.jar
COPY --from=frontend-build /frontend/.next ./frontend
ENV JAVA_OPTS="-Xms512m -Xmx512m"
CMD ["sh","-c","java $JAVA_OPTS -jar order.jar"]