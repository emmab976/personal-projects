package assignment1;

import java.util.Iterator;
import java.util.Vector;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class driver {
	
	public driver() {
		
		JFrame f = new JFrame();
		f.setSize(500, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel tab1 = new SearchByAuthorPanel();
		JPanel tab2 = new SearchByCategoryPanel();
		JPanel tab3 = new SearchByPublisherPanel();
		// Place the tabs on the tabbed pane
		
		JTabbedPane tabControl = new JTabbedPane();
		tabControl.addTab("Author", tab1);
		tabControl.addTab("Category", tab2);
		tabControl.addTab("Publisher", tab3);
		
		mainPanel.add(tabControl, BorderLayout.CENTER);

		f.setContentPane(mainPanel);

		f.setVisible(true);
	}

	public static void main(String[] args) {

		new driver();
		
	}

}
