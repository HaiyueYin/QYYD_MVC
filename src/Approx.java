import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

//@author: Cong Du
//inside this class is the heuristic solution
//we implement the very easy to code ListLeft algorithm provided by reference [4]

public class Approx {
	public static void solve(Graph graph) {
		long start = System.currentTimeMillis();
		//sort it first
		graph.vertexCovered = new boolean[graph.vertexList.size()];
		List<Vertex> sortedList = new ArrayList<>(graph.vertexList.subList(1, graph.vertexList.size()));
		Set<Integer> onLeft = new HashSet<>();
		Set<Vertex> VertexCoverSol = new HashSet<>();
		Collections.sort(sortedList, new Comparator<Vertex>() {//descending order
			@Override
			public int compare(Vertex v1, Vertex v2) {
				return Integer.compare(v2.degree, v1.degree);
			}
		});
		for(int i=0;i<sortedList.size();i++) {
			//interation through the list from left to right;
			Vertex currentVertex = sortedList.get(i);
			int currentId = currentVertex.getId();
			boolean feasible = false;
			for(Vertex neighbor:currentVertex.getAdjVertexList()) {
				int neighborId = neighbor.getId();
				if(!onLeft.contains(neighborId)&&!graph.vertexCovered[neighborId]) {//if it's on right and not covered
					feasible = true;
					break;
				}
			}
			if(feasible == false) {
				onLeft.add(currentId);
				continue;//go to next vertex
			}else {
				VertexCoverSol.add(currentVertex);//add it into solution
				graph.vertexCovered[currentId] = true;//mark it as covered (used)
				onLeft.add(currentId);//tell following node this node is on the left
				for(Edge e:currentVertex.getAdjEdgeList()) {
					if(e.covered==false) {
						e.covered = true;
						graph.coveredEdgeSize++;
					}
				}
				if(graph.coveredEdgeSize == graph.edgeMap.size()) {
					break;
				}
			}
		}
		long end = System.currentTimeMillis();
		double time =(double)((end-start)/1e3);
		System.out.println("Vertex Cover Size:"+VertexCoverSol.size()+" time:"+time);
		SolutionRecorder sr = SolutionRecorder.getInstance();
		sr.printTrace("%.3f,%d%n", time, VertexCoverSol.size());//trace
		sr.printSolution(new ArrayList<Vertex>(VertexCoverSol));
	}
}
