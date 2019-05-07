/*******************************************************************************
 * Linear Regression Demo Code
 * Author: Du Ke  (xadke@cn.ibm.com)
 * Date: 2018-8-10
 * The code just for study.
 *******************************************************************************/
package demo.ai.linear_regression;

import java.util.ArrayList;
import java.util.HashMap;

import demo.ai.utils.Matrix;

public class LinearRegression {

	public static final int METHOD_GRADIENT_DESCENT = 0;	// 梯度下降法
	public static final int METHOD_NORMAL_EQUATIONS = 1;	// 正规方程法
	
	private float[][] x;					// 特征矩阵
	private float[][] parameters;			// 参数矩阵
	private float[][] y;					// 标签矩阵(只用第一1列) 0 or 1
	private float step = 0.0001f ;			// 步长step,即公式中的 a
	private int iterationTimes = 10 ;		// 迭代次数

	private int m ;							// 特征矩阵x的行数
	private int n ;							// 特征矩阵x的列数
	private float[][] xT ;					// 特征矩阵x的转置矩阵	= Matrix.transpose(x);
	private float[][] a_times_xT ;			// 梯度 a 与 xT 的乘积 = Matrix.constantProduct(a, xT);
	private float[][] parametersT ;			// 参数矩阵的转置矩阵 = Matrix.transpose(parameters);
	
	private float[][] A ;					// 中间计算矩阵 A = new float[m][1];
	private float[][] E ;					// 中间计算矩阵 E= new float[m][1];
	private float[][] a_times_xT_times_E ;	// 中间计算矩阵 a*xT*E = new float[n][1];
	
	private float[][] testX ;				// 测试数据集合x
	private float[][] testY ;				// 测试数据集合y
	
	private int moitorCount = 100 ;												// 监控输出数据的数量
	private ArrayList<HashMap> moitorTraining = new ArrayList<HashMap>();		// 训练监控数据
	private ArrayList<HashMap> moitorTesting ;									// 测试监控数据
	
	private float totalErrorAvg = 0 ;		// 总体平均误差
	private float totalErrorAvgPercent = 0;	// 总体平均误差比
	private float[][] parametersCopy;		// 参数拷贝
	
	public LinearRegression() {
	}
	
	////////////////////////////////////////////////////////////
	
	public void setXY(float[][] x, float[][] y) throws Exception {
		setX(x);
		setY(y);
	}
	
	public float[][] getX() {
		return x;
	}

	public void setX(float[][] x) throws Exception {
		this.x = x;
		this.xT = Matrix.transpose(x);
		this.m = x.length;
		this.n = x[0].length;
		
		this.parameters = new float[1][n];
		this.A = new float[m][1];
		this.E = new float[m][1];
		this.a_times_xT_times_E = new float[n][1];
	}

	public float[][] getParameters() {
		return parameters;
	}

	public void setParameters(float[][] parameters) {
		this.parameters = parameters;
	}

	public float[][] getY() {
		return y;
	}

	public void setY(float[][] y) {
		this.y = y;
	}

	public float getStep() {
		return step;
	}

	public void setStep(float alpha) {
		this.step = alpha;
	}

	public int getIterationTimes() {
		return iterationTimes;
	}

	public void setIterationTimes(int iterationTimes) {
		this.iterationTimes = iterationTimes;
	}
	
	public int getMoitorCount() {
		return moitorCount;
	}

	public void setMoitorCount(int moitorCount) {
		this.moitorCount = moitorCount;
	}

	public ArrayList<HashMap> getMoitorTraining() {
		return moitorTraining;
	}

	public void setMoitorTraining(ArrayList<HashMap> moitorTraining) {
		this.moitorTraining = moitorTraining;
	}
	
	public ArrayList<HashMap> getMoitorTesting() {
		return moitorTesting;
	}

	public void setMoitorTesting(ArrayList<HashMap> moitorTesting) {
		this.moitorTesting = moitorTesting;
	}
	
	public float[][] getTestX() {
		return testX;
	}

	public void setTestX(float[][] testX) {
		this.testX = testX;
	}

	public float[][] getTestY() {
		return testY;
	}

	public void setTestY(float[][] testY) {
		this.testY = testY;
	}
	
	public float getTotalErrorAvg() {
		return totalErrorAvg;
	}

	public float getTotalErrorAvgPercent() {
		return totalErrorAvgPercent;
	}
	
	////////////////////////////////////////////////////////////
	
