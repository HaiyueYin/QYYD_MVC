import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class HC_MVC {
    private static String currDir = System.getProperty("user.dir");
    private static String hcOutFilePath;
    private static String hcTraceFilePath;
    private static PrintWriter hcOut;
    private static PrintWriter hcTrace;

    public static void HCMVC(Graph datagraph, String dataname, int randseed, int cutoff) throws IOException{

//        int edgeId = Solver.getEdgeIndex(1,2);
//        System.out.println(datagraph.edgeMap.get(edgeId));

        hcOutFilePath = currDir + "/output/" + dataname + "_LS1_" + Integer.toString(cutoff) + "_" + Integer.toString(randseed) + ".sol";
        hcTraceFilePath = currDir + "/output/" + dataname + "_LS1_" + Integer.toString(cutoff) + "_" + Integer.toString(randseed) + ".trace";

        hcOut = new PrintWriter(hcOutFilePath);
        hcTrace = new PrintWriter(hcTraceFilePath);

        long startTime = System.currentTimeMillis(); //Calculation of Overall time concumption
        long startTimeCutoff = System.currentTimeMillis();
        long end = startTime + cutoff * 1000;
        double stopmili = 0;

        ArrayList<Vertex> InitialVC = solveHC(datagraph);
        Random generator = new Random(randseed);
        ArrayList<Vertex> sortedVCList = sortVertices(datagraph, InitialVC, generator); //sort vertices by degree(small first)
        ArrayList<Vertex> removeVCList = removeVC(datagraph, sortedVCList, end, startTimeCutoff); //do hill climbing, remove vertices one by one

        stopmili = (System.currentTimeMillis() - startTime)/1000F; //present the overall time consumption
        System.out.println("The overall time consumption for " + String.format("%30s",dataname) + " is " + String.format("%.5f", stopmili) + " and the number MVC is " + Integer.toString(removeVCList.size()));

        hcOut.printf("%d%n", removeVCList.size()); //write .sol file
        for(int i =0; i < removeVCList.size(); i++){
            hcOut.printf("%s",removeVCList.get(i).getId());
            if(i != removeVCList.size()-1){
                hcOut.printf(",");
            }
        }

        hcOut.close();
        hcTrace.close();
    }

    public static ArrayList<Vertex> solveHC(Graph graph) {
        //sort it first
        graph.vertexCovered = new boolean[graph.vertexList.size()];
        List<Vertex> sortedList = new ArrayList<>(graph.vertexList.subList(1, graph.vertexList.size()));
        Set<Integer> onLeft = new HashSet<>();
        Set<Vertex> VertexCoverSol = new HashSet<>();
        ArrayList<Vertex> result = new ArrayList<Vertex>();
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
                result.add(currentVertex);
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
        return result;
    }

    public static ArrayList<Vertex> sortVertices(Graph graph, ArrayList<Vertex> vc, Random generator){

        int position = 0;
        for (int i = 0; i < vc.size(); i++)
        {
            int j = i + 1;
            position = i;
            Vertex temp = vc.get(i);
            for (; j < vc.size(); j++) {
                if (vc.get(j).getDegree() < temp.getDegree()) {
                    temp = vc.get(j);
                    position = j;
                    continue;
                }else if(vc.get(j).getDegree() == temp.getDegree()){
                    if(sortVCEqualDegree(generator)) {
                        temp = vc.get(j);
                        position = j;
                    }
                }
            }
            vc.set(position, vc.get(i));
            vc.set(i, temp);
        }
//
//        Collections.sort(vc, new Comparator<Vertex>(){
//            public int compare(Vertex vertex1, Vertex vertex2) {
//                if(vertex1.getDegree() == vertex2.getDegree()){
//                    return sortVCEqualDegree(randomValue);
//                }else if((vertex1.getDegree() > vertex2.getDegree())){
//                    return 1;
//                }else return -1;
//                return (vertex1.getDegree() == vertex2.getDegree() ? 0 : (vertex1.getDegree() > vertex2.getDegree() ? 1 : -1));
//            }
//        });
//        System.out.println(randomValue);
//        for(int i =0; i<vc.size();i++){
//            System.out.println(vc.get(i));
//        }

        return vc;
    }

    public static double randomDoubleNum(Random generator){
        double randomValue = generator.nextDouble();
        return randomValue;
    }

    public static boolean sortVCEqualDegree(Random generator){
        if(randomDoubleNum(generator) <= 0.5){
            return true;
        }else{
            return false;
        }
    }

    public static ArrayList<Vertex> removeVC(Graph graph, ArrayList<Vertex> vertices, Long end, Long startTimeCutoff){  //remove 的时候去掉degree最小的 如果degree有相等的可以加随机性！！！！！
        int numVC = vertices.size();
        int finalNumVC = numVC;
        int numDeleted = 0;
        Boolean rmOrNot = true;
        ArrayList<Vertex> cpVertices;
        ArrayList<Vertex> VertexRemoved = new ArrayList<Vertex>();

        long startTime = System.currentTimeMillis();
        double stopmili = 0;

        for (int i = 0; i < numVC; i ++){
            if((System.currentTimeMillis()) <= end) {

                cpVertices = new ArrayList<Vertex>(vertices);
                if (rmOrNot == true) {
                    VertexRemoved.add(cpVertices.get(i - numDeleted));
                }
                cpVertices.remove(i - numDeleted);
                rmOrNot = isVC(graph, cpVertices, VertexRemoved);

                stopmili = (System.currentTimeMillis() - startTime) / 1000F;

                if (rmOrNot == true) {
                    vertices.remove(i - numDeleted);
                    finalNumVC--;
                    numDeleted++;

                    hcTrace.printf("%.3f,%d%n", stopmili, vertices.size());  //print the solution for .trace file

                } else {
                    continue;  //if not remove, calculate the next vertex
                }

            }else{
                return vertices;
            }

        }
        return vertices;
    }

    public static boolean isVC(Graph graph, ArrayList<Vertex> vertices, ArrayList<Vertex> VertexRemoved){

        for(int i = 1; i < graph.vertexList.size(); i++){
            for(Edge e : graph.vertexList.get(i).getAdjEdgeList()) {
                e.covered = false;
            }
        }

        for(int i = 1; i < graph.vertexList.size(); i++){
            for(Edge e : graph.vertexList.get(i).getAdjEdgeList()) {
                if(!e.covered){
                    if(vertices.indexOf(e.getV1()) != -1 || vertices.indexOf(e.getV2()) != -1){ //edge两顶点至少有一个在vertex cover里
                        e.covered = true;
                        continue;
                    }else if(VertexRemoved.indexOf(e.getV1()) != -1 || VertexRemoved.indexOf(e.getV2()) != -1) { //edge两顶点里有被删除的
                        if(VertexRemoved.indexOf(e.getV1()) != -1 && VertexRemoved.indexOf(e.getV2()) != -1){ //edge两顶点都被删除
                            return false;
                        }else if(vertices.indexOf(e.getV1()) != -1 || vertices.indexOf(e.getV2()) != -1){ //两顶点里只有一个被删除，没被删除的顶点在vc里
                            e.covered = true;
                            continue;
                        }else{  //两顶点只有一个被删除，没被删除的顶点没在vc里
                            return false;
                        }
                    } else{ //两顶点没有都在vc里
                        return false;
                    }
                }else { //是之前考虑过的边
                    continue;
                }
            }
        }
        return true;
    }

}