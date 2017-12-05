import java.util.Map;
import java.util.Iterator;

import scpsolver.problems.LPSolution;
import scpsolver.problems.LPWizard;
import scpsolver.problems.LPWizardConstraint;

// @author:Cong Du
// a utility to get VC lower bound of graph through linear programming
// a library SCPSolver is used

public class LBUtil {
	
	public static int LPsolve(Graph g) {
		LPWizard wizard = new LPWizard();
		for(int i=1;i<g.vertexCovered.length;i++) {
			if(!g.vertexCovered[i]) {//if the vertex is not covered, it should be part of the target function.
				wizard.plus("v"+Integer.toString(i),1.0);
			}
		}
		Iterator<Map.Entry<Integer,Edge>> it = g.edgeMap.entrySet().iterator();
		while(it.hasNext()) {
			Edge constraintEdge = it.next().getValue();
			if(constraintEdge.covered) {
				continue;
			}else {//not covered, then edge should add a constraint to the LP program.
				LPWizardConstraint constraint = wizard.addConstraint("ce"+constraintEdge.id, 1.0, "<=");
				constraint.plus("v"+constraintEdge.getV1().myId,1.0);
				constraint.plus("v"+constraintEdge.getV2().myId,1.0);
			}
		}
		for(int i=1;i<g.vertexCovered.length;i++) {
			if(!g.vertexCovered[i]) {//if the vertex is not covered, it should be part of the target function.
				wizard.addConstraint("cv"+Integer.toString(i),0.0,"<=").plus("v"+Integer.toString(i),1.0);
			}
		}
		wizard.setMinProblem(true);
		
		LPSolution sol = wizard.solve();
		int result = 0;
		for(int i=1;i<g.vertexCovered.length;i++) {
			if(!g.vertexCovered[i]&&sol.getDouble("v"+Integer.toString(i))>=0.5) {
				result++;
			}
		}
		System.out.println("lower bound by LP :"+result/2);
		return result/2;
	}
	
	

}
