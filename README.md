# Notes Patients

Le service **Notes Patients** est un microservice de l'application **MediLabo** responsable de la gestion des notes médicales associées aux patients.

Chaque note est rattachée à un patient grâce à son identifiant et est stockée dans une base de données **MongoDB**.

Ce service permet aux professionnels de santé de consulter, ajouter et supprimer les notes associées aux patients.

---

## Fonctionnalités

Le service permet de :

* Consulter les notes d'un patient.
* Ajouter une nouvelle note médicale.
* Supprimer l'ensemble des notes d'un patient.

---

## Technologies

* Java 21
* Spring Boot 4.0.6
* Spring MVC
* Spring Data MongoDB
* MongoDB
* Maven
* Docker
* Spring Validation
* SpringDoc OpenAPI (Swagger)

---

## Architecture

Le service possède sa propre base de données MongoDB et expose une API REST destinée à être consommée via l'API Gateway de MediLabo.

```text
Client
    │
    ▼
API Gateway
    │
    ▼
Notes Patients
    │
    ▼
MongoDB
```

---

## Configuration

Le service est configuré à l'aide des variables d'environnement suivantes :

| Variable                 | Description                                        | Valeur par défaut |
| ------------------------ | -------------------------------------------------- | ----------------- |
| `HOST_MONGO_BDD`         | Adresse du serveur MongoDB                         | -                 |
| `PORT_MONGO_BDD`         | Port du serveur MongoDB                            | -                 |
| `DATABASE_MONGO_BDD`     | Nom de la base MongoDB utilisée par le service     | -                 |
| `SERVER_PORT`            | Port d'écoute de l'application                     | `8080`            |
| `API_SERVER_GATEWAY_URL` | URL publique de l'API Gateway utilisée par Swagger | -                 |
| `API_GATEWAY_PREFIX`     | Préfixe des routes exposées par la Gateway         | `/api/v1`         |

---

## Base de données

Le service utilise **MongoDB** avec **Spring Data MongoDB**.

Les notes médicales sont stockées sous forme de documents MongoDB dans une collection dédiée.

La connexion à MongoDB est configurée avec les propriétés suivantes :

```properties
spring.mongodb.host=${HOST_MONGO_BDD}
spring.mongodb.port=${PORT_MONGO_BDD}
spring.mongodb.database=${DATABASE_MONGO_BDD}
```

---

## Lancement en local

### Prérequis

* Java 21
* Maven 3.9 ou supérieur
* MongoDB

Pour un développement ou des tests en local, il est recommandé d'utiliser un fichier de configuration dédié :

```text
application-dev.properties
```

Ce fichier permet de séparer la configuration locale de celle utilisée en environnement Docker.

Exemple :

```properties
spring.application.name=notesPatients

spring.mongodb.host=localhost
spring.mongodb.port=27017
spring.mongodb.database=medilabo

server.port=9001

springdoc.swagger-ui.path=/doc
springdoc.api-docs.path=/patient/note/v3/api-docs

api.server.gateway.url=http://localhost:8080/api/v1
```

### Compilation

```bash
mvn clean package
```

### Exécution

```bash
mvn spring-boot:run
```

Ou après compilation :

```bash
java -jar target/*.jar
```

---

## Docker

### Construction de l'image

```bash
docker build -t notes-patients .
```

### Exécution

```bash
docker run \
  -e HOST_MONGO_BDD=host \
  -e PORT_MONGO_BDD=27017 \
  -e DATABASE_MONGO_BDD=medilabo \
  -p 8080:8080 \
  notes-patients
```

> **Remarque :**
> Dans l'architecture MediLabo, ce service est généralement lancé via **Docker Compose** avec l'ensemble des autres microservices.

---

## API REST

| Méthode  | Endpoint                    | Description                                      |
| -------- | --------------------------- | ------------------------------------------------ |
| `GET`    | `/patient/note/{patientId}` | Retourne toutes les notes associées à un patient |
| `POST`   | `/patient/note`             | Ajoute une nouvelle note pour un patient         |
| `DELETE` | `/patient/note/{patientId}` | Supprime toutes les notes associées à un patient |

Les opérations de création utilisent **Jakarta Validation** afin de vérifier la validité des données reçues avant leur traitement.

---

## Sécurité

Le service **Notes Patients** n'implémente aucun mécanisme d'authentification ou d'autorisation.

La sécurité de l'application est assurée en amont par **l'API Gateway**, qui est responsable notamment de :

* l'authentification des utilisateurs ;
* la validation des jetons d'accès (JWT) ;
* le contrôle des accès aux différents microservices.

Ce microservice considère donc que toute requête reçue provient d'une source de confiance (la Gateway) et se concentre uniquement sur sa logique métier.

---

## Documentation Swagger

Chaque microservice MediLabo expose sa propre documentation **OpenAPI** grâce à SpringDoc.

Les chemins Swagger sont configurables dans les propriétés de l'application :

```properties
springdoc.swagger-ui.path=/doc
springdoc.api-docs.path=/patient/note/v3/api-docs
```

### Exécution locale

Lorsque le service est lancé localement (sans Docker), la documentation est accessible directement :

Swagger UI :

```text
http://localhost:<port>/doc
```

Documentation OpenAPI :

```text
http://localhost:<port>/patient/note/v3/api-docs
```

### Déploiement Docker

Dans l'architecture Docker de MediLabo, les microservices ne sont pas exposés directement.

La documentation Swagger est centralisée par l'API Gateway et accessible via :

```text
http(s)://<host>:8080/swagger-ui/index.html
```

Cette interface constitue le point d'entrée unique pour explorer les API de l'application.

La Gateway récupère les documentations OpenAPI exposées par les différents microservices :

```text
/patient/note/v3/api-docs
```

---

## Tests

Les tests peuvent être exécutés avec :

```bash
mvn test
```

Le projet utilise notamment :

* Spring Boot Test
* Spring Data MongoDB Test
* Spring MVC Test

---

## Structure du projet

```text
src
├── controllers
├── model
│   └── repositorys
├── services
├── exceptions
└── configuration
```

---

## Intégration dans MediLabo

Le service **Notes Patients** fait partie de l'architecture microservices de **MediLabo**.

Il est appelé via l'API Gateway et fournit les notes médicales associées aux patients aux autres services de l'application.

Chaque microservice expose sa propre spécification **OpenAPI**, tandis que l'API Gateway centralise ces documentations afin de proposer une interface Swagger unique pour l'ensemble de la plateforme.

