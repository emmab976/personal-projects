package emmabryant_painter_comp2355;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import java.util.ArrayList;

public class Painter extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	Color color;
	static PaintingPrimitive shape;
	Point start;
	Point end;
	String type;
	static PaintingPanel centerPanel;
	String message;
	static String name;
	static ArrayList<PaintingPrimitive> objects = new ArrayList<PaintingPrimitive>();
	static JTextArea messages;
	static JTextArea chat;
	
	static ObjectOutputStream oos;
	static ObjectInputStream ois;	

	
	public Painter() throws IOException {
		
		name = JOptionPane.showInputDialog("Enter your name");
		
		setSize(500, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel holder = new JPanel();
		holder.setLayout(new BorderLayout());
		Border header = BorderFactory.createTitledBorder(name);
		holder.setBorder(header);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(2, 2));
		bottomPanel.setBackground(Color.decode("#D3D3D3"));
		
		chat = new JTextArea();
		bottomPanel.add(chat);
		

		
		messages = new JTextArea();
		bottomPanel.add(messages);
		
		
		JButton messageButton = new JButton("Send Message");
		bottomPanel.add(messageButton);
		messageButton.addActionListener(this);
		messageButton.setActionCommand("send");
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(3, 1)); // 3 by 1
		
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.decode("#D3D3D3"));
		topPanel.setLayout(new GridLayout(1, 2));
		
		centerPanel = new PaintingPanel();
		centerPanel.addMouseListener(this);
		centerPanel.addMouseMotionListener(this);
		
		
		JButton lineButton = new JButton("Line");
		topPanel.add(lineButton);
		lineButton.addActionListener(this);
		lineButton.setActionCommand("line");
		
		JButton circleButton = new JButton("Circle");
		topPanel.add(circleButton);
		circleButton.addActionListener(this);
		circleButton.setActionCommand("circle");

		// add red paint button
		JButton redPaint = new JButton();
		redPaint.setBackground(Color.RED);
		redPaint.setOpaque(true);
		redPaint.setBorderPainted(false);
		leftPanel.add(redPaint);
		redPaint.addActionListener(this);
		redPaint.setActionCommand("red");
		
		
		JButton greenPaint = new JButton();
		greenPaint.setBackground(Color.GREEN);
		greenPaint.setOpaque(true);
		greenPaint.setBorderPainted(false);
		leftPanel.add(greenPaint);
		greenPaint.addActionListener(this);
		greenPaint.setActionCommand("green");
		
		JButton bluePaint = new JButton();
		bluePaint.setBackground(Color.BLUE);
		bluePaint.setOpaque(true);
		bluePaint.setBorderPainted(false);
		leftPanel.add(bluePaint);
		bluePaint.addActionListener(this);
		bluePaint.setActionCommand("blue");
		


		// add the panels to the overall panel, holder
		// note that holder's layout should be set to BorderLayout
		holder.add(leftPanel, BorderLayout.WEST);
		holder.add(topPanel, BorderLayout.NORTH);
		holder.add(centerPanel, BorderLayout.CENTER);
		holder.add(bottomPanel, BorderLayout.SOUTH);
		// omit the center panel for now
		// after finishing the PaintingPanel class (described later) add a
		// new object of this class as the CENTER panel

		// And later you will add the chat panel to the SOUTH

		// Lastly, connect the holder to the JFrame
		setContentPane(holder);

		// And make it visible to layout all the components on the screen
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "red") {
			color = Color.RED;
			System.out.println("Red");
		}
		else if(e.getActionCommand() == "green") {
			color = Color.GREEN;
			System.out.println("Green");
		}
		else if(e.getActionCommand() == "blue") {
			color = Color.BLUE;
			System.out.println("Blue");
		}
		else if(e.getActionCommand() == "circle") {
			System.out.println("Circle");
			type = "Circle";
		}
		else if(e.getActionCommand() == "line") {
			System.out.println("Line");
			type = "Line";
		}
		else if(e.getActionCommand() == "send") {
			message = chat.getText();
			message = name + ": " + message;
			try {
				oos.writeObject(message);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Pressed");
		start = e.getLocationOnScreen();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Released");
		end = e.getLocationOnScreen();
		if(type == "Circle") {
			shape = new Circle(start, end, color);
		}
		else if(type == "Line") {
			shape = new Line(start, end, color);
		}
		
		objects.add(shape);
		shape.draw(getGraphics());
		centerPanel.addPrimitive(shape);
		
		
		try {
			oos.writeObject(shape);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void drawNew(PaintingPrimitive obj) {
		System.out.println("Drawing from Thread!");
		obj.draw(getGraphics());
		centerPanel.addPrimitive(obj);
	}
	
	public synchronized void showMessage(String s) {
		messages.setText(s);
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		System.out.println("About to call");
		Socket s;
		
		s = new Socket("localhost", 7000);

		System.out.println("Connected");
		
		Painter self = new Painter();
		
		System.out.println("Name: "+ name);
		
		// DataOutputStream
		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());

		oos.writeObject(name);
		
		try {
			objects = (ArrayList<PaintingPrimitive>)ois.readObject();
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(PaintingPrimitive pt : objects) {
			self.drawNew(pt);
		}
		
		Thread readObject = new Thread(new Runnable()
		{
			@Override
			public void run() {
				while(true) {
					try {
						/*
						if(ois.readObject() instanceof String) {
							String str = (String)ois.readObject();
							self.showMessage(str);
						}
						else { */
						PaintingPrimitive paint = (PaintingPrimitive)ois.readObject();
						if(!objects.contains(paint)) {
							self.drawNew(paint);
							objects.add(paint);	
						}
						//}
						
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	    });
		
		readObject.start();
	}
}
