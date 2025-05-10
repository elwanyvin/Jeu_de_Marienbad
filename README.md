Ces deux versions ont été réalisées avec l'aide de Bastian LE OUEDEC dans le cadre de mes études à l'IUT de Vannes.
Pour ce projet, certaines fonctionnalités de Java ont été "bloquées" par les professeurs (principalement les fonctionnalités de POO de Java) 
de manière à nous faire utiliser Java comme un langage de programmation algorithmique simple (type python).
Ceci explique la présences des fichiers SimpleInput et Start, qui ne sont pas de ma conception mais de celle des professeurs de l'IUT,
qui servent respectivement à récupérer une entrée utilisateur et lancer le programme en contournant le schéma classique Java (avec public static void main(String[] args)).

Le fichier MarienbadJvsJ_LeOuedec_Yvin contient la version Joueur contre Joueur du jeu, et le fichier MarienbadJvsO_LeOuedec_Yvin contient la version Joueur contre Ordinateur.
Cette version contient 3 niveau de difficultés :

- Niveau 1 : L’ordinateur ne joue que des coups aléatoires légaux ( i. e. sur des lignes ou
c’est possible et en retirant un nombre d’allumettes strictement positifs et inférieur ou
égal au nombre d’allumettes de la ligne)
- Niveau 2 : L’ordinateur joue des coups aléatoires légaux jusqu’à ce qu’il ne reste plus
que 3 lignes non vides ou moins, puis il passe en stratégie gagnante (plus détaillée dans
le niveau 3)
- Niveau 3 : L’ordinateur utilise la stratégie gagnante dès le début de la partie.
Pour ce faire, à chaque tour, il calcule les valeurs binaires du nombre d’allumettes de
chaque ligne, puis effectue la somme en base 10 de ces nombres par colonne (chaque
colonne est une puissance de 2).
Si toutes les colonnes sont paires, l’ordinateur joue un coup au hasard.
Si il y a au moins une colonne impaire, l’ordinateur essaie tous les coups légaux
possibles jusqu’à trouver un coup qui rend toutes les colonnes paires.
S' il n’en trouve pas, alors il joue au hasard.
