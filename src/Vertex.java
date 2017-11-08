import java.util.List;
import java.util.ArrayList;

public class Vertex {
	//private static int id = 0;
	private int myId;
	private List<Vertex> adjacentVertex;
	
	public Vertex(int id) {
		this.myId = id;
		this.adjacentVertex = new ArrayList<Vertex>();
	}
	
	public Vertex(int id,List<Vertex> adj) {
		this.myId = id;
		this.adjacentVertex = adj;//copy in the adjacentVertex as well
	}
	
	public List<Vertex> getAdjList(){
		return this.adjacentVertex;
	}
	
	public int getId() {
		return this.myId;
	}
	
	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.myId);
		sb.append(" addjacent:");
		for(Vertex v: adjacentVertex) {
			sb.append(" "+v.getId());
		}
		return sb.toString();
	}

}
