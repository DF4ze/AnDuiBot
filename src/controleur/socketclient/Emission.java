package controleur.socketclient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import modeles.dao.communication.FifoSenderAction;
import modeles.dao.communication.beansactions.IAction;

/**
 * 
 * Classe qui va g�rer l'�mission des objets (de type IAction) dans le stream
 *
 * Fonctionne sur une pile FIFO qui est synchronis�e
 * Permet d'une part de stocker les objets a envoyer
 * d'autre part de g�rer une mise en attente passive lorsqu'il n'y a rien � envoyer.
 *
 */

public class Emission implements Runnable{

	private ObjectOutputStream out;
	private boolean bRunning = false;;
	private static LinkedList<IAction> fifo = FifoSenderAction.getInstance();
	
	// Le constructeur r�cup�re le stream
	public Emission( ObjectOutputStream oos ) {
		out = oos;
	}

	// m�thode synchronis�e sur la pile g�rant l'insertion d'un objet IAction
	public void addAction( IAction action ){
		synchronized (fifo) {
			// ajout de l'item
			fifo.addLast(action);
			// reveil du thread
			fifo.notifyAll();
		}
		
	}
	
	// m�thode synchronis�e sur la pile g�rant le retrait d'un objet IAction
	protected IAction getAction(){
		IAction action = null;
		synchronized (fifo) {
			try{
				action = fifo.removeFirst();
			}catch(NoSuchElementException e){}
		}
		return action;
	}

	
	// m�thode synchronis�e sur la pile g�rant l'arret des traitements
	public void stop(){
		// on break la boucle du thread
		bRunning = false;
		synchronized (fifo) {
			// on vide la pile
			fifo.clear();
			// on reveille le thread
			fifo.notifyAll();
		}

	}
	
	
	@Override
	public void run() {
		// si nous entrons dans le Thread : nous sommes en Running
		bRunning = true;
		
		// Tant qu'on est Running
		while( bRunning ){
			// si la pile est vide... on attend
			if( fifo.size() == 0  )
				synchronized (fifo) {
					try {
						fifo.wait();
					} catch (InterruptedException e) {}
				}
			
			// Tant qu'il y a des Actions et qu'on est Running, on les envoies
			IAction action;
			while( (action = getAction()) != null && bRunning ){
				try {
					// on �crit l'Action dans le stream
					out.writeObject( action );
					// on tire la chasse...
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
		
		// On ferme la porte en sortant!
		try {
			out.close();
		} catch (IOException e) {}
		
	}

}
