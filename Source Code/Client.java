import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Client est la classe représentant un client du serveur.
 * @author Ayoub SANHAJI et Andriamanantoanina RAKOTONDRANALY
 * @version 1.0
 */
public class Client 
{
    public static void main(String[] args) 
    {
    	/**
    	 * Permet au client de se connecter au serveur
    	 */
        Socket socket = null;
        /**
         * Permet de lire un message dans le flux
         */
		BufferedReader in;
		/**
		 * Permet d'envoyer un message dans le flux
		 */
		PrintWriter out;
		/**
		 * Permet de recuperer un message a partir du clavier
		 */
		Scanner sc = new Scanner(System.in);
		/**
		 * Utilise pour stocker les messages recus
		 */
		String text;
		/**
		 * Le score du joueur
		 */
		int score;
		/**
		 * Le debut et la fin du chaque essaie faite par le joueur pour trouver le mot 
		 */
		long deb, fin;
		/**
		 * C'est fin-deb
		 * @see deb, fin
		 */
		long duree = 0;
		
        try 
        {
			//Demande d'ouverture d'une connexion sur le serveur local(localhost) et le numero de port 60000
        	 socket = new Socket(args[0],60000);

			//Attente d'une reponse (Demande du saisie de NOM par le serveur)
			in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			String message_distant = in.readLine();

			//Reponse (Saisie du NOM)
			out = new PrintWriter(socket.getOutputStream());
			if(args[1].compareTo("Bonjour")==0)
			{
				text = "";
				while(text.length()<3)
				{
					System.out.println(message_distant);
					text = sc.nextLine();
				}
				out.println(text);
			}
			else
			{
				out.println("Impoli!");
				socket.close();
			}
			out.flush();

			//Attente d'une reponse (Demande du saisie de PRENOM par le serveur)
			in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			message_distant = in.readLine();
			
			//Reponse (Saisie du PRENOM)
			out = new PrintWriter(socket.getOutputStream());
			text = "";
			while(text.length()<3)
			{
				System.out.println(message_distant);
				text = sc.nextLine();
			}
			out.println(text);
			out.flush();
			
			//Attente d'une reponse :
			//-Affichage du CodeLicencie, -Demande du saisie du CodeLicencie
			in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			message_distant = in.readLine();
			System.out.println(message_distant);
			message_distant = in.readLine();
			System.out.println(message_distant);

			// Test sur le 2eme message affiché
			if(message_distant.compareTo(" ") == 0)
			{
				// Saisie du CodeLicencie
				out = new PrintWriter(socket.getOutputStream());
				text = sc.nextLine();
				out.println(text);
				out.flush();
				//Affichage des statistiques
				message_distant = in.readLine();
				if(message_distant != null)
					System.out.println(message_distant);				
				message_distant = in.readLine();
				if(message_distant != null)
					System.out.println(message_distant);
			}
			
			// Fermeture de la connexion dans le cas ou le code entre est faux
			if(message_distant == null)
			{
				System.out.println("Vous êtes déconnecté du serveur !");
				System.out.println("Code erroné !");
				socket.close();
			}
		 
			if(!socket.isClosed())
			{
				in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream());
				System.out.println("Pour abandonner taper -Give Up-");
				while(true)
				{
					// Stockage de l'instant du debut
					deb = System.currentTimeMillis();
					// Demande de saisie le mot
					message_distant = in.readLine();
					System.out.println(message_distant);
					// Saisie du mot
					text = sc.nextLine().toUpperCase();
					// Stockage de l'instant du fin
					fin = System.currentTimeMillis();
					// Envoie du mot saisie
					out.println(text);
					out.flush();
	
					// Recevoire les infos
					message_distant = in.readLine();
					String[] infos = message_distant.split(" ");
					if(message_distant.compareTo("Gagné !")==0)
						break;
					if(text.compareTo("GIVE UP")==0)
					{
						// Affichage du message d'abandonne
						message_distant = in.readLine();
						System.out.println(message_distant);
						duree = 0;
					}
					else
					{
						// Test sur le nombre de caracteres du mot
						if(Integer.parseInt(infos[0])==-1 || Integer.parseInt(infos[1])==-1)
						{
							message_distant = in.readLine();
							System.out.println(message_distant);
						}
						else
						{
							// Test sur le type des caracteres saisient (alphabet ou numero)
							if(Integer.parseInt(infos[2])==0)
							{
								message_distant = in.readLine();
								System.out.println(message_distant);
							}
							// Comparison du mot entre avec le mot genere
							else
							{
								message_distant = in.readLine();
								System.out.println(message_distant);
								if(message_distant.compareTo("Gagné !")==0)
									break;
								message_distant = in.readLine();
								System.out.println(message_distant);
							}
						}
						if(message_distant.compareTo("Gagné !")==0)
							break;
					}
					// Calcule et affichage de la durée
					duree += (fin-deb)/1000;
					System.out.println("Temps ecoulé: "+duree+ "s");
				}
	
				// Calcule et affichage du score
				duree = duree/60;
				if(duree<=1)
					score = 10;
				else 
					if(duree<=3)
						score = 5;
					else
						if(duree<=5)
							score = 2;
						else
							score = 1;
	
				// Envoie de score au serveur
				out = new PrintWriter(socket.getOutputStream());
				out.println(score);
				out.flush();
	
				// Recevoire les statistiques
				in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
				message_distant = in.readLine();
				System.out.println(message_distant);
				message_distant = in.readLine();
				System.out.println(message_distant);
        }
			
			
		    // Fermeture de la connexion
			socket.close();
        }

        catch (SocketException e) 
        {
			System.out.println("Vous êtes déconnecté du serveur !");
        }
        catch (UnknownHostException e) 
        {
			e.printStackTrace();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}