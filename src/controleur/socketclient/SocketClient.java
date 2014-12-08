package controleur.socketclient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import modeles.dao.communication.ArduiBotServer;

/**
 * 
 * Classe qui va g�rer la connexion au socket
 * et dispatcher le stream a un Thread d'emission.
 *
 */


public class SocketClient implements Runnable {
	
	private ArduiBotServer server = null;
	private Socket socket;
	private Emission em = null;
	
	// R�cup�re les parametres de connexion
	public SocketClient( ArduiBotServer abs ){
		server = abs;
	}
	


	@Override
	public void run() {

		try {
			// V�rifie le bon format de l'IP
			InetAddress serverAddr = InetAddress.getByName(server.getIp());

			// Tentative d'ouverture du socket.
			socket = new Socket(serverAddr, server.getPort());
			
			// On r�cup�re le stream
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			// que l'on donne au Thread d'�mission
			em = new Emission(out);
			new Thread(em).start();

		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void stop(){
		em.stop();
	}
	
	public Emission getEmission(){
		return em;
	}
}