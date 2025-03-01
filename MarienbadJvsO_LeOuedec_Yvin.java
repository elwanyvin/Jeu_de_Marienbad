/**
 *Jeu de Nim en Joueur vs Ordinateur avec niveaux de difficultés
 *@author Elwan Yvin et Bastian Le Ouedec
 */
class MarienbadJvsO_LeOuedec_Yvin{
	void principal() 	{
		String nom = SimpleInput.getString("Nom du joueur : ");
		System.out.println();
		String ordi = "Ordinateur";
		String joueur = nom;

		int nbLignes = SimpleInput.getInt("Nombre de lignes : ");
		System.out.println();
		int [] stockAllumettes = initJeuTab(nbLignes);

		while (nbLignes <= 1){
			nbLignes = SimpleInput.getInt("Nombre incorrect, choisissez un nombre de ligne correct : ");
			System.out.println();
		}

		System.out.println("1 : Facile");
		System.out.println("2 : Normal");
		System.out.println("3 : Difficile");
		System.out.println();
		int diff = SimpleInput.getInt("Choisissez la difficulte : ");
		System.out.println();

		while (diff < 1 || diff > 3) {
			diff = SimpleInput.getInt("Veuillez entrer une valeur correcte. Choisissez la difficulte : ");
			System.out.println();
		}

		int commence = SimpleInput.getInt("Qui commence ? 1 pour le joueur, 0 pour l'ordinateur : ");
		System.out.println();
		while (commence != 1 && commence != 0){
			commence = SimpleInput.getInt("Mauvaise valeur choisie, choisissez 1 ou 0 : ");
			System.out.println();
		}

		if(commence == 1){
			joueur = nom;
		}else{
			joueur = ordi;
		}

		System.out.println("Chargement d'une partie a " + nbLignes + " lignes en difficulte " + diff);
		displayJeu(stockAllumettes);

		do{
			if (joueur.equals(nom)){
				jeu(joueur,stockAllumettes);
				joueur=ordi;
			}else if (joueur.equals(ordi)){
				jeuOrdi(diff,stockAllumettes,nbLignes);
				joueur = nom;
			}
		}while(!checkwin(stockAllumettes));

		if (joueur.equals(nom)){
			System.out.print("L'ordinateur remporte la partie.");
		}else{
			System.out.print("Bravo "+nom+", vous remportez la partie !");
		}
	}

	/**
	 * Tour d'un joueur
	 * @param nomJoueur Joueur dont c'est le tour
	 * @param stock tableau contenant le nombre d'allumettes pour chaque ligne
	 * @return wincon Vrai si le joueur a gagné, Faux sinon
	 */
	boolean jeu(String nomJoueur, int [] stock) {
		boolean wincon = false;

		System.out.println("C'est le tour de " + nomJoueur);
		System.out.println();

		int ligne = SimpleInput.getInt("Entrez le numero de la ligne dont vous voulez enlever des allumettes : ");
		while(ligne<0||ligne>stock.length-1||stock[ligne]==0){
			ligne = SimpleInput.getInt("Numero incorrect. Recommencez : ");
		}
		System.out.println();

		int nbAllumettes = SimpleInput.getInt("Entrez le nombre d'allumettes que vous voulez enlever : ");
		while (nbAllumettes > stock[ligne] || nbAllumettes<=0){
			nbAllumettes = SimpleInput.getInt("Nombre incorrect. Recommencez : ");
		}
		System.out.println();

		modifie(ligne,nbAllumettes,stock);
		displayJeu(stock);

		if (checkwin(stock)){
			wincon = true;
		}
		return wincon;

	}

	/**
	 * Tour de l'ordinateur
	 * @param difficulte Difficulté de l'ordinateur
	 * @param stock Tableau contenant le nombre d'allumettes par ligne
	 * @param nbLignes Nombre de lignes dans le jeu
	 * @return wincon Vrai si l'ordinateur a gagné, Faux sinon
	 */
	void jeuOrdi(int difficulte, int [] stock, int nbLignes){
		System.out.println("C'est le tour de l'ordinateur ");

		if (difficulte==1){
			ordiNiveau1(stock, nbLignes);
		}else if (difficulte==2) {
			ordiNiveau2(stock, nbLignes);
		}else{
			ordiNiveau3(stock, nbLignes);
		}
	}

