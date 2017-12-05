
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.TreeSet;

//Author: Yunwei Qiang, Bowen Yang, Cong Du
//a reimplementation of BnB algorithm, using the same algorithm but change data structure to TreeMap.
class BnBClone{
	private int upperBound;
	private int currentLayer;
	public long start, end;
	static public ArrayList<Vertex> optimalSolution;
	private ArrayList<Vertex> currentSolution;
	// private int optimalSize;
	private Graph G;
	private TreeSet<Vertex> candidateVertexSet;
	SolutionRecorder recorder;




	BnBClone(long interval, Graph g)  {

        this.recorder = SolutionRecorder.getInstance();
		candidateVertexSet = new TreeSet<Vertex>(g.vertexList.subList(1, g.vertexList.size()));
		start = System.currentTimeMillis();
		end = start + interval * 1000;
		upperBound = Integer.MAX_VALUE;
		currentLayer = 0;
		G = g;
		optimalSolution = new ArrayList<Vertex>();
		currentSolution = new ArrayList<Vertex>();
		// optimalSize = Integer.MAX_VALUE;
		//System.out.println("size of set: "+candidateVertexSet.size());
	}
	
	
	public boolean checkTime(){
		return System.currentTimeMillis()>=end;
	}

	public boolean vertexCover(){
		if(G.coveredEdgeSize-G.edgeMap.size()>0)
		System.out.println(G.coveredEdgeSize-G.edgeMap.size());
		return G.coveredEdgeSize == G.edgeMap.size();
	}

	public int DFS(int index){
		// System.out.println("hello "+index);
//		System.out.println("current level "+lowerBound);
		currentLayer++;
		if(checkTime()){
			currentLayer--;
			return 0;
		}
		if(vertexCover()){
			// System.out.println("we find a current solution!");
			// for(int i=0; i<currentSolution.size(); i++){
			// 	System.out.println(currentSolution.get(i).myId);
			// }
			if(currentSolution.size() < upperBound){

				optimalSolution = new ArrayList<Vertex>(currentSolution);
				upperBound = optimalSolution.size();
				System.out.println("we find a solution! time: "+ (System.currentTimeMillis()-start));
				System.out.println(optimalSolution.size());
				recorder.printTrace("%.3f,%d%n", (double)(System.currentTimeMillis()-start)/1000, optimalSolution.size());  //print the solution for .trace file
//				for(int i=0; i<optimalSolution.size(); i++){
//					System.out.println(optimalSolution.get(i).myId);
//				}
			}
			currentLayer--;
			return 0;
		}
		
		if(currentLayer>upperBound)
		{
			currentLayer--;
			return 0;
		}
//		System.out.println(twoApprox()+currentSolution.size());
		if(twoApprox()+currentSolution.size()>=upperBound) {
		//if((LBUtil.LPsolve(G)+currentSolution.size())>=upperBound) {
			//System.out.println("lower bound: " + (LBUtil.LPsolve(G)+currentSolution.size())+" larger than "+upperBound);
//			System.out.println(twoApprox()+currentSolution.size());
//			System.out.println("yeah!");
			currentLayer--;
			return 0;
		}
		// int index;
		//Stack<Integer> vertexStackForThisLevel = new Stack<Integer>();
		//Stack<Integer> edgeStackForThisLevel = new Stack<Integer>();
		
		Vertex currentChoice = candidateVertexSet.first();//we don't need to determine whether it's used, because it should be unused.
		//System.out.println("vertex is " + currentChoice.myId );
		candidateVertexSet.remove(currentChoice);
		G.vertexCovered[currentChoice.myId]=true;
		currentSolution.add(currentChoice);
		Stack<Integer> vertexStack = new Stack<Integer>();
		Stack<Integer> edgeStack = new Stack<Integer>();
		//vertexStack.add(currentChoice.myId); we don't need to add currentChoice back again since it will never be a candidate again.
		//mark all edges as covered.
		int[] keys = currentChoice.getEdgeKeys();//get all edges' keys linked to this vertex 
		if(keys.length>0){//check whether this vertex has at least one edge
			for(int key:keys){
				Edge temp = G.edgeMap.get(key);//find correspond edge in HashMap by key

				if(!temp.covered){
					temp.covered = true;
					edgeStack.push(temp.id);
					G.coveredEdgeSize++;
				}
				//prune vertices
				Vertex another = temp.getAnother(currentChoice);//get adjacent vertex of this edge "temp"
				if(!G.vertexCovered[another.myId]){
					int[] keysAnother = another.getEdgeKeys();//get all the edges' keys of vertex "another"
					if(keysAnother.length>0){
						boolean tempBoolean = true;
						for(int keyAnother:keysAnother){
							Edge tempAnother = G.edgeMap.get(keyAnother);//find correspond edge in HashMap by keyAnother
							tempBoolean = tempBoolean && tempAnother.covered;
						}
						G.vertexCovered[another.myId] = tempBoolean;
						if(tempBoolean) {
							vertexStack.push(another.myId);
							//System.out.println(another.myId+ " is pruned");
							candidateVertexSet.remove(another);
						}
						
					}
				}
			}
		}
		
		DFS(0);//seems that the number here is useless now;
		//restore what we messed
		while(!vertexStack.empty()){
			int id = vertexStack.pop();
			G.vertexCovered[id] = false;
			candidateVertexSet.add(G.vertexList.get(id));
		}
		while(!edgeStack.empty()){
			Edge tempEdge = G.edgeMap.get(edgeStack.pop());
			tempEdge.covered = false;//change to key later
			G.coveredEdgeSize--;
		}
		//restore current solution
		currentSolution.remove(currentSolution.size()-1);
		G.vertexCovered[currentChoice.myId] = false;
		
		//if we don't choose currentChoice, then we add all it's neighbors into currentSolution;
		List<Vertex> neighborList =  currentChoice.getAdjVertexList();
		List<Vertex> neighborToRestore= new ArrayList<>();
		for(Vertex neighbor : neighborList) {
			if(!G.vertexCovered[neighbor.myId]) {
				G.vertexCovered[neighbor.myId] = true;
				candidateVertexSet.remove(neighbor);
				neighborToRestore.add(neighbor);
				currentSolution.add(neighbor);
				int[] neighborKeys = neighbor.getEdgeKeys();
				if(neighborKeys.length > 0) {
					for(int neighborKey: neighborKeys) {
						Edge neighborEdge = G.edgeMap.get(neighborKey);
						if(!neighborEdge.covered) {
							neighborEdge.covered = true;
							edgeStack.add(neighborEdge.id);
							G.coveredEdgeSize++;
						}
						Vertex neighborAnother = neighborEdge.getAnother(neighbor);
						if(!G.vertexCovered[neighborAnother.myId]) {
							int[] keysNeighborAnother = neighborAnother.getEdgeKeys();//get all the edges' keys of vertex "another"
							if(keysNeighborAnother.length>0){
								boolean tempBoolean = true;
								for(int keyNeighborAnother:keysNeighborAnother){
									Edge tempNeighborAnother = G.edgeMap.get(keyNeighborAnother);//find correspond edge in HashMap by keyAnother
									tempBoolean = tempBoolean && tempNeighborAnother.covered;
								}
								G.vertexCovered[neighborAnother.myId] = tempBoolean;
								if(tempBoolean) {
									candidateVertexSet.remove(neighborAnother);
									vertexStack.add(neighborAnother.myId);
								}
							}
						}
					}
				}
			}
		}
		DFS(0);
		//resotre everything that is messed by select all neighbors
		while(!vertexStack.empty()){
			int id = vertexStack.pop();
			G.vertexCovered[id] = false;
			candidateVertexSet.add(G.vertexList.get(id));
		}
		while(!edgeStack.empty()){
			Edge tempEdge = G.edgeMap.get(edgeStack.pop());
			tempEdge.covered = false;//change to key later
			G.coveredEdgeSize--;
		}
		//restore current solution
		for(Vertex neighbor:neighborToRestore) {
			currentSolution.remove(currentSolution.size()-1);
			G.vertexCovered[neighbor.myId] = false;
			candidateVertexSet.add(neighbor);//neighbor should be added back.
		}
		
		
		currentLayer--;
		return 0;
	}

