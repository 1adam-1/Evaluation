# Documentation des Méthodes Implémentées

## Vue d'ensemble
Ce document décrit toutes les méthodes ajoutées aux services selon les exigences du projet.

## 1. EmployeService

### 1.1 afficherTachesRealisees(int employeId)
**Description:** Affiche la liste des tâches réalisées par un employé donné.

**Fonctionnalités:**
- Requête HQL pour récupérer toutes les tâches d'un employé
- Affichage formaté avec: Numéro, Nom de la tâche, Projet, Date début réelle, Date fin réelle
- Tri par date de début réelle
- Gestion des cas où l'employé n'a aucune tâche

**Exemple d'affichage:**
```
=== Tâches réalisées par ALAMI Ahmed ===
Num   Nom Tâche            Projet               Date Début Réelle    Date Fin Réelle     
--------------------------------------------------------------------------------
1     Analyse              Gestion de stock     10/02/2013           20/02/2013          
2     Conception           Gestion de stock     10/03/2013           15/03/2013          
```

### 1.2 afficherProjetsGeres(int employeId)
**Description:** Affiche la liste des projets gérés par un employé (en tant que chef de projet).

**Fonctionnalités:**
- Requête HQL pour récupérer les projets où l'employé est manager
- Affichage formaté avec: ID, Nom du projet, Date de début
- Tri par date de début
- Format de date: "dd MMMM yyyy" (ex: 14 Janvier 2013)

**Exemple d'affichage:**
```
=== Projets gérés par ALAMI Ahmed ===
ID    Nom du Projet                  Date Début          
----------------------------------------------------------------
1     Gestion de stock               14 janvier 2013     
```

## 2. ProjetService

### 2.1 afficherTachesPlanifiees(int projetId)
**Description:** Affiche la liste des tâches planifiées pour un projet donné.

**Fonctionnalités:**
- Affiche les informations du projet (ID, Nom, Date début)
- Liste toutes les tâches avec dates planifiées et prix
- Affichage formaté avec: Num, Nom, Date Début Planifiée, Date Fin Planifiée, Prix (DH)
- Tri par date de début planifiée

**Exemple d'affichage:**
```
Projet : 1		Nom : Gestion de stock		Date début : 14 janvier 2013
Liste des tâches planifiées:
Num   Nom                  Date Début Planifiée Date Fin Planifiée   Prix (DH) 
--------------------------------------------------------------------------------
1     Analyse              20 janvier 2013      28 février 2013      800.00    
2     Conception           01 mars 2013         20 mars 2013         1200.00   
```

### 2.2 afficherTachesRealisees(int projetId)
**Description:** Affiche la liste des tâches réalisées avec les dates réelles d'exécution.

**Fonctionnalités:**
- Format conforme à l'exemple demandé dans l'énoncé
- Affichage: Projet info + Liste des tâches avec dates réelles
- Format de date: "dd/MM/yyyy" pour les tâches
- Tri par date de début réelle

**Exemple d'affichage (Format demandé):**
```
Projet : 4      Nom : Gestion de stock     Date début : 14 Janvier 2013
Liste des tâches:
Num   Nom                  Date Début Réelle   Date Fin Réelle     
----------------------------------------------------------------
12    Analyse              10/02/2013          20/02/2013          
13    Conception           10/03/2013          15/03/2013          
14    Développement        10/04/2013          25/04/2013          
```

## 3. TacheService

### 3.1 afficherTachesPrixSuperieur(double prix)
**Description:** Affiche les tâches dont le prix est supérieur à 1000 DH en utilisant une **requête nommée**.

