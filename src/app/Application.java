package app;

import comm.Server;

public class Application implements Server.OnMessageListener{

private Server connection;
	
	public Application() {
		connection = new Server();
		connection.setPuerto(6000);
		connection.setListener(this);
	}

	public void init() {
		connection.start();
		
	}

	@Override
	public void time(String msg) {
		System.out.println(msg);
	}
}
