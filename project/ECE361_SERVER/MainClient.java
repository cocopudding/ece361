import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.ws.handler.MessageContext.Scope;

// The network is represented by a graph, that contains nodes and edges
class Node implements Comparable<Node>
{
	public final int name;
	public Edge[] neighbors;
	public double minDistance = Double.POSITIVE_INFINITY;
	public Node previous;     // to keep the path
	public Node(int argName) 
	{ 
		name = argName; 
	}

	public int compareTo(Node other)
	{
		return Double.compare(minDistance, other.minDistance);
	}
}

class Edge
{
	public final Node target;
	public final double weight;
	public Edge(Node argTarget, double argWeight)
	{ 
		target = argTarget;
		weight = argWeight; 
	}
}

public class MainClient {
	
	public static int lastAck = 0;

	static String mode;
	static String host;
	static int port;
	
	public static void setAckNum (int ackNum)
	{
		if (lastAck<ackNum)
		lastAck = ackNum;
	}

	public static void adjacenyToEdges(double[][] matrix, List<Node> v)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			v.get(i).neighbors = new Edge[matrix.length];
			for(int j = 0; j < matrix.length; j++)
			{
				v.get(i).neighbors[j] =  new Edge(v.get(j), matrix[i][j]);	
			}
		}           
	}
	public static void computePaths(Node source)
	{
		source.minDistance = 0;
		PriorityQueue<Node> NodeQueue= new PriorityQueue<Node>();
		NodeQueue.add(source);
		while(!NodeQueue.isEmpty()){
			Node sourceNode=NodeQueue.poll();
			for(Edge e: sourceNode.neighbors)
			{
				Node targetNode = e.target;
				double distanceThroughSource = sourceNode.minDistance + e.weight;
				if(distanceThroughSource < targetNode.minDistance)
				{
					NodeQueue.remove(targetNode);
					targetNode.minDistance = distanceThroughSource;
					targetNode.previous = sourceNode;
					NodeQueue.add(targetNode);
				}
			}
		}
	}

	public static List<Integer> getShortestPathTo(Node target)
	{
		List<Integer> path = new ArrayList<Integer>();
		Node n = target;
		while (n!= null)
		{
		path.add(n.name);
		n = n.previous;
		}
		Collections.reverse(path);
		return path;
	}

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		if(args.length<=0)
		{
			mode="client";
			host="localhost";
			port=9876;
		}
		else if(args.length==1)
		{
			mode=args[0];
			host="localhost";
			port=9876;
		}
		else if(args.length==3)
		{
			mode=args[0];
			host=args[1];
			port=Integer.parseInt(args[2]);
		}
		else
		{
			System.out.println("improper number of arguments.");
			return;
		}

		try 
		{
			Socket socket=null;
			if(mode.equalsIgnoreCase("client"))
			{
				socket=new Socket(host, port);
			}
			else if(mode.equalsIgnoreCase("server"))
			{
				ServerSocket ss=new ServerSocket(port);
				socket=ss.accept();
			}
			else
			{
				System.out.println("improper type.");
				return;
			}
			System.out.println("Connected to : "+ host+ ":"+socket.getPort());

			//reader and writer:
			BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream())); //for reading lines
			DataOutputStream writer=new DataOutputStream(socket.getOutputStream());	//for writing lines.
			BufferedReader system_in_reader = new BufferedReader(new InputStreamReader(System.in));
			String reader_result;
			
			while(socket!=null && socket.isConnected() && !socket.isClosed()){
				
				//receive the number of nodes from the server				
				System.out.println("Waiting for the number of nodes... ");
				reader_result = reader.readLine();
				int noNodes = Integer.parseInt(reader_result);
				System.out.println("The number of nodes is : "+noNodes);
				
				
	 /************************************shortest path computation***************************************/
				
				// Create an adjacency matrix after reading from server
				double[][] matrix = new double[noNodes][noNodes];
				
				// Use StringToenizer to store the values read from the server in matrix
				String entry = reader.readLine();
				StringTokenizer strtok = new StringTokenizer(entry);
				int i = 0;
				while (strtok.hasMoreTokens()) {
					for (int j = 0; j<noNodes;j++)
					{
						matrix[i][j]=Double.parseDouble(strtok.nextToken());
					}
					i++;
				}
				
				// list out all the distance infromation about the nodes 
				System.out.println("adjancy matrix:\n ");
				for (int x =0; x<noNodes; x++)
				{
					for(int y = 0; y<noNodes;y++)
					{
						System.out.print(matrix[x][y] + "  ");
					}
					System.out.println();
				}
				
				//The nodes are stored in a list, nodeList
				List<Node> nodeList = new ArrayList<Node>();
				for(int m = 0; m < noNodes; m++){
					nodeList.add(new Node(m));
				}
				
				// Create edges from adjacency matrix
				adjacenyToEdges(matrix, nodeList);
				
				// Finding shortest path from node 0 to all other nodes.
				    int b = 0;
					for (int d=0; d<noNodes; d++){
						nodeList.get(d).minDistance=Double.POSITIVE_INFINITY;
						nodeList.get(d).previous = null;
					}
					
					System.out.println("Node " + b);
					computePaths(nodeList.get(b));
					
				// print out the shortest paths from node 0 to all other nodes
					for(int c=0; c<noNodes;c++)
					{
						List<Integer> current = getShortestPathTo(nodeList.get(c));
						System.out.println("Totaltime to reach node " + b + ":  "+nodeList.get(c).minDistance+"ms, Path: "+current );		
					}	
					
				//send the shortest path to the server
					List<Integer> path_to_server = getShortestPathTo(nodeList.get(noNodes-1));
					writer.writeBytes(path_to_server.toString() + "\r\n");
			
			
	  /************************************file transfer***************************************/
			
			System.out.print("Enter the name of the file: ");
			 String Filename=system_in_reader.readLine();
			 
			   //defining a file with a specific name and checking if it exists:
			   File file=new File(Filename);
			   byte[][] buffer;
			   int sent = 1;

			   
			   if (file.exists())
				{
				   //timeout interval is set to 2* delay of the shortest path from node0 + node "noNode-1"
				   long time_out=2*(long)(nodeList.get(noNodes-1).minDistance)+200;		
				   
				   //write the file name and NO of packets to the server
				   long file_len = file.length();
				   System.out.println("The length of the file is: " + file_len);				   
				   long no_packets = file_len/1000 +1;
				   writer.writeBytes(Filename +"\r\n");
				   writer.writeBytes(Long.toString(no_packets) +"\r\n");
				
				  
				   //create an 2D byte array to read all the file data
				    System.out.println("starting creation 2D array for file content");
					FileInputStream fis= new FileInputStream(file);
					buffer = new byte[(int) no_packets][1004];
					for (byte [] a: buffer)
						fis.read(a, 4, 1000);
						System.out.println("done creation 2D array");						
						
					   //creating a new listner thread
						Thread thread=new Thread(new Listener(socket,(int)no_packets));
		  				thread.start();
					
		  				//declare and define some useful variables
		  				 int ssthresh=100;
						 int cwnd=1;
						 int index=1;
						 long[] current_time=new long[(int)no_packets];
						 int tester=0;
						 long check_time=0;
						 long beginningTime=System.currentTimeMillis();
						 
						 
		  			//writing the sequence number and sending packages
					while(lastAck<no_packets)
					{
						
						  // if the server asks us to quit unexpectedly
							 if (lastAck == -1)
			            			break;
							 
							 System.out.println();
			            	 System.out.println("cwnd = "+cwnd);
			            	 //send the "cwnd" number of packets
			            	 while(index<=cwnd && sent<=no_packets)
			            		{
			            		   //write the sequence NO into the first four bytes of the packet
			            		 	ByteBuffer buf = ByteBuffer.allocate(4);
									buf.putInt(sent);
									for (int f = 0; f < 4; f++)
										buffer[sent-1][f] = buf.get(f);
						
									 //accurately record the send time and sent the packet
									 current_time[sent-1]=System.currentTimeMillis();
			            			 System.out.println("Sending packet no: "+sent);
			            			 writer.write(buffer[sent-1]);
			            			 index++; 
			            			 sent++;
			            		}
			            	
			                //wait here until we received all the Acks or timeout fires, whichever comes first
			            	 if(lastAck!=sent-1){
			            		 while( lastAck!= sent-1)
				            		{     
			            			 	check_time=System.currentTimeMillis();
				            			if((check_time - current_time[sent-2]) > time_out)
				            			{
						    			    break;	
				            			}
				            		}
			            	 }
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
			            		 //in case of timeout
			            		 if(lastAck!=sent-1){
			            				
			            				System.out.println("Timeout!");
			            				ssthresh = cwnd/2;
			            				cwnd = 1;
			            				sent = lastAck+1;
			            		 }
			            		
			            	 index = 1;
			            	 System.out.println("last ack: "+lastAck);

		            		 
						 
					}
					 long endTime = System.currentTimeMillis();
		    		 long totalTime = endTime - beginningTime;
		    		 System.out.println("No. of packets transimitted = " + no_packets);
		    		 System.out.println("Total time used = " + totalTime);	 
		    		 //close the file and thread
		    		 fis.close();
				}
			   else
			   {
				   System.out.println("file does not exist");
			   }
			
			
			
			System.out.println("Quiting...");
			socket.close();
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}