# MVVMAlbumsApp
### Test technique LBC
L'idée est d'afficher toutes les données en utilisatant la dernière architecture

![ui_basic](https://user-images.githubusercontent.com/11477823/128469309-e830b3ff-956a-477e-ad20-b31460d0c567.png)

- Le projet est à réaliser sur la plateforme Android (API minimum 21) 
- Le code doit être du Kotlin et tu devras implémenter un système de persistance des données afin que les données puissent être disponibles offline, même après redémarrage de l'application. 

#### Les choix d'architecture 
MVVM,  le découpage des responsabilités des différentes couches, aligner au principe de conception, rendre le test unitaire facile: 
1. La couche de vue - fragment + xml, tous les composants graphiques en interaction directe avec l’utilisateur final
2. La couche de modèle - Le modèle représente l’ensemble des entités et des modèles de données dont aura besoin la vue
3. La couche de modèle de vue - viewmodel, elle concerne les opérations de traitements qui s’opèrent entre repository et la vue, je utilise kotlin flow à la place de live data pour rendre ui réactive
4. La couches d'accès aux données, repository (separation of concerns), une couche interface entre web api, base de données et viewmodel, les données venus de web service sont enregistrés dans la base de données local et uniquement aller chercher dans la base de données local (single source of truth) 
![MVVM](https://user-images.githubusercontent.com/11477823/128477287-73d4eb16-57e4-4fcd-938c-32d036d2ff4e.png)

#### Les choix des librairies

1. Jetpack Navigation component: La simplicité, la sûreté et la visibilité. la dernière version prend en charge la navigation du bas, Gestion des transactions des fragments et Safe Args donne la surté de la transition des donnés entre les fragments.
2. 

- La gestion des changements de configuration 
- Les performances de l'application 
- Les tests
- Sensibilité UX / UI
- Couche business
- Scroll sauvegardé à la rotation 

Au niveau des technos : kotlin, Jetpack, coroutines, room, data binding, MVVM, dagger/hilt
