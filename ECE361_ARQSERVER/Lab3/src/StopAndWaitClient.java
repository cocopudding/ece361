import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;


public class StopAndWaitClient {

	
	public static void main(String[] args) {
		try {
			// define a (client socket)
			Socket socket = new Socket("localhost", 1998);
			BufferedReader socket_bf = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			
			DataInputStream InputData=new DataInputStream (socket.getInputStream());
			
		
            DataOutputStream socket_dos = new DataOutputStream(
					socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
        	//Response Message from Server
        	String str = socket_bf.readLine();
            System.out.println(str);
            
            //long StartTime=0;
			//long result;
			//long EndTime;
			//int i =0;
            
       
            	
            	Scanner scr=new Scanner(System.in);
            	int noPackets = scr.nextInt();
            	socket_dos.write(noPackets);
            	int sent=1;
            	
            	while(sent<=noPackets){
            		socket_dos.write(sent);
            		System.out.print("Packet Number sent to server: "+sent+"\n");
            		
            		while(true){
            			if(socket_bf.read()==sent)
            			{
            				System.out.print("Ackknowlege Number received: "+sent+"\n");
            				break;
            			}
            		}
            		
            		sent=sent+1;
            		
            	}
            	
            	/*String Userinput = reader.readLine();
          	    

            	socket_dos.writeBytes(Userinput+"\r\n");
            	//EndTime=System.currentTimeMillis();
    			//result=EndTime-StartTime;
    			//if(i!=0)
    			//System.out.println("Round time is "+result);
    			//i++;
            	//Server Response
            	
            	str = socket_bf.readLine();
            	if(str.equalsIgnoreCase("File does not exist")) {
            		 System.out.println(str);
               	  	// StartTime=System.currentTimeMillis(); 

            	}
               
            	
            	else
            	{
            		System.out.println("Start receiving data...");
            		
            		FileOutputStream fos= new FileOutputStream(Userinput);
 
            		int len;
            		byte[] buffer = new byte[1024];
            	  do{
            		 if(InputData.available()!=0)
            		 {            		  
            		  len=InputData.read(buffer); 
            		  fos.write(buffer,0,len); 
            		 }
            		 
            	  	} 
            		  while(InputData.available()!=0);
            	  //StartTime=System.currentTimeMillis(); 
            	  System.out.println("Done receiving data."); 
            	// i++;
            	  fos.close();                             	   
            	   File file=new File(Userinput);
             	  System.out.println("The file's length is " +file.length() +" bytes"); 

            	  
            	  
            	  
            	  
            	   }
            	if(str.equalsIgnoreCase("quit"))
    				break;	
    				*/

            
            
            socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}

