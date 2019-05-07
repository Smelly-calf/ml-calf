/*******************************************************************************
 * Linear Regression Demo Code
 * Author: Du Ke  (xadke@cn.ibm.com)
 * Date: 2018-8-10
 * The code just for study.
 *******************************************************************************/
package demo.ai.utils;

import java.util.ArrayList;

public class Line {
	
	private ArrayList<Point> data = new ArrayList<Point>();
	
	private String title = "" ;
	
	public Line(String title) {
		setTitle(title);
	}
	
	public Line(String title, ArrayList<Point> data) {
		setTitle(title);
		setData(data);
	}
	
	public ArrayList<Point> getData() {
		return data;
	}

	public void setData(ArrayList<Point> data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void addPoint(Point point){
		data.add(point);
	}
	
	public void addPoint(float x, float y){
		addPoint(new Point(x,y));
	}

}
