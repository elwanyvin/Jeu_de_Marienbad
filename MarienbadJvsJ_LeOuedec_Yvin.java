/**
*Programme qui simule une partie de Jeu de Nim entre 2 joueurs
* @author E.Yvin, B.Le Ouedec
*/
class MarienbadJvsJ_LeOuedec_Yvin{
	
	void principal(){
    	String nom1 = SimpleInput.getString("Nom du joueur 1 : ");
    	String nom2 = SimpleInput.getString("Nom du joueur 2 : ");
    	int nbLignes = SimpleInput.getInt("Nombre de lignes : ");
		int [] stockAllumettes = initJeuTab(nbLignes);

		while (nbLignes <= 1){
			nbLignes = SimpleInput.getInt("Nombre incorrect, choisissez un nombre de ligne correct : ");
		}
		
    	System.out.println("Preparez-vous, " + nom1 + " et " + nom2);
    	System.out.println("Chargement d'une partie a " + nbLignes + " lignes");
    	displayJeu(stockAllumettes);
    	
    	String joueur=nom2;
		do{
			if (joueur==nom1){
				joueur=nom2;
			}else{
				joueur=nom1;
			}
			jeu(joueur,stockAllumettes);
		}while(checkwin(stockAllumettes)==false);
		System.out.println();
		System.out.print("Bravo "+joueur+", vous remportez la partie !");

	}

	
	/**
	 * Simule le tour d'un joueur
	 * @param nomJoueur Joueur dont c'est le tour
	 * @param stock tableau conteant le nombre d'allumettes pour chaque ligne
	 * @return wincon Vrai si un joueur a gagné, Faux sinon
	*/
	boolean jeu(String nomJoueur, int [] stock) {
		boolean wincon = false;

		System.out.println("C'est le tour de " + nomJoueur);
		System.out.println();

		int ligne = SimpleInput.getInt("Entrez le numero de la ligne dont vous voulez enlever des allumettes : ");
		while(ligne<0||ligne>stock.length-1 || stock[ligne]==0){
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
	 * Affiche une ligne du jeu
	 * @param nbAllumettes Nombre d'allumettes de la ligne
	 */
	void displayLigne(int nbAllumettes) {
		for(int j=1; j<=nbAllumettes;j++){
				System.out.print(" | ");
			}
		System.out.println();
	}
		
	/**
	 * Affiche le jeu complet
	 * @param stock tableau contenant le nombre d'allumettes pour chaque ligne
	 */
	void displayJeu(int []stock) {
		System.out.println();
		System.out.println("------------------------------");
		System.out.println();
		for (int i = 0; i < stock.length; i++) {
			System.out.print(i + " : ");
			displayLigne(stock[i]);
		}
		System.out.println();
		System.out.println("------------------------------");
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
		System.out.println("La ligne "+ligne+" contient maintenant "+ stock[ligne]+" allumettes.");
		System.out.println();
	}
	
	/**
	 * Vérifie si un joueur à gagné
	 * @param stock tableau contenant le nombre d'allumettes pour chaque ligne
	 * @return wincon Vrai si un joueur a gagné, Faux sinon
	 */
	boolean checkwin(int[] stock){
		boolean result=true;
		
		for(int i=0; i< stock.length; i++){
			if (stock[i] != 0){
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * Initialise le stock 
	 * param nblignes Nombre de lignes du jeu
	 * @return stockAllumettes tableau contenant le nombre d'allumettes par ligne
	 */
	int[] initJeuTab (int nbLignes){
		int [] stockAllumettes = new int [nbLignes];
		for(int i = 0;i<nbLignes;i++){
			stockAllumettes[i] = (2*i)+1;
		}
		return stockAllumettes;
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
		System.out.print("modifie(" + ligne + "," + nbAllumettes + ", "+stock+") = " + result + "\t : ");

		// Appel de la méthode modifie
		modifie(ligne, nbAllumettes, stock);

		// Vérification du résultat
		if (stock[ligne] == result) {
			System.out.println("OK");
		} else {
			System.err.println("ERREUR, stock[" + ligne + "] = " + stock[ligne] + ", attendu : " + result);
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
			System.err.println ("ERREUR");
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
	 * Teste un appel de initJeuTab().
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
			System.err.println("ERREUR");
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
			System.err.println("ERREUR");
		}
	}
}