import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HC_MVC {
    private static String currDir = System.getProperty("user.dir");
    private static String hcOutFilePath;
    private static String hcTraceFilePath;
    private static PrintWriter hcOut;
    private static PrintWriter hcTrace;
    private static int randseed = 0;
    private static int cutoff = 600;

    public static void HC(Graph datagraph, String dataname) throws IOException{

//        int edgeId = Solver.getEdgeIndex(1,2);
//        System.out.println(datagraph.edgeMap.get(edgeId));

        hcOutFilePath = currDir + "/output/" + dataname + "_LS1_3600_0.sol";
        hcTraceFilePath = currDir + "/output/" + dataname + "_LS1_3600_0.trace";

        hcOut = new PrintWriter(hcOutFilePath);
        hcTrace = new PrintWriter(hcTraceFilePath);

        long startTime = System.currentTimeMillis(); //Calculation of Overall time concumption
        long startTimeCutoff = System.currentTimeMillis();
        long end = startTime + cutoff * 1000;
        double stopmili = 0;

        ArrayList<Vertex> sortedVCList = sortVertices(datagraph); //sort vertices by degree(small first)
        ArrayList<Vertex> removeVCList = removeVC(datagraph, sortedVCList, end, startTimeCutoff); //do hill climbing, remove vertices one by one

        stopmili = (System.currentTimeMillis() - startTime)/1000F; //present the overall time consumption
        System.out.println("The overall time consumption is " + String.format("%.5f", stopmili) + " and the number MVC is " + Integer.toString(removeVCList.size()));

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

    public static ArrayList<Vertex> sortVertices(Graph graph){

        ArrayList<Vertex> vc = new ArrayList<Vertex>();
        for(int k = 0;k < graph.vertexList.size() - 1;k++){// get the initial vertex cover
            vc.add(graph.vertexList.get(k+1));
        }
        Collections.sort(vc, new Comparator<Vertex>(){
            public int compare(Vertex vertex1, Vertex vertex2) {
                return (vertex1.getDegree() == vertex2.getDegree() ? 0 : (vertex1.getDegree() > vertex2.getDegree() ? 1 : -1));
            }
        });

        return vc;
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