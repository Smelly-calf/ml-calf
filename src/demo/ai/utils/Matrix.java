/*******************************************************************************
 * Linear Regression Demo Code
 * Author: Du Ke  (xadke@cn.ibm.com)
 * Date: 2018-8-10
 * The code just for study.
 *******************************************************************************/
package demo.ai.utils;

import java.util.Arrays;
import java.util.HashMap;

public class Matrix {

	public Matrix() {
	}
	
	// 拷贝矩阵
	public static float[][] copy(float[][] matrix, float[][] result) throws Exception{
         int rows = rows(matrix);
         int cols = cols(matrix);
         if(result==null || rows!=rows(result) || cols!=cols(result)){
        	 result = new float[rows][cols];
         }
         float[] row;
         for(int i=0;i<rows;i++){
        	 row = matrix[i];
        	 System.arraycopy(row, 0, result[i], 0, cols);
         }
         return result;
	}
	
	// 拷贝矩阵
	public static float[][] copy(float[][] matrix) throws Exception{
        int rows = rows(matrix);
        int cols = cols(matrix);
		float[][] result = new float[rows][cols];
		return copy(matrix, result);
	}
	
	// 取得矩阵的行数
	public static int rows(float[][] matrix){
		return matrix.length;
	}
	
	// 取得矩阵的列数
	public static int cols(float[][] matrix) throws Exception{
		if(matrix.length<1){
			throw new Exception("To get the cols of matrix, the matrix must have one column at least.");
		}
		return matrix[0].length;
	}
	
	// 取得矩阵的某行
	public static float[] getRow(float[][] matrix, int row) throws Exception{
		int rows = rows(matrix);
		if(row<0 || row>=rows){
			throw new Exception("To row number " + row + " is not in the range of the matrix.");
		}
		return matrix[row];
	}
	
	// 取得矩阵的某列
	public static float[] getCol(float[][] matrix, int col) throws Exception{
		int cols = cols(matrix);
		if(col<0 || col>=cols){
			throw new Exception("To col number " + col + " is not in the range of the matrix.");
		}
		final int rows = rows(matrix);
		float[] result = new float[rows];
		for(int i=0;i<rows;i++){
			result[i] = matrix[i][col];
		}
		return result;
	}
	
	// 矩阵乘积: 左矩阵 * 右矩阵
	public static float[][] times(float[][] left, float[][] right) throws Exception{
		float[][] newMatrix = new float[rows(left)][cols(right)];
		return times(left, right, newMatrix);
	}
	
	// 矩阵乘积: 左矩阵 * 右矩阵 ：结果存入传入的矩阵对象
	public static float[][] times(float[][] left, float[][] right, float[][] result) throws Exception{
		final int leftRows = rows(left);
		final int leftCols = cols(left);
		final int rightRows = rows(right);
		final int rightCols = cols(right);
		
		if(leftCols!=rightRows){
			throw new Exception("The matrix columns on the left must be equal to the rows of the right matrix");
		}
		
		for(int i=0;i<leftRows;i++){
			float[] row = getRow(left,i);
			for(int j=0;j<rightCols;j++){
				float[] col = getCol(right,j);
				result[i][j] = dotProduct(row, col);
			}
		}
		return result;
	}
	
	// 常数a与矩阵乘积: a * 矩阵
	public static float[][] times(float a, float[][] matrix) throws Exception{
		float[][] newMatrix = new float[rows(matrix)][cols(matrix)];
		return times(a, matrix, newMatrix);
	}
	
	// 常数a与矩阵乘积: a * 矩阵 ：结果存入传入的矩阵对象
	public static float[][] times(float a, float[][] matrix, float[][] result) throws Exception{
		for(int i=0;i<rows(matrix);i++){
			for(int j=0;j<cols(matrix);j++){
				result[i][j] = a * matrix[i][j];
			}
		}
		return result;
	}
	
	// 矩阵相加: 左矩阵 + 乘数 * 右矩阵
	public static float[][] add(float[][] left, float multiplier, float[][] right) throws Exception{
		float[][] newMatrix = new float[rows(left)][cols(left)];
		return add(left,multiplier,right,newMatrix);
	}
	
