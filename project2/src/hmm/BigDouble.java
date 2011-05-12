package hmm;

import java.math.BigDecimal;

/**
 * value=significand*2^exponent;
 * 
 * @author ruedi
 *
 */
public class BigDouble {
	private double significand;
	private long exponent2;
	
	public static BigDouble ONE=new BigDouble(1);
	public static BigDouble ZERO=new BigDouble(0);
	
	private static double log2(double d){
		return Math.log(d)/Math.log(2);
	}
	public static BigDouble valueOf(double d, long exponent10){
		long exp=(long)Math.floor(log2(d)+exponent10/Math.log10(2));
		double s=d*Math.pow(10, ((double)exponent10)-exp*Math.log10(2));
		return new BigDouble(s,exp);
	}
	
	private BigDouble(double significand, long exponent2){
		this.significand=significand;
		this.exponent2=exponent2;
		normalize();
	}
	
	/**
	 * normalize the representation
	 */
	private void normalize() {
		// handle zero
		if (significand==0){
			exponent2=0;
			return;
		}
		
		long exp=Math.getExponent(significand);
		if (exp!=0){
			exponent2+=exp;
			significand=setExponent(significand, 0);
		}		
	}

	private static double setExponent(double d,long exponent){
		if (exponent>Double.MAX_EXPONENT){
			if (d>0) 
				return Double.POSITIVE_INFINITY;
			else
				return Double.NEGATIVE_INFINITY;
		}
		
		if (d==0) return 0;
		
		if (exponent<Double.MIN_EXPONENT)
			return 0;
		
		long bits=Double.doubleToLongBits(d);
		bits=(bits&~0x7ff0000000000000L)|((1023+exponent)<<52);
		return Double.longBitsToDouble(bits);
	}
	
	public BigDouble(double d){
		significand=d;
		exponent2=0;
		normalize();
	}
	
	public BigDouble multiply(BigDouble d){
		return new BigDouble(significand*d.significand,exponent2+d.exponent2);
	}
	
	public BigDouble divide(BigDouble d){
		return new BigDouble(significand/d.significand,exponent2-d.exponent2);
	}
	
	public BigDouble add(BigDouble d){
		long exp=Math.max(exponent2, d.exponent2);
		return new BigDouble(
				setExponent(significand, exponent2-exp)
				+setExponent(d.significand, d.exponent2-exp),
				exp);
	}
	
	public BigDouble subtract(BigDouble d){
		long exp=Math.max(exponent2, d.exponent2);
		return new BigDouble(
				setExponent(significand, exponent2-exp)
				-setExponent(d.significand, d.exponent2-exp),
				exp);
	}
	
	public int compareTo(BigDouble d) {
		BigDouble diff=subtract(d);
		if (diff.significand==0) return 0;
		if (diff.significand<0) return -1;
		return 1;	
	}

	public double doubleValue() {
		if (exponent2>Double.MAX_EXPONENT) {
			if (significand>0)
				return Double.POSITIVE_INFINITY;
			else
				return Double.NEGATIVE_INFINITY;
		}
		
		if (exponent2<Double.MIN_EXPONENT) return 0;
		return setExponent(significand, exponent2);
	}
	
	public double doubleValueAlt(){
		double res=significand*Math.pow(2, exponent2);
		return res;
	}

	public static BigDouble valueOf(double d) {
		return new BigDouble(d);
	}

	public BigDouble abs() {
		return new BigDouble(Math.abs(significand), exponent2);
	}
	
	@Override
	public String toString() {
		if (significand==0) return "0";
		long exp=(long)Math.floor(Math.log10(significand)+exponent2/log2(10));
		double f=significand*Math.pow(2, (double)exponent2-log2(10)*exp);
		return Double.toString(f)+"e"+Long.toString(exp);
	}
}
