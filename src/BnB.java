
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


//Author: Yunwei Qiang, Bowen Yang
//initial implementation of BnB algorithm


class BnB{
	private int upperBound;
	private int lowerBound;
	public long start, end;
	static public ArrayList<Vertex> optimalSolution;
	private ArrayList<Vertex> currentSolution;
	// private int optimalSize;
	private Graph G;


	BnB(long interval, Graph g){
		Collections.sort(g.vertexList);
		start = System.currentTimeMillis();
		end = start + interval * 1000;
		upperBound = Integer.MAX_VALUE;
		lowerBound = 0;
		G = g;
		optimalSolution = new ArrayList<Vertex>();
		currentSolution = new ArrayList<Vertex>();
		// optimalSize = Integer.MAX_VALUE;
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
		lowerBound++;
		if(checkTime()){
			lowerBound--;
			return 0;
		}
		if(vertexCover()){
			// System.out.println("we find a current solution!");
			// for(int i=0; i<currentSolution.size(); i++){
			// 	System.out.println(currentSolution.get(i).myId);
			// }
			if(currentSolution.size() < upperBound){

				optimalSolution = new ArrayList<Vertex>(currentSolution.size());
				for(Vertex v:currentSolution){
					optimalSolution.add(new Vertex(v));
				}
				upperBound = optimalSolution.size();
				System.out.println("we find a solution! time: "+ (System.currentTimeMillis()-start));
				System.out.println(optimalSolution.size());
//				for(int i=0; i<optimalSolution.size(); i++){
//					System.out.println(optimalSolution.get(i).myId);
//				}
			}
			lowerBound--;
			return 0;
		}
		
		if(lowerBound>upperBound)
		{
			lowerBound--;
			return 0;
		}
//		System.out.println(twoApprox()+currentSolution.size());
		if(twoApprox()+currentSolution.size()>=upperBound) {
		//if((LBUtil.LPsolve(G)+currentSolution.size())>=upperBound) {
			//System.out.println("lower bound: " + (LBUtil.LPsolve(G)+currentSolution.size())+" larger than "+upperBound);
//			System.out.println(twoApprox()+currentSolution.size());
//			System.out.println("yeah!");
			lowerBound--;
			return 0;
		}
		// int index;
		Stack<Integer> vertexStackForThisLevel = new Stack<Integer>();
		Stack<Integer> edgeStackForThisLevel = new Stack<Integer>();
		int currentSolutionSizeForThisLevel = currentSolution.size();
		for(int i=index+1; i<G.vertexCovered.length-1; i++){//choose a vertex not pruned or selected
			if(!G.vertexCovered[G.getVertex(i).myId]){
				index = i;
				if(lowerBound==1)
				 	System.out.println("the first chosen node is "+index);
				//make a stack to store changed vertices and edges
				Stack<Integer> vertexStack = new Stack<Integer>();
				Stack<Integer> edgeStack = new Stack<Integer>();
//				if(twoApprox()+currentSolution.size()<=upperBound) {
				Vertex currentChoice = G.getVertex(index);//the vertex we choose to add in solution
				currentSolution.add(currentChoice);
				
				//choose currentChoice vertex
//				System.out.println(index+" "+twoApprox()+currentSolution.size());
				////////////////////////////////////////////////
//				if(twoApprox()+currentSolution.size()<=upperBound) {
				G.vertexCovered[G.getVertex(i).myId] = true;//first change
				vertexStack.push(G.getVertex(i).myId);

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
								if(tempBoolean) vertexStack.push(another.myId);
							}
						}
					}
				}
				DFS(index);
//				}
				//restore what we messed
				while(!vertexStack.empty()){
					G.vertexCovered[vertexStack.pop()] = false;
				}
				while(!edgeStack.empty()){
					Edge tempEdge = G.edgeMap.get(edgeStack.pop());
					tempEdge.covered = false;//change to key later
					G.coveredEdgeSize--;
				}
				//restore current solution
				currentSolution.remove(currentSolution.size()-1);
				
				//choose currentChoice's neighbors
				ArrayList<Vertex> neighborList = (ArrayList<Vertex>) currentChoice.getAdjVertexList();
				for(Vertex neighbor : neighborList) {
					if(!G.vertexCovered[neighbor.myId]) {
						G.vertexCovered[neighbor.myId] = true;
						vertexStackForThisLevel.add(neighbor.myId);
						currentSolution.add(neighbor);
						int[] neighborKeys = neighbor.getEdgeKeys();
						if(neighborKeys.length > 0) {
							for(int neighborKey: neighborKeys) {
								Edge neighborEdge = G.edgeMap.get(neighborKey);
								if(!neighborEdge.covered) {
									neighborEdge.covered = true;
									edgeStackForThisLevel.add(neighborEdge.id);
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
										if(tempBoolean) vertexStackForThisLevel.push(neighborAnother.myId);
									}
								}
							}
						}
					}
				}
			}
			
		}
		// System.out.println("current optimal solution!");
		// 		for(int i=0; i<optimalSolution.size(); i++){
		// 			System.out.println(optimalSolution.get(i).myId);
		// 		}
//		System.out.println("lowerBound " + lowerBound);
		
		while(!vertexStackForThisLevel.empty()){
			G.vertexCovered[vertexStackForThisLevel.pop()] = false;
		}
		while(!edgeStackForThisLevel.empty()){
			Edge tempEdge = G.edgeMap.get(edgeStackForThisLevel.pop());
			tempEdge.covered = false;//change to key later
			G.coveredEdgeSize--;
		}
		//restore current solution
		while(currentSolution.size()!=currentSolutionSizeForThisLevel) {
			currentSolution.remove(currentSolution.size()-1);
		}
		lowerBound--;
		return 1;

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