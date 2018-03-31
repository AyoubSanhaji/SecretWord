import java.net.*;
import java.util.*;
import java.io.*;

/**
 * ServeurMultiClient est la classe représentant un serveur
 * @author Ayoub SANHAJI et Andriamanantoanina RAKOTONDRANALY
 * @version 1.0
 */
public class ServeurMultiClient
{
    public static void main(String[] args) 
    {
    	/**
    	 * Connexion qui attend qu'un client vienne se connecter
    	 */
        ServerSocket socket;
        try 
        {
        	// Affichage d'un menu
        	System.out.println("****************************************");
        	System.out.println("**  Choisir parmi les choix suivants: **");
        	System.out.println("**     0: remise à zéro les inscrits  **");
        	System.out.println("**      1: remise à zéro les scors    **");
        	System.out.println("**         ?: demarrer le serveur     **");
        	System.out.println("****************************************");
        	System.out.print("Votre choix:  ");
        	Scanner sc = new Scanner(System.in);
        	int choix = (int) sc.nextInt();
        	switch(choix)
        	{
        		case 0 : 
        		{
        			Service.ecrireFile("Users.txt", "", false);
        			break;
        		}
        		case 1 :
        		{
        			Service.mettreScoreAZero("Users.txt");
        			break;
        		}
        		default : 
        		{
        			int i=0;
		        	socket = new ServerSocket(60000);
		        	while(i<10)
		        	{
		        		Thread t = new Thread(new Service(socket));
		        		t.start();
		        		i++;
		        	}
		        	System.out.println("J'attends des connexions mais pas trop!");
		        	System.out.println("Serveur en attente");
		        }
        	}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }
}

/**
 * @author Ayoub SANHAJI
 * @version 1.0
 */
class Service implements Runnable
{
	/**
	 * Socket du seveur et du client
	 */
	private ServerSocket socketserveur  ;
    private Socket s ;

    /**
     * Le nombre de clients connectes au serveur
     */
	private static int nbrclient = 0;
	/**
	 * Le nombre max des clients
	 */
	private static final int nbMaxClient =10;

	/**
	 * Nombre de caractere du mot genere
	 */
	private static  final int nbreCaract = 5;
	
	/**
	 * Le constructeur de la classe Service
	 * @param socket
	 */
	 public Service(ServerSocket socket)
	 {
		 socketserveur = socket;
	 }
	 
	/**
	 * Cette methode a pour objectif de generer Le CodeLicencie a partir du nom et prenom de joueur
	 * @param nom: Le nom du joueur
	 * @param prenom: Le prenom du joueur
	 * @return: Le CodeLicencie
	 */
	public String genererCodeLicencie (String nom, String prenom)
	{
		char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		
		char n = Character.toUpperCase(nom.charAt(0));
		char p = Character.toUpperCase(prenom.charAt(0));
		
		String nbr = new String();
		for(int i=0 ; i<3 ; i++)
		{
			char tmp = nom.charAt(i);
			for(int j=0 ; j<52 ; j++)
			{
				if(tmp==alphabet[j])
				{
					nbr = nbr + Integer.toString(j+1);
					break;
				}
			}
		}
		String res = Character.toString(n)+Character.toString(p)+nbr;
		return res;
	}
	
	/**
	 * Cette methode a pour objectif de generer un mot que le joueur doit trouver
	 * @param n: La longeur du mot genere
	 * @return un mot de 5 caracteres
	 */
	public String genererCombinaison(int n)
	{
		String res = new String();
		char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		int indice;
		Random rand;
		for(int i=0 ; i<n ; i++)
		{
			rand = new Random();
			indice = rand.nextInt(26);
			res = res + Character.toString(alphabet[indice]);		
		}
		System.out.println(res);
		return res;
	}

