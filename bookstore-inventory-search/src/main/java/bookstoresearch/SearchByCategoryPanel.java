package bookstoresearch;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchByCategoryPanel extends JPanel {
	private static final long serialVersionUID = 1L; 
	
	Vector<Book> bookList = new Vector<Book>();
	Vector<String> categories = new Vector<String>();
	Vector<Branch> branches = new Vector<Branch>();
	
	JComboBox<String> genres = new JComboBox<String>();
	JComboBox<Book> books = new JComboBox<Book>();
	
	JTextField price = new JTextField(10);
	
	DefaultListModel<Branch> inventory = new DefaultListModel<Branch>();
	
	
	public SearchByCategoryPanel() {
		this.setBackground(Color.blue);
		
		inventory.clear();
		

		categories = HenryDAO.getBookGenres();
		bookList = HenryDAO.getBooksByCategory(categories.get(0));
		branches = HenryDAO.getInventory(bookList.get(0).getBookNum());
		
		genres.removeAllItems();
		books.removeAllItems();
		price.removeAll();
		
		for(int i = 0; i<categories.size(); i++) {
			genres.addItem(categories.get(i));
		}
		
		for(int i = 0; i<bookList.size(); i++) {
			books.addItem(bookList.get(i));
		}
		
		for(int i = 0; i<branches.size(); i++) {
			inventory.addElement(branches.get(i));
		}
		
		genres.setSelectedIndex(0);
		books.setSelectedIndex(0);
		
		this.add(genres);
		this.add(books);
		
		price.setText(bookList.get(0).getPrice());
		this.add(price);
		
		JList<Branch> list = new JList<Branch>(inventory);
		this.add(list);
		

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == genres) {
					JComboBox<String> box = (JComboBox<String>) e.getSource();
					String genre = (String)box.getSelectedItem();
					
				
					bookList = HenryDAO.getBooksByCategory(genre);
				
				
					books.removeAllItems();
					for (int i=0; i<bookList.size(); i++) {
						books.addItem(bookList.get(i));
					}
				
					books.setSelectedIndex(0);
					price.setText(bookList.get(0).getPrice());
				}
				
				else if(e.getSource() == books) {
					JComboBox<Book> box = (JComboBox<Book>) e.getSource();
					Book newBook = (Book)box.getSelectedItem();
					int i = box.getSelectedIndex();
							
					price.setText(bookList.get(i).getPrice());
					inventory.clear();
					
					branches = HenryDAO.getInventory(newBook.getBookNum());
					System.out.println("Location: "+ branches.get(0));
					
					for(int j = 0; j<branches.size(); j++) {
						inventory.addElement(branches.get(j));
					}
					
				}
			}
		
		};
		

		
		genres.addActionListener(listener);
		books.addActionListener(listener);
		
	
		this.add(list);
	}

}