	// 矩阵相加: 左矩阵 + 乘数 * 右矩阵 ：结果存入传入的矩阵对象
	public static float[][] add(float[][] left, float multiplier, float[][] right, float[][] result) throws Exception{
		final int leftRows = rows(left);
		final int leftCols = cols(left);
		final int rightRows = rows(right);
		final int rightCols = cols(right);
		final int resultRows = rows(result);
		final int resultCols = cols(result);
		
		if(leftRows!=rightRows || leftCols!=rightCols){
			throw new Exception("To add matrix, the matrix rows and columns on the left must be equal to the rows and columns of the right matrix");
		}
		
		if(leftRows!=resultRows || leftCols!=resultCols){
			throw new Exception("The result matrix rows and columns must be equal to the rows and columns of left and right matrix");
		}
		
		for(int i=0;i<leftRows;i++){
			for(int j=0;j<leftCols;j++){
				result[i][j] = left[i][j] + (multiplier * right[i][j]);
			}
		}
		return result;
	}
	
	// 转置矩阵
	public static float[][] transpose(float[][] matrix) throws Exception{
		float[][] newMatrix = new float[cols(matrix)][rows(matrix)];
		return transpose(matrix, newMatrix);
	}
	
	// 转置矩阵 ：结果存入传入的矩阵对象
	public static float[][] transpose(float[][] matrix, float[][] result) throws Exception{
		for(int i=0;i<rows(matrix);i++){
			for(int j=0;j<cols(matrix);j++){
				result[j][i] = matrix[i][j];
			}
		}
		return result;
	}
	
	// 将一个行向量变为矩阵
	public static float[][] rowVectorToMatrix(float[] row) throws Exception{
		float[][] newMatrix = new float[1][row.length];
		return rowVectorToMatrix(row,newMatrix);
	}
	
	// 将一个行向量变为矩阵 ：结果存入传入的矩阵对象
	public static float[][] rowVectorToMatrix(float[] row, float[][] result) throws Exception{
		if(row.length!=cols(result)){
			throw new Exception("The row vector legth must be equal to the columns of the result matrix");
		}
		
		for(int i=0;i<row.length;i++){
			result[0][i] = row[i];
		}
		return result;
	}
	
	// 将一个列向量变为矩阵
	public static float[][] colVectorToMatrix(float[] col) throws Exception{
		float[][] newMatrix = new float[col.length][1];
		return colVectorToMatrix(col,newMatrix);
	}
	
	// 将一个列向量变为矩阵 ：结果存入传入的矩阵对象
	public static float[][] colVectorToMatrix(float[] col, float[][] result) throws Exception{
		if(col.length!=rows(result)){
			throw new Exception("The col vector legth must be equal to the cols of the result matrix");
		}
		
		for(int i=0;i<col.length;i++){
			result[i][0] = col[i];
		}
		return result;
	}
	
	// 将一个矩阵的第一行转为向量
	public static float[] matrixToRowVector(float[][] matrix) throws Exception{
		return getRow(matrix, 0);
	}
	
	// 将一个矩阵的第一列转为向量
	public static float[] matrixToColVector(float[][] matrix) throws Exception{
		return getCol(matrix, 0);
	}
	
	// Dot Product 点积（数量积）
	public static float dotProduct(float[] left, float[] right){
		int len = (left.length > right.length) ? right.length : left.length;
		float result = 0;
		for(int i=0;i<len;i++){
			result += left[i]* right[i];
		}
		return result;
	}
	
	// 矩阵转为字符串输出
	public static StringBuffer matrixToString(float[][] matrix){
		return matrixToString(matrix,-1);
	}
	
	// 矩阵转为字符串输出
	public static StringBuffer matrixToString(float[][] matrix, int dot){
		StringBuffer str = new StringBuffer("");
		for(int i=0;i<matrix.length;i++){
			float[] row = matrix[i];
			if(dot>=0){
				for(int j=0;j<row.length;j++) str.append(String.format("%." + dot + "f", row[j]));
			}else{
				str.append(Arrays.toString(row));
			}
			if(i<matrix.length-1)str.append("\n");
		}
		return str;
	}
	
	// 向量转为字符串输出
	public static StringBuffer matrixToString(float[] vector){
		return matrixToString(vector,-1);
	}
	
	// 向量转为字符串输出
	public static StringBuffer matrixToString(float[] vector, int dot){
		StringBuffer str = new StringBuffer("");
		if(dot>=0){
			for(int j=0;j<vector.length;j++){
				str.append(String.format("%." + dot + "f", vector[j]));
			}
			str.append("\n");
		}else{
			str.append(Arrays.toString(vector));
		}
		return str;
	}
	
	// 交换行
	public static float[][] swapRow(float[][] matrix, int i, int j) throws Exception{
		float temp;
		int cols = Matrix.cols(matrix);
		for(int col=0;col<cols;col++){
			temp = matrix[i][col];
			matrix[i][col] = matrix[j][col];
			matrix[j][col] = temp;
		}
		return matrix;
	}
	
