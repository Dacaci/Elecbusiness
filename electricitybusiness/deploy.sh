#!/bin/bash

# Arrêter le script en cas d'erreur
set -e

echo "Démarrage du déploiement..."

# Compiler le projet
echo "Compilation du projet..."
./mvnw clean package -DskipTests

# Créer le répertoire de déploiement s'il n'existe pas
echo "Préparation du répertoire de déploiement..."
mkdir -p /opt/electricitybusiness

# Copier le fichier JAR
echo "Copie du fichier JAR..."
cp target/electricitybusiness-1.0.0.jar /opt/electricitybusiness/

# Créer le fichier de configuration systemd
echo "Configuration du service systemd..."
cat > /etc/systemd/system/electricitybusiness.service << EOL
[Unit]
Description=Electricity Business API
After=network.target

[Service]
User=electricitybusiness
WorkingDirectory=/opt/electricitybusiness
ExecStart=/usr/bin/java -jar electricitybusiness-1.0.0.jar --spring.profiles.active=prod
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
EOL

# Recharger systemd
echo "Rechargement de systemd..."
systemctl daemon-reload

# Redémarrer le service
echo "Redémarrage du service..."
systemctl restart electricitybusiness

echo "Déploiement terminé avec succès!" 