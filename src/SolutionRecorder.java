import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
	

// author: Cong Du
// a utility to save trace files and sol files. singleton class, at each run there is only one SolutionRecorder, and initialized at
// the very beginning using input parameters.
public class SolutionRecorder {
	private static final SolutionRecorder sr = new SolutionRecorder();
    private  String currDir = System.getProperty("user.dir");
    private  String hcOutFilePath;
    private  String hcTraceFilePath;
    private  PrintWriter hcOut;
    private  PrintWriter hcTrace;
	
	public void configure(String filePath,String alg,int randomSeed,int cutoffTime) throws IOException {
		String[] elements = filePath.split("\\.|/");
		this.hcOutFilePath = this.currDir + "/output/" + elements[elements.length-2] + "_"+alg+"_"+cutoffTime+"_"+
				randomSeed+".sol";
		this.hcTraceFilePath = this.currDir + "/output/" + elements[elements.length-2] + "_"+alg+"_"+cutoffTime+"_"+
				randomSeed+".trace";
		this.hcOut = new PrintWriter(this.hcOutFilePath);
        this.hcTrace = new PrintWriter(this.hcTraceFilePath);
	}
    
	public void printSolution(List<Vertex> optimalSolution) {
		hcOut.printf("%d%n", optimalSolution.size()); //write .sol file
        for(int i =0; i < optimalSolution.size(); i++){
            hcOut.printf("%s",optimalSolution.get(i).getId());
            if(i != optimalSolution.size()-1){
                hcOut.printf(",");
            }
        }
        hcOut.close();
        hcTrace.close();
	}
	
	public void printTrace(String format,Object... args) {
		hcTrace.printf(format,args);
	}
	
	
	public static SolutionRecorder getInstance() {
		return sr;
	}
	
	private SolutionRecorder() {}

}
