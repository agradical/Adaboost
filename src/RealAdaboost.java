public class RealAdaboost {
	double e;

	public RealAdaboost(double e) {
		this.e = e;
	}
	
	public IterationResponse iterate(double[] x, int[] y, double[] p, Double z_o) {
		HypothesisResponse v = Util.getRealHypothesis(y, p, e);
		
		//lets take x < v as -ve, will classify corrrectly based on error
		boolean leftNegative = v.leftNegative;
		int n = y.length;
		
		System.out.println("\tWeak classifier: h = "+x[v.index]);
		System.out.println("\tError weak classifier: e = "+v.training_error);
		System.out.println("\tWeights: ct+, ct- = "+v.c_p+", "+v.c_n);
		
		double z = 0;
		double[] new_p = new double[n];
		
		for(int i=0; i<n; i++) {
			if(leftNegative) {
				if(i<=v.index) {
					new_p[i] = p[i]*Math.exp(-1*y[i]*v.c_n);
				} else {
					new_p[i] = p[i]*Math.exp(-1*y[i]*v.c_p);
				}
			} else {
				if(i<=v.index) {
					new_p[i] = p[i]*Math.exp(-1*y[i]*v.c_p);
				} else {
					new_p[i] = p[i]*Math.exp(-1*y[i]*v.c_n);
				}
			}
			z += new_p[i];
		}
		
		System.out.println("\tNormalization factor: z = "+z);
		String new_prob = "";	
		for(int i=0; i<n; i++) {
			new_prob += new_p[i]+" ";
		}
		System.out.println("\tProbabilities after normalization: p = "+new_prob);
		HypothesisResponse tempv = Util.getRealHypothesis(y, new_p, e);
		System.out.println("\tBoosted classifier: f = "+x[tempv.index]);
		System.out.println("\tError boosted classifier: E = "+ tempv.training_error);
		System.out.println("\tBound: b = "+(z*z_o));
		z_o = z*z_o;
		
		return new IterationResponse(new_p, z_o);
		
	}
}