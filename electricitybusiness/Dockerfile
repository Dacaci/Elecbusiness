# Étape de build
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape de production
FROM openjdk:17-slim
WORKDIR /app

# Créer un utilisateur non-root
RUN addgroup --system javauser && adduser --system --no-create-home --ingroup javauser javauser

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/electricitybusiness-1.0.0.jar app.jar

# Créer le répertoire pour les logs
RUN mkdir -p /var/log/electricitybusiness && \
    chown -R javauser:javauser /var/log/electricitybusiness

# Définir les permissions
RUN chown -R javauser:javauser /app

# Passer à l'utilisateur non-root
USER javauser

# Exposer le port
EXPOSE 5000

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"] 