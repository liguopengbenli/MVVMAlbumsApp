# MVVMAlbumsApp
### Test technique LBC
L'idée est d'afficher toutes les données en utilisant la dernière architecture

![ui_basic](https://user-images.githubusercontent.com/11477823/128469309-e830b3ff-956a-477e-ad20-b31460d0c567.png)

- Le projet est à réaliser sur la plateforme Android (API minimum 21) 
- Le code doit être du Kotlin et tu devras implémenter un système de persistance des données afin que les données puissent être disponibles offline, même après redémarrage de l'application. 

#### Les choix d'architecture 
MVVM,  le découpage des responsabilités des différentes couches, aligner au principe de conception, rendre le test unitaire facile: 
1. La couche de vue - fragment + XML, tous les composants graphiques en interaction directe avec l’utilisateur final
2. La couche de modèle - le modèle représente l’ensemble des entités et des modèles de données dont aura besoin la vue
3. La couche de modèle de vue - viewmodel, elle concerne les opérations de traitements qui s’opèrent entre repository et la vue, j'utilise kotlin flow à la place de live data pour rendre UI réactive
4. La couche d'accès aux données, repository (separation of concerns), une couche interface entre web api, base de données et viewmodel, les données venues de web service sont enregistrés dans la base de données local et uniquement allé chercher dans la base de données local (single source of truth) 
![MVVM](https://user-images.githubusercontent.com/11477823/128477287-73d4eb16-57e4-4fcd-938c-32d036d2ff4e.png)

#### Les choix des librairies

1. Jetpack navigation component: la simplicité, le sûreté et la visibilité. la dernière version prend en charge la navigation du bas, Gestion des transactions des fragments et Safe Args donne la surté de la transition des donnés entre les fragments.
2. Retrofit est basée sur le client REST OKHttp qui est simple à faire des appels à des Web services REST
3. Room, une librairie fournissant des outils pour créer, requêter et manipuler plus facilement des bases de données SQLite, base de données locale
4. Glide, qui gère un cache d’images et aussi erreur d'affichage
5. Dagger/Hilt, réutilisabilité des classes et découplage des dépendances (design principe), Hilt est une bibliothèque qui utilise Dagger en interne et simplifie simplement son utilisation, notamment pour l'injection de viewmodel
6. Coroutine pour effectuer des tâches asynchrones (faire passer en arrière-plan les tâches "bloquantes" qui ralentiraient le thread UI principal). Les coroutines sont légères et rapides, une coroutine peut fournir un très haut niveau de concurrence avec un très faible surcoût. Il fonctionne également très bien avec les librairies mentionées avant. 
7. ViewBinding, simplification du code et vérification des éléments (null safety, type safety) dans le temps de la compilation au lieu d'exécution

 
#### Les performances de l'application 
* La pagination se fait manuellement sans utiliser la Paging 3 lib pour la simplicité et compatibilité de la gestion des erreurs avec networkboundRessource  
* Gestion des "process death" avec SavedStateHandle
* Résister au changement de configuration avec viewmodel 
* Réactivité de UI avec coroutine channel and flow 
* Offline support avec Room 
* Error handling avec networkBoundResource, les erreurs vont être gérés dans repository et envoyer en tant qu'events dans une coroutine Channel   

Au niveau des technos mots clés : kotlin, Jetpack, coroutines, room, data binding, retrofit, MVVM, dagger/hilt
