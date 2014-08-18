package dev.polygon;

import java.awt.Polygon;

public class PolygonJava {
	
	public PolygonJava(){
		
	}

	Polygon mPoly = null;
	
	public boolean CheckPolygon(int[] nPointX, int[] nPointY, int nX, int nY){
		
//		System.out.println(System.currentTimeMillis());
		mPoly = new Polygon(nPointX, nPointY, nPointX.length);
//		System.out.println(System.currentTimeMillis());
		boolean mResult = mPoly.contains(nX, nY);
//		System.out.println(System.currentTimeMillis());
		
		return mResult;
	}
	
}
