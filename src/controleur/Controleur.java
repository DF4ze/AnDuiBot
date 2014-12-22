package controleur;

import modeles.dao.communication.ArduiBotServer;
import android.app.Activity;
import android.widget.EditText;

import com.example.testsocketclient.R;

import controleur.socketclient.Emission;
import controleur.socketclient.SocketClient;
public class Controleur {

	private ArduiBotServer _server = null;
	private SocketClient _sc = null;
	private Activity _act = null;
	
	/*
	 * Constructeur du controleur
	 * 
	 * Instancie le modele
	 * 
	 */
	public Controleur( Activity act ) {
		_server = new ArduiBotServer();
		_act = act;
	}
	
	
	/*
	 * Démarrage du Thread de connexion au Socket
	 */
	public Emission startThreadClient(){
		// Instanciation de la classe Socket
		_sc = new SocketClient( _server );
		
		if( _sc != null ){
			// lancement
			new Thread( _sc ).start();
			
			try {
				// on patiente légèrement que le thread est bien démarré
				Thread.sleep(100);
				// on retourne l'objet Emission
				return _sc.getEmission();
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
		if( _sc != null )
			_sc.stop();
	}
	
	/*
	 * Prend le texte de l'Edittext pour mettre à jour le modele
	 */
	public boolean majIP(){
		EditText ed = (EditText) _act.findViewById(R.id.etIP);
		String fullIp = ed.getText().toString();
		boolean ok = false;
		
		if( !fullIp.equals("") ){
			int pos = -1;
			if( (pos = fullIp.indexOf(":")) != -1 ){
				String ip = fullIp.substring(0, pos);
				String[] oct = ip.split("\\.");
				if( oct.length == 4 ){
					int port = -1;
					try{
						port = Integer.valueOf( fullIp.substring(pos+1) );
						_server.setIp(ip);
						_server.setPort(port);
						ok = true;
					}catch( Exception e ){}
				}
			}
		}else ok = true;
		
		return ok;
	}
}
