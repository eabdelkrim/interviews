# Proposition d'une solution d'implémentation du Bank Account Kata (FR)
> Ceci est un exercice assez libre d'implémentation à faire chez vous, dans un temps restreint, il a pour objectif d'ouvrir la discussion technique lors d'un futur entretien.

## Consignes
Le Kata suivant est composé d'une partie obligatoire, le [Minimum Valuable Product ou MVP](#minimum-valuable-product---mvp)
et de [fonctionnalités facultatives](#features-bonus) afin d'utiliser le temps restant pour vous démarquer.  
S'il vous manque une information, faites un choix et restez cohérent avec celui-ci.  
_Faites attention à la taille de vos commits et leurs messages._

**Il n'y a pas de "bonne" façon de réaliser ce Kata, nous sommes intéressés par votre choix d'implémentation, votre technique et le respect des contraintes.**

### Livraison
- Le Kata est fourni sous forme d'un repository [Github (https://github.com/eabdelkrim/interviews) sous la branche Github est aem.
- Pour lancer les tests:

```
 mvnw test
 
 ```
- Pour lancer l'application, on exécute la commande suivante sur un terminal Windows:
 
 ```
 mvnw spring-boot:run
 
 ```
 
### Contraintes techniques
* Java@8 
* JUnit@5+  
* Maven

### Conception
Une banque compte plusieurs comptes client.
Un Compte bancaire a un identifiant et un historique de transactions.
Un client peut réaliser plusieurs transactions de différents types sur son compte.

### Choix technique
- Ce projet a été développé avec Spring Boot qui permet de créer des applications stand-alone sans avoir à se soucier de la gestion des dépendances, la configuration et l'exécution.
- Pour la partie UI, le choix s'est porté sur Vaadin, qui est une plateforme de développement d'applications Web Java d'une manière simplifiée en améliorant l'éxperience utilisateur.  
## Implémentation
#### API REST
- Une API REST consommable via http qui permet de:
   - consulter un compte
   - consulter les transactions d'un compte
   - réaliser un dépôt
   - réaliser un retrait

- Sécuriser l'API en utilisant Spring Security, JWT:
   - On génére un JSON Web Token en faisant une requête POST avec [localhost:8080/authenticate] en utilisant notre utilisateur est bank_user avec le corps de requête:
    ```
    {"username":"bank_user", 
     "password":"password"
    }
    ```
   - On utilise le token généré pour consommer notre API REST en resneignant le token dans le header Authorization, par exemple une requête GET [localhost:8080/accounts] pour récupérer tous les comptes de la banque.
- Utiliser une solution non-bloquante avec WebClient de Spring.

#### Clients & Comptes
* La banque a plusieurs clients
* Un client peut avoir plusieurs comptes

#### Persistence
* Proposer une solution de persistence des données avec Spring JPA et la base de données H2.

#### Interface Utilisateur
* Proposer une interface graphique pour interagir avec les services réalisés dans le MVP

#### CI/CD
* Utiliser Gradle au lieu de Maven
* Proposer un system de CI/CD pour le projet
* Proposer des tests End to End à destination de votre livrable