	/**
	 * Stratégie de l'ordinateur en difficulté 1 (décisions aléatoires)
	 * @param stock Tableau contenant le nombre d'allumettes par ligne
	 * @param nbLignes Nombre d'allumettes de la ligne
	 */
	void ordiNiveau1(int [] stock, int nbLignes){

		int ligne = (int) (Math.random()*nbLignes);

		while (stock[ligne] == 0) {
			ligne = (int) (Math.random() * nbLignes);
		}

		int nbAllumettes = (int) (Math.random() * stock[ligne]-1) + 1;

		System.out.println("L'ordinateur supprime " + nbAllumettes + " allumette(s) en ligne " + ligne);
		System.out.println();

		modifie(ligne,nbAllumettes,stock);
		System.out.println("La ligne "+ligne+" contient maintenant "+ stock[ligne]+" allumettes.");
		System.out.println();
		displayJeu(stock);
	}

	/**
	 * Stratégie de l'ordinateur en difficulté 2 (décisions aléatoires puis stratégie gagnante)
	 * @param stock Tableau contenant le nombre d'allumettes par ligne
	 * @param nbLignes Nombre d'allumettes de la ligne
	 */
	void ordiNiveau2(int [] stock, int nbLignes){

		int lockIn = nbLignesNonVides(stock);
		if (lockIn <= 3) {
			System.out.println("Concentration maximale");
			System.out.println();
			ordiNiveau3(stock, nbLignes);
		} else {
			ordiNiveau1(stock, nbLignes);
		}
	}

	/**
	 * Stratégie de l'ordinateur en difficulté 3 (stratégie gagnante)
	 * @param stock Tableau contenant le nombre d'allumettes par ligne
	 * @param nbLignes Nombre de lignes dans le jeu
	 */
	void ordiNiveau3(int[] stock, int nbLignes) {
		int[][] bin = decToBin(stock); // Tableau contenant les valeurs binaires du nombre d'allumettes de chaque ligne
		int[] sommeBits = sommeColonne(bin, nbLignes); // Résultat de l'addition binaire

		// Si toutes les colonnes sont paires, l'ordinateur joue un coup aléatoire
		if (colonnesPairesOnly(sommeBits)) {
			ordiNiveau1(stock, nbLignes);
		} else {
			// Teste tous les coups jusqu'à trouver un coup gagnant
			boolean coupTrouve = false;
			int nbAllumettesARetirer=1;
			int ligneAModifier =0;

			for (int ligne=0; ligne < stock.length && !coupTrouve; ligne++) {
				if (stock[ligne] > 0){
					for (int coup=0; coup <= stock[ligne] && !coupTrouve; coup++) {

						//Essaie un coup sur une copie du jeu
						int [] copieStock = copierTableau(stock);
						modifie(ligne,coup,copieStock);

						//Vérifie si le coup est un coup gagnant
						int[][] copieStockBin = decToBin(copieStock);
						int[] sommeBitsCopieStock = sommeColonne(copieStockBin, nbLignes);

						if(colonnesPairesOnly(sommeBitsCopieStock)){
							nbAllumettesARetirer=coup;
							ligneAModifier=ligne;
							coupTrouve = true;
						}
					}
				}
			}
			if(!coupTrouve){
				ordiNiveau1(stock, nbLignes);
			}else {
				System.out.println("L'ordinateur supprime " + nbAllumettesARetirer + " allumette(s) en ligne " + ligneAModifier);
				System.out.println();
				modifie(ligneAModifier, nbAllumettesARetirer, stock);
				System.out.println("La ligne "+ligneAModifier+" contient maintenant "+ stock[ligneAModifier]+" allumettes.");
				System.out.println();
				displayJeu(stock);
			}
		}
	}

	/**
	 * Vérifie si des lignes contiennent encore des allumettes
	 * @param stock Tableau contenant le nombre d'allumettes par ligne
	 */
	int nbLignesNonVides(int[] stock) {
		int count=0;
		for (int i=0; i<stock.length; i++) {
			if (stock[i] > 0) {
				count+=1;
			}
		}
		return count;
	}

	/**
	 * Affiche une ligne du jeu
	 * @param nbAllumettes Nombre d'allumettes de la ligne
	 */
	void displayLigne(int nbAllumettes) {
		String barre = " | ";

		for(int j=1; j<=nbAllumettes;j++){
			System.out.print(barre);
		}
		System.out.println();
	}

