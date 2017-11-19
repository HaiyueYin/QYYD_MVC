import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class HC_SA {
    private static String currDir = System.getProperty("user.dir");
    private static String hcOutFilePath;
    private static String hcTraceFilePath;
    private static PrintWriter hcOut;
    private static PrintWriter hcTrace;


    public static void HCSA(Graph datagraph, String dataname, int randseed, int cutoff) throws IOException{

        hcOutFilePath = currDir + "/output/" + dataname + "_LS2_" + Integer.toString(cutoff) + "_" + Integer.toString(randseed) + ".sol";
        hcTraceFilePath = currDir + "/output/" + dataname + "_LS2_" + Integer.toString(cutoff) + "_" + Integer.toString(randseed) + ".trace";

        hcOut = new PrintWriter(hcOutFilePath);
        hcTrace = new PrintWriter(hcTraceFilePath);

        long startTime = System.currentTimeMillis(); //Calculation of Overall time concumption
        long startTimeCutoff = System.currentTimeMillis();
        long end = startTime + cutoff * 1000;
        double stopmili = 0;

        ArrayList<Vertex> InitialVC = HC_MVC.solveHC(datagraph);
        Random generator = new Random(randseed);
        ArrayList<Vertex> removeVCList = removeVC(datagraph, InitialVC, end, startTimeCutoff, generator); //do hill climbing, remove vertices one by one

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

    public static int randomIntNum(int rangeMax, int rangeMin, Random generator){//放在外面 ， 然后再里面用
        long randomValue = Math.round(rangeMin + (rangeMax - rangeMin) * generator.nextDouble());
        return (int)randomValue;
    }

    public static double randomDoubleNum(Random generator){
        double randomValue = generator.nextDouble();
        return randomValue;
    }

    public static ArrayList<Vertex> removeVC(Graph graph, ArrayList<Vertex> vertices, Long end, Long startTimeCutoff, Random generator){  //remove 的时候去掉degree最小的 如果degree有相等的可以加随机性！！！！！

        for(Vertex v : graph.vertexList) {
            v.covered = false;
        }

        double A = 0.9995; //
        double T = 500;
        int numVC = vertices.size();
        int index;
        Boolean rmOrNot = true;
        ArrayList<Vertex> cpVertices;
        ArrayList<Vertex> VertexRemoved = new ArrayList<Vertex>();

        long startTime = System.currentTimeMillis();
        double stopmili = 0;

        while(T >= 0.00001 && System.currentTimeMillis() <= end){

            int i = randomIntNum(graph.vertexList.size()-1, 1, generator);//generate a random vertex index to process on

//            if(graph.vertexList.get(i).covered == true){
//                T = T * A;
//                continue;
//            }

            cpVertices = new ArrayList<Vertex>(vertices);

            if(vertices.indexOf(graph.vertexList.get(i)) != -1){
                index = vertices.indexOf(graph.vertexList.get(i));
                cpVertices.remove(index);
                rmOrNot = HC_MVC.isVC(graph, cpVertices, VertexRemoved);
                if(rmOrNot == true){
                    vertices.remove(index);

                    stopmili = (System.currentTimeMillis() - startTime) / 1000F;
                    hcTrace.printf("%.3f,%d%n", stopmili, vertices.size());  //print the solution for .trace file
                }
//                else{
//                    vertices.get(index).covered = true;
//                    T *= A;
//                    continue;
//                }
            }else{
                if(randomDoubleNum(generator) <= Math.exp(-1/T)){
                    vertices.add(graph.vertexList.get(i));

                    stopmili = (System.currentTimeMillis() - startTime) / 1000F;
                    hcTrace.printf("%.3f,%d%n", stopmili, vertices.size());  //print the solution for .trace file
                }
//                else{
//                    T *= A;
//                    continue;
//                }
            }
            T *= A;
        }

        return vertices;
    }

}