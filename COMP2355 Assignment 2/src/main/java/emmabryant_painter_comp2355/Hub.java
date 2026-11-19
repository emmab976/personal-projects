package emmabryant_painter_comp2355;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Hub{
	static ArrayList<PaintingPrimitive> primitives = new ArrayList<PaintingPrimitive>();
	static ArrayList<PaintingThread> painters = new ArrayList<PaintingThread>();
	static ArrayList<ObjectOutputStream> outputs =  new ArrayList<ObjectOutputStream>();
	static int numPainters = 0;
	
	public static void main(String[] args) throws IOException{


		
		ServerSocket ss = new ServerSocket(7000);
		Socket s;

		while(true) {
			
			s = ss.accept();
			
			
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			
			outputs.add(oos);
			
			oos.writeObject(primitives);
			
			String name = "";
			
			try {
				name = (String)ois.readObject();

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			PaintingThread pt = new PaintingThread(s, name, ois, oos);
			
			Thread t = new Thread(pt);
			
			painters.add(pt);
			
			t.start();
			
			
			numPainters++;
			
		}
		

	}

}
