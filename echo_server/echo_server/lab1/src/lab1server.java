
import java.net.*; 
import java.io.*; 

public class lab1server {
	 public static void main(String[]args){
		 try{
			 ServerSocket serverSocket = null; 
			 serverSocket = new ServerSocket(9876);
			 
			 Socket clientSocket = null; 
			 clientSocket = serverSocket.accept();
			 System.out.println ("Client online");
			 
			 DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());
			 BufferedReader client_reader = new BufferedReader(new InputStreamReader( clientSocket.getInputStream())); 
			 BufferedReader server_reader = new BufferedReader(new InputStreamReader(System.in));
			 writer.writeBytes("Welcome" + "\r\n");
			 
			 String user_input=client_reader.readLine();
			 while (user_input!=null) {
				 
				 System.out.println("receive: " + user_input);
				 writer.writeBytes(server_reader.readLine() + "\r\n");
				 user_input=client_reader.readLine();
				 
			 }
			 

			    clientSocket.close(); 
			    serverSocket.close(); 
			 
		 }catch(Exception e){e.getStackTrace();}
	 }

}
