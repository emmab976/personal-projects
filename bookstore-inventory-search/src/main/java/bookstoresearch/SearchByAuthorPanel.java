package bookstoresearch;

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
	private static final Book NO_BOOKS = new Book("No books found", "", "", "", "", "");
	
	Vector<Book> bookList = new Vector<Book>();
	Vector<Author> authList = new Vector<Author>();
	Vector<Branch> branches = new Vector<Branch>();
	
	JComboBox<Author> authors= new JComboBox<Author>();
	JComboBox<Book> books = new JComboBox<Book>();
	
	JTextField price = new JTextField(10);
	
	DefaultListModel<String> inventory = new DefaultListModel<String>();
	
	
	public SearchByAuthorPanel() {
		this.setBackground(Color.blue);
		
		inventory.clear();
		

		authList = HenryDAO.getAuthor();
		bookList = HenryDAO.getBooksByAuthor(authList.get(0).getID());
		
		authors.removeAllItems();
		books.removeAllItems();
		price.removeAll();
		
		for(int i = 0; i<authList.size(); i++) {
			authors.addItem(authList.get(i));
		}
		
		authors.setSelectedIndex(0);
		
		if(bookList.isEmpty()) {
			books.setEnabled(false);
			books.addItem(NO_BOOKS);
			price.setText("N/A");
			inventory.addElement("No copies in stock");
		}
		else {
			branches = HenryDAO.getInventory(bookList.get(0).getBookNum());
			
			for(int i = 0; i<bookList.size(); i++) {
				books.addItem(bookList.get(i));
			}
			
			for(int i = 0; i<branches.size(); i++) {
				inventory.addElement(branches.get(i).toString());
			}
			
			books.setSelectedIndex(0);
			price.setText(bookList.get(0).getPrice());
		}
		
		this.add(authors);
		this.add(books);
		this.add(price);
		
		JList<String> list = new JList<String>(inventory);
		this.add(list);
		

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == authors) {
					JComboBox<Author> box = (JComboBox<Author>) e.getSource();
					Author author = (Author)box.getSelectedItem();
					
				
					bookList = HenryDAO.getBooksByAuthor(author.getID());
				
				
					books.removeAllItems();
					inventory.clear();
					
					if(bookList.isEmpty()) {
						books.setEnabled(false);
						books.addItem(NO_BOOKS);
						price.setText("N/A");
						inventory.addElement("No copies in stock");
					}
					else {
						books.setEnabled(true);
						for (int i=0; i<bookList.size(); i++) {
							books.addItem(bookList.get(i));
						}
						
						books.setSelectedIndex(0);
						price.setText(bookList.get(0).getPrice());
					}
				}
				
				else if(e.getSource() == books) {
					JComboBox<Book> box = (JComboBox<Book>) e.getSource();
					Book newBook = (Book)box.getSelectedItem();
					
					// Ignore events from clearing the list or from the "No books found" placeholder
					if(newBook == null || !box.isEnabled()) {
						return;
					}
							
					price.setText(newBook.getPrice());
					inventory.clear();
					
					branches = HenryDAO.getInventory(newBook.getBookNum());
					
					if(branches.isEmpty()) {
						inventory.addElement("No copies in stock");
					}
					
					for(int j = 0; j<branches.size(); j++) {
						inventory.addElement(branches.get(j).toString());
					}
					
				}
			}
		
		};
		

		
		authors.addActionListener(listener);
		books.addActionListener(listener);
	}

}
