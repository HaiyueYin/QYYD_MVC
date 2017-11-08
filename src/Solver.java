import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Solver {
	
	public static void main(String[] args) {
		Graph g = parseFile(args[0]);
		System.out.println(g.getVertex(12).toString());
		//System.out.println(g.getVertex(5).toString());
		
	}

	public static Graph parseFile(String dataFile) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(dataFile));
		} catch (FileNotFoundException e) {
			System.err.println("fail to open the resource file: "+dataFile );
			e.printStackTrace();
		}
		
		int size = sc.nextInt();
		sc.nextLine();
		Graph g = new Graph(size);
		int currentIndex = 1;//start from the first vertex;
		while(sc.hasNextLine()) {
			Vertex currentVertex = g.getVertex(currentIndex);
			List<Vertex> currentAdjList = currentVertex.getAdjList();
			String line = sc.nextLine();
			if(!line.isEmpty()){//check if this line is empty
				String[] vertexs = line.trim().split("\\s+");
				for(String adjVertex:vertexs) {
					currentAdjList.add(g.getVertex(Integer.parseInt(adjVertex)));
				}
				currentIndex++;
			}
				if(currentIndex==size+1) break;
			}
			return g;
		}
	}
