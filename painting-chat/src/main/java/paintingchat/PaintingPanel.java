package paintingchat;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PaintingPanel extends JPanel{
	private ArrayList<PaintingPrimitive> primitives;
	
	public PaintingPanel() {
		primitives = new ArrayList<PaintingPrimitive>();
	}
	
	public synchronized void addPrimitive(PaintingPrimitive obj) {
        this.primitives.add(obj);
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(PaintingPrimitive obj : primitives) {  
                    obj.draw(g);
        }
	}
}
