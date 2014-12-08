package controleur;

import modeles.dao.communication.ArduiBotServer;
import controleur.socketclient.*;
public class Controleur {

	private ArduiBotServer server = null;
	private SocketClient sc = null;
	
	/*
	 * Constructeur du controleur
	 * 
	 * Instancie le modele
	 * 
	 */
	public Controleur() {
		server = new ArduiBotServer();
	}
	
	
	/*
	 * Démarrage du Thread de connexion au Socket
	 */
	public Emission startThreadClient(){
		// Instanciation de la classe Socket
		sc = new SocketClient( server );
		
		if( sc != null ){
			// lancement
			new Thread( sc ).start();
			
			try {
				// on patiente légèrement que le thread est bien démarré
				Thread.sleep(100);
				// on retourne l'objet Emission
				return sc.getEmission();
			} catch (InterruptedException e) {
				return null;
			}
			
		}else
			return null;

	}

	/*
	 * Arret du socket
	 */
	public void stopThreadClient(){
		if( sc != null )
			sc.stop();
		
		
	}
}
