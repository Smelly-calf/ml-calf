/*******************************************************************************
 * Linear Regression Demo Code
 * Author: Du Ke  (xadke@cn.ibm.com)
 * Date: 2018-8-10
 * The code just for study.
 *******************************************************************************/
package demo.ai.linear_regression;

import java.util.ArrayList;
import java.util.HashMap;

import demo.ai.utils.Line;
import demo.ai.utils.LineChart;
import demo.ai.utils.Matrix;

public class LinearRegressionMonitor {
	
	public void showResult(LinearRegression LR, double yAxisMax1, double yAxisMax2) throws Exception {
		
		float[][] parameters = LR.getParameters();
		
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("步长梯度：\t" + String.format("%.6f",LR.getStep()));
		System.out.println("迭代次数：\t" + LR.getIterationTimes());
		System.out.println("参数结果：\t" + Matrix.matrixToString(parameters).toString());
		System.out.println("测试行数：\t" + LR.getTestX().length);
		System.out.println("---------------------------------------------------------------------------------");
		
		
		//监控输出
		LineChart chart = new LineChart("收敛曲线", yAxisMax1);
		Line line1 = new Line("收敛百分比");
		Line line2 = new Line("收敛值");
		ArrayList<HashMap> moitor = LR.getMoitorTraining();
		for(int i=0;i<moitor.size();i++) {
			HashMap item = moitor.get(i);
			float times = (Integer)item.get("times") * 1.0f;   //(Float)item.get("times");
			float avgPercent = (Float)item.get("distAvgPercent");
			float avg = (Float)item.get("distAvg");
			if(i>=10) {
				line1.addPoint(times,avgPercent);
				line2.addPoint(times,avg);
			}
			if(i<10 || i>moitor.size()-10){
				System.out.println(" >>>>>>>>>>>>>>> 迭代：" + (times+1) + "\t平均误差：" + String.format("%.6f",avg) + "\t收敛百分比：" + String.format("%.6f",avgPercent) + " %" );
			}
		}
		chart.addLine(line2);
		chart.run();
		System.out.println("---------------------------------------------------------------------------------");
		
		float[][] testX = LR.getTestX();
		float[][] testY = LR.getTestY();
		
		
		// 预测拟合曲线
		LineChart testChart = new LineChart("预测曲线", yAxisMax2);
		Line testLine1 = new Line("实际值");
		Line testLine2 = new Line("预测值");
		ArrayList<HashMap> moitorTesting = LR.getMoitorTesting();
		for(int i=0;i<moitorTesting.size();i++) {
			HashMap item = moitorTesting.get(i);
			float idx = (Integer)item.get("index") * 1.0f;
			float realY = (Float)item.get("realY");
			float hypotheticalY = (Float)item.get("hypotheticalY");
			testLine1.addPoint(idx,realY);
			testLine2.addPoint(idx,hypotheticalY);
		}
		testChart.addLine(testLine1);
		testChart.addLine(testLine2);
		testChart.run();
		
		
		// 动画拟合
		testChart.removeLine(testLine2);
		for(int i=10;i<moitor.size();i++) {
			HashMap item = moitor.get(i);
			int times = (Integer)item.get("times");
			Line testLineTmp = new Line("预测值（第" + (times+1) + "次迭代）");
			float[][] _parameters = (float[][])item.get("parameters");
			moitorTesting = LR.test(LinearRegression.METHOD_GRADIENT_DESCENT, testX, testY, _parameters);
			for(int j=0;j<moitorTesting.size();j++) {
				HashMap itemTest = moitorTesting.get(j);
				float idx = (Integer)itemTest.get("index") * 1.0f;
				float hypotheticalY = (Float)itemTest.get("hypotheticalY");
				testLineTmp.addPoint(idx,hypotheticalY);
			}
			testChart.addLine(testLineTmp);
			Thread.sleep(50);
			if(i<moitor.size()-1) testChart.removeLine(testLineTmp);
		}
	}

}
