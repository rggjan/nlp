package test;

import static junit.framework.Assert.*;
import hmm.BigDouble;

import org.junit.Test;

public class BigDoubleTest {

	@Test
	public void testAdd(){
		BigDouble a=BigDouble.valueOf(1);
		BigDouble b=BigDouble.valueOf(2);
		assertEquals(1.0+2.0, a.add(b).doubleValue());
		
		a=BigDouble.valueOf(1e10);
		b=BigDouble.valueOf(2);
		assertEquals(1.0e10+2.0, a.add(b).doubleValue());
		
		a=BigDouble.valueOf(1e100);
		b=BigDouble.valueOf(2);
		assertEquals(1.0e100+2.0, a.add(b).doubleValue());
		
		a=BigDouble.valueOf(0);
		b=BigDouble.valueOf(2);
		assertEquals(0+2.0, a.add(b).doubleValue());
	}
	
	@Test
	public void testSubtract(){
		BigDouble a=BigDouble.valueOf(1);
		BigDouble b=BigDouble.valueOf(2);
		assertEquals(1.0-2.0, a.subtract(b).doubleValue());
		
		a=BigDouble.valueOf(1e10);
		b=BigDouble.valueOf(2);
		assertEquals(1.0e10-2.0, a.subtract(b).doubleValue());
		
		a=BigDouble.valueOf(1e100);
		b=BigDouble.valueOf(2);
		assertEquals(1.0e100-2.0, a.subtract(b).doubleValue());
		
		a=BigDouble.valueOf(0);
		b=BigDouble.valueOf(2);
		assertEquals(0-2.0, a.subtract(b).doubleValue());
	}
	
	@Test
	public void testMultiply(){
		BigDouble a=BigDouble.valueOf(1);
		BigDouble b=BigDouble.valueOf(2);
		assertEquals(1.0*2.0, a.multiply(b).doubleValue());
		
		a=BigDouble.valueOf(1e10);
		b=BigDouble.valueOf(2);
		assertEquals(1.0e10*2.0, a.multiply(b).doubleValue());
		
		a=BigDouble.valueOf(1e100);
		b=BigDouble.valueOf(2);
		assertEquals(1.0e100*2.0, a.multiply(b).doubleValue());
		
		a=BigDouble.valueOf(0);
		b=BigDouble.valueOf(2);
		assertEquals(0*2.0, a.multiply(b).doubleValue());
	}
	
	@Test
	public void testDivide(){
		BigDouble a=BigDouble.valueOf(1);
		BigDouble b=BigDouble.valueOf(2);
		assertEquals(1.0/2.0, a.divide(b).doubleValue());
		
		a=BigDouble.valueOf(1e10);
		b=BigDouble.valueOf(2);
		assertEquals(1.0e10/2.0, a.divide(b).doubleValue());
		
		a=BigDouble.valueOf(1e100);
		b=BigDouble.valueOf(2);
		assertEquals(1.0e100/2.0, a.divide(b).doubleValue());
		
		a=BigDouble.valueOf(0);
		b=BigDouble.valueOf(2);
		assertEquals(0/2.0, a.divide(b).doubleValue());
	}
	
}
