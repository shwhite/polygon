package dev.polygon;

import java.awt.Rectangle;
import java.util.Arrays;

public class DevPolygon {

	int xpoints[];
	int ypoints[];
	int npoints;
	
	protected Rectangle bounds;
	
	public DevPolygon(int xpoints[], int ypoints[], int npoints){
		// Fix 4489009: should throw IndexOutofBoundsException instead
		// of OutofMemoryException if npoints is huge and > {x,y}points.length
		if (npoints > xpoints.length || npoints > ypoints.length) {
			throw new IndexOutOfBoundsException("npoints > xpoints.length || "+"npoints > ypoints.length");
		}
		// Fix 6191114: should throw NegativeArraySizeException with
		// negative npoints
		if (npoints < 0) {
			throw new NegativeArraySizeException("npoints < 0");
		}
		// Fix 6343431: Applet compatibility problems if arrays are not
		// exactly npoints in length
		this.npoints = npoints;
		this.xpoints = Arrays.copyOf(xpoints, npoints);
		this.ypoints = Arrays.copyOf(ypoints, npoints);		
	}
	
	public boolean contains(double x, double y){
		if (npoints <= 2 || !getBoundingBox().contains(x, y)) {
			return false;
		}
		
		int hits = 0;
		
		int lastx = xpoints[npoints - 1];
		int lasty = ypoints[npoints - 1];
		int curx, cury;
		
		// Walk the edges of the polygon
		for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {
			curx = xpoints[i];
			cury = ypoints[i];
		
		if (cury == lasty) {
			continue;
		}
		
		int leftx;
		if (curx < lastx) {
			if (x >= lastx) {
				continue;
			}
				leftx = curx;
			} else {
				if (x >= curx) {
					continue;
				}
				leftx = lastx;
			}
			
			double test1, test2;
			if (cury < lasty) {
				if (y < cury || y >= lasty) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - curx;
				test2 = y - cury;
			} else {
				if (y < lasty || y >= cury) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - lastx;
				test2 = y - lasty;
			}
			
			if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
				hits++;
			}
		}
		
		return ((hits & 1) != 0);
		
	}
	
	@Deprecated
	public Rectangle getBoundingBox() {
		if (npoints == 0) {
			return new Rectangle();
		}
		
		if (bounds == null) {
			calculateBounds(xpoints, ypoints, npoints);
		}
		return bounds.getBounds();
	}
	
	void calculateBounds(int xpoints[], int ypoints[], int npoints) {
		int boundsMinX = Integer.MAX_VALUE;
		int boundsMinY = Integer.MAX_VALUE;
		int boundsMaxX = Integer.MIN_VALUE;
		int boundsMaxY = Integer.MIN_VALUE;
		
		for (int i = 0; i < npoints; i++) {
			int x = xpoints[i];
			boundsMinX = Math.min(boundsMinX, x);
			boundsMaxX = Math.max(boundsMaxX, x);
			int y = ypoints[i];
			boundsMinY = Math.min(boundsMinY, y);
			boundsMaxY = Math.max(boundsMaxY, y);
		}
			bounds = new Rectangle(boundsMinX, boundsMinY,
			boundsMaxX - boundsMinX,
			boundsMaxY - boundsMinY);
	}
}
