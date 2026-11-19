package paintingchat;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public abstract class PaintingPrimitive implements Serializable{
	private Color color;
	
	public PaintingPrimitive(Color color) {
		this.color = color;
	}
	
	public String toString() {
		return color.toString();
	}

	//This is an example of the Template Design Pattern
	
	public final void draw(Graphics g) {
	    g.setColor(this.color);
	    drawGeometry(g);
	}
	
	protected abstract void drawGeometry(Graphics g);

}
