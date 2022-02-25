package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Enumeration;

public class Server extends Thread{

	private ServerSocket server;
	private Socket socket;
	private int puerto;
	
	public OnMessageListener listener;
	
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(puerto);
			
			while(true) {
			System.out.println("Esperando un cliente");
			socket = server.accept();
			System.out.println("Cliente conectado");
			InputStream is = socket.getInputStream();
			BufferedReader breader = new BufferedReader(new InputStreamReader(is));
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bwriter = new BufferedWriter(osw) ;
				String msg = breader.readLine();
				System.out.println(msg);
				if(msg.equalsIgnoreCase("whatTimeIsIt")) {
					Calendar c = Calendar.getInstance();
					String fecha = c.getTime().toString();
					bwriter.write(fecha+"\n");
					bwriter.flush();
					listener.time(fecha);
				}else if(msg.equalsIgnoreCase("remoteIpconfig")) {
					InetAddress ip = InetAddress.getLocalHost();
                    bwriter.write(ip.getHostAddress()+"\n");
                    bwriter.flush();
				}else if(msg.equalsIgnoreCase("interface")) {
					InetAddress localHost = InetAddress.getLocalHost();
					NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
					byte[] hardwareAddress = ni.getHardwareAddress();
					String[] hexadecimal = new String[hardwareAddress.length];
					for (int i = 0; i < hardwareAddress.length; i++) {
					    hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
					}
					String macAddress = String.join("-", hexadecimal);
					bwriter.write(macAddress+"\n");
                    bwriter.flush();
				}else {
					bwriter.write(msg+"\n");
					bwriter.flush();
				}
			
			}
		}catch(IOException ex) {
			
		}
		
	}
	
	public void setListener(OnMessageListener listener) {
		this.listener = listener;
	}
	
	public interface OnMessageListener{
		public void time(String msg);
	}
}
