import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Iterator;

public class Solver {
	
	public static void main(String[] args) {
		Graph g = parseFile(args[0]);
		System.out.println(g.getVertex(12).toString());
		System.out.println(g.EdgeList);
		System.out.println(g.EdgeList.size());
		System.out.println(g.edgeSize);
	}

	public static Graph parseFile(String dataFile) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(dataFile));
		} catch (FileNotFoundException e) {
			System.err.println("fail to open the resource file: "+dataFile );
			e.printStackTrace();
		}
		int vertexSize = sc.nextInt();
		int edgeSize = sc.nextInt();
		sc.nextLine();
		Graph g = new Graph(vertexSize, edgeSize);
		int currentVertexIndex = 1;//start from the first vertex;
		int currentEdgeIndex = 1;
		while(sc.hasNextLine()) {
			Vertex currentVertex = g.getVertex(currentVertexIndex);
			List<Vertex> currentAdjList = currentVertex.getAdjList();
			HashSet<Edge> currentEdge = g.EdgeList;
			String line = sc.nextLine();
			if(!line.isEmpty()){//check if this line is empty
				String[] vertexs = line.trim().split("\\s+");
				for(String adjVertex:vertexs) {
					int mark = Integer.parseInt(adjVertex);
					currentAdjList.add(g.getVertex(mark));
					if(currentVertexIndex<=mark)
						currentEdge.add(new Edge(g.getVertex(currentVertexIndex)
												,g.getVertex(mark)));
				}
				currentVertexIndex++;
				currentEdgeIndex++;
			}
				if(currentVertexIndex==vertexSize+1) break;
			}
			// Iterator<Edge> edgePrint = g.EdgeList.iterator();
			// System.out.println(Arrays.toString((Object)edgePrint.next()));
			return g;
		}
	}
