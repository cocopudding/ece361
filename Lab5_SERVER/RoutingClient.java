import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
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

public class RoutingClient {

	static String mode;
	static String host;
	static int port;

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
		// Complete the body of this function
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
		// Complete the body of this function
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
			Scanner scr = new Scanner(System.in);
			
			while(socket!=null && socket.isConnected() && !socket.isClosed()){
				System.out.println("Enter number of nodes in the network, 0 to Quit: ");
				int noNodes = scr.nextInt();
				

				// Send noNodes to the server, and read a String from it containing adjacency matrix
				writer.write(noNodes);
				String entry = reader.readLine();
						// Complete the code here 
				
				// Create an adjacency matrix after reading from server
				double[][] matrix = new double[noNodes][noNodes];
				
				// Use StringToenizer to store the values read from the server in matrix
				StringTokenizer strtok = new StringTokenizer(entry);
						// Complete the code here 
				int i = 0;
				while (strtok.hasMoreTokens()) {
					for (int j = 0; j<noNodes;j++)
					{
						matrix[i][j]=Double.parseDouble(strtok.nextToken());
					}
					i++;
				}
				
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
				
				// Finding shortest path for all nodes
				
						// Complete the code here 
				for(int b=0; b<noNodes; b++)
				{
					for (int d=0; d<noNodes; d++){
						nodeList.get(d).minDistance=Double.POSITIVE_INFINITY;
						nodeList.get(d).previous = null;
					}
					
					System.out.println("Node " + b);
					computePaths(nodeList.get(b));
					for(int c=0; c<noNodes;c++)
					{
						List<Integer> current = getShortestPathTo(nodeList.get(c));
						System.out.println("Totaltime to reach node " + b + ":  "+nodeList.get(c).minDistance+"ms, Path: "+current );
						
			
					}
					
				}
				
				
				
				socket.close();
			}
			System.out.println("Quit");


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
