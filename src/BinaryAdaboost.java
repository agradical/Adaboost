public class BinaryAdaboost {

	public IterationResponse iterate(double[] x, int[] y, double[] p, Double z_o) {
		HypothesisResponse v = Util.getBinaryHypothesis(y, p);
		
		//lets take x < v as -ve, will classify corrrectly based on error
		boolean leftNegative = v.leftNegative;
		int n = y.length;
		
		System.out.println("\tWeak classifier: h = "+x[v.index]);
		System.out.println("\tError weak classifier: e = "+v.training_error);

		double alpha = 0.5*(Math.log((1-v.training_error)/(v.training_error)));
		
		System.out.println("\tWeight weak classifier: a = "+alpha);

		double q_wrong = Math.exp(alpha);
		double q_right = Math.exp(-1*alpha);
		
		double z = 0;
		double new_p[] = new double[n];
		
		for(int i=0; i<n; i++) {
			if(leftNegative) {
				if((i <= v.index && y[i] == -1)||(i > v.index && y[i] == 1)) {
					new_p[i] = p[i]*q_right;
					z += new_p[i];
				} else if((i <= v.index && y[i] == 1) || (i > v.index && y[i] == -1)) {
					new_p[i] += p[i]*q_wrong;
					z += new_p[i];
				}
			} else {
				if((i <= v.index && y[i] == 1)||(i > v.index && y[i] == -1)) {
					new_p[i] = p[i]*q_right;
					z += new_p[i];
				} else if((i <= v.index && y[i] == -1) || (i > v.index && y[i] == 1)) {
					new_p[i] += p[i]*q_wrong;
					z += new_p[i];
				}
			}
		}
		
		for(int i=0; i<n ; i++) {
			new_p[i] = new_p[i]/z;
		}
		
		System.out.println("\tNormalization factor: z = "+z);
		String new_prob = "";	
		for(int i=0; i<n; i++) {
			new_prob += new_p[i]+" ";
		}
		System.out.println("\tProbabilities after normalization: p = "+new_prob);
		HypothesisResponse tempv = Util.getBinaryHypothesis(y, new_p);
		System.out.println("\tBoosted classifier: f = "+x[tempv.index]);
		System.out.println("\tError boosted classifier: E = "+ tempv.training_error);
		System.out.println("\tBound: b = "+(z*z_o));
		z_o = z*z_o;
		
		return new IterationResponse(new_p, z_o);
	}
}
