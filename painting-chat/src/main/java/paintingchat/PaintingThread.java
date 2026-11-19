package paintingchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class PaintingThread implements Runnable{
	
	Socket s;
	private String name;
	final ObjectInputStream ois;
	final ObjectOutputStream oos;
	boolean active;
	int objects;
	public PaintingThread(Socket s, String name, ObjectInputStream ois, ObjectOutputStream oos) {
		this.s = s;
		this.name = name;
		this.ois = ois;
		this.oos = oos;
		active = true;
		objects = 0;
	}

	@Override
	public void run() {
		PaintingPrimitive paint;
		String message;
		
		while(true) {
			try {
				/*
				if(ois.readObject() instanceof String) {
					System.out.println("String");
					message = (String)ois.readObject();
					for(PaintingThread pt : Hub.painters) {
						
						System.out.println("Message " + pt.name);
						pt.oos.writeObject(message);
				
					}
				}
				
				else if () {
				*/
				paint = (PaintingPrimitive)ois.readObject();
					
	
					
				Hub.primitives.add(paint);
				System.out.println("Object Added" + paint.toString());
					
					
				for(PaintingThread pt : Hub.painters) {
						
					System.out.println("Painting the new object " + pt.name);
					pt.oos.writeObject(paint);
				
				}
				//}
				
				objects++;
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
	}
	
	
}