**Fonctionnalités:**
- Utilise la requête nommée `Tache.findByPrixSuperieur` définie dans l'entité Tache
- Affichage formaté avec: ID, Nom, Projet, Prix (DH)
- Paramètre flexible (peut chercher avec n'importe quel prix)

**Requête nommée dans Tache.java:**
```java
@NamedQuery(name = "Tache.findByPrixSuperieur", 
            query = "SELECT t FROM Tache t WHERE t.prix > :prix")
```

**Exemple d'affichage:**
```
=== Tâches dont le prix est supérieur à 1000.0 DH ===
ID    Nom                            Projet               Prix (DH)      
------------------------------------------------------------------------
2     Conception                     Gestion de stock     1200.00        
3     Développement                  Gestion de stock     2500.00        
5     Design UI                      Site web e-commerce  1500.00        
```

### 3.2 afficherTachesEntreDeuxDates(Date dateDebut, Date dateFin)
**Description:** Affiche les tâches réalisées entre deux dates données.

**Fonctionnalités:**
- Requête HQL avec clause WHERE sur dateDebutReelle et dateFinReelle
- Affichage complet: ID, Nom Tâche, Employé, Projet, Date Début, Date Fin
- Tri par date de début réelle
- Paramètres flexibles pour n'importe quelle période

**Exemple d'affichage:**
```
=== Tâches réalisées entre le 01/03/2013 et le 30/04/2013 ===
ID    Nom Tâche            Employé              Projet               Date Début           Date Fin            
--------------------------------------------------------------------------------------------
2     Conception           ALAMI Ahmed          Gestion de stock     10/03/2013           15/03/2013          
5     Design UI            SEBIHI Fatima        Site web e-commerce  06/03/2013           18/03/2013          
6     Intégration          RAMI Hassan          Site web e-commerce  25/03/2013           12/04/2013          
3     Développement        SEBIHI Fatima        Gestion de stock     10/04/2013           25/04/2013          
```

## 4. Programme de Test (Main.java)

Le programme Main.java contient un test complet qui:

### Phase 1: Création des données de test
1. Crée 3 employés (ALAMI, SEBIHI, RAMI)
2. Crée 2 projets (Gestion de stock, Site web e-commerce)
3. Crée 6 tâches avec prix variés
4. Assigne les tâches aux employés avec dates réelles

### Phase 2: Tests des méthodes
- **Test 1:** Tâches réalisées par un employé
- **Test 2:** Projets gérés par un employé
- **Test 3:** Tâches planifiées pour un projet
- **Test 4:** Tâches réalisées pour un projet (format demandé)
- **Test 5:** Tâches avec prix > 1000 DH (requête nommée)
- **Test 6:** Tâches réalisées entre deux dates

## 5. Structure des Entités

### Employe
- id, nom, prenom, telephone
- Relations: OneToMany avec EmployeTache et Projet

### Projet
- id, nom, dateDebut
- Relations: ManyToOne avec Employe (chef de projet), OneToMany avec EmployeTache

### Tache
- id, nom, dateDebut, dateFin, prix
- Relations: ManyToOne avec Projet, OneToMany avec EmployeTache
- **Requête nommée:** `Tache.findByPrixSuperieur`

### EmployeTache (Table d'association)
- id, dateDebutReelle, dateFinReelle
- Relations: ManyToOne avec Employe, Projet, et Tache

## 6. Technologies Utilisées

- **Hibernate:** ORM pour la persistance
- **Spring Framework:** Injection de dépendances et gestion des transactions
- **JPA Annotations:** @Entity, @OneToMany, @ManyToOne, @NamedQuery
- **HQL:** Hibernate Query Language pour les requêtes personnalisées

## 7. Exécution

Pour exécuter le programme de test:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="ma.projet.Main"
```

## Notes Importantes

1. Toutes les méthodes utilisent `sessionFactory.getCurrentSession()` pour bénéficier de la gestion des transactions Spring
2. Les méthodes d'affichage incluent une gestion des cas null (dates ou objets non trouvés)
3. Le formatage utilise `String.format()` pour un alignement professionnel des colonnes
4. Les dates sont formatées selon le contexte (format court ou long)
5. La requête nommée dans TacheService est réutilisable et maintenue dans l'entité

