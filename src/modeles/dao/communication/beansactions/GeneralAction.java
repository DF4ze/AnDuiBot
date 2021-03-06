package modeles.dao.communication.beansactions;

import java.util.Date;

/**
 * 
 * Classe qui permet de généraliser certaines partie propre aux Actions
 *
 */


public abstract class GeneralAction implements IAction {

	private static final long serialVersionUID = 1L;
	
	private int priority 	= 0;
	private long timeStamp 	= 0;

	public GeneralAction() {
		setTimeStamp(0);
	}

	@Override
	public abstract String getAction();
	@Override
	public abstract boolean isComplete();

	@Override
	public long getTimeStamp() {
		return timeStamp;
	}

	@Override
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setTimeStamp(long timeStamp) {
		if( timeStamp == 0 )
			this.timeStamp = new Date().getTime();
		else
			this.timeStamp = timeStamp;
	}
	
	public String toString(){
		return getAction();
	}

}
