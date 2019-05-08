/*******************************************************************************
 * Linear Regression Demo Code
 * Author: Du Ke  (xadke@cn.ibm.com)
 * Date: 2018-8-10
 * The code just for study.
 *******************************************************************************/
package demo.ai.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class MatrixDataFile {
	
	public static final int FILE_TYPE_CSV = 0;			// CSV文件类型
	
	public static final int CONSTANT_IN_LAST = 0;		// 常数位置位于开头
	
	public static final int CONSTANT_IN_FIRST = 1;		// 常数位置位于最后
	
	private int fileType = FILE_TYPE_CSV ;				// 目前仅CSV文件类型
	
	private boolean addConstant = true;					// 是否增加常数，默认为增加
	
	private int constantLocation = CONSTANT_IN_LAST;	// 默认常数位置位于最后

	private String sourceFile;							// 数据文件位置

	private ArrayList<String> data;						// 原始行字符串数据

	private float[][] x;								// 全部特征矩阵数据
	
	private float[][] y;								// 全部值矩阵数据
	
	private HashMap<Integer, HashMap<String, Float>> colStatistics = new HashMap<Integer, HashMap<String, Float>>();	// 列数据归一化统计信息
	
	public MatrixDataFile(String sourceFile) throws Exception {
		setSourceFile(sourceFile);
		loadData();
		initData();
	}
	
	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public ArrayList<String> getData() {
		return data;
	}

	public void setData(ArrayList<String> data) {
		this.data = data;
	}
	
	//从文件读数据
	public void loadData(){
		data = Common.readFileToArrayList(sourceFile);
	}
	
	public void initData() throws Exception{
		String line;			// 文件中的一行数据
		String split;			// 分隔符，默认为 CSV 文件的逗号
		String[] features;		// 特征值数组
		int constLocation;		// 常数在矩阵中的位置：开头或最后
		int featureLocation;	// 特征在矩阵中的位置：根据constLocation来决定
		int row = 0 ;
		int i;
		
		// 设置x与y矩阵行数
		final int rows = data.size() ;
		
		// 设置x矩阵列数
		line = data.get(0);
		split = (fileType == FILE_TYPE_CSV) ? "," : ",";
		features = line.split(split);
		final int featureCount = features.length-1 ;
		final int cols = addConstant ? featureCount+1 : featureCount;  // 增加常数列,即 (wx+b) 中的参数 b
		
		// 初始化原始数据矩阵
		x = new float[rows][cols];
		y = new float[rows][1];
		
		// x(0)数据，常数列，所以设置x(0)值始终为1
		if(addConstant){
			constLocation = (constantLocation == CONSTANT_IN_FIRST) ? 0 : featureCount;
			for(i=0;i<rows;i++) x[i][constLocation] = 1 ;
		}
		
		//填充特征与标签数据
		for(i=0;i<rows;i++){
			line = data.get(i);
			features = line.split(",");
			// 设置特征值
			for(int j=0;j<features.length-1;j++){
				// 特征值的位置
				featureLocation = (addConstant && constantLocation == CONSTANT_IN_FIRST) ? j+1 : j;
				// 将特征值放入特征矩阵x
				x[row][featureLocation] = Float.parseFloat(features[j].trim());
			}
			// 设置y值：y值位于一行数据最后，将y值放入值矩阵y
			y[row][0] = Float.parseFloat(features[features.length-1]) ;
			row++;
		}
	}
	
	//特征缩放
	public void featureScaling() throws Exception{
		// 开始缩放
		float min,max,avg,range;
		int rows = Matrix.rows(x);
		int cols = Matrix.cols(x);
		
		for(int col=0;col<cols;col++) {
			HashMap<String, Float> statistic = Matrix.colStatistic(x, col);
			min = statistic.get("min");
			max = statistic.get("max");
			avg = statistic.get("avg");
			range = max - min ;
			if(max==min) continue;  // 常数列略过
			if(min>=-1 && max<=1) continue;  // 特征合适则略过
			colStatistics.put(col, statistic);
			// 缩放该列
			for(int row=0;row<rows;row++) {
				x[row][col] = (x[row][col] - avg ) / range ;
			}
		}
	}
	
	//获取矩阵数据;包含x和y，存放于ArrayList中的第一和第二项中返回
	// form: 起始位置，从1开始, 若果小于1则默认为从1开始。
	// to:   结束位置，到数据数量count，若果小于1则默截止到最后一个。
	public ArrayList<float[][]> get(int from, int to) throws Exception{
		int row = 0 ;
		int i;
		float[][] _x;			// 特征矩阵x
		float[][] _y;			// 值矩阵y
		final int total = Matrix.rows(x) ;
		
		// 设置x与y矩阵行数, 设置x矩阵列数
		if(from<1 || from>total) from = 1 ;
		if(to<1 || to >total || to<from) to = total;
		final int rows = to - from + 1;
		final int cols = Matrix.cols(x);
		
		// 初始化矩阵
		_x = new float[rows][cols];
		_y = new float[rows][1];
		
		//填充特征与标签数据
		for(i=(from-1);i<to;i++){
			System.arraycopy(x[i], 0, _x[row], 0, cols);
			_y[row][0] = y[i][0];
			row++;
		}
		
		// 返回数据
		ArrayList<float[][]> r = new ArrayList<float[][]>();
		r.add(_x);
		r.add(_y);
		return r ;
	}

	
	//获取矩阵数据，全部文件数据
	public ArrayList<float[][]> get() throws Exception{
		return this.get(-1, -1);
	}

	public float[][] getY() throws Exception{
		return this.get().get(1);
	}
	
	//获取矩阵数据，开头的first条记录
	public ArrayList<float[][]> getFirst(int first) throws Exception{
		return this.get(-1, first);
	}
	
	//获取矩阵数据，最后的last条记录
	public ArrayList<float[][]> getLast(int last) throws Exception{
		return this.get(data.size()-last+1, -1);
	}

	public ArrayList<float[][]> getColumn(int from, int to) throws Exception{
		return this.get(data.size()-from+1,-1);
	}

}
