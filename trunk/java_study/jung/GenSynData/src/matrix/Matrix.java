package matrix;
import java.util.*;

/**
 * ������������ݽṹ�ķ�װ����ʵ��������Ҫ�Ĺ��� ����Ԫ��ʹ��double����������䣬��ʱ����Ҫ���ӵķ�װʵ�֣�
 * ������������ʹ�ã���ת��Ϊdouble��ֵ����1�7
 * 
 * @author frogcherry 周晓龄1�7
 * @created 2010-12-9
 * @Email frogcherry@gmail.com
 */
public class Matrix implements AbstractMatrix<Double> {
	private double[][] matrix;
	private int rowCount;
	private int columnCount;

	/**
	 * ʹ�ö�ά��������й�ģ��ʼ��һ������1�7
	 * 
	 * @param matrix
	 * @param rowCount
	 * @param columnCount
	 */
	public Matrix(double[][] matrix, int rowCount, int columnCount) {
		this.matrix = matrix;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}
	
	/**
	 * ʹ�����й�ģ��ʼ��һ���յľ���1�7
	 * 
	 * @param rowCount
	 * @param columnCount
	 */
	public Matrix(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.matrix = new double[rowCount][columnCount];
	}
	
	/**
	 * ����һ����¡�1�7
	 * @param tMatrix
	 */
	public Matrix(Matrix tMatrix) {
		this.rowCount = tMatrix.getRowCount();
		this.columnCount = tMatrix.getColumnCount();
		this.matrix = new double[rowCount][columnCount];
		
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				this.updateElement(i, j, tMatrix.element(i, j));
			}
		}
	}

	/**
	 * �������е�����Ԫ������ΪtMatirx�е�����Ԫ��
	 * @param tMatrix
	 */
	public void Reset(Matrix tMatrix)
	{
		for(int i = 0; i < rowCount; i++)
			for(int j = 0; j < columnCount; j++)
			{
				matrix[i][j]= tMatrix.element(i, j);
			}
	}
	/**
	 * ������������Ԫ������Ϊd
	 * @param d
	 */
	public void setAllElements(double d)
	{
		for(int i = 0; i < rowCount; i++)
			for(int j = 0; j < columnCount; j++)
			{
				matrix[i][j] = d;
			}
	}
	/**
	 * @see edu.zju.cs.ooobgy.algo.math.matrix.AbstractMatrix#getRowCount()
	 */
	//@Override
	public int getRowCount(){
		return rowCount;
	}

	/**
	 * @see edu.zju.cs.ooobgy.algo.math.matrix.AbstractMatrix#getColumnCount()
	 */
	//@Override
	public int getColumnCount() {
		return columnCount;
	}
	/**
	 * ȡ�õ�RowIndex�е�����Ԫ�أ�����������
	 * @param RowIndex
	 * @return
	 */
	public double[] getRow(int RowIndex)
	{
		checkRow(RowIndex);
		double[] row = new double[columnCount];
		
		for(int i = 0; i < columnCount; i++)
			row[i] = this.element(RowIndex, i);
		return row;
	}
	/**
	 * ����RowIndex������Ԫ������Ϊ����d
	 * @param RowIndex
	 * @param d
	 */
	public void setRow(int RowIndex, double[] d)
	{
		checkRow(RowIndex);
		for(int i = 0; i < columnCount; i++)
			matrix[RowIndex][i] = d[i];
	}
	/**
	 * ȡ�õ�ColumnIndex�е�����Ԫ�أ�����������
	 * @param ColumnIndex
	 * @return
	 */
	public double[] getColumn(int ColumnIndex)
	{
		checkColumn(ColumnIndex);
		double[] column = new double[rowCount];
		
		for(int i = 0; i < rowCount; i++)
			column[i] = this.element(i, ColumnIndex);
		return column;
	}
	/**
	 * ����ColumnIndex������Ԫ������Ϊ����d
	 * @param ColumnIndex
	 * @param d
	 */
	public void setColumn(int ColumnIndex, double[] d)
	{
		checkColumn(ColumnIndex);
		for(int i = 0; i < rowCount; i++)
			matrix[i][ColumnIndex] = d[i];
	}
	/**
	 * @see edu.zju.cs.ooobgy.algo.math.matrix.AbstractMatrix#element(int, int)
	 */
	//@Override
	public Double element(int rowIndex, int columnIndex) {
		checkColumn(columnIndex);
		checkRow(rowIndex);
		return matrix[rowIndex][columnIndex];
	}

	/**
	 * ���ĳ�еļӺͽ���1�7
	 * 
	 * @return
	 */
	public double sumRow(int rowIndex) {
		checkRow(rowIndex);

		double sum = 0;
		for (int i = 0; i < columnCount; i++) {
			sum += matrix[rowIndex][i];
		}

		return sum;
	}

	/**
	 * ����в�����Խ�����׳��쳣
	 * 
	 * @param rowIndex
	 */
	private void checkRow(int rowIndex) {
		if (rowIndex >= rowCount) {
			throw new IllegalArgumentException("matrix row index <" + rowIndex
					+ "> id out of bound [" + rowCount + "]!");
		}
	}

	/**
	 * �����������Խ�����׳��쳣
	 * 
	 * @param columnIndex
	 */
	private void checkColumn(int columnIndex) {
		if (columnIndex >= columnCount) {
			throw new IllegalArgumentException("matrix column index <"
					+ columnIndex + "> id out of bound [" + columnCount + "]!");
		}
	}

	/**
	 * ���ĳ�еļӺͽ���1�7
	 * 
	 * @return
	 */
	public double sumColumn(int columnIndex) {
		checkColumn(columnIndex);

		double sum = 0;
		for (int i = 0; i < rowCount; i++) {
			sum += matrix[i][columnIndex];
		}

		return sum;
	}

	/**
	 * �Ծ�������Ԫ�ؽ��мӺ̈́1�7
	 * 
	 * @return
	 */
	public double sumElements() {
		double sum = 0;
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				sum += matrix[i][j];
			}
		}

		return sum;
	}

	/**
	 * Ԫ�صݼ�һ�����݄1�7
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @param addition
	 * @return
	 */
	public double addElement(int rowIndex, int columnIndex, double addition) {
		checkColumn(columnIndex);
		checkRow(rowIndex);
		matrix[rowIndex][columnIndex] += addition;
		return matrix[rowIndex][columnIndex];
	}

	/**
	 * @see edu.zju.cs.ooobgy.algo.math.matrix.AbstractMatrix#updateElement(int, int, double)
	 */
	//@Override
	public Double updateElement(int rowIndex, int columnIndex, Double newElement) {
		checkColumn(columnIndex);
		checkRow(rowIndex);
		double oldElement = matrix[rowIndex][columnIndex];
		matrix[rowIndex][columnIndex] = newElement;
		return oldElement;
	}
	
	public Double setElement(int rowIndex, int columnIndex, Double newElement){
		return updateElement(rowIndex, columnIndex, newElement);
	}

	@Override
	public String toString() {
		StringBuilder matrixStr = new StringBuilder();
		for (int i = 0; i < rowCount; i++) {
			matrixStr.append("[");
			for (int j = 0; j < columnCount; j++) {
				matrixStr.append(matrix[i][j]);
				if (j < columnCount - 1) {
					matrixStr.append("\t");
				}
			}
			matrixStr.append("]");
			if (i < rowCount - 1) {
				matrixStr.append("\n");
			}
		}

		return matrixStr.toString();
	}

	/**
	 * @see edu.zju.cs.ooobgy.algo.math.matrix.AbstractMatrix#transpose()
	 */
	//@Override
	public Matrix transpose() {
		Matrix matrixT = new Matrix(columnCount, rowCount);

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				matrixT.updateElement(j, i, matrix[i][j]);
			}
		}

		return matrixT;
	}

	/**
	 * ��defaultElement������ʹ���Ϊ���� ���ش����ķ���
	 * ԭ���󲻱�1�7
	 * 
	 * @return
	 */
	public Matrix makeSquareMatrix(double defaultElement) {
		if (columnCount == rowCount) {//����������Ƿ���ֱ�ӷ��؄1�7
			return new Matrix(this);
		}
		
		boolean rowMore = rowCount > columnCount;	
		int squareCount = rowMore ? rowCount : columnCount;
		Matrix matrixS = new Matrix(squareCount, squareCount);
		//1.���ɾ���1�7
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				matrixS.updateElement(i, j, matrix[i][j]);
			}
		}
		
		//2.Ĭ��ֵ�������λ�Ä1�7
		for (int i = rowCount; i < squareCount; i++) {
			for (int j = columnCount; j < squareCount; j++){
				matrixS.updateElement(i, j, defaultElement);
			}
		}

		return matrixS;
	}
	
	/**
	 * �������ÿ��Ԫ�ض�ȡ���������¾���ԭ���󲻴���
	 * @return
	 */
	public Matrix negElements() {
		Matrix nMatrix = new Matrix(rowCount, columnCount);
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				nMatrix.updateElement(i, j, -matrix[i][j]);
			}
		}
		
		return nMatrix;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Matrix cloneMatrix = new Matrix(this);
		return cloneMatrix;
	}
	
	/**
	 * ת����str����
	 * @return
	 */
	public MatrixStr toMatrixStr() {
		return new MatrixStr(this);
	}
	
	/**
	 * ���ؾ����е����Ԫ�؄1�7
	 * @return
	 */
	public double maxElement(){
		double max = matrix[0][0] ;
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				if (matrix[i][j] > max) {
					max = matrix[i][j];
				}
			}
		}
		
		return max;
	}
	
	/**
	 * �������е�ÿһ��Ԫ�ض�һ��ֵcompleteȡ��
	 * ��m[i][j] = complete - m[i][j]
	 * @param complete
	 * @return
	 */
	public Matrix invMatrix(double complete){
		Matrix invMatrix = new Matrix(rowCount, columnCount);
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				invMatrix.updateElement(i, j, complete - matrix[i][j]);
			}
		}
		
		return invMatrix;
	}
	
	/**
	 * ������tMatrix��ĳԪ��'operator'�ڣ��߼��������limit���򽫾����ж�Ӧλ�õ�Ԫ������ΪsetValue
	 * @param tMatrix
	 * @param limit
	 * @param setVlaue
	 * @param operator
	 */
	public void UnEqualSet(Matrix tMatrix, double limit, double setValue, String operator)
	{
		for(int i = 0; i < tMatrix.getRowCount(); i++)
			for(int j = 0; j < tMatrix.getColumnCount();j++)
			{
				double element = tMatrix.element(i,j);
				if(operator.equals("<"))
				{	
					if( element < limit)				
						matrix[i][j]= setValue;
				}
				else if(operator.equals("<="))
				{	
					if( element <= limit)				
						matrix[i][j]= setValue;
				}
				else if(operator.equals(">"))
				{	
					if( element > limit)				
						matrix[i][j]= setValue;
				}
				else if(operator.equals(">="))
				{	
					if( element >= limit)				
						matrix[i][j]= setValue;
				}
			}
	}
	
	/**
	 * ����ӷ�
	 * @param tMatrix
	 * @return
	 */
	public Matrix MatrixAdd(Matrix tMatrix)
	{
		Matrix sumMatrix = new Matrix(rowCount, columnCount);
		
		for(int i = 0; i < rowCount; i++)
			for(int j = 0; j < columnCount; j++)
			{
				double sum = matrix[i][j]+ tMatrix.element(i, j);
				sumMatrix.setElement(i, j, sum);
			}
		return sumMatrix;
	}
	
	/**
	 * ����˷�
	 * @param tMatrix
	 * @return
	 */
	public Matrix MatrixMultiply(Matrix tMatrix)
	{
		if(columnCount != tMatrix.rowCount)
			throw new IllegalArgumentException("Cannot Multiply ! Please check !!!");
		Matrix rMatrix = new Matrix(rowCount, tMatrix.columnCount);
		
		for(int i = 0; i < rowCount; i++)
		{
			double[] Row = this.getRow(i);
			double sum = 0;
			for(int j = 0; j < tMatrix.columnCount; j++)
			{
				double[] tColumn = tMatrix.getColumn(j);
				for(int k = 0; k < Row.length; k++)
					sum += Row[k]*tColumn[k];
				rMatrix.setElement(i, j, sum);
			}
		}
		return rMatrix;
	}
	/**
	 * �Ե�colIndex�е�����Ԫ�ؽ����������
	 * @param colIndex
	 */
	public void ColumnRandperm(int colIndex)
	{
		checkColumn(colIndex);
		ArrayList<Double> tmpList = new ArrayList<Double>();
		for(int i = 0; i < rowCount; i++)
			tmpList.add(matrix[i][colIndex]);
		Collections.shuffle(tmpList);
		for(int i = 0; i < rowCount; i++)
			matrix[i][colIndex] = tmpList.get(i);
	}
}