	public void train(float[] initParameters, float initStep, int iterationTimes) throws Exception{
		float[] params;
		float hx;
		float percent = 0 ;
		float yValue = 0;// duke
		float dist = 0;
		float distAll = 0 ;
		float distAvg = 0 ;
		float distAvgBefore = 0 ;
		int moitorStep = (iterationTimes / moitorCount);
		float moitorDiv;
		
		// 初始参数矩阵
		for(int i=0;i<parameters.length;i++){
			if(i<initParameters.length) parameters[0][i] = initParameters[i];
		}
		
		// 设置梯度
		this.setStep(initStep);
		
		// 设置迭代次数
		this.setIterationTimes(iterationTimes);
		
		// 转置参数矩阵
		parametersT = Matrix.transpose(parameters);
		
		// 中间矩阵 a * xT !!!! 需要优化
		a_times_xT = Matrix.times(this.step, xT);
		
		// 迭代
		for(int times= 0; times<iterationTimes; times++){
			// (1) 求 A = x * parameters;
			Matrix.times(x, parametersT, A);
			
			// (2) 求 E = A - y
			Matrix.add(A, -1, y, E);
			
			// (3) 求 parametersT := parametersT - a * xT * E
			Matrix.times(a_times_xT, E, a_times_xT_times_E);
			Matrix.add(parametersT, -1, a_times_xT_times_E, parametersT);
			
			// 新的参数
			parameters = Matrix.transpose(parametersT);
			
			// 监控输出，求下降程度
			distAll = 0 ;
			params = Matrix.matrixToColVector(parametersT);
			for(int i=0;i<x.length;i++){
				hx = Matrix.dotProduct(x[i], params);
				yValue = y[i][0];
				dist = (hx - yValue)*(hx - yValue);
				distAll += dist;
			}
			distAvg = distAll / (x.length * 2);  // j(θ)
			if(distAvgBefore>0){
				percent = ((distAvgBefore - distAvg) / distAvgBefore ) * 100 ;
			}
			distAvgBefore = distAvg;
			
			// 监控数据
			moitorDiv = times%moitorStep ;
			if(times<10 || times>iterationTimes-10 || (moitorDiv==0.0)){
				HashMap monitorItem = new HashMap();
				monitorItem.put("times", times);
				monitorItem.put("distAvg", distAvg);
				monitorItem.put("distAvgPercent", percent);
				monitorItem.put("parameters", Matrix.copy(parameters));  // 记录本次参数
				moitorTraining.add(monitorItem);
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 测试模型
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 假设函数
	public float hypothesize(float[] x, float[][] parameters) throws Exception{
		parametersCopy = Matrix.copy(parameters, parametersCopy);
		float[] p = parametersCopy[0];
		float hx = Matrix.dotProduct(x, p);
		return hx;
	}
	
	// 假设函数
	public float hypothesize(float[] x) throws Exception{
		return hypothesize(x,this.parameters);
	}
	
	// 测试单行数据
	public float test(int method, float[] x, float y, float[][] parameters) throws Exception{
		return hypothesize(x,parameters);
	}
	
	// 测试单行数据
	public float test(int method, float[] x, float y) throws Exception{
		return hypothesize(x,this.parameters);
	}
	
	// 测试数据集
	public ArrayList<HashMap> test(int method, float[][] testX, float[][] testY, float[][] parameters) throws Exception{
		moitorTesting = new ArrayList<HashMap>();
		int testCount = testX.length;
		totalErrorAvg = 0 ;
		totalErrorAvgPercent = 0 ;
		for(int i=0;i<testX.length;i++){
			float[] x = testX[i];
			float y = testY[i][0];
			float hy = test(method,x,y,parameters);
			float error = Math.abs(hy-y);		//误差
			float errorPercent = error / y ;
			totalErrorAvg += error / testCount ;
			totalErrorAvgPercent += (error / y) / testCount ;
			HashMap monitorItem = new HashMap();
			monitorItem.put("index", i);
			monitorItem.put("realY", y);
			monitorItem.put("hypotheticalY", hy);
			monitorItem.put("error", error);
			monitorItem.put("errorPercent", errorPercent);
			moitorTesting.add(monitorItem);
		}
		return moitorTesting;
	}
	
	// 测试数据集
	public ArrayList<HashMap> test(int method, float[][] testX, float[][] testY) throws Exception{
		this.testX = testX;
		this.testY = testY;
		
		moitorTesting = test(method, testX, testY, parameters);
		return moitorTesting;
	}
	
	
	public static void main(String[] args) {

	}

}