	int twoApprox() {
//		System.out.println("approx");
		int choosedEdgeSize = 0;
		Iterator<Map.Entry<Integer, Edge>> it = G.edgeMap.entrySet().iterator();
		Stack<Integer> tempEdgeStack = new Stack<Integer>();
		int tempsize = G.coveredEdgeSize;
//		System.out.println(it.hasNext());
		while(it.hasNext()) {
//			System.out.println(it.hasNext());
			if(vertexCover()) break;
				
				Map.Entry<Integer,Edge> entry = (Map.Entry<Integer,Edge>)it.next();
				Edge temp = entry.getValue();
				if(temp.covered) continue;
					temp.covered = true;
					tempEdgeStack.add(temp.id);
					choosedEdgeSize++;
					G.coveredEdgeSize++;
					Vertex v1 = temp.getV1();
					int[] keys1 = v1.getEdgeKeys();
					if(keys1.length>0) {
						for(int key1:keys1) {
							Edge tempv1e = G.edgeMap.get(key1);//find correspond edge in HashMap by key
							if(!tempv1e.covered){
								tempv1e.covered = true;
								tempEdgeStack.push(tempv1e.id);
	//							choosedEdgeSize++;
								G.coveredEdgeSize++;
							}
						}
					}
					Vertex v2 = temp.getV2();
					int[] keys2 = v2.getEdgeKeys();
					if(keys2.length>0) {
						for(int key2:keys2) {
							Edge tempv2e = G.edgeMap.get(key2);//find correspond edge in HashMap by key
							if(!tempv2e.covered){
								tempv2e.covered = true;
								tempEdgeStack.push(tempv2e.id);
	//							choosedEdgeSize++;
								G.coveredEdgeSize++;
							}
						}
					}
				}
			
		
		G.coveredEdgeSize = tempsize;
		while(!tempEdgeStack.empty()){
			Edge tempEdge = G.edgeMap.get(tempEdgeStack.pop());
			tempEdge.covered = false;//change to key later
//			System.out.print("Here?");
		}
//		System.out.println(choosedEdgeSize+" "+"asdf");
		return choosedEdgeSize;
	}

}