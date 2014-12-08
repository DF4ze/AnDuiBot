package modeles.dao.communication;

import java.util.LinkedList;

import modeles.dao.communication.beansactions.IAction;


/**
 * 
 * Classe qui permet simplement de gérer le singleton de la pile FIFO
 *
 */

public class FifoSenderAction {
	private static LinkedList<IAction> fifo = null;

	private FifoSenderAction() {
		
	}
	public static LinkedList<IAction> getInstance(){
		if( fifo == null ){
			fifo = new LinkedList<IAction>();
		}
		return fifo;
	}
}
