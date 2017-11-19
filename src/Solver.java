
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

//import org.apache.commons.cli.CommandLine;
//import org.apache.commons.cli.CommandLineParser;
//import org.apache.commons.cli.DefaultParser;
//import org.apache.commons.cli.Options;
//import org.apache.commons.cli.ParseException;


public class Solver {
	
	public static void main(String[] args) throws IOException {
		
		//CLi Definition Stage
//		Options options = new Options();
//		options.addOption("inst",true,"define the graph file to run algorithm");
//		options.addOption("alg",true,"define the algorithm to solve problem");
//		options.addOption("time",true,"cutoff time in seconds");
//		options.addOption("seed",true,"random seed");
//
//		//CLi Parse Stage
//		CommandLineParser parser = new DefaultParser();
//		CommandLine cmd = null;
//		try {
//			cmd = parser.parse(options, args);
//		} catch (ParseException exp) {
//			System.err.println("Parsing failed. Reason: "+exp.getMessage());
//		}
//		//begin Interrogation Stage
//		String file = cmd.getOptionValue("inst");
//		if(file == null) {
//			System.err.println("error file name format");
//		}
//		String cutOffTime = cmd.getOptionValue("time");
//		String alg = cmd.getOptionValue("alg");
//		String seed = cmd.getOptionValue("seed");
		
		//System.out.println("cutoff time "+cutOffTime+" alg "+alg+" seed: "+seed);  
		 
		//add test code here to run different algorithm
		int cutoff = 600;
		int randseed = 10000000;
		int i = 0;
		Graph a1 = parseFile(args[1]);//HC_MVC.HC(a1,args[0]);
		Graph a2 = parseFile(args[7]);
		Graph a3 = parseFile(args[2]);
		Graph a4 = parseFile(args[3]);
		Graph a5 = parseFile(args[4]);
		Graph a6 = parseFile(args[5]);
		Graph a7 = parseFile(args[6]);
		Graph a8 = parseFile(args[0]);
		Graph a9 = parseFile(args[8]);
		Graph a10 = parseFile(args[9]);
		Graph a11 = parseFile(args[10]);

		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a1,args[1], randseed, cutoff);
			HC_SA.HCSA(a1,args[1], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a2,args[7], randseed, cutoff);
			HC_SA.HCSA(a2,args[7], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a3,args[2], randseed, cutoff);
			HC_SA.HCSA(a3,args[2], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a4,args[3], randseed, cutoff);
			HC_SA.HCSA(a4,args[3], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a5,args[4], randseed, cutoff);
			HC_SA.HCSA(a5,args[4], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a6,args[5], randseed, cutoff);
			HC_SA.HCSA(a6,args[5], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a7,args[6], randseed, cutoff);
			HC_SA.HCSA(a7,args[6], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a8,args[0], randseed, cutoff);
			HC_SA.HCSA(a8,args[0], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a9,args[8], randseed, cutoff);
			HC_SA.HCSA(a9,args[8], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a10,args[9], randseed, cutoff);
			HC_SA.HCSA(a10,args[9], randseed, cutoff);
		}
		for(i = 0; i < 1; i++){
			HC_MVC.HCMVC(a11,args[10], randseed, cutoff);
			HC_SA.HCSA(a11,args[10], randseed, cutoff);
//			HC_SA.HCSA(a8,args[0], randseed, cutoff);//run test.graph
		}

//		System.out.println(g.getVertex(1).toString());
//		System.out.println(g.getVertex(5).toString());
		
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
