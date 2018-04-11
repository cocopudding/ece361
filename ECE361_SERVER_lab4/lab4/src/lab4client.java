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


public class lab4client {
	public static int lastAck = 0;

	
	public static void main(String[] args) {
		try {
			// define a (client socket)
			Socket socket = new Socket("localhost", 9878);
			BufferedReader socket_bf = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			
			DataInputStream InputData=new DataInputStream (socket.getInputStream());

		
            DataOutputStream socket_dos = new DataOutputStream(
					socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            Scanner scr=new Scanner(System.in);

            	Thread thread=new Thread(new Listener(socket));
  				thread.start();
            	
            	 System.out.println("Enter number of packets to be sent to the server:");
            	int noPackets = scr.nextInt();

            	socket_dos.write(noPackets);
		
		long[] timer_array = new long[noPackets];	
            	
            	long EstimatedRTT = 1200;
            	int cwnd =1;


            	int sent=0;
            	int i=1;
            	long startTime = 0;
            	int timeout = 0;
            	int ssthresh = 16;

  		long beginningTime=System.currentTimeMillis();

            	while(sent<noPackets){
            		if (lastAck == -1)
            			break;
            		 System.out.println("cwnd = "+cwnd+"\n");
            		while(i<=cwnd && sent<noPackets)
            		{
            			 sent++;
            			 timer_array[sent-1]=System.currentTimeMillis();
            			 timeout = 0;
            			 socket_dos.write(sent);
            			 System.out.print("Sending packet no: "+sent+"\n");
            			 i++;
            			 
            		}

            		while( lastAck < (sent-1))
            		
            		{

        
            			if((System.currentTimeMillis() - timer_array[sent-1]) > EstimatedRTT)
            			{
					sent = lastAck;
		    			ssthresh = cwnd/2;
		    			cwnd = 1;

	 				System.out.println("Timeout!\n");
		    			   break;	
            			}
            		}
            		i = 1;
            		  
        			   
            		System.out.println("last ack: "+lab4client.lastAck+"\n");
            		

            			 //slow start
            			 if(ssthresh >  cwnd)
            			 {
            				 cwnd = 2*cwnd; 
            			 }
            			 //congestion avoidance
            			 else
            			 {
            				 cwnd = cwnd+1;
            			 }
            			           		
            		
            	}
    			long endTime = System.currentTimeMillis();
    			long totalTime = endTime - beginningTime;
    			System.out.println("No. of packets = " + noPackets);
    			System.out.println("Total time = " + totalTime);
            	
            socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void setAckNum (int ackNum)
	{
		if (lastAck<=ackNum)
		lab4client.lastAck = ackNum;
		System.out.println("Received ack no = "+lastAck);
	}
}
