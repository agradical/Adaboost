
public class Util {
	
	public static HypothesisResponse getBinaryHypothesis(int[] y, double[] p) {
		//check for first sign change
		double min_error = Double.MAX_VALUE;
		int index = 0;
		boolean leftNegative = true;
		HypothesisResponse r = new HypothesisResponse();
		
		for(int i=0; i<y.length; i++) {
			double error = 0;
			error = getBinaryTrainingError(y, p, i);
		
			double _min_error = Math.min(error, 1-error);
			if(min_error > _min_error) {
				if(error>0.5) {
					leftNegative = false;
				}
				min_error = _min_error;
				index = i;
			}
		}
		r.index = index;
		r.leftNegative = leftNegative;
		r.training_error = min_error;
		return r;
	}
	
	public static double getBinaryTrainingError(int[] y, double[] p, int index) {
		double error = 0;
		for(int i=0; i<y.length; i++) {
			if(i<=index && y[i] == 1) {
				error += p[i];
			} else if(i>index && y[i] == -1) {
				error += p[i];
			}
		}
		return error;
	}
	
	
	public static HypothesisResponse getRealHypothesis(int[] y, double[] p, double e) {
		//check for first sign change
		double min_error = Double.MAX_VALUE;
		int index = 0;
		boolean leftNegative = true;
		HypothesisResponse r = new HypothesisResponse();
		
		double c_p = 0;
		double c_n = 0;
		
		for(int i=0; i<y.length; i++) {
			TrainingErrorResponse error = getRealTrainingError(y, p, i);
			double _min_error = Math.min(error.g, 1-error.g);
			if(min_error > _min_error) {
				if(error.g>0.5) {
					leftNegative = false;
					c_p = 0.5*Math.log((error.pw_n + e)/(error.pr_p + e));
					c_n = 0.5*Math.log((error.pr_n + e)/(error.pw_p + e));
					
				} else {
					c_p = 0.5*Math.log((error.pr_p + e)/(error.pw_n + e));
					c_n = 0.5*Math.log((error.pw_p + e)/(error.pr_n + e));
				}
				min_error = _min_error;
				index = i;
			}
		}
		r.index = index;
		r.leftNegative = leftNegative;
		r.training_error = min_error;
		r.c_p = c_p;
		r.c_n = c_n;
		
		return r;
	}
	
	public static TrainingErrorResponse getRealTrainingError(int[] y, double[] p, int index) {
		double pr_p = 0;
		double pr_n = 0;
		double pw_p = 0;
		double pw_n = 0;
		
		for(int i=0; i<y.length; i++) {
			if(i<=index && y[i] == 1) {
				pw_p += p[i];
			} else if(i>index && y[i] == -1) {
				pw_n += p[i];
			} else if (i<=index && y[i] == -1) {
				pr_n = p[i];
			} else if (i>index && y[i] == 1) {
				pr_p = p[i];
			}
		}
		double g = Math.sqrt(pr_p*pw_n) + Math.sqrt(pw_p*pr_n);
		TrainingErrorResponse tr = new TrainingErrorResponse();
		tr.pr_n = pr_n;
		tr.pr_p = pr_p;
		tr.pw_n = pw_n;
		tr.pw_p = pw_p;
		tr.g = g;
		return tr;
	}
	
	
}
