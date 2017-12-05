
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


//Author: all team members
//togather we write out a main class and make sure it's easy to use for all of us.
//thanks to Cong Du for adding the parameters parsing code and file reading function.


public class Solver {
	public static void main(String[] args) throws IOException {
		
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
		SolutionRecorder recorder = SolutionRecorder.getInstance();
		
		
		int seed_int = (seed==null)?1:Integer.parseInt(seed);
		int cutOffTime_int = (cutOffTime==null)?600:Integer.parseInt(cutOffTime);
		recorder.configure(file, alg, seed_int, cutOffTime_int);
		//System.out.println("cutoff time "+cutOffTime+" alg "+alg+" seed: "+seed);  
		 
		//add test code here to run different algorithm
		Graph g = parseFile(file);
		if(alg==null) {
			System.err.println("no algorithm detected, please choose an algorithm!");
		}
		else if(alg.equals("BnB")) {
			String[] temp = file.split("\\.|/");
			BnBClone BnBsolve = new BnBClone(cutOffTime_int,g);
			BnBsolve.DFS(-1);
			System.out.println("we find the optimal solution!");
			System.out.println(BnBsolve.optimalSolution.size());
			System.out.println("used " + (System.currentTimeMillis()-BnBsolve.start)/1000 + "seconds");
			recorder.printSolution(BnBsolve.optimalSolution);
		}
		else if(alg.equals("Approx")) {
			Approx.solve(g);
		}
		else if(alg.equals("LS1")) {
			LocalSearch lsSolver = new LocalSearch(cutOffTime_int,g,seed_int);
			System.out.println(lsSolver.HCsolve());
		}
		else if(alg.equals("LS2")) {
			LocalSearch lsSolver = new LocalSearch(cutOffTime_int,g,seed_int);
			System.out.println(lsSolver.SAsolve());
		}
		else {
			System.err.println("error algorithm name format");
		}
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
