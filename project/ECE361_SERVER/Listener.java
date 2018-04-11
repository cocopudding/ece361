import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.lang.Thread;


public class Listener implements Runnable {
	Socket soc;
	int p;
	public Listener (Socket socket, int packets)
	{
		soc = socket;
		p=packets;
	}
	public void run()
	{
		try{
			BufferedReader socket_bf = new BufferedReader(
					new InputStreamReader(soc.getInputStream()));
			
			String a;
			
			while(true){
					
					 a = socket_bf.readLine();
					 
					 //System.out.print("Received ack no: "+a+"\n");
					 MainClient.setAckNum(Integer.parseInt(a));
					 
					 if(Integer.parseInt(a)==p){
						 break;
					 }
				
			}
			soc.close();
		
		}catch (Exception e) {
			e.printStackTrace();
	}
	}
	

}