	/**
	 * Affiche le jeu complet
	 * @param stock tableau contenant le nombre d'allumettes pour chaque ligne
	 */
	void displayJeu(int []stock) {
		System.out.println();
		System.out.println("--------------------");
		System.out.println();

		for (int i = 0; i < stock.length; i++) {
			System.out.print(i + " : ");
			displayLigne(stock[i]);

		}
		System.out.println();
		System.out.println("--------------------");
		System.out.println();
	}

	/**
	 * Modifie le nombre d'allumettes d'une ligne dans le stock
	 * @param ligne Ligne à modifier
	 * @param nbAllumettes Nombre d'allumettes de la ligne
	 * @param stock tableau contenant le nombre d'allumettes pour chaque ligne
	 */
	void modifie(int ligne, int nbAllumettes, int[] stock){
		stock[ligne] -= nbAllumettes;
	}

	/**
	 * Vérifie si un joueur a gagné
	 * @param stock tableau contenant le nombre d'allumettes pour chaque ligne
	 * @return result Vrai si un joueur a gagné, Faux sinon
	 */
	boolean checkwin(int[] stock){
		boolean result=true;

		for(int i=0; i < stock.length; i++){
			if (stock[i] != 0){
				result = false;
			}
		}
		return result;
	}

	/**
	 * Initialise le stock
	 * @param nbLignes Nombre de lignes du jeu
	 * @return stock tableau contenant le nombre d'allumettes par ligne
	 */
	int[] initJeuTab (int nbLignes){
		int [] stock = new int [nbLignes];
		for(int i = 0;i<nbLignes;i++){
			stock[i] = (2*i)+1;
		}
		return stock;
	}

	/**
	 * Convertit une valeur décimale en binaire
	 * @param stock Tableau contenant le nombre d'allumettes par ligne
	 * @return bin tableau à 2 dimensions contenant les valeurs binaires de chaque nombre de stock
	 */
	int[][] decToBin(int [] stock){
		int[]copie=new int[stock.length]; //Copie du stock pour éviter de le modifier
		int[][] bin = new int[stock.length][maxBits(nbMaxAllumettesDansLigneDuJeu(stock))];// Tableau à double entrée contenant les valeurs binaires des nombres d'allumettes pour chaque ligne

		//Copie du tableau de stockage
		for (int i=0;i<stock.length;i++){
			copie[i]=stock[i];
		}
		//Calcul du nombre d'allumettes en binaire
		for(int i = 0; i < stock.length; i++){
			int index = bin[0].length-1; // On réinitialise `index` pour chaque ligne
			while (copie[i] > 0) {
				bin[i][index] = copie[i] % 2;  // Stocker le bit dans le tableau à la position `index`
				copie[i] = copie[i] / 2;  // Diviser la valeur pour obtenir le bit suivant
				index--;  // Décrémenter l'index pour avancer vers le bit moins significatif
			}
		}
		return bin;
	}

	/**
	 * Fait la somme des nombres binaires par colonne
	 * @param tab Tableau 2D stockant les valeurs binaires de chaque ligne
	 * @param nbLignes Nombre de lignes dans le jeu
	 * @return result Résultat de la somme
	 */
	int[] sommeColonne(int[][] tab, int nbLignes) {
		int maxBitPosition = -1;
		int[] result;

		// Trouver la position du bit le plus significatif
		for (int i = 0; i < nbLignes; i++) {
			for (int j = tab[i].length - 1; j >= 0; j--) {
				if (tab[i][j] != 0 && j > maxBitPosition) {
					maxBitPosition = j;
				}
			}
		}

		// Si tous les bits sont à 0, renvoyer un tableau vide
		if (maxBitPosition == -1) {
			result = new int[0];
		} else {
			// Créer le tableau de résultat avec la taille du bit le plus significatif + 1
			result = new int[maxBitPosition + 1];

			// Calculer la somme des bits pour chaque colonne (jusqu'à maxBitPosition)
			for (int i = 0; i <= maxBitPosition; i++) {
				int sum = 0; // Somme des bits pour chaque colonne
				for (int j = 0; j < nbLignes; j++) {
					// Vérification que la ligne j a bien assez de colonnes
					if (i < tab[j].length) {
						sum += tab[j][i]; // Additionne les bits de la même colonne pour chaque ligne
					}
				}
				result[i] = sum;
			}
		}
		return result;
	}

