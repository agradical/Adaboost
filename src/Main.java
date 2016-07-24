import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String args[]) throws Exception {
		
		if(args.length<1) throw new Exception("insufficent args");
		
		File infile = new File(args[0]);
		Scanner s = new Scanner(infile);
		String header_str = s.nextLine();
		String x_arr_str = s.nextLine();
		String y_arr_str = s.nextLine();
		String p_arr_str = s.nextLine();
		
		String[] header = header_str.split(" ");
		int T = Integer.parseInt(header[0]);
		double e = Double.parseDouble(header[2]);

		String x_str[] = x_arr_str.split(" ");
		double[] x = new double[x_str.length];
		for(int i=0; i<x_str.length; i++) {
			x[i] = Double.parseDouble(x_str[i]);
		}
		
		String y_str[] = y_arr_str.split(" ");
		int[] y = new int[y_str.length];
		for(int i=0; i<y_str.length; i++) {
			y[i] = Integer.parseInt(y_str[i]);
		}
		
		String p_str[] = p_arr_str.split(" ");
		double[] p = new double[p_str.length];
		for(int i=0; i<p_str.length; i++) {
			p[i] = Double.parseDouble(p_str[i]);
		}
		
		boolean real = false;
		if(args.length>=2){
			if(args[1].equals("r")) {
				real = true;
			}
		}
		
		if(!real) {
			BinaryAdaboost ba = new BinaryAdaboost();
			double[] temp_p = Arrays.copyOf(p, p.length);
			Double z = 1d;
			for(int i=0; i<T; i++) {
				System.out.println("Iteration: "+i);
				IterationResponse temp = ba.iterate(x, y, temp_p, z);
				temp_p = temp.prob;
				z = temp.z;
			}
		} else {
			RealAdaboost ra = new RealAdaboost(e);
			double[] temp_p = Arrays.copyOf(p, p.length);
			Double z = 1d;
			for(int i=0; i<T; i++) {
				System.out.println("Iteration: "+i);
				IterationResponse temp = ra.iterate(x, y, temp_p, z);
				temp_p = temp.prob;
				z = temp.z;
			}
		}
		
		s.close();
	}
}
