
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class Solver {
	
	
	
	public static void main(String[] args) {
		//CLi Definition Stage
		Options options = new Options();
		options.addOption("inst",true,"define the graph file to run algorithm");
		options.addOption("alg",true,"define the algorithm to solve problem");
		options.addOption("time",true,"cutoff time in seconds");
		options.addOption("seed",true,"random seed");
		
		//CLi Parse Stage
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException exp) {
			System.err.println("Parsing failed. Reason: "+exp.getMessage());
		}
		//begin Interrogation Stage
		String file = cmd.getOptionValue("inst");
		if(file == null) {
			System.err.println("error file name format");
		}
		String cutOffTime = cmd.getOptionValue("time");
		String alg = cmd.getOptionValue("alg");
		String seed = cmd.getOptionValue("seed");
		
		//System.out.println("cutoff time "+cutOffTime+" alg "+alg+" seed: "+seed);  
		 
		//add test code here to run different algorithm
		Graph g = parseFile(file);
		Collections.sort(g.vertexList);
//		System.out.println(g.getVertex(1).toString());
//		System.out.println(g.getVertex(2).toString());

		BnB BnBsolve = new BnB(600,g);
		
		BnBsolve.DFS(-1);
		System.out.println("we find the optimal solution!");
		System.out.println(BnBsolve.optimalSolution.size());
		
	}
	
	public static int getEdgeIndex(int vertexId1,int vertexId2) {
		int larger = Math.max(vertexId1, vertexId2);
		int smaller = Math.min(vertexId1, vertexId2);
		return smaller*50000+larger;
	}

	public static Graph parseFile(String dataFile) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(dataFile));
		} catch (FileNotFoundException e) {
			System.err.println("fail to open the resource file: "+dataFile );
			e.printStackTrace();
		}
		
		int size = sc.nextInt();
		int Edgesize = sc.nextInt();
		sc.nextLine();
		Graph g = new Graph(size);
		g.edgeMap = new HashMap<Integer,Edge>(Edgesize,(float)0.70);
		
		int currentIndex = 1;//start from the first vertex;
		while(sc.hasNextLine()) {
			Vertex currentVertex = g.getVertex(currentIndex);			
			List<Vertex> currentAdjList = currentVertex.getAdjVertexList();
			List<Edge> currentAdjEdgeList = currentVertex.getAdjEdgeList();
			String line = sc.nextLine();
			if(!line.isEmpty()) {
				String[] vertexs = line.trim().split("\\s+");			
				for(String adjVertexStr:vertexs) {
					//the index of the other Vertex
					int adjIndex = Integer.parseInt(adjVertexStr);
					//add it to current adjList;
					Vertex adjVertex = g.getVertex(adjIndex);
					currentAdjList.add(adjVertex);
				//	check the 'suppose to be' id of this edge;
					int edgeId =getEdgeIndex(currentIndex, adjIndex);
					if(!g.edgeMap.containsKey(edgeId)) {//create new edge if not already exist;
						g.edgeMap.put(edgeId, new Edge(currentVertex,adjVertex));
					}
					currentAdjEdgeList.add(g.edgeMap.get(edgeId));
					}
				}
			currentVertex.degree = currentAdjList.size();
			currentIndex++;
			if(currentIndex==size+1) break;
		}
		return g;	
	}
}