	/**
	 * Cette methode a pour objectif de verifier que les caracters du mot entree sont bien des alphabets
	 * @param mot: Le mot a verifier
	 * @return un entier 0 ou 1
	 */
	public int verifierMot(String mot)
	{
		char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		int l = mot.length();
		int cpt = 0;

       for(int i=0 ; i<l ; i++)
           for(int j=0 ; j<alphabet.length; j++)
                if(mot.charAt(i)==alphabet[j])
                    cpt++;
	    if(cpt == l)
        	return 1;
        return 0;    
	}

	/**
	 * Cette methode a pour objectif de calculer le nombre de lettres du mot entre placees dans l'ordre
	 * @param motG: la combinaison genere par le serveur
	 * @param motT: le mot entre par l'utilisateur
	 * @return un entier -1 ou le nombre de lettres placees dans l'ordre
	 */
	public int nbreLettrePlacees (String motG, String motT)
	{
        // Longueur de la chaine
        int l = nbreCaract;
        // Nombre de lettre placees dans l'ordre
        int cpt = 0;
        if(motT.length()!=l)
            return -1;      
        else
        {
                for(int i=0 ; i<l ; i++)
                        if(motG.charAt(i)==motT.charAt(i))
                                cpt++;
        }    
        return cpt;   
	}

	/**
	 * Cette methode a pour objectif de calculer le nombre des lettres du mot entre presents dans le mot genere
	 * @param motG: la combinaison genere par le serveur
	 * @param motT: le mot entre par l'utilisateur
	 * @return un entier -1 ou le nombre de lettres present dans le mot genere
	 */
	public int nbreLettrePresent (String motG, String motT)
	{
        // Longueur de la chaine
        int l = nbreCaract;
        // Nombre de lettre presentes dans la combinaison
        int cpt = 0;
        if(motT.length()!=l)
                return -1;      
        else
        {
            for(int i=0 ; i<l ; i++)
                for(int j=0 ; j<l ; j++)
                    if(motG.charAt(i)==motT.charAt(j))
                    {
                        cpt++;
                        break;
                    }
        }    
        return cpt; 
	}

