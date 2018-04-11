import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class StopAndWaitServer {


	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Server starts");
		try{
			
			Socket client;
			
			ServerSocket socket = new ServerSocket(1998);
			
			client=socket.accept();
	        			
			
			String connectl="Welcome to the Server";
			
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(client.getOutputStream());
			writer.writeBytes(connectl + "\r\n");
			
			int numPacket=socket_reader.read();
			int lastAck=0;
			System.out.print("CHECK NUMPACKET1 "+numPacket);
			while(lastAck<=numPacket){
				while(socket_reader.read()!=lastAck+1){
							
				}
				lastAck++;
				System.out.print("Packet Number received: "+lastAck+"\n");
				writer.write(lastAck);
				System.out.print("Ack Number sent to client: "+lastAck+"\n");
				
				
			}
			System.out.print("CHECK ACK "+lastAck);
			System.out.print("CHECK NUMPACKET2 "+numPacket);
			/*while(true){
				
			
		   String Filename=socket_reader.readLine();
		   
		   //defining a file with a specific name and checking if it exists:
		   File file=new File(Filename);
		   	
			
			
			//Transfer Files
			if (file.exists())
			{
				writer.writeBytes("File Transfer Starts" + "\r\n");
			
				FileInputStream fis= new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int len;
					//writer.write(buffer);
					//fis.read
				do{
					len=fis.read(buffer);
					if(len==-1)
						break;
					writer.write(buffer,0,len);				
				}
				while(len!=-1);
			    
				fis.close();
			System.out.println(file.length());
				//writer.writeBytes("File Transfer Completed" + "\r\n");
			}
				
					
				
			
		    //Report Error to the client
			else
				writer.writeBytes("File does not exist" + "\r\n");

				
			}	
			//String sendmessage=reader.readLine();
			//if(sendmessage.equalsIgnoreCase("quit"))
				//break;}*/
			socket.close();
		}catch(Exception e){e.getStackTrace();}
		
	}
	
	
	
}