	/**
	 * Donne le nombre maximum d'allumettes dans une ligne du jeu
	 * @param stock Tableau contenant le nombre d'allumettes par ligne
	 * @return max Nombre maximum d'allumettes dans la ligne
	 */
	int nbMaxAllumettesDansLigneDuJeu(int[]stock){
		int max=0;
		for(int i=0; i < stock.length; i++){
			if(stock[i]>max){
				max=stock[i];
			}
		}
		return max;
	}

	/**
	 * Calcule le nombre de bits nécéssaires pour coder en binaire un nombre
	 * @param nbMax nombre à coder en binaire
	 * @return count nombre de bits nécéssaires pour coder nbMax
	 */
	int maxBits(int nbMax){
		int count=0;
		while(Math.pow(2, count)<=nbMax){
			count++;
		}
		return count;
	}

	/**
	 * Copie les éléments d'un tableau source dans un nouveau tableau de destination.
	 *
	 * @param source le tableau à copier
	 * @return un nouveau tableau contenant les mêmes éléments que le tableau source
	 */
	int[] copierTableau(int[] source) {
		int[] destination = new int[source.length];
		for (int i = 0; i < source.length; i++) {
			destination[i] = source[i];
		}
		return destination;
	}

	/**
	 * Vérifie si toutes les colonnes (éléments du tableau) sont des nombres pairs.
	 *
	 * @param sommeBits le tableau d'entiers représentant les sommes des bits
	 * @return true si toutes les colonnes sont paires, false si au moins une colonne est impaire
	 */
	boolean colonnesPairesOnly(int[] sommeBits) {
		boolean existeColonneImpaire = false;
		int i = 0;
		boolean result = true;

		while (i < sommeBits.length) {
			if (sommeBits[i] % 2 != 0) { // Vérification de la présence d'une colonne impaire
				existeColonneImpaire = true; // Mise à jour de la variable
				i = sommeBits.length; // Sortir de la boucle
			} else {
				i++; // Passer à la colonne suivante
			}
		}

		if (existeColonneImpaire) {
			result = false;
		}

		return result;
	}

	/**
	 * Affiche le contenu d'un tableau d'entiers sous la forme {a,b,c,...}.
	 * Si le tableau est vide, affiche {}.
	 * @param t Le tableau d'entiers à afficher.
	 */
	void displayTab(int[] t){

		int i = 0;
		if (t.length == 0) {
			System.out.print("{}");
		}else{
			System.out.print("{");
			while(i<t.length-1){
				System.out.print(t[i] + ",");
				i=i+1;
			}
			System.out.print(t[i]+"}");
			System.out.println();
		}
	}

	/**
	 * Compare deux tableaux d'entiers pour vérifier leur égalité.
	 *
	 * @param tab1 Premier tableau à comparer.
	 * @param tab2 Deuxième tableau à comparer.
	 * @return true si les deux tableaux sont égaux, sinon false.
	 */
	boolean compareTableaux(int[] tab1, int[] tab2) {
		boolean sontEgaux = true;

		if (tab1.length != tab2.length) {
			sontEgaux = false;
		}
		int i = 0;
		while (i < tab1.length && sontEgaux) {
			if (tab1[i] != tab2[i]) {
				sontEgaux = false;
			}
			i = i + 1;
		}
		return sontEgaux;
	}

	/**
	 * Teste la méthode ordiNiveau1().
	 */
	void testOrdiNiveau1(int [] stock) {
		System.out.println();
		System.out.println("*** testOrdiNiveau1");

		// Initialisation d'une copie du jeu
		int[] copieStock = copierTableau(stock);

		//Appel de ordiNiveau1 sur cette copie
		ordiNiveau1(copieStock, copieStock.length);

		//Vérification de la légalité du coup
		boolean coupLegal = true;
		if (compareTableaux(copieStock, stock) == true) {
			coupLegal = false;
		}else{
			for(int i=0;i<copieStock.length;i++){
				if(copieStock[i]<0){
					coupLegal = false;
				}
			}
		}
		if (coupLegal) {
			System.out.println("OK");
		} else {
			System.out.println("ERREUR");
		}
	}