	// 计算行列式的值 (高斯消元法求解)
	public static float detValue2(float[][] det) throws Exception{
		int n = det.length;
		if(n==0 || n!=det[0].length){
			throw new Exception("The determinant's rows must be equal to it's columns.");
		}
		
		float key;
		float value = 0 ;
		float minus = 1;	// 交换一次行，则变换一次负号, 初始为正号
		int i;	//行
		int j;	//列
		
		// 复制det
		float[][] workDet = new float[n][n];
		for( i=0;i<n;i++){
			for( j=0;j<n;j++){
				workDet[i][j]=det[i][j];
			}
		}
		
		//消元
		for(j=0;j<n;j++){
			// 如果key为0，则与下面某行交换，保证key不能为0
			if(workDet[j][j]==0){
				for(int idx=j+1; idx<n;idx++){
					if(workDet[idx][j]!=0){
						Matrix.swapRow(workDet, j, idx); // 交换2行
						minus = minus * (-1f); 		 // 变号
						break;
					}
				}
			}
			key = workDet[j][j];
			for(i=j+1;i<n;i++){
				float target = workDet[i][j];
				if(target==0) continue;  // 若值已经为0则不需要再消了
				float times = target / key;
				// i行加上第j行的负times倍，即: r(i) - times * r(j)
				for(int idx=0; idx<n;idx++){
					workDet[i][idx] = workDet[i][idx] - times * workDet[j][idx];
				}
			}
		}
		
		// 对角线乘积求值：
		value = minus ;
		for(i=0;i<n;i++) value = value * workDet[i][i];
		return value;
	}
	
	// 提取余子式 cofactor
	public static float[][] cofactor(float[][] matrix, int row, int col, float[][] result) throws Exception{
		int n = matrix.length;
		if(n==0 || n!=matrix[0].length){
			throw new Exception("To get cofactor for a matrix, the matrix's rows must be equal to it's columns.");
		}
		
		if(Matrix.rows(result)!=(n-1)||Matrix.cols(result)!=(n-1)) result = new float[n-1][n-1];
		
		int newRow, newCol;
		for(int i=0;i<n;i++){
			if(i<row){
				newRow=i;
			}else if(i>row){
				newRow=i - 1;
			}else{
				continue;
			}
			for(int j=0;j<n;j++){
				if(j<col){
					newCol=j;
				}else if(j>col){
					newCol=j - 1;
				}else{
					continue;
				}
				result[newRow][newCol]=matrix[i][j];
			}
		}
		return result;
	}
	
	// 提取伴随矩阵 adjointMatrix
	public static float[][] adjointMatrix(float[][] matrix) throws Exception{
		int n = matrix.length;
		if(n==0 || n!=matrix[0].length){
			throw new Exception("To get adjoint matrix for a matrix, the matrix's rows must be equal to it's columns.");
		}
		
		float[][] result = new float[n][n];
		float[][] tmp = new float[n-1][n-1];
		float value;
		float negative;
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				negative = (Math.pow(-1, i+j)==1d) ? 1f :-1f;
				tmp = Matrix.cofactor(matrix, i, j, tmp);
				value = Matrix.detValue2(tmp);
				result[i][j] = negative * value;
			}
		}
		
		return result;
	}
	
	// 矩阵的某行的最小，最大，平均值
	public static HashMap<String, Float> rowStatistic(float[][] matrix, int row) throws Exception {
		float[] targetRow = getRow(matrix, row);
		int cols = targetRow.length;
		
		float min = targetRow[0] ;
		float max = min ;
		float avg = 0 ;
		
		for(float value : targetRow) {
			if(value < min) min = value;
			if(value > max) max = value;
			avg += (value / cols) ;
		}
		
		HashMap<String, Float> result = new HashMap<String, Float>();
		result.put("min", min);
		result.put("max", max);
		result.put("avg", avg);
		return result;
	}
	
	// 矩阵的某列的最小，最大，平均值
	public static HashMap<String, Float> colStatistic(float[][] matrix, int col) throws Exception {
		int rows = rows(matrix);
		int cols = cols(matrix);
		if(col<0 || col>=cols){
			throw new Exception("To col number " + col + " is not in the range of the matrix.");
		}
		
		float min = matrix[0][col] ;
		float max = min ;
		float avg = 0 ;
		float value;
		
		for(int i=0;i<rows;i++){
			value = matrix[i][col];
			if(value < min) min = value;
			if(value > max) max = value;
			avg += (value / rows) ;
		}
		
		HashMap<String, Float> result = new HashMap<String, Float>();
		result.put("min", min);
		result.put("max", max);
		result.put("avg", avg);
		return result;
	}

}
