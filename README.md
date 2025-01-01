# Documentation du projet Java Spring Boot

## Introduction

Ce projet est une application back-end développée avec **Java Spring Boot**. Il permet la gestion de produits, l'authentification sécurisée avec **JWT**, ainsi que la gestion de paniers et de listes de souhaits.

### Fonctionnalités principales :
- Gestion des produits : création, récupération, mise à jour et suppression.
- Authentification avec JWT.
- Gestion des utilisateurs et des comptes.
- Gestion des paniers et listes de souhaits.

## Structure du projet

### Organisation des fichiers

Voici une vue simplifiée des principaux packages et fichiers du projet :

- `com.alten.entities` : Contient les entités JPA (ex : `Product`, `AppUser`).
- `com.alten.service` : Services métier (ex : `ProductService`, `AccountService`).
- `com.alten.web` : Contrôleurs exposant les endpoints REST (ex : `ProductController`, `AccountRestController`).
- `com.alten.repository` : Interfaces de repository Spring Data JPA.
- `com.alten.filters` : Filtres pour la gestion de la sécurité avec JWT.

### Dépendances principales (extrait de `pom.xml`)

- **Spring Boot Starter Web** : Pour développer des API REST.
- **Spring Security** : Pour la gestion de la sécurité.
- **Java JWT** : Pour générer et valider les tokens JWT.
- **H2 Database** : Base de données embarquée.
- **Springdoc OpenAPI** : Génération de la documentation Swagger.
- **JUnit et Mockito** : Pour les tests unitaires.

## Endpoints API

### Documentation Swagger

Une documentation complète des API est générée automatiquement via **Swagger**. Voici quelques exemples de routes :

#### Gestion des produits :
- `[POST] /products` : Créer un nouveau produit.
- `[GET] /products` : Récupérer tous les produits.
- `[PATCH] /products/{id}` : Mettre à jour un produit existant.
- `[DELETE] /products/{id}` : Supprimer un produit.

#### Gestion des utilisateurs :
- `[POST] /account` : Créer un compte utilisateur.
- `[POST] /token` : Générer un token JWT.

#### Gestion des paniers :
- `[POST] /cart/{id}` : Ajouter un produit au panier.
- `[GET] /cart` : Récupérer les produits du panier.

#### Gestion des listes de souhaits :
- `[POST] /wishlist/{id}` : Ajouter un produit à la liste de souhaits.
- `[GET] /wishlist` : Récupérer la liste de souhaits.

## Résultats des tests

### Tests unitaires

Les tests ont été implémentés pour les services et contrôleurs principaux. Voici un extrait des résultats :

- **Taux de couverture :** 85 % des lignes de code.
- **Tests réussis :** 32 sur 32.
- **Frameworks utilisés :** JUnit 5 et Mockito.

Les résultats complets sont disponibles dans le fichier de rapport de tests fourni.

## Utilisation de la collection Postman

Une collection Postman est disponible pour tester les différents endpoints. Elle inclut des scénarios pour :

- Créer un compte utilisateur.
- Authentifier un utilisateur.
- Tester les routes sécurisées avec un token JWT.
- Vérifier les fonctionnalités des produits, paniers et listes de souhaits.

Pour importer cette collection, utilisez le fichier `Test KATA alten.postman_collection.json` fourni.

## Sécurité avec JWT

L'application utilise des **tokens JWT** pour sécuriser les endpoints.

### Fonctionnement :
1. **Création d'un compte** : Un utilisateur peut s'inscrire via la route `[POST] /account`.
2. **Authentification** : Envoi des identifiants à `[POST] /token` pour recevoir un token JWT.
3. **Accès sécurisé** : Ajout du token dans l'en-tête `Authorization` pour accéder aux routes protégées.

### Gestion des autorisations :
- Seul l'utilisateur `admin@admin.com` a le droit de créer, modifier ou supprimer des produits.
- Les autres utilisateurs peuvent uniquement consulter les produits et gérer leurs paniers et listes de souhaits.

## Conclusion

Cette application est une solution complète pour gérer des produits avec un système sécurisé et bien documenté. Les outils Swagger et Postman facilitent les tests, et la couverture des tests unitaires assure une certaine fiabilité.
