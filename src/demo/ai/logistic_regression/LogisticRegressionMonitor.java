package demo.ai.logistic_regression;

import demo.ai.linear_regression.LinearRegression;
import demo.ai.linear_regression.LinearRegressionMonitor;
import demo.ai.utils.Line;
import demo.ai.utils.LineChart;
import demo.ai.utils.Matrix;

import java.util.ArrayList;
import java.util.HashMap;

public class LogisticRegressionMonitor extends LinearRegressionMonitor {

    public void showResult(LogisticRegression LR, double yAxisMax1, double yAxisMax2) throws Exception {

        float[][] parameters = LR.getParameters();

        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("步长梯度：\t" + String.format("%.6f", LR.getStep()));
        System.out.println("迭代次数：\t" + LR.getIterationTimes());
        System.out.println("参数结果：\t" + Matrix.matrixToString(parameters).toString());
        System.out.println("测试行数：\t" + LR.getTestX().length);
        System.out.println("---------------------------------------------------------------------------------");


        //监控输出
        ArrayList<HashMap> moitor = LR.getMoitorTraining();
        monitorOutput(yAxisMax1, moitor);
        System.out.println("---------------------------------------------------------------------------------");

        float[][] testX = LR.getTestX();
        float[][] testY = LR.getTestY();


        // 预测拟合曲线
        LineChart testChart = new LineChart("预测曲线", yAxisMax2);
        Line testLine1 = new Line("实际值");
        Line testLine2 = new Line("预测值");
        ArrayList<HashMap> moitorTesting = LR.getMoitorTesting();
        predCurve(moitorTesting, testChart, testLine1, testLine2);
        // 动画拟合
        testChart.removeLine(testLine2);
        for (int i = 10; i < moitor.size(); i++) {
            HashMap item = moitor.get(i);
            int times = (Integer) item.get("times");
            Line testLineTmp = new Line("预测值（第" + (times + 1) + "次迭代）");
            float[][] _parameters = (float[][]) item.get("parameters");
            moitorTesting = LR.test(LinearRegression.METHOD_GRADIENT_DESCENT, testX, testY, _parameters);
            for (int j = 0; j < moitorTesting.size(); j++) {
                HashMap itemTest = moitorTesting.get(j);
                float idx = (Integer) itemTest.get("index") * 1.0f;
                float hypotheticalY = (Float) itemTest.get("hypotheticalY");
                testLineTmp.addPoint(idx, hypotheticalY);
            }
            testChart.addLine(testLineTmp);
            Thread.sleep(50);
            if (i < moitor.size() - 1) testChart.removeLine(testLineTmp);
        }
    }


}
