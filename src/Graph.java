
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

//Author: all team members
//together we figure out a data structure for Graph and make sure it's easy to use for all of us.

public class Graph {
	public List<Vertex> vertexList;
	 	//maybe another boolean array to denote visited
	 	//add data structure as you need
	public Map<Integer,Edge> edgeMap;
	public boolean[] vertexCovered;//should be vertexUsed
	public int coveredEdgeSize = 0;
	 
	
	public Graph(int initSize) {
	 		vertexList = new ArrayList<>(initSize+1);
	 		for(int i = 0;i<initSize+1;i++) {
	 			vertexList.add(new Vertex(i));
	 		}
	 		vertexCovered = new boolean[initSize+1];
	 		//don't initialize Edge here because we can initialize with a initial size later.
	 		
	 	}
	 	
	public Graph() {
	 		vertexList = new ArrayList<>();
	 		edgeMap = new HashMap<>();
	 	}
	 	
	public int size() {
	 		return vertexList.size()-1;
	 	}
	 	
	public Vertex getVertex(int i) {
	 		return this.vertexList.get(i);
	 	}
	
	public Edge getEdge(int id) {
		return this.edgeMap.get(id);
	}
}
