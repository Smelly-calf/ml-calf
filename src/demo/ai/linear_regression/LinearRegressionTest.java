/*******************************************************************************
 * Linear Regression Demo Code
 * Author: Du Ke  (xadke@cn.ibm.com)
 * Date: 2018-8-10
 * The code just for study.
 *******************************************************************************/
package demo.ai.linear_regression;

import java.util.ArrayList;

import demo.ai.utils.MatrixDataFile;

public class LinearRegressionTest {

	public LinearRegressionTest() {
	}
	
	public static void log(String text) {
		System.out.println(text);
	}
	
	public static void testConcrete() throws Exception{
		log("测试名称：\t建立水泥抗压强度回归");
		log("收敛算法：\t梯度下降");
		
		// 数据文件
		MatrixDataFile dataSource = new MatrixDataFile("data/ConcreteTable.csv");  // 1030条数据
		dataSource.featureScaling();
		
		// 训练与测试数据集
		ArrayList<float[][]> trainData = dataSource.get(830,950);
		ArrayList<float[][]> testData  = dataSource.getLast(80);
		
		// 建立模型开始训练
		LinearRegression LR = new LinearRegression();
		LR.setXY(trainData.get(0),trainData.get(1));

		//第0列为0参数，其余为特征属性权重
		LR.train(new float[]{1,1,1, 1,1,1, 1,1,1}, 0.000015f, 130000);
		
		// 测试模型
		LR.test(LinearRegression.METHOD_GRADIENT_DESCENT,testData.get(0), testData.get(1));
		
		// 监控输出
		LinearRegressionMonitor monitor = new LinearRegressionMonitor();
		monitor.showResult(LR,80,80);
	}
	
	public static void main(String[] args) throws Exception {
		testConcrete();
	}

}
