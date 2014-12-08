package modeles.dao.communication;


/**
 * 
 * Modele pour la connexion au serveur via une socket
 *
 */


public class ArduiBotServer {
	private String name;
	private String ip;
	private int port;

	public ArduiBotServer( ) {
		this.setName("AnDuiBot");
		this.setIp("10.114.132.44");
		this.setPort(2009);
	}	
	
	public ArduiBotServer( String name, String ip, int port ) {
		this.setName(name);
		this.setIp(ip);
		this.setPort(port);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
