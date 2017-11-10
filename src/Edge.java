
public class Edge {
	Vertex v1;
	Vertex v2;
	//edge constructor by vertex
	public Edge(Vertex v1, Vertex v2) {
		this.v1=v1;
		this.v2=v2;
	}
	//edge constructor by edge
	public Edge(Edge e) {//current use address pass not copy a new one
		//this.v1=new Vertex(e.v1.getId());
		//this.v2=new Vertex(e.v2.getId());
		this.v1=e.v1;
		this.v2=e.v2;
	}
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
	public boolean equals(Edge e) {
		if(this == e)
			return true;
		else if (e == null)
			return false;
		else if(
			(this.v1.equals((Vertex)e.v1)&&this.v2.equals((Vertex)e.v2))||
			(this.v1.equals((Vertex)e.v2)&&this.v2.equals((Vertex)e.v1)))
			return true;
		return false;
	}
	//print the edge
	@Override 
	public String toString() {
		return(v1.getId()+"-"+v2.getId());
	}
}
