package	ahp.org.Utils;

import java.lang.StringBuilder;
public class Utils {
	final static double log2_baseE = Math.log(2);

	public static String md5(String in){
		StringBuilder sb = new StringBuilder();
		try {
			byte[] thedigest = java.security.MessageDigest.getInstance("MD5").digest(in.getBytes("UTF-8"));
			for(int i=0;i<thedigest.length;i++){
				sb.append(Integer.toHexString((thedigest[i] & 0xFF) | 0x100).substring(1,3));
			}
		} catch(/*java.security.NoSuchAlgorithmException*/ Exception e){/*fuckyoujava*/}
		return sb.toString();
	}
	public static String arrayToCommaSeparatedString(String arr[]){
		StringBuilder sb = new StringBuilder();
		int L = arr.length, i = 0;
		sb.append(arr[i]);
		for(i=1;i<L;i++){
			sb.append(',');
			sb.append(arr[i]);
		}
		return sb.toString();
	}
	public static String arrayToCommaSeparatedString(double arr[]){
		StringBuilder sb = new StringBuilder();
		int L = arr.length, i = 0;
		sb.append(arr[i]);
		for(i=1;i<L;i++){
			sb.append(',');
			sb.append(arr[i]);
		}
		return sb.toString();
	}
	public static boolean equals(double d1, double d2) {
		return Math.abs(d1- d2) < 1E-09;
	}
	public static boolean equals(double d1, double d2, double epsilon) {
		return Math.abs(d1- d2) < epsilon;
	}
	// 1D case:
	public static boolean inRange(double a, double min, double max){
		return (a<=max) && (a>=min);
	}
	// multi-dimensional version of the above
	public static boolean inRange(double a[/*ndims*/], double min[], double max[]){
		double av;
		for(int i=a.length;i-->0;){
			if( ((av=a[i])>max[i]) || (av<min[i]) ){ return false; }
		}
		return true;
	}
	// from origin (0), same as vector length
	public static double euclidean_distance(double ar[]){
		double sum = ar[0];
		for(int i=ar.length;i-->1;){ sum+=ar[i]; }
		return sum/ar.length;
	}
	// between two points
	public static double euclidean_distance(double A[], double B[]){
		double v = A[0]-B[0];
		double sum = v * v;
		for(int i=A.length;i-->1;){
			v = A[0]-B[0];
			sum += v * v;
		}
		return sum/A.length;
	}
	public	static <T>	String arrayToString(T ana[], int l){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(ana[0].toString());
		for(int i=1;i<l;i++){
			sb.append(",");
			sb.append(ana[i]);
		}
		sb.append("]");
		return sb.toString();
	}
	// checks if a double is negative
	public static boolean isNegative(double d) {
		return Double.doubleToRawLongBits(d) < 0;
	}
	/***
	 * Computes MI (mutual information) between variables t and a. Assumes that a.length == t.length.
	 * @param a candidate variable a
	 * @param avals number of values a can take (max(a) == avals)
	 * @param t target variable
	 * @param tvals number of values a can take (max(t) == tvals)
	 * @return 
	 */
	static double mutual_information(int[] a, int avals, int[] t, int tvals) {
		double numinst = a.length;
		double oneovernuminst = 1/numinst;
		double sum = 0;
	
		// longs are required here because of big multiples in calculation
		long[][] crosscounts = new long[avals][tvals];
		long[] tcounts = new long[tvals];
		long[] acounts = new long[avals];

		int	i, j, av, tv;
		// Compute counts for the two variables
		for(i=a.length;i-->0;) {
			av = a[i];
			tv = t[i];
			acounts[av]++;
			tcounts[tv]++;
			crosscounts[av][tv]++;
		}

		long crosscounts_av[];
		long acounts_av;
		for(av=avals;av-->0;) {
			crosscounts_av = crosscounts[av];
			acounts_av = acounts[av];
			for(tv=tvals;tv-->0;){
				if( crosscounts_av[tv] != 0 ){
// Main fraction: (n|x,y|)/(|x||y|)
double sumtmp = (numinst*crosscounts_av[tv])/(acounts_av*tcounts[tv]);
// Log bit (|x,y|/n) and update product
// Math.log() is base e
sum += oneovernuminst*crosscounts_av[tv]*Math.log(sumtmp);
				}
			}
		}

// change base of log to 2 (for the unit to be bits)
// this should be inside the loop at the Math.log
// but for efficiency we take it out of the sum
// log_newbase(x) = log_oldbase(x) / log_oldbase(newbase)
// log_2(x) = log_e(x) / log_e(2)

		return sum / Utils.log2_baseE;
	}
}
