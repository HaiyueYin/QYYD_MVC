
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Solver {
	
	
	
	public static void main(String[] args) {
		Graph g = parseFile(args[0]);
		System.out.println(g.getVertex(1).toString());
		System.out.println(g.getVertex(5).toString());

		
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
				currentIndex++;
				if(currentIndex==size+1) break;
		}
		return g;	
	}
