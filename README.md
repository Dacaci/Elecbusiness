# Service de Recharge

Ce projet est une application de service de recharge qui permet aux utilisateurs de recharger leurs comptes.

## Technologies utilisées

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security avec JWT
- Spring Data JPA
- PostgreSQL
- Maven

### Frontend
- React
- Vite
- Axios
- React Router

## Prérequis

- Java 17 ou supérieur
- Node.js 16 ou supérieur
- PostgreSQL 12 ou supérieur
- Maven

## Installation

### Backend

1. Cloner le repository
```bash
git clone [URL_DU_REPO]
```

2. Configurer la base de données
- Créer une base de données PostgreSQL
- Configurer les paramètres de connexion dans `application.properties`

3. Compiler et exécuter le backend
```bash
cd electricitybusiness
mvn clean install
mvn spring-boot:run
```

### Frontend

1. Installer les dépendances
```bash
cd frontend
npm install
```

2. Démarrer le serveur de développement
```bash
npm run dev
```

## API Documentation

La documentation de l'API est disponible à l'adresse : `http://localhost:5000/swagger-ui.html`

### Endpoints principaux

#### Authentification
- POST `/api/auth/register` - Inscription d'un nouvel utilisateur
- POST `/api/auth/login` - Connexion et obtention du token JWT

#### Recharge
- POST `/api/recharge` - Effectuer une recharge
- GET `/api/recharge/historique` - Consulter l'historique des recharges

## Sécurité

L'application utilise JWT (JSON Web Tokens) pour l'authentification. Les tokens sont valides pendant 24 heures.

Pour utiliser les endpoints protégés :
1. Obtenir un token via `/api/auth/login`
2. Inclure le token dans l'en-tête Authorization : `Bearer <token>`

## Tests

### Backend
```bash
mvn test
```

### Frontend
```bash
npm test
```

## Déploiement

### Backend
```bash
mvn clean package
java -jar target/electricitybusiness-1.0.0.jar
```

### Frontend
```bash
npm run build
```

## Support

Pour toute question ou problème, veuillez contacter : support@example.com 