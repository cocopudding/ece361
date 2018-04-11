import java.io.*;
import java.net.*;

public class lab1client {


		 public static void main(String[]args){
		System.out.println("Hello World");// prints Hello World
		try{
			Socket socket = new Socket("localhost",9876);
			
			
			 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));			 
			 BufferedReader client_reader = new BufferedReader(new InputStreamReader(System.in));
			 DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			 
			 System.out.println(reader.readLine());
			 while (true) {
				 
			 String user_input = client_reader.readLine();
//			 System.out.println("receive: " + reader.readLine());

			 if(user_input.equals("quit"))
				 break;
			 
			 long start = System.currentTimeMillis();
			// System.out.println("start: " + start);
		     writer.writeBytes(user_input + "\r\n");
		     String temp = reader.readLine();
		     long end = System.currentTimeMillis();
		    // System.out.println("end: " + end);
			System.out.println("receive: " + temp);
			long result=end-start;
			 System.out.println("time: " + result);
			 }
			socket.close();
		}catch(Exception e){e.getStackTrace();}
		
		}

}
