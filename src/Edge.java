
public class Edge {
	
	private Vertex v1;
	private Vertex v2;
	private int id;
	public boolean covered;
	//edge constructor by vertex
	public Edge(Vertex v1, Vertex v2) {
		this.id = Solver.getEdgeIndex(v1.getId(), v2.getId());
		this.v1=v1;
		this.v2=v2;
		this.covered = false;
	}
	
	//edge constructor by edge
	//should not be used
	/*
	 public Edge(Edge e) {//current use address pass not copy a new one
	 
		//this.v1=new Vertex(e.v1.getId());
		//this.v2=new Vertex(e.v2.getId());
		this.v1=e.v1;
		this.v2=e.v2;
	}
	*/
	
	//check if edge constains given vertex v
	public boolean contains(Vertex v) {
		if(v1.getId()==v.getId()||v2.getId()==v.getId()) {
			return true;
		}
		return false;
	}
	//check if edge contains vertex i directly
	public boolean contains(int i) {
		if(this.v1.getId()==i||this.v2.getId()==i) {
			return true;
		}
		return false;
	}
	//check if two edges are equal
	public boolean equals(Edge e) {=
		if(this==e)=
			return true;
		if(this.id == e.id) {
			return true;
		}
		return false;
	}
	//print the edge
	@Override 
	public String toString() {=
		return("Between vertex: "+v1.getId()+" and vertex: "+v2.getId());=
	}
}
