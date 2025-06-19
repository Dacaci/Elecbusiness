#!/bin/bash

# Arrêter le script en cas d'erreur
set -e

echo "Démarrage du déploiement du frontend..."

# Installer les dépendances
echo "Installation des dépendances..."
npm install

# Construire l'application
echo "Construction de l'application..."
npm run build

# Créer le répertoire de déploiement s'il n'existe pas
echo "Préparation du répertoire de déploiement..."
sudo mkdir -p /var/www/electricitybusiness

# Copier les fichiers de build
echo "Copie des fichiers de build..."
sudo cp -r dist/* /var/www/electricitybusiness/

# Configurer Nginx
echo "Configuration de Nginx..."
sudo cat > /etc/nginx/sites-available/electricitybusiness << EOL
server {
    listen 80;
    server_name electricitybusiness.com www.electricitybusiness.com;

    root /var/www/electricitybusiness;
    index index.html;

    location / {
        try_files \$uri \$uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:5000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host \$host;
        proxy_cache_bypass \$http_upgrade;
    }
}
EOL

# Activer le site
echo "Activation du site Nginx..."
sudo ln -sf /etc/nginx/sites-available/electricitybusiness /etc/nginx/sites-enabled/

# Tester la configuration Nginx
echo "Test de la configuration Nginx..."
sudo nginx -t

# Redémarrer Nginx
echo "Redémarrage de Nginx..."
sudo systemctl restart nginx

echo "Déploiement du frontend terminé avec succès!" 