package paintingchat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Circle extends PaintingPrimitive{
	private Point center;
	private Point radiusPoint;
	private Color color;
	
	public Circle(Point center, Point radius, Color color) {
		super(color);
		this.center = center;
		radiusPoint = radius;
		this.color = color;
	}
	
	public synchronized void drawGeometry(Graphics g) {
        int radius = (int) Math.abs(center.distance(radiusPoint));
        g.drawOval(center.x - radius, center.y - radius, radius*2, radius*2);   
	}

}
