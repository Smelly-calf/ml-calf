/*******************************************************************************
 * Linear Regression Demo Code
 * Author: Du Ke  (xadke@cn.ibm.com)
 * Date: 2018-8-10
 * The code just for study.
 *******************************************************************************/
package demo.ai.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChart {
	
	private String title ;
	
	private ChartPanel panel;
	
	private JFrame frame;
	
	private XYSeriesCollection seriescollection = new XYSeriesCollection();
	
	private HashMap<String, XYSeries> seriesNameMap = new HashMap<String, XYSeries>();
	
	
	private double yAxisMax = 0 ;		// Y轴最大值
	
	////////////////////////////////////////
	
	public double getyAxisMax() {
		return yAxisMax;
	}

	public void setyAxisMax(double yAxisMax) {
		this.yAxisMax = yAxisMax;
	}

	public LineChart(String title) {
		this.setTitle(title);
	}
	
	public LineChart(String title, double yAxisMax) {
		this.setTitle(title);
		this.setyAxisMax(yAxisMax);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public XYSeriesCollection getXYSeriesCollection() {
		return seriescollection;
	}
	
	public ChartPanel getChartPanel() {
		return panel;
	}
	
	public JFrame getJFrame() {
		return frame;
	}

	public void addLine(Line line){
		XYSeries series = new XYSeries(line.getTitle());
		ArrayList<Point> data = line.getData();
		for(Point point : data){
			series.add(point.getX(), point.getY());
		}
		seriescollection.addSeries(series);
		seriesNameMap.put(line.getTitle(), series);
	}
	
	public void addLine(String title, ArrayList<Point> data){
		addLine(new Line(title, data));
	}
	
	public void removeLine(Line line){
		seriescollection.removeSeries(seriesNameMap.get(line.getTitle()));
		seriesNameMap.remove(line.getTitle());
	}
	
	public void init(){
		JFreeChart jfreechart = ChartFactory.createXYLineChart(
				this.getTitle(),
				"X", 
				"Y", 
				seriescollection, 
				PlotOrientation.VERTICAL, 
				true, 
				true,
				false);
		
		
		XYPlot plot = (XYPlot) jfreechart.getPlot();	// 获取折线图plot对象
		plot.setBackgroundPaint(Color.WHITE);			// 设置背景颜色
		plot.setDomainGridlinePaint(Color.gray);		// 设置网格竖线颜色
		plot.setRangeGridlinePaint(Color.gray);			// 设置网格横线颜色
		plot.setNoDataMessage("暂无数据显示！");			// 没有数据显示的时候显示这个提示
		plot.setBackgroundAlpha(1f);					// 设置背景透明度（0~1）
		plot.setForegroundAlpha(1f);					// 设置前景色透明度（0~1）
		
		ValueAxis xAxis = plot.getDomainAxis();										// 取得横轴
		xAxis.setLabelFont(new Font("黑体", Font.PLAIN, 15));						// 设置横轴的字体
		xAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 15));					// 设置分类标签字体
		
		
		NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();						// 取得纵轴
		if(yAxisMax>0) yAxis.setRange(0, yAxisMax);									// 纵轴范围
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());			// 纵轴单位
		yAxis.setLabelFont(new Font("黑体", Font.PLAIN, 15));						// 设置纵轴的字体 
		yAxis.setUpperMargin(0.15);													// 设置最高数据显示与顶端的距离
		yAxis.setLowerMargin(0);													// 设置最低的一个值与图片底端的距离
		
		
		XYLineAndShapeRenderer shape = (XYLineAndShapeRenderer)plot.getRenderer();	// 数据点
        shape.setShapesVisible(true); 												// 数据点可见
        shape.setShapesFilled(true); 												// 数据点被填充即不是空心点
        
        
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));			// 图例字体
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));				// 标题字体
		panel = new ChartPanel(jfreechart, true);
	}

	
	public void run(){
		init();
		frame = new JFrame(this.getTitle());
		frame.setLayout(new GridLayout(1, 1, 10, 10));
		frame.add(this.getChartPanel()); // 添加折线图
		frame.setBounds(50, 50, 800, 600);
		frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		Line l = new Line("Test Line");
		l.addPoint(1,5);
		l.addPoint(2,11);
		l.addPoint(5,32);
		l.addPoint(3,25);
		l.addPoint(10,15);
		
		LineChart c = new LineChart("测试图形");
		c.addLine(l);
		c.run();
	}

}