	/**
	 * Cette methode a pour objectif d'ecrire un text dans un fichier avec la possibilite soit de le remplacer soit d'ajouter du text
	 * @param file: Le nom du fichier avec l'extension 
	 * @param text: Le text a ecrire dans le fichier
	 * @param b: True pour ajouter a la fin du fichier, False pour remplace le fichier
	 */
	public synchronized static void ecrireFile(String file, String text, boolean b)
	{
		File ecrire = null;
		try
		{
			ecrire = new File(file);
			ecrire.createNewFile();
			FileWriter fr = new FileWriter(ecrire, b);
			fr.write(text);
			fr.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Cette methode a pour objectif de mettre tous les score a zero
	 * @param file: Le nom de fichier
	 */
	public synchronized static void mettreScoreAZero(String file)
	{
		try
		{
			File lire = new File(file);
			FileReader fr = new FileReader(lire);
			BufferedReader br = new BufferedReader(fr);
			String newfile = "";
			try
			{
				String line = br.readLine();
				while(line != null)
				{
					String[] l = line.split(" ");
					newfile += l[0]+' '+l[1]+' '+l[2]+" 0 "+l[4]+"\r";
					line = br.readLine();
				}
				br.close();
				fr.close();
				ecrireFile(file, newfile, false);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		catch(FileNotFoundException e){}
	}

	/**
	 * Cette methode a pour objectif de retourner les informations du joueur dont son CodeLicencie est entre en parametres 
	 * @param file: Le nom de fichier
	 * @param code: Le CodeLicencie
	 * @param b: True pour eliminer la ligne trouvee, False pour la conserver
	 * @return la ligne qui contient le code
	 */
	public synchronized String trouverCodeFile(String file, String code, boolean b)
	{
		try
		{
			File lire = new File(file);
			FileReader fr = new FileReader(lire);
			BufferedReader br = new BufferedReader(fr);
			String res = null;
			String newfile = "";
			try
			{
				String line = br.readLine();
				while(line != null)
				{
					if((line.split(" ")[0]).equals(code))
						res = line;
					else
						newfile += line+"\r";
					line = br.readLine();
				}
				br.close();
				fr.close();
				if(b)
					ecrireFile("Users.txt", newfile, false);
				return res;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}
		catch(FileNotFoundException e){}
		return null;
	}
    
	/**
	 * Cette methode a pour objectif de calculer le nombre de lignes dans un fichier
	 * @param file: Le nom de fichier
	 * @return la taille de fichier
	 */
	public synchronized int tailleFile(String file)
	{
		try
		{
			File lire = new File(file);
			FileReader fr = new FileReader(lire);
			BufferedReader br = new BufferedReader(fr);
			int taille = 0;
			try
			{
				String tmp = br.readLine();
				while(tmp != null)
				{
					taille++;
					tmp = br.readLine();
				}
				br.close();
				fr.close();
				return taille;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return 0;
		}
		catch(FileNotFoundException e){}
		return 0;
	}

	/**
	 * Cette methode a pour objectif de structure le CodeLicencie et le score dans un tableau de deux dimensions
	 * @param file: Le nom de fichier
	 * @return un tableau qui contient le CodeLicencie avec son score correspond
	 */
    public synchronized String[][] tabCodeScore(String file)
    {
        try
		{
			File lire = new File(file);
			FileReader fr = new FileReader(lire);
			BufferedReader br = new BufferedReader(fr);
			int taille = tailleFile(file); 
			String[][] res = new String[taille][2];
			try
			{
				int i=0;
				String line = br.readLine();
				while(line != null)
				{
					String[] lines = line.split(" ");
					res[i][0] = lines[0];
                    res[i][1] = lines[3];
					line = br.readLine();
                    i++;
				}
				br.close();
				fr.close();
				return res;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}
		catch(FileNotFoundException e){}
		return null;
    }

    /**
     * Cette methode a pour objectif de copier un tableau dans un autre
     * @param tableauOriginal: Le tableau source
     * @param tableauCopie: Le tableau resultat
     * @param tailleTableau: La taille du tableau
     */
    static void copie(int tableauOriginal[], Integer tableauCopie[], int tailleTableau)
	{
	    int i ;
	 
	    for(i = 0 ; i < tailleTableau ; i++)
	    tableauCopie[i] = new Integer(tableauOriginal[i]) ;
	}
	
    /**
     * Cette methode a pour objectif de trouver le classement d'un joueur
     * @param nom: Le CodeLicencie du joueur
     * @return le classement du joueur
     */
	public int retournerPlaceJoueur(String nom)
	{
		String[][] liste = tabCodeScore("Users.txt");
		int place = 0;
		String[] code = new String[liste.length];
		int score[] = new int[liste.length];
		
		for(int i=0;i<liste.length;i++)
		{
			code[i] = liste[i][0];
		}
		for(int i=0;i<liste.length;i++)
		{
			score[i] = Integer.parseInt(liste[i][1]);
		}
		Integer scoreTrie[] = new Integer[liste.length];
		copie(score,scoreTrie,liste.length);
		Arrays.sort(scoreTrie,Collections.reverseOrder());
		for(int i=0;i<liste.length;i++)
		{
			if(code[i].compareToIgnoreCase(nom)==0)
			{
				place = i;
				break;
			}
		}
		int scoreARechercher = score[place];
		int placeFinale=0;

		for(int i=0;i<liste.length;i++)
		{
			if (scoreARechercher==scoreTrie[i])
			{
				placeFinale = i+1;
				break;
			}
		}
		return placeFinale;
	}
     
	/**
	 * Cette methode a pour objectif d'executer les differents thread
	 */
    public void run()
    {
    	// Le nom et le prenom du joueur
    	String nom=null, prenom = null;
    	// Le score du joueur
    	int score=0;
    	// Le nombre de partie jouer par ke joueur
		int nbrepartie=0;
		// L'adresse du joueur
		String sonIp="";

		// Creation du fichier
		ecrireFile("Users.txt", "", true);
		try 
		{
		    PrintWriter out;
		    BufferedReader in;

	    	while(nbrclient<=nbMaxClient)
	    	{
 	       		// Ecoute d'un service entrant - association socket client et socket serveur.		
               	s = socketserveur.accept();
	       		sonIp=s.getInetAddress().toString();
	       		nbrclient ++;	
               	System.out.println("le client "+nbrclient+" s'est connecté :"+ sonIp);
	    
	   			out = new PrintWriter(s.getOutputStream());
			   	in = new BufferedReader (new InputStreamReader (s.getInputStream()));
			   	
			   	// Demande de saisie du nom
			   	Thread.sleep(1000);
			   	out.println("Bonjour "+sonIp+", Entrez votre nom: ");
			   	out.flush();
			   	
			   	// Recuperation du nom saisie
				Thread.sleep(1000); 
			   	nom = in.readLine();      	
				
				out = new PrintWriter(s.getOutputStream());
				in = new BufferedReader (new InputStreamReader (s.getInputStream()));
			   	
				// Demande de saisie du prenom
				Thread.sleep(1000);
			   	out.println("Bonjour "+nom+", Entrez votre prenom: ");
			   	out.flush();
			   	
			   	// Recuperation du prenom
				Thread.sleep(1000); 
			   	prenom = in.readLine();    	
			   	
				// Generation du Code
				String code = genererCodeLicencie(nom,prenom);

				// Tester si le joueur exist deja dans la bdd
				String line = trouverCodeFile("Users.txt", code, false);
				String[] lineJ = null;
				if(line != null)
					lineJ = line.split(" ");
				
				out = new PrintWriter(s.getOutputStream());
				in = new BufferedReader (new InputStreamReader (s.getInputStream()));
				String text = nom + ' ' + prenom;
				
				// Si l'utilisateur a entree son nom et prenom
				if(text!=null)
				{
					if(text.length()!=0 || lineJ!=null)
					{
						String codeR;
						if(lineJ != null)
						{
							// Demande de saisie du CodeLicencie
							out.println("Veuillez entrer votre code: ");
							out.println(" ");
							out.flush();
							Thread.sleep(1000); 
			   				codeR = in.readLine();
			   				// Comparaison avec le code donne a la premier fois
							if(code.compareTo(codeR)==0)
							{
								score = Integer.parseInt(lineJ[3]);
								nbrepartie = Integer.parseInt(lineJ[4]);
								out.println("Vous nous avez manqué "+text);
								out.println("Nombre de jeux effectués: "+nbrepartie+" - Score actuel: "+score+" - Classement: "+retournerPlaceJoueur(code));
							}
							else
							{
								Thread.sleep(1000);
								s.close();
           						System.out.println("Déconnexion du client numero "+nbrclient+" d'ip "+sonIp);
           						nbrclient--;
							}
						}
						else
						{
							out.println("Merci "+text);
							out.println("Votre code licencie est "+ code + " (Gardez le précieusement)");
						}	
						nbrepartie += 1;
					}
					else
						out.println("impoli, bye!");
					out.flush();
				}
				
				if(!s.isClosed())
				{
					// Jeu
					String combinaison = genererCombinaison(nbreCaract);
					String mot = "";
					while(combinaison.equals(mot) == false)
					{
						in = new BufferedReader (new InputStreamReader (s.getInputStream()));
						out = new PrintWriter(s.getOutputStream());
					   	out.println("Taper un mot de "+combinaison.length()+" caractères: ");
					   	out.flush();	
					   	Thread.sleep(1000);
					   	mot = in.readLine();
	
				   		int n1 = nbreLettrePresent(combinaison, mot);
				   		int n2 = nbreLettrePlacees(combinaison, mot);
				   		int n3 = verifierMot(mot);
				   		out = new PrintWriter(s.getOutputStream());
				   		out.println(n1+" "+n2+" "+n3);
				   		out.flush();
	
				   		out = new PrintWriter(s.getOutputStream());
	
				   		if(mot.compareTo("GIVE UP")==0)
				   		{
				   			out.println("Vous avez abandonné le jeu !");
				   			combinaison = genererCombinaison(nbreCaract);
				   			score += -5;
				   		}
				   		else
				   		{
							if(n1==-1 || n2==-1)
							{
								out.println("Le nombre de caractères n'est pas "+combinaison.length()+" caractères !");
							}
							else
							{
								if(n3 == 0)
									out.println("Le mot ne doit se composer que de caractères !");	
								else
								{
									if(n2 != combinaison.length())
									{
										out.println("Le nombre de caractères placés dans l'ordre est:  "+n2);
										out.println("Le nombre de caractères présents est:  "+n1);
									}
									else
										out.println("Gagné !");
								}
							}
				   		}
				   		out.flush();
					}
					// Recuperation du score et de nombre des parties jouees
					line = trouverCodeFile("Users.txt", code, false);
					if(line != null)
					{
						lineJ = line.split(" ");
						score = Integer.parseInt(lineJ[3]);
						nbrepartie = Integer.parseInt(lineJ[4]);
					}
					
					// Lecture de score
					in = new BufferedReader (new InputStreamReader (s.getInputStream()));
					Thread.sleep(1000);
					int scorepartie = Integer.parseInt(in.readLine());
					score += scorepartie;
					
					// Suppression de la ligne correspond au joueur
					trouverCodeFile("Users.txt", code, true);
					
					// Ecriture des stats dans le fichier
					if(score<0)
	        			score = 0;
					ecrireFile("Users.txt", genererCodeLicencie(nom,prenom)+" "+nom+" "+prenom+" "+score+" "+nbrepartie+"\r", true);
					
					// Envoie des statistiques
					out = new PrintWriter(s.getOutputStream());
					out.println("A bientôt "+text);
					out.println("Nombre de jeux effectués: "+nbrepartie+" - Score de partie: "+scorepartie+" - Score actuel: "+score+" - Classement: "+retournerPlaceJoueur(code));
					out.flush();
					
	        	   	// Cloture de la connexion avec le service entrant
		   			Thread.sleep(1000);
		   			s.close();
	           		System.out.println("Déconnexion du client numero "+nbrclient+" d'ip "+sonIp); 
	           		nbrclient--;
		    	}
	 	    } 
	    	
			// Ecoute d'un service entrant -association socket client et socket serveur.		
            s = socketserveur.accept(); 
	       	nbrclient++;	
            System.out.println("le client "+nbrclient+" s'est connecté !"); 
			
			// Envoi d'un message de trop de connexions
		   	out = new PrintWriter(s.getOutputStream());
		   	Thread.sleep(1000);
		   	out.println("Trop de clients, débordé, désolé!");
		   	out.flush();

           	// Cloture de la connexion avec le service entrant
		   	s.close();
		   	nbrclient--;
		   	// Cloture de l'ecoute
    		socketserveur.close();
		}	
		
		catch (SocketException e) 
        {
        	if(nom!=null && prenom!=null)
        	{
        		score = score-5;
        		if(score<0)
        			score = 0;
        		ecrireFile("Users.txt", genererCodeLicencie(nom,prenom)+" "+nom+" "+prenom+" "+score+" "+nbrepartie+"\r", true);
        	}
           	System.out.println("Déconnexion du client numero "+nbrclient+" d'ip "+sonIp);
           	try 
           	{
				s.close();
			} 
           	catch (IOException e1) {}
			nbrclient--;
        }
		catch (IOException e) 
		{
			e.printStackTrace();
		}            
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
    }
}