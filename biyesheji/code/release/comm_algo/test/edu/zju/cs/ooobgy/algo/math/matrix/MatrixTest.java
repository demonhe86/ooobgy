package edu.zju.cs.ooobgy.algo.math.matrix;

import org.junit.Test;

import edu.zju.cs.ooobgy.algo.math.matrix.Matrix;

import junit.framework.TestCase;

/**
 * 
 * @author frogcherry 周晓龙
 * @created 2010-12-12
 * @Email frogcherry@gmail.com
 */
public class MatrixTest extends TestCase{
	private Matrix matrix;
	
	@Override
	protected void setUp() throws Exception {
		double[][] old = {{1,2,3,4},{11,22,33,44},{111,222,333,444}};
		matrix = new Matrix(old, 3, 4);
		super.setUp();
	}

	@Test
	public void testMatrix(){
		Matrix matrix = new Matrix(3, 3);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(matrix.element(i, j) + "\t");
			}
			System.out.println();
		}
		
		System.out.println("-------------------------");
		matrix.addElement(1, 1, 0.3);
		System.out.println(matrix);
	}
	
	@Test
	public void testTranspose(){
		System.out.println("matrix:");
		System.out.println(matrix);
		System.out.println("Transposed matrix:");
		System.out.println(matrix.transpose());
	}
	
	@Test
	public void testMakeSquare(){
		System.out.println("matrix:");
		System.out.println(matrix);
		System.out.println("Squared matrix:");
		System.out.println(matrix.makeSquareMatrix(0));
		System.out.println("transposed Squared matrix:");
		System.out.println(matrix.transpose().makeSquareMatrix(-1));
	}
	@Test
	public void testNeg(){
		System.out.println("matrix:");
		System.out.println(matrix);
		System.out.println("neg matrix:");
		System.out.println(matrix.negElements());
	}
}
