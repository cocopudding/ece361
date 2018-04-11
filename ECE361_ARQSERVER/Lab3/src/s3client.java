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


public class s3client {

	static int lastAck = 0;
	public static void setAckNum (int ackNum)
	{
		s3client.lastAck = ackNum;
	}
	public static void main(String[] args) {
		try {
			// define a (client socket)
			Socket socket = new Socket("localhost", 9876);
			BufferedReader socket_bf = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			
			DataInputStream InputData=new DataInputStream (socket.getInputStream());
			
		
            DataOutputStream socket_dos = new DataOutputStream(
					socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            

            //long StartTime=0;
			//long result;
			//long EndTime;
			//int i =0;
            
       
                System.out.print("Enter number of packets to be sent to the server, 0 to quit: \n");
            	Scanner scr=new Scanner(System.in);
            	int noPackets = scr.nextInt();
            	//int probError = 0;
            	socket_dos.write(noPackets);
            	
            	System.out.print("Enter the probability of drooping packets(0-100): \n");
            	scr = new Scanner(System.in);
                int probError = scr.nextInt();
                socket_dos.write(probError);
                
                System.out.print("Enter the window size: \n");
            	scr = new Scanner(System.in);
                int wSize = scr.nextInt();
                
                System.out.print("Enter the timeout interval: \n");
            	scr = new Scanner(System.in);
                int timeOut = scr.nextInt();

                long [] timer = new long [wSize];
                
                Thread thread=new Thread(new listener(socket));
				thread.start();

                int sent=1;
                int done = 0;        
                while (sent <= noPackets)
                {
                	int first = sent;
                	int n = 0;
                 while(n<wSize){
            		socket_dos.write(sent);
            		timer[n]=System.currentTimeMillis();
            		System.out.print("Packet number sent:");
                	System.out.print(sent);
                	System.out.println();
                	n++;
                	
                	sent++;
                	if(sent>noPackets){
                		done =1;
                		break;
                	}
            	}
            	
            	
            	n = 0;
            	
            	while(n<wSize){
            		int out = 1;
            		while(System.currentTimeMillis()-timer[n]<timeOut){
            			if(lastAck>=first+n){
            				out=0;
            				n = lastAck-first;
            				//System.out.println(lastAck);
            				System.out.print("Packet number acknowledged:");
                        	System.out.print(n+first);
                        	System.out.println();
                        	n++;
            				break;
            			}
            		}
            		if(out==1){
            			System.out.println("TIMEOUT");
            			break;
            		}
            		
            	}
            	sent = first + n;
            }
            
		
	
            		//System.out.print("lastAck is: "+lastAck+"\n");
            		/*if(sent<=wSize)
            		{
            			
            			socket_dos.write(sent);
            			System.out.print("Packet Number sent to server: "+sent+"\n");
                		timer[(sent-1)%wSize]=System.currentTimeMillis();
                		sent++;
                		System.out.print("lastAck is: "+lastAck+"\n");
                		first = sent;
                		n = 0;
            		}
            		else{
            			int out = 1;
                		while(System.currentTimeMillis()-timer[n]<timeOut){
                			if(lastAck>=first+n){
                				out=0;
                				n = lastAck-first;
                				System.out.println(lastAck);
                				System.out.print("Packet number acknowledged:");
                            	System.out.print(n+first);
                            	System.out.println();
                            	n++;
                				break;
                			}
                		}
                		if(out==1){
                			System.out.println("TIMEOUT");
                			break;
                		}
                		
                	}
                	sent = first + n;
            		}*/
            		/*else if((sent-lastAck)<=wSize)
            		{
            			
            			System.out.print("lastAck is: "+lastAck+"\n");
            				socket_dos.write(sent);
            				System.out.print("Packet Number sent to server: "+sent+"\n");
            				timer[(sent-1)%wSize]=System.currentTimeMillis();
            				sent++;
            			
            			
            		}
            		else
            		{
            			int a=0;
        				a++;
            			if (System.currentTimeMillis()-timer[lastAck%wSize]>timeOut)
            			{
            				
            				System.out.print("Timeout\n");
            				break;
            			}
            		}*/
            		/*
            		while(true){
            			if(sent==lastAck)
            			{
            		 	System.out.print("Ackknowlege Number received: "+lastAck+"\n");
            				break;
            			}
            		}
            		socket_dos.write(sent);
            		System.out.print("Packet Number sent to server: "+sent+"\n");
            		
            		sent=sent+1;*/
            		
            	//}
            	
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

