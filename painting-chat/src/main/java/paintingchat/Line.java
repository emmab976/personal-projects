package paintingchat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Line extends PaintingPrimitive{
	private Point start;
	private Point end;
	private Color color;
	
	public Line(Point start, Point end, Color color) {
		super(color);
		this.start = start;
		this.end = end;
		this.color = color;
	}
	
	public synchronized void drawGeometry(Graphics g) {
		g.drawLine(start.x, start.y, end.x, end.y);
	}
}
