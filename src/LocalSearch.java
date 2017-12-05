import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;


// Author: Haiyun Yin
// Local Search algorithm implmentations by Hill Climbing and Simulated Annealing.
// Thanks to Cong Du for code refactoring.
public class LocalSearch {
	private Graph g;
	private long cutoffTime;
	private long start;
	private Random r;
	private int currentCoverSize = 0;
	private SolutionRecorder recorder;
	
	public LocalSearch(long cutoff,Graph graph,int seed) {
		this.cutoffTime = cutoff;
		this.g = graph;
		this.start = System.currentTimeMillis();
		r = new Random(seed);
		this.currentCoverSize = g.vertexList.size()-1;
		this.recorder = SolutionRecorder.getInstance();
	}
	
	

	public int HCsolve() {
		//get the priorityQueue;
		PriorityQueue<Vertex> pq= new PriorityQueue<Vertex>(Collections.reverseOrder());
		for(int i=1;i<g.vertexList.size();i++) {
			pq.add(g.vertexList.get(i));
		}
		//set all vertex as covered at the very beginning
		for(int i=0;i<g.vertexCovered.length;i++) {
			g.vertexCovered[i] = true;
		}
		//set all edges as covered;
		Iterator<Map.Entry<Integer, Edge>> edges = g.edgeMap.entrySet().iterator();
		while(edges.hasNext()) {
			edges.next().getValue().covered=true;
		}
		g.coveredEdgeSize = g.edgeMap.size();
		
		while(pq.size()!=0&&System.currentTimeMillis()<=(start+cutoffTime*1000)) {
			int degree = pq.peek().degree;
			List<Vertex> currentDegreeList= new ArrayList<Vertex>();
			//add all vertex with same degree to the List;
			while(!pq.isEmpty()&&degree==pq.peek().degree) {
				currentDegreeList.add(pq.poll());
			}
			while(!currentDegreeList.isEmpty()) {
				int index = r.nextInt(currentDegreeList.size());
				Vertex toRemove = currentDegreeList.remove(index);
				boolean deleteable = true;
				//check all the adj vertex, if not covered, the node cannot be moved.
				for(Vertex adj:toRemove.getAdjVertexList()) {
					if(g.vertexCovered[adj.myId]==false) {
						deleteable = false;
						break;
					}
				}
				if(deleteable) {
					g.vertexCovered[toRemove.myId] = false;
					currentCoverSize--;
					recorder.printTrace("%.3f,%d%n", (double)(System.currentTimeMillis()-start)/1000, currentCoverSize);
				}
			}
		}
		List<Vertex> optSolution = new ArrayList<Vertex>(currentCoverSize);
		for(int i=1;i<g.vertexCovered.length;i++) {
			if(g.vertexCovered[i]==true) {
				optSolution.add(g.vertexList.get(i));
			}
		}
		
		recorder.printSolution(optSolution);
		
		return currentCoverSize;
	}
	
	public int SAsolve() {
		//set all vertex as covered at the very beginning
		int bestCoverSize = this.currentCoverSize;
		for(int i=0;i<g.vertexCovered.length;i++) {
			g.vertexCovered[i] = true;
		}
		double A = 0.9999999;
		double T = 500;
		
		while(T>=0.00001&&System.currentTimeMillis()<=(start+cutoffTime*1000)) {
			int index = r.nextInt(g.vertexCovered.length-1)+1;
			Vertex vertex= g.getVertex(index);
			if(g.vertexCovered[vertex.getId()]) {//vertex is used, let's decide whether to delete or not;
				boolean deleteable = true;
				//check all the adj vertex, if not covered, the node cannot be moved.
				for(Vertex adj:vertex.getAdjVertexList()) {
					if(g.vertexCovered[adj.myId]==false) {
						deleteable = false;
						break;
					}
				}
				if(deleteable) {
					g.vertexCovered[vertex.myId] = false;
					currentCoverSize--;
					if(currentCoverSize<bestCoverSize) {
						bestCoverSize = currentCoverSize;
						recorder.printTrace("%.3f,%d%n", (double)(System.currentTimeMillis()-start)/1000, currentCoverSize);
					}
				}
				//else doing nothing
			}else {//then we decide whether to add a vertex to current solution set.
				double p = Math.exp(-(1/T));
				if(p>r.nextDouble()) {
					currentCoverSize++;
					g.vertexCovered[index] = true;
				}//else doing nothing;
			}
			T *= A;
		}
		
		List<Vertex> optSolution = new ArrayList<Vertex>(currentCoverSize);
		int result = 0;
		for(int i=1;i<g.vertexCovered.length;i++) {
			if(g.vertexCovered[i]==true) {
				result++;
				optSolution.add(g.vertexList.get(i));
			}
		}
		
		recorder.printSolution(optSolution);
		//System.out.println("current Temp "+T);
		return result;
		
		
	}
	
}
