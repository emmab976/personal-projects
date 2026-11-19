package assignment1;

import java.awt.Color;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchByAuthorPanel extends JPanel {
	private static final long serialVersionUID = 1L; 
	
	Vector<Book> bookList = new Vector<Book>();
	Vector<Author> authList = new Vector<Author>();
	Vector<Branch> branches = new Vector<Branch>();
	
	JComboBox<Author> authors= new JComboBox<Author>();
	JComboBox<Book> books = new JComboBox<Book>();
	
	JTextField price = new JTextField(10);
	
	DefaultListModel<Branch> inventory = new DefaultListModel<Branch>();
	
	
	public SearchByAuthorPanel() {
		this.setBackground(Color.blue);
		
		inventory.clear();
		

		authList = HenryDAO.getAuthor();
		bookList = HenryDAO.getBooksByAuthor(authList.get(0).getID());
		branches = HenryDAO.getInventory(bookList.get(0).getBookNum());
		
		authors.removeAllItems();
		books.removeAllItems();
		price.removeAll();
		
		for(int i = 0; i<authList.size(); i++) {
			authors.addItem(authList.get(i));
		}
		
		for(int i = 0; i<bookList.size(); i++) {
			books.addItem(bookList.get(i));
		}
		
		for(int i = 0; i<branches.size(); i++) {
			inventory.addElement(branches.get(i));
		}
		
		authors.setSelectedIndex(0);
		books.setSelectedIndex(0);
		
		this.add(authors);
		this.add(books);
		
		price.setText(bookList.get(0).getPrice());
		this.add(price);
		
		JList<Branch> list = new JList<Branch>(inventory);
		this.add(list);
		

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == authors) {
					JComboBox<Author> box = (JComboBox<Author>) e.getSource();
					Author author = (Author)box.getSelectedItem();
					
				
					bookList = HenryDAO.getBooksByAuthor(author.getID());
				
				
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
		

		
		authors.addActionListener(listener);
		books.addActionListener(listener);
		
	
		this.add(list);
	}

}