	/**
	 * Teste un cas de la méthode ordiNiveau1().
	 * @param stock Stock initial des allumettes
	 * @param nbLignes Nombre de lignes dans le jeu
	 * @param result Stock attendu après modification
	 */
	void testCasOrdiNiveau1(int[] stock, int nbLignes, int[] result) {
		System.out.println("ordiNiveau1("+stock+"," + nbLignes + ")");

		// Appel de la méthode à tester avec le nombre de lignes
		ordiNiveau1(stock, nbLignes);

		// Vérification du résultat
		displayTab(stock);
		if (compareTableaux(stock, result)) {
			System.out.println("OK");
		} else {
			System.out.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode ordiNiveau3().
	 */
	void testOrdiNiveau3() {
		System.out.println();
		System.out.println("*** testOrdiNiveau3");

		// Initialisation des coups gagnants
		int[][] coupsGagnants1 = {{1, 1}, {1, 2}, {2, 4}, {2, 3}, {2, 2}, {2, 1}};
		int[][] coupsGagnants2 = {{0, 1}, {1, 1}, {2, 1}};
		int[][] coupsGagnants3 = {{0, 3}};
		int[][] coupsGagnants4 = {{0, 1}};

		// Cas de test : simuler un coup de l'ordinateur niveau 3
		testCasOrdiNiveau3(new int[]{1, 3, 5}, coupsGagnants1);
		testCasOrdiNiveau3(new int[]{1, 1, 1}, coupsGagnants2);
		testCasOrdiNiveau3(new int[]{3, 2, 2}, coupsGagnants3);
		testCasOrdiNiveau3(new int[]{1, 0, 0}, coupsGagnants4);

	}

	/**
	 * Teste un cas de la méthode ordiNiveau3().
	 * @param stock Stock initial des allumettes
	 * @param result Stock attendu après modification
	 */
	void testCasOrdiNiveau3(int[] stock, int[][] result) {
		System.out.println("ordiNiveau3()");

		//Copie du stock
		int[]copieStock = copierTableau(stock);
		// Appel de la méthode à tester
		ordiNiveau3(copieStock,copieStock.length);

		// Vérification du résultat
		boolean coupOpti = false;
		int ligneChange = 0;
		int nbAllumettesChange=0;

		for(int i=0;i<copieStock.length;i++){
			if (copieStock[i] != stock[i]){
				ligneChange=i;
				nbAllumettesChange=stock[i]-copieStock[i];
			}
		}
		for(int j=0;j<result.length;j++){
			if(result[j][0]==ligneChange && result[j][1]==nbAllumettesChange){
				coupOpti=true;
			}
		}
		if (coupOpti == true) {
			System.out.println("OK");
		} else {
			System.out.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode nbLignesNonVides().
	 */
	void testNbLignesNonVides() {
		System.out.println();
		System.out.println("*** testNbLignesNonVides");

		// Cas de test 1 : 3 lignes non vides
		int[] stock1 = {1, 3, 5};
		testCasNbLignesNonVides(stock1, 3);

		// Cas de test 2 : 2 lignes non vides
		int[] stock2 = {1, 0, 5};
		testCasNbLignesNonVides(stock2, 2);

		// Cas de test 3 : aucune ligne non vide
		int[] stock3 = {0, 0, 0};
		testCasNbLignesNonVides(stock3, 0);
	}

	/**
	 * Teste un cas de la méthode nbLignesNonVides().
	 * @param stock Stock des allumettes
	 * @param result Nombre attendu de lignes non vides
	 */
	void testCasNbLignesNonVides(int[] stock, int result) {
		System.out.print("nbLignesNonVides("+stock+") = " + result + " : ");

		// Appel de la méthode à tester
		int resExec = nbLignesNonVides(stock);

		// Vérification du résultat
		if (resExec == result) {
			System.out.println("OK");
		} else {
			System.out.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode decToBin().
	 */
	void testDecToBin() {
		System.out.println();
		System.out.println("*** testDecToBin");

		int[] input = {5, 10, 3}; // 5 = 101, 10 = 1010, 3 = 11
		int[][] expectedOutput = {
				{0, 1, 0, 1},     // 5 en binaire
				{1, 0, 1, 0},  // 10 en binaire
				{0, 0, 1, 1}      // 3 en binaire
		};
		testCasDecToBin(input,expectedOutput);

		int[] input3 = {1}; // 1 en binaire
		int[][] expectedOutput3 = {
				{1} // 1 en binaire
		};
		testCasDecToBin(input3, expectedOutput3);
	}

	/**
	 * Teste un appel de la méthode decToBin().
	 * @param tab Tableau d'entiers à convertir en binaire
	 * @param result Tableau d'entiers binaires correspondanat au résultat attendu
	 */
	void testCasDecToBin(int[]tab, int [][] result){
		// Appel de la méthode
		int[][] resExec = decToBin(tab);

		// Vérification des résultats
		boolean success = true;
		for (int i = 0; i < result.length; i++) {
			for(int j = 0; j<result[0].length; j++){
				if( resExec[i][j] != result [i][j]){
					success = false;
				}
			}
		}
		if (success) {
			System.out.println("OK");
		}else{
			System.out.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode sommeColonne().
	 */
	void testSommeColonne() {
		System.out.println();
		System.out.println("*** testSommeColonne");

		// Cas normal
		int[][] tab1 = {{1, 0, 1}, {1, 1, 0}, {0, 0, 1}};
		int[] res1={2,1,2};

		testCasSommeColonne(tab1, 3,res1);

		// Cas avec un tableau vide
		int[][] tab2 = {};
		int[] res2 = {}; // Résultat attendu : tableau vide
		testCasSommeColonne(tab2, 0,res2);

		// Cas avec une seule colonne
		int[][] tab3 = {{1}, {1}, {0}};
		int[] res3 = {2}; // Résultat attendu : somme de la seule colonne
		testCasSommeColonne(tab3,3, res3);

		// Cas avec un tableau de taille 1x1
		int[][] tab6 = {{1}};
		int[] res6 = {1}; // Résultat attendu : somme de la seule colonne
		testCasSommeColonne(tab6,1, res6);
	}

	/**
	 * Teste un cas de la méthode sommeColonne().
	 * @param tab Tableau de nombres binaires
	 * @param nbLignes Colonne à tester
	 * @param result Résultat attendu de la somme
	 */
	void testCasSommeColonne(int[][] tab, int nbLignes, int[] result) {
		System.out.print("sommeColonne(" + nbLignes + ") = ");
		displayTab(result);
		System.out.println(" : ");

		// Appel de la méthode à tester
		int[] resExec = sommeColonne(tab, nbLignes);

		// Vérification du résultat
		if (compareTableaux(resExec,result) == true) {
			System.out.println("OK");
		} else {
			System.err.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode nbMaxAllumettesDansLigneDuJeu().
	 */
	void testNbMaxAllumettesDansLigneDuJeu() {
		System.out.println();
		System.out.println("*** testNbMaxAllumettesDansLigneDuJeu");

		// Cas avec 5 allumettes au maximum
		int[] jeu1 = {1, 3, 5};
		testCasNbMaxAllumettesDansLigneDuJeu(jeu1, 5);

		// Cas avec 3 allumettes au maximum
		int[] jeu2 = {1, 0, 3};
		testCasNbMaxAllumettesDansLigneDuJeu(jeu2, 3);

		// Cas avec des allumettes réparties sur une ligne unique
		int[] jeu3 = {7};
		testCasNbMaxAllumettesDansLigneDuJeu(jeu3, 7);

		// Cas avec plusieurs lignes, toutes à zéro
		int[] jeu4 = {0, 0, 0};
		testCasNbMaxAllumettesDansLigneDuJeu(jeu4, 0);

	}

	/**
	 * Teste un cas de la méthode nbMaxAllumettesDansLigneDuJeu().
	 * @param jeu Tableau représentant le jeu
	 * @param result Résultat attendu
	 */
	void testCasNbMaxAllumettesDansLigneDuJeu(int[] jeu, int result) {
		System.out.print("nbMaxAllumettesDansLigneDuJeu() = " + result + " : ");

		// Appel de la méthode à tester
		int resExec = nbMaxAllumettesDansLigneDuJeu(jeu);

		// Vérification du résultat
		if (resExec == result) {
			System.out.println("OK");
		} else {
			System.err.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode maxBits().
	 */
	void testMaxBits() {
		System.out.println();
		System.out.println("*** testMaxBits");

		// Cas avec des nombres binaires de longueur max 3
		int[] tab1 = {5, 3, 1};
		testCasMaxBits(5, 3);

		// Cas avec des nombres binaires de longueur max 4
		int[] tab2 = {8, 4, 2};
		testCasMaxBits(8, 4);

		// Cas avec des nombres binaires de longueur max 1
		int[] tab3 = {1};
		testCasMaxBits(1, 1);

	}

	/**
	 * Teste un cas de la méthode maxBits().
	 * @param nbMax Nombre max d'allumettes en jeu
	 * @param result Résultat attendu (nombre max de bits)
	 */
	void testCasMaxBits(int nbMax, int result) {
		System.out.print("maxBits() = " + result + " : ");

		// Appel de la méthode à tester
		int resExec = maxBits(nbMax);

		// Vérification du résultat
		if (resExec == result) {
			System.out.println("OK");
		} else {
			System.err.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode colonnesPairesOnly().
	 */
	void testColonnesPairesOnly() {
		System.out.println();
		System.out.println("*** testColonnesPairesOnly");

		// Cas de test : tableau avec des colonnes paires
		int[] tab1 = {0,2,4};
		testCasColonnesPairesOnly(tab1, true);

		// Cas de test : tableau avec des colonnes impaires
		int[] tab2 = {0,1,2};
		testCasColonnesPairesOnly(tab2, false);

		// Cas avec un tableau vide
		int[] tab3 = {};
		testCasColonnesPairesOnly(tab3, true); // true car aucune colonne impaire

		// Cas avec une seule colonne paire
		int[] tab4 = {2};
		testCasColonnesPairesOnly(tab4, true);

		// Cas avec une seule colonne impaire
		int[] tab5 = {1};
		testCasColonnesPairesOnly(tab5, false);
	}

	/**
	 * Teste un cas de la méthode colonnesPairesOnly().
	 * @param tab Tableau de nombres binaires
	 * @param result Résultat attendu (true si toutes les colonnes sont paires, false sinon)
	 */
	void testCasColonnesPairesOnly(int[] tab, boolean result) {
		System.out.print("colonnesPairesOnly() = " + result + " : ");

		// Appel de la méthode à tester
		boolean resExec = colonnesPairesOnly(tab);

		// Vérification du résultat
		if (resExec == result) {
			System.out.println("OK");
		} else {
			System.err.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode copierTableau().
	 */
	void testCopierTableau() {
		System.out.println();
		System.out.println("*** testCopierTableau");

		// Cas avec un tableau de 3 éléments
		int[] tab1 = {1, 2, 3};
		testCasCopierTableau(tab1, new int[]{1, 2, 3});

		// Cas avec un tableau vide
		int[] tab2 = {};
		testCasCopierTableau(tab2, new int[]{});

		// Cas avec un tableau d'un seul élément
		int[] tab3 = {42};
		testCasCopierTableau(tab3, new int[]{42});
	}

	/**
	 * Teste un cas de la méthode copierTableau().
	 * @param tab Tableau à copier
	 * @param result Résultat attendu (copie du tableau)
	 */
	void testCasCopierTableau(int[] tab, int[] result) {
		System.out.print("copierTableau() : ");

		// Appel de la méthode à tester
		int[] resExec = copierTableau(tab);

		// Vérification du résultat
		if (compareTableaux(resExec, result)) {
			System.out.println("OK");
		} else {
			System.err.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode modifie()
	 */
	void testModifie() {
		System.out.println();
		System.out.println("*** testModifie");

		// Initialisation de différents cas de test avec un tableau représentant les lignes d'allumettes
		int[] stock1 = {5, 3, 7}; // Exemple avec 3 lignes

		testCasModifie(0, 2, stock1, 3);
		testCasModifie(1, 1, stock1, 2);
		testCasModifie(2, 5, stock1, 2);

		// On peut ajouter d'autres cas de test similaires en initialisant d'autres tableaux si nécessaire.
	}

	/**
	 * teste un appel de modifie
	 * @param ligne Ligne à modifier
	 * @param nbAllumettes Nombre d'allumettes à retirer de la ligne
	 * @param stock tableau contenant le nombre d'allumettes pour chaque ligne
	 * @param result résultat attendu après modification de la ligne
	 */
	void testCasModifie(int ligne, int nbAllumettes, int[] stock, int result) {
		// Affichage
		System.out.print("modifie(" + ligne + ", " + nbAllumettes + ", "+stock+") = " + result + "\t : ");

		// Appel de la méthode modifie
		modifie(ligne, nbAllumettes, stock);

		// Vérification du résultat
		if (stock[ligne] == result) {
			System.out.println("OK");
		} else {
			System.out.println("ERREUR, stock[" + ligne + "] = " + stock[ligne] + ", attendu : " + result);
		}
	}

	/**
	 * Teste la méthode checkwin()
	 */
	void testCheckwin () {
		System.out.println ();
		System.out.println ("*** testCheckwin");

		int[] t1 = {0,0,0,0};
		int[] t2 = {};
		int[] t3 = {1,2,3,4};
		int[] t4 = {0, 0, 3, 0, -1, 0};

		testCasCheckwin (t1, true );
		testCasCheckwin (t2, true);
		testCasCheckwin (t3, false);
		testCasCheckwin (t4, false);
	}

	/**
	 * teste un appel de Checkwin
	 * @param stock Tableau contenant le nombre d'allumettes par ligne
	 * @param result Résultat attendu
	 */
	void testCasCheckwin (int[] stock, boolean result) {
		// Affichage
		System.out.print ("Checkwin (" + stock +") \t= " + result + "\t : ");
		// Appel
		boolean resExec = checkwin(stock);
		// Verification
		if (resExec == result){
			System.out.println ("OK");
		} else {
			System.out.println ("ERREUR");
		}
	}

	/**
	 * Teste la méthode initJeuTab()
	 */
	void testInitJeuTab() {
		System.out.println();
		System.out.println("*** testInitJeuTab");

		// Cas de test pour initJeuTab avec différents nombres de lignes
		testCasInitJeuTab(1, new int[]{1}); // Une seule ligne, le résultat attendu est [1]
		testCasInitJeuTab(2, new int[]{1, 3}); // Deux lignes, le résultat attendu est [1, 3]
		testCasInitJeuTab(3, new int[]{1, 3, 5}); // Trois lignes, le résultat attendu est [1, 3, 5]
		testCasInitJeuTab(4, new int[]{1, 3, 5, 7}); // Quatre lignes, le résultat attendu est [1, 3, 5, 7]
	}

	/**
	 * Teste un appel d'initJeuTab().
	 * @param nbLignes Nombre de lignes à tester.
	 * @param result Résultat attendu après initialisation.
	 */
	void testCasInitJeuTab(int nbLignes, int[] result) {
		// Affichage
		System.out.print("initJeuTab(" + nbLignes + ") = ");
		displayTab(result); // Affiche le tableau attendu

		// Appel de la méthode initJeuTab
		int[] resExec = initJeuTab(nbLignes);

		// Affichage du tableau obtenu
		System.out.print("Obtenu : ");
		displayTab(resExec);

		// Vérification du résultat
		if (compareTableaux(resExec, result)) {
			System.out.println("OK");
		} else {
			System.out.println("ERREUR");
		}
	}

	/**
	 * Teste la méthode compareTableaux().
	 */
	void testCompareTableaux() {
		System.out.println("*** testCompareTableaux");

		// Cas 1 : Deux tableaux vides
		testCasCompareTableaux(new int[]{}, new int[]{}, true);

		// Cas 2 : Deux tableaux avec un seul élément identique
		testCasCompareTableaux(new int[]{1}, new int[]{1}, true);

		// Cas 3 : Deux tableaux avec plusieurs éléments identiques
		testCasCompareTableaux(new int[]{1, 2, 3}, new int[]{1, 2, 3}, true);

		// Cas 4 : Deux tableaux avec des tailles différentes
		testCasCompareTableaux(new int[]{1, 2, 3}, new int[]{1, 2}, false);

		// Cas 5 : Deux tableaux avec des éléments différents
		testCasCompareTableaux(new int[]{1, 2, 3}, new int[]{1, 2, 4}, false);

		// Cas 6 : Deux tableaux avec des éléments identiques mais dans un ordre différent
		testCasCompareTableaux(new int[]{3, 2, 1}, new int[]{1, 2, 3}, false);
	}

	/**
	 * Teste un cas de comparaison de tableaux avec compareTableaux().
	 *
	 * @param tab1 Premier tableau à comparer.
	 * @param tab2 Deuxième tableau à comparer.
	 * @param result  Résultat attendu (true si les tableaux sont égaux, false sinon).
	 */
	void testCasCompareTableaux(int[] tab1, int[] tab2, boolean result) {
		// Affichage des tableaux à comparer
		System.out.print("compareTableaux(");
		displayTab(tab1);
		System.out.print(", ");
		displayTab(tab2);
		System.out.print(") = " + result + " : ");

		// Appel de la méthode compareTableaux
		boolean resExec = compareTableaux(tab1, tab2);

		// Vérification du résultat
		if (result == resExec) {
			System.out.println("OK");
		} else {
			System.out.println("ERREUR");
		}
	}
}