
import java.util.List;
import java.util.ArrayList;


//Author: all team members
//together we figure out a data structure for Vertex and make sure it's easy to use for all of us.

public class Vertex implements Comparable<Vertex> {
	//private static int id = 0;
	 	public int myId;
	 	private List<Vertex> adjacentVertex;
	 	private List<Edge> adjacentEdge;
	 	public int degree;
	 	public boolean covered;
	 	
	 	public Vertex(int id) {
	 		this.myId = id;
	 		this.adjacentVertex = new ArrayList<Vertex>();
	 		this.adjacentEdge = new ArrayList<Edge>();
	 	}
	 	
	 	
	 	
	 	public Vertex(int id,List<Vertex> adj) {
	 		this.myId = id;
	 		this.adjacentVertex=adj;
	 	}

	 	public Vertex(Vertex v){
	 		this.myId = v.myId;
	 	}
	 	
	 	public void addAdjVertex(Vertex v) {
	 		this.adjacentVertex.add(v);
	 	}
	 	
	 	public void addAdjEdge(Edge e) {
	 		this.adjacentEdge.add(e);
	 	}
	 	
	 	public List<Vertex> getAdjVertexList(){
	 		return this.adjacentVertex;
	 	}
	 	
	 	public List<Edge> getAdjEdgeList(){
	 		return this.adjacentEdge;
	 	}
	 	
	 	public int getId() {
	 		return this.myId;
	 	}

	 	public int getDegree(){ // YHY
	 		return this.degree;
	 	}  //YHY
	 	
	 	public int[] getEdgeKeys(){
	 		int[] keys = new int [adjacentEdge.size()];
	 		for(int i=0; i<keys.length; i++){
	 			Edge temp = adjacentEdge.get(i);
	 			keys[i] = temp.id;
	 		}
	 		return keys;
	 	}
	 	
	 	@Override
	 	public int compareTo(Vertex v) {
	 		int comparing = v.degree;
	 		if(comparing-this.degree!=0)
	 			return comparing-this.degree;
	 		else {
	 			//breaking tie.
	 			return Integer.compare(this.myId,v.myId);
	 		}
	 	}

	 	@Override 
	 	public String toString() {
	 		StringBuilder sb = new StringBuilder();
	 		sb.append(this.myId);
/*	 		sb.append(" addjacent vertex:");
	 		for(Vertex v: adjacentVertex) {
	 			sb.append(" "+v.getId());
	 		}
	 		sb.append(" adjacent edge: ");
	 		for(Edge e: adjacentEdge) {
	 			sb.append(" "+e);
	 		}
	 		*/
	 		return sb.toString();
	 	}
}
