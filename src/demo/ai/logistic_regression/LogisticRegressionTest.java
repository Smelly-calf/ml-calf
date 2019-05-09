package demo.ai.logistic_regression;

import demo.ai.linear_regression.LinearRegression;
import demo.ai.linear_regression.LinearRegressionMonitor;
import demo.ai.utils.MatrixDataFile;

import java.util.ArrayList;

public class LogisticRegressionTest  {
    public LogisticRegressionTest() {
    }

    public static void log(String text) {
        System.out.println(text);
    }

    public static void testConcrete() throws Exception{
        log("测试名称：\t建立分类用户购买意愿的回归");
        log("收敛算法：\t梯度上升");

        // 数据文件
        MatrixDataFile dataSource = new MatrixDataFile("data/Social_Network_Ads_scaler.csv");  // sklearn.preprocessing.StandardScaler 特征缩放后的数据
//        dataSource.featureScaling();

        // 训练与测试数据集
        ArrayList<float[][]> trainData = dataSource.getFirst(300);
        ArrayList<float[][]> testData  = dataSource.getLast(100);

        // 建立模型开始训练
        LogisticRegression LR = new LogisticRegression();
        LR.setXY(trainData.get(0),trainData.get(1));
        LR.train(new float[]{1, 1,1}, 0.0015f, 500);

        // 测试模型
        LR.test(LogisticRegression.METHOD_GRADIENT_DESCENT,testData.get(0), testData.get(1));

        // 监控输出
        LogisticRegressionMonitor monitor = new LogisticRegressionMonitor();
        monitor.showResult(LR,5,1);
    }

    public static void main(String[] args) throws Exception {
        testConcrete();
    }
}
