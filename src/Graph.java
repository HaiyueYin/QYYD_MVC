import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

public class Graph {
	public List<Vertex> VertexList;
	public HashSet<Edge> EdgeList;
	int vertexSize, edgeSize;
	//maybe another boolean array to denote visited
	//add data structure as you need
	
	public Graph(int initVertexSize, int initEdgeSize) {
		VertexList = new ArrayList<>(initVertexSize+1);
		for(int i = 0;i<initVertexSize+1;i++) {
			VertexList.add(new Vertex(i));
		}
		EdgeList = new HashSet<Edge>(initEdgeSize);
		this.vertexSize = initVertexSize;
		this.edgeSize = initEdgeSize;
	}
	
	public Graph() {
		VertexList = new ArrayList<>();
		EdgeList = new HashSet<Edge>();
	}
	
	public int vertexSize() {
		return VertexList.size()-1;
	}

	public int edgeSize() {
		return EdgeList.size()-1;
	}
	
	public Vertex getVertex(int i) {
		return this.VertexList.get(i);
	}

	

}
