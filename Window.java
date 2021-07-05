package com.database;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.poi.ss.formula.functions.Mode;

import javafx.scene.control.Alert;

@SuppressWarnings("unused")
public class Window implements ActionListener{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = (int)screenSize.getWidth(), HEIGHT = 700;
	private int tabSelect = 0;

	private boolean allowed = true, allowed1 = true;
	
	//alert message setup
	Object[] ok = {"OK"};
	
	private JFrame frame;
	private JPanel panel, cardPanel, mainPanel;
	
	private CardLayout cl;
	
	private JLabel timeAndDate;
	
	private JLabel dateLabel;
	private JTextField dateTextField;

	private JLabel jobSiteLabel;
	private JTextField jobSiteTextField;
	
	private JLabel drNumberLabel;
	private JTextField drNumberTextField;
	
	private JLabel itemCodeLabel;
	private JTextField itemCodeTextField;
	
	private JLabel itemDescriptionLabel;
	private JTextField itemDescriptionTextField;
	
	private JLabel UOMLabel;
	private JTextField UOMTextField;

	private JLabel quantityLabel;
	private JTextField quantityTextField;
	
	private JLabel deliveredByLabel;
	private JTextField deliveredByTextField;

	private JLabel receivedByLabel;
	private JTextField receivedByTextField;

	private JLabel amountLabel;
	private JTextField amountTextField;

	private JLabel totalAmountLabel;
	private JTextField totalAmountTextField;
	
	private JButton discardButton;
	private JButton editButton;
	private JButton saveButton;
	private JButton deleteButton;
	
	private JButton nightModeButton;
	private boolean nightMode = false;
	
	private JTextField filterBar;
	private JButton filterButton;
	private JButton refreshButton;
	
	//Table
	private LinkedList<String> list;
	private JTable mainTable;
	private DefaultTableModel model;
	private DefaultTableCellRenderer centerRenderer;
	private Object[] columns = {"Date", "Job Site", "DR#", "Item Code", "Item Description", "Quantity", "UOM", "Delivered By", "Received By", "Amount",  "Total Amount"};
	private Object[] rows;
	private JScrollPane scrollPane;
	
	private TableColumn column[];

	
	private int[] selectedValue = new int[2];
	private String[] columnNames = {"_Date", "_JobSite", "_DR", "_ItemCode", "_ItemDescription", "_Quantity", "_UOM", "_DeliveredBy", "_ReceivedBy", "_Amount",  "_TotalAmount"};

	
	//Settings options 
	//prefs
	private Color white = Color.WHITE, black = Color.BLACK, gray = Color.GRAY, green = Color.GREEN, orange = Color.ORANGE, cyan = Color.CYAN, red = Color.RED;
	

	private Font normalFont = new Font("Arial", 0, 12);
	private Font largeFont = new Font("Arial", 1, 20);
	
	Main main;
	DatabaseManager databaseManager;
	public Window(Main main, DatabaseManager databaseManager) {
		this.main = main;
		this.databaseManager = databaseManager;
		
		frame = new JFrame("Database");
		initComponents();
	}
	
	private void initComponents() {
		AddPanel();	
		AddCardPanel();
		AddMainPanel();

		//Time and Date
		AddTimeAndDate();
		ShowDate();
		
		//Labels
		AddDate();
		AddJobSite();
		AddDRNumber();
		AddItemCode();
		AddItemDescription();
		AddUOM();
		AddQuantity();
		AddDeliveredBy();
		AddReceivedBy();
		AddAmountLabel();
		
		AddDiscardButton();
		AddEditButton();
		AddDeleteButton();
		AddSaveButton();
		
		AddFilterBar();
		AddFilterButton();
		AddRefreshButton();
		
		AddNightModeButton();
		
		AddMainTable();
		
		AddFrame();
	}

	private void AddFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setFocusable(true);
		
		AddKeyListener(frame);
		
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Component comp = (Component)e.getSource();
				ResizeJTable(comp);
			}
		});
		
		cl.show(cardPanel, "1");
	}
	
	public void AddKeyListener(Component comp) {
		comp.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(SaveButton()) {
						DiscardButton(frame.getFocusOwner());
					}
				}
			}
		});
	}
	
	public void ResizeJTable(Component comp) {
		scrollPane.setBounds(220, 50, comp.getWidth() - 250, 600);
		nightModeButton.setBounds(comp.getWidth() - 155, 10, 125, 30);
	}
	
	public void AddPanel() {
		panel = new JPanel();
		panel.setBackground(white);
		panel.setLayout(null);
		frame.add(panel);
	}
	
	public void AddCardPanel() {
		cl = new CardLayout();
		
		cardPanel = new JPanel();
		cardPanel.setLayout(cl);
		cardPanel.setBounds(0, 0, WIDTH, HEIGHT);
		panel.add(cardPanel);
		
	}
	
	public void AddMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBackground(white);
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, WIDTH,  HEIGHT);
		
		cardPanel.add(mainPanel, "1");
	}
	
	public void AddTimeAndDate() {
		timeAndDate = new JLabel();
		timeAndDate.setBounds(10, 0, 550, 50);
		timeAndDate.setFont(largeFont);
		
		mainPanel.add(timeAndDate);
	}
	
	public void AddDate() {
		dateLabel = new JLabel("Date");
		dateLabel.setBounds(10, 40, 100, 50);
//		dateLabel.setFont(normalFont);
		
		dateTextField = new JTextField("");
		dateTextField.setHorizontalAlignment(SwingConstants.CENTER);
		dateTextField.setBackground(white);
		dateTextField.setFont(normalFont);
		dateTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		dateTextField.setBounds(10, 75, 200, 25);
		
		AddKeyListener(dateTextField);
		
		mainPanel.add(dateLabel);
		mainPanel.add(dateTextField);
	}
	
	public void AddJobSite() {
		jobSiteLabel = new JLabel("Job Site");
		jobSiteLabel.setBounds(10, 90, 100, 50);
//		dateLabel.setFont(normalFont);
		
		jobSiteTextField = new JTextField("");
		jobSiteTextField.setHorizontalAlignment(SwingConstants.CENTER);
		jobSiteTextField.setBackground(white);
		jobSiteTextField.setFont(normalFont);
		jobSiteTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		jobSiteTextField.setBounds(10, 125, 200, 25);
		
		AddKeyListener(jobSiteTextField);
		
		mainPanel.add(jobSiteLabel);
		mainPanel.add(jobSiteTextField);
	}
	
	public void AddDRNumber() {
		drNumberLabel = new JLabel("DR#");
		drNumberLabel.setBounds(10, 140, 100, 50);
//		dateLabel.setFont(normalFont);
		
		drNumberTextField = new JTextField("");
		drNumberTextField.setHorizontalAlignment(SwingConstants.CENTER);
		drNumberTextField.setBackground(white);
		drNumberTextField.setFont(normalFont);
		drNumberTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		drNumberTextField.setBounds(10, 175, 200, 25);
		
		AddKeyListener(drNumberTextField);
		
		mainPanel.add(drNumberLabel);
		mainPanel.add(drNumberTextField);
	}
	
	public void AddItemCode() {
		itemCodeLabel = new JLabel("Item Code");
		itemCodeLabel.setBounds(10, 190, 100, 50);
//		dateLabel.setFont(normalFont);
		
		itemCodeTextField = new JTextField("");
		itemCodeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		itemCodeTextField.setBackground(white);
		itemCodeTextField.setFont(normalFont);
		itemCodeTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		itemCodeTextField.setBounds(10, 225, 200, 25);
		
		AddKeyListener(itemCodeTextField);
		
		mainPanel.add(itemCodeLabel);
		mainPanel.add(itemCodeTextField);
	}
	
	public void AddItemDescription() {
		itemDescriptionLabel = new JLabel("Item Description");
		itemDescriptionLabel.setBounds(10, 240, 100, 50);
//		dateLabel.setFont(normalFont);
		
		itemDescriptionTextField = new JTextField("");
		itemDescriptionTextField.setHorizontalAlignment(SwingConstants.CENTER);
		itemDescriptionTextField.setBackground(white);
		itemDescriptionTextField.setFont(normalFont);
		itemDescriptionTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		itemDescriptionTextField.setBounds(10, 275, 200, 25);
		
		AddKeyListener(itemDescriptionTextField);
		
		mainPanel.add(itemDescriptionLabel);
		mainPanel.add(itemDescriptionTextField);
	}
	
	public void AddQuantity() {
		quantityLabel = new JLabel("Quantity");
		quantityLabel.setBounds(10, 290, 100, 50);
//		dateLabel.setFont(normalFont);
		
		quantityTextField = new JTextField("");
		quantityTextField.setHorizontalAlignment(SwingConstants.CENTER);
		quantityTextField.setBackground(white);
		quantityTextField.setFont(normalFont);
		quantityTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		quantityTextField.setBounds(10, 325, 200, 25);
		
		AddKeyListener(quantityTextField);
		
		quantityTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String value = quantityTextField.getText();
				if(DigitCheck(value)) {
					try {
						NightMode(quantityTextField);
						double quantity = Double.parseDouble(value);
						
						if(quantity < 0 || value.equals("")) {
							JOptionPane.showMessageDialog(null, "Invalid Input");
							quantityTextField.setText("");
							InvalidInput("Invalid Input (negative value)", quantityTextField);
						}
						allowed = true;
					}
					catch(Exception ee){
						if(!value.equals("")) {
							quantityTextField.setBackground(red);
							quantityTextField.setForeground(white);
						}
					}
				}
				else {
					allowed = false;
					quantityTextField.setBackground(red);
					quantityTextField.setForeground(white);
				}
			}
		});
		
		mainPanel.add(quantityLabel);
		mainPanel.add(quantityTextField);
	}
	
	public void AddUOM() {
		UOMLabel = new JLabel("UOM");
		UOMLabel.setBounds(10, 340, 100, 50);
//		dateLabel.setFont(normalFont);
		
		UOMTextField = new JTextField("");
		UOMTextField.setHorizontalAlignment(SwingConstants.CENTER);
		UOMTextField.setBackground(white);
		UOMTextField.setFont(normalFont);
		UOMTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		UOMTextField.setBounds(10, 375, 200, 25);
		
		AddKeyListener(UOMTextField);
		
		mainPanel.add(UOMLabel);
		mainPanel.add(UOMTextField);
	}
	
	public void AddDeliveredBy() {
		deliveredByLabel = new JLabel("Delivered By");
		deliveredByLabel.setBounds(10, 390, 100, 50);
//		dateLabel.setFont(normalFont);
		
		deliveredByTextField = new JTextField("");
		deliveredByTextField.setHorizontalAlignment(SwingConstants.CENTER);
		deliveredByTextField.setBackground(white);
		deliveredByTextField.setFont(normalFont);
		deliveredByTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		deliveredByTextField.setBounds(10, 425, 200, 25);
		
		AddKeyListener(deliveredByTextField);
		
		mainPanel.add(deliveredByLabel);
		mainPanel.add(deliveredByTextField);
	}
	
	public void AddReceivedBy() {
		receivedByLabel = new JLabel("Received By");
		receivedByLabel.setBounds(10, 440, 100, 50);
//		dateLabel.setFont(normalFont);
		
		receivedByTextField = new JTextField("");
		receivedByTextField.setHorizontalAlignment(SwingConstants.CENTER);
		receivedByTextField.setBackground(white);
		receivedByTextField.setFont(normalFont);
		receivedByTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		receivedByTextField.setBounds(10, 475, 200, 25);
		
		AddKeyListener(receivedByTextField);
		
		mainPanel.add(receivedByLabel);
		mainPanel.add(receivedByTextField);
	}
	
	public void AddAmountLabel() {
		amountLabel = new JLabel("Amount");
		amountLabel.setBounds(10, 490, 100, 50);
//		dateLabel.setFont(normalFont);
		
		amountTextField = new JTextField("");
		amountTextField.setHorizontalAlignment(SwingConstants.CENTER);
		amountTextField.setBackground(white);
		amountTextField.setFont(normalFont);
		amountTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		amountTextField.setBounds(10, 525, 200, 25);
		
		AddKeyListener(amountTextField);
		
		amountTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String value = amountTextField.getText();
				if(DigitCheck(value)) {
					try {
						NightMode(amountTextField);
						double quantity = Double.parseDouble(value);
						
						if(quantity < 0 || value.equals("")) {
							JOptionPane.showMessageDialog(null, "Invalid Input");
							amountTextField.setText("");
							InvalidInput("Invalid Input (negative value)", amountTextField);
						}
						allowed1 = true;
					}
					catch(Exception ee){
						if(!value.equals("")) {
							amountTextField.setBackground(red);
							amountTextField.setForeground(white);
						}
					}
				}
				else {
					allowed1 = false;
					amountTextField.setBackground(red);
					amountTextField.setForeground(white);
				}
			}
		});
		
		mainPanel.add(amountLabel);
		mainPanel.add(amountTextField);
	}
	
	public void InvalidInput(String message, JTextField textField) {
		Color defBColor = textField.getBackground();
		Color defFColor = textField.getForeground();
		
		textField.setBackground(red);
		textField.setForeground(white);
		
		if(AlertMessage(message) == 0) {
			textField.setBackground(defBColor);
			textField.setForeground(defFColor);
			textField.setText("");
		}
	}
	
	//Buttons
	public void AddSaveButton() {
		saveButton = new JButton("Add / Save");
		saveButton.setBounds(115, 610, 95, 40);
		saveButton.setBackground(white);
		saveButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		saveButton.setBackground(green);
		saveButton.setForeground(black);
		saveButton.addActionListener(this);
		mainPanel.add(saveButton);
	}
	public void AddDeleteButton() {
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(10, 610, 95, 40);
		deleteButton.setBackground(white);
		deleteButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		deleteButton.setBackground(red);
		deleteButton.setForeground(white);
		deleteButton.addActionListener(this);
		mainPanel.add(deleteButton);
	}
	
	public void AddDiscardButton() {
		discardButton = new JButton("Discard");
		discardButton.setBounds(10, 560, 95, 40);
		discardButton.setBackground(white);
		discardButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		discardButton.setBackground(orange);
		discardButton.setForeground(black);
		discardButton.addActionListener(this);
		mainPanel.add(discardButton);
	}
	public void AddEditButton() {
		editButton = new JButton("Edit");
		editButton.setBounds(115, 560, 95, 40);
		editButton.setBackground(white);
		editButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		editButton.setBackground(cyan);
		editButton.setForeground(black);
		editButton.addActionListener(this);
		mainPanel.add(editButton);
	}
	
	public void AddNightModeButton() {
		nightModeButton = new JButton("Night Mode : OFF");
		nightModeButton.setBounds(WIDTH - 155, 10, 125, 30);
		nightModeButton.setBackground(white);
		nightModeButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, green));
		nightModeButton.setBackground(black);
		nightModeButton.setForeground(white);
		nightModeButton.addActionListener(this);
		mainPanel.add(nightModeButton);
	}
	
	//Table
	@SuppressWarnings("serial")
	public void AddMainTable() {
		list = new LinkedList<String>();
		mainTable = new JTable();
		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setColumnIdentifiers(columns);
		
		scrollPane = new JScrollPane(mainTable);
		
		mainTable.setModel(model);
		mainTable.setRowHeight(25);
		mainTable.setFont(new Font("Arial", 1, 12));

		mainTable.getTableHeader().setReorderingAllowed(false);
		mainTable.getTableHeader().setResizingAllowed(true);
		
		mainTable.setOpaque(false);
		scrollPane.getViewport().setBackground(Color.LIGHT_GRAY);
		
		scrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		
		scrollPane.setBounds(220, 50, WIDTH - 250, 600);
		
		column = new TableColumn[columns.length];
		rows = new Object[columns.length];

		for(int i = 0; i < columns.length; i++) {
			mainTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		for(int i = 0; i < column.length; i++) {
			column[i] = mainTable.getColumnModel().getColumn(i);
		}
		
		column[0].setPreferredWidth(75);
		column[1].setPreferredWidth(140);
		column[2].setPreferredWidth(50);
		column[3].setPreferredWidth(50);
		column[4].setPreferredWidth(140);
		column[5].setPreferredWidth(30);
		column[6].setPreferredWidth(30);
		column[7].setPreferredWidth(140);
		column[8].setPreferredWidth(100);
		column[9].setPreferredWidth(50);
		column[10].setPreferredWidth(75);
		
		RefreshButton();
		
		mainPanel.add(scrollPane);
		
		AddKeyListener(mainTable);
		
		mainTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int index = mainTable.getSelectedRow();
				
				selectedValue[0] = index;
				selectedValue[1] = mainTable.getSelectedColumn();
				
				dateTextField.setText(model.getValueAt(index, 0).toString());
				jobSiteTextField.setText(model.getValueAt(index, 1).toString());
				drNumberTextField.setText(model.getValueAt(index, 2).toString());
				itemCodeTextField.setText(model.getValueAt(index, 3).toString());
				itemDescriptionTextField.setText(model.getValueAt(index, 4).toString());
				quantityTextField.setText(model.getValueAt(index, 5).toString());
				UOMTextField.setText(model.getValueAt(index, 6).toString());
				deliveredByTextField.setText(model.getValueAt(index, 7).toString());
				receivedByTextField.setText(model.getValueAt(index, 8).toString());
				amountTextField.setText(model.getValueAt(index, 9).toString());
				
				filterBar.setText(model.getValueAt(index, mainTable.getSelectedColumn()).toString());
			}
		});
	}
	
	//FILTER
	public void AddFilterBar() {
		filterBar = new JTextField();
		filterBar.setHorizontalAlignment(SwingConstants.CENTER);
		filterBar.setBackground(white);
		filterBar.setFont(normalFont);
		filterBar.setFont(new Font("Arial", 1, 15));
		filterBar.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		filterBar.setBounds((WIDTH / 2) - (250 / 2), 20, 250, 25);
		
		mainPanel.add(filterBar);
	}
	
	public void AddFilterButton() {
		filterButton = new JButton("Search");
		filterButton.setBounds(WIDTH - 550, 20, 50, 25);
		filterButton.setBackground(white);
		filterButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		filterButton.setBackground(green);
		filterButton.setForeground(black);
		filterButton.addActionListener(this);
		mainPanel.add(filterButton);
	}
	
	public void AddRefreshButton() {
		refreshButton = new JButton("Refresh");
		refreshButton.setBounds(WIDTH - 490, 20, 50, 25);
		refreshButton.setBackground(white);
		refreshButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		refreshButton.setBackground(cyan);
		refreshButton.setForeground(black);
		refreshButton.addActionListener(this);
		mainPanel.add(refreshButton);
	}
	
	public void RefreshButton() {
		filterBar.setText("");
		model.setRowCount(0);
		list.removeAll(list);
		databaseManager.GetDatabase((DefaultTableModel)mainTable.getModel(), list);
	}
	
	//Getters
	public JLabel GetJLabel() {
		return dateLabel;
	}

	public int AlertMessage(String message) {
		return JOptionPane.showOptionDialog(frame, message, "Error", JOptionPane.YES_OPTION, JOptionPane.ERROR_MESSAGE, null, ok, ok);
	}
	
	int ctr = 0;
	public void ShowDate() {
		new Timer(500,(ActionEvent e) -> {
			Date d = new Date();
			SimpleDateFormat s = new SimpleDateFormat("EEEEEEEE | MMMMMMMMM dd, yyyy | MM/dd/yy | hh:mm:ss a");
			SimpleDateFormat ss = new SimpleDateFormat("MMMMMMMMM dd, yyyy");
			timeAndDate.setText(s.format(d));
		}).start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == saveButton) {
			if(SaveButton()) {
				DiscardButton(dateTextField);
			}
		}
		else if(e.getSource() == deleteButton) {
			DeleteButton();
		}
		else if(e.getSource() == editButton) {
			EditButton();
		}
		else if(e.getSource() == discardButton) {
			DiscardButton(dateTextField);
		}
		else if(e.getSource() == nightModeButton) {
			NightModeButton();
		}
		else if(e.getSource() == filterButton) {
//			new FilterFrame(WIDTH, filterBar.getText(), (DefaultTableModel)mainTable.getModel());
			FilterButton();
		}
		else if(e.getSource() == refreshButton) {
			RefreshButton();
		}
		
	}
	
	public void FilterButton() {
		model.setRowCount(0);
		list.removeAll(list);
		
		databaseManager.GetSpecificDatabase(columnNames[selectedValue[1]], filterBar.getText(), (DefaultTableModel)mainTable.getModel(), list);
	}
	
	public boolean SaveButton() {
		try {
			rows[0] = StringCheck(dateTextField.getText());
			rows[1] = StringCheck(jobSiteTextField.getText());
			rows[2] = StringCheck(drNumberTextField.getText());
			rows[3] = StringCheck(itemCodeTextField.getText());
			rows[4] = StringCheck(itemDescriptionTextField.getText());
			rows[5] = StringCheck(quantityTextField.getText());
			rows[6] = StringCheck(UOMTextField.getText());
			rows[7] = StringCheck(deliveredByTextField.getText());
			rows[8] = StringCheck(receivedByTextField.getText());
			rows[9] = StringCheck(amountTextField.getText());
			
			double amount = Double.parseDouble(amountTextField.getText());
			double quantity = Double.parseDouble(quantityTextField.getText());
			rows[10] = GetTotalAmount(amount, quantity);
			
			if(allowed && allowed1) {
				model.addRow(rows);
			}
			else {
				AlertMessage("Invalid Input");
				allowed = true;
				allowed1 = true;
			}
			
			mainTable.scrollRectToVisible(mainTable.getCellRect(mainTable.getRowCount() - 1, 0, true));
			
			databaseManager.AddDatabase((DefaultTableModel)mainTable.getModel(), timeAndDate.getText(), list);
			
			return true;
		}
		catch(Exception ee) {
			AlertMessage("Error Adding table : " + ee.getMessage());
		}
		finally {
			dateTextField.requestFocus();
		}
		
		return false;
	}
	
	public String StringCheck(String value) {
		if(!value.equals("")) {
			return value;
		}
		return "null";
	}
	
	public void DeleteButton() {
		int index = mainTable.getSelectedRow();
		if(index >= 0) {
			if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete index : " + (index + 1)) == 0) {
				if(databaseManager.DeleteDatabase(list.get(index))) {
					list.remove(index);
					model.removeRow(index);
					mainPanel.requestFocus();
					AlertMessage("DELETE SUCCESSFUL");
				}
			}
		}
		else {
			AlertMessage("No Index Selected");
		}
	}
	
	public void EditButton() {
		int index = mainTable.getSelectedRow();
		
		if(index >= 0) {
			if(JOptionPane.showConfirmDialog(null, "Are you sure you want to Edit index : " + (index + 1)) == 0) {
			
				model.setValueAt(dateTextField.getText(), index, 0);
				model.setValueAt(jobSiteTextField.getText(), index, 1);
				model.setValueAt(drNumberTextField.getText(), index, 2);
				model.setValueAt(itemCodeTextField.getText(), index, 3);
				model.setValueAt(itemDescriptionTextField.getText(), index, 4);
				model.setValueAt(quantityTextField.getText(), index, 5);
				model.setValueAt(UOMTextField.getText(), index, 6);
				model.setValueAt(deliveredByTextField.getText(), index, 7);
				model.setValueAt(receivedByTextField.getText(), index, 8);
				model.setValueAt(amountTextField.getText(), index, 9);
				
				double amount = Double.parseDouble(amountTextField.getText());
				double quantity = Double.parseDouble(quantityTextField.getText());
				
				model.setValueAt(GetTotalAmount(amount, quantity), index, 10);
				
				if(databaseManager.EditDatabase((DefaultTableModel)mainTable.getModel(), list.get(index), index)) {
					JOptionPane.showMessageDialog(null, "Edit Succesful");
				}
			}
		}
		else {
			AlertMessage("No Index Selected");
		}
		
		mainPanel.requestFocus();
	}
	
	public void DiscardButton(Component comp) {
		dateTextField.setText("");
		jobSiteTextField.setText("");
		drNumberTextField.setText("");
		itemCodeTextField.setText("");
		itemDescriptionTextField.setText("");
		UOMTextField.setText("");
		quantityTextField.setText("");
		deliveredByTextField.setText("");
		receivedByTextField.setText("");
		amountTextField.setText("");
		
		comp.requestFocus();
	}
	
	public void NightModeButton() {
		nightMode = !nightMode;
		NightMode(mainPanel);
		NightMode(timeAndDate);
		NightMode(nightModeButton);
		NightMode(dateLabel);
//		NightMode(dateTextField);
		NightMode(jobSiteLabel);
//		NightMode(jobSiteTextField);
		NightMode(drNumberLabel);
		NightMode(itemCodeLabel);
		NightMode(itemDescriptionLabel);
		NightMode(UOMLabel);
		NightMode(quantityLabel);
		NightMode(deliveredByLabel);
		NightMode(receivedByLabel);
		NightMode(amountLabel);
		
		NightMode(scrollPane.getViewport());
		if(nightMode) {
			scrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			dateTextField.setBackground(black);
			dateTextField.setForeground(white);
			dateTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			jobSiteTextField.setBackground(black);
			jobSiteTextField.setForeground(white);
			jobSiteTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			drNumberTextField.setBackground(black);
			drNumberTextField.setForeground(white);
			drNumberTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			itemCodeTextField.setBackground(black);
			itemCodeTextField.setForeground(white);
			itemCodeTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			itemDescriptionTextField.setBackground(black);
			itemDescriptionTextField.setForeground(white);
			itemDescriptionTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			UOMTextField.setBackground(black);
			UOMTextField.setForeground(white);
			UOMTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			quantityTextField.setBackground(black);
			quantityTextField.setForeground(white);
			quantityTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			deliveredByTextField.setBackground(black);
			deliveredByTextField.setForeground(white);
			deliveredByTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			receivedByTextField.setBackground(black);
			receivedByTextField.setForeground(white);
			receivedByTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			amountTextField.setBackground(black);
			amountTextField.setForeground(white);
			amountTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			filterBar.setBackground(black);
			filterBar.setForeground(white);
			filterBar.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			
			filterButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, green));
			
			saveButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			deleteButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			editButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
			discardButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, white));
		}
		else {
			scrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));	
			
			dateTextField.setBackground(white);
			dateTextField.setForeground(black);
			dateTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			jobSiteTextField.setBackground(white);
			jobSiteTextField.setForeground(black);
			jobSiteTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			drNumberTextField.setBackground(white);
			drNumberTextField.setForeground(black);
			drNumberTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			itemCodeTextField.setBackground(white);
			itemCodeTextField.setForeground(black);
			itemCodeTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			itemDescriptionTextField.setBackground(white);
			itemDescriptionTextField.setForeground(black);
			itemDescriptionTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			UOMTextField.setBackground(white);
			UOMTextField.setForeground(black);
			UOMTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			quantityTextField.setBackground(white);
			quantityTextField.setForeground(black);
			quantityTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			deliveredByTextField.setBackground(white);
			deliveredByTextField.setForeground(black);
			deliveredByTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			receivedByTextField.setBackground(white);
			receivedByTextField.setForeground(black);
			receivedByTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			amountTextField.setBackground(white);
			amountTextField.setForeground(black);
			amountTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));

			filterBar.setBackground(white);
			filterBar.setForeground(black);
			filterBar.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			filterButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			
			saveButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			deleteButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			editButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
			discardButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, black));
		}
		
		nightModeButton.setText(nightMode? "NightMode : ON" : "NightMode : OFF");
		
		frame.requestFocus();
	}
	
	
	
	public void NightMode(Component component) {
		Color bcolor, fcolor;
		if(nightMode) {
			bcolor = Color.BLACK;
			fcolor = Color.WHITE;
		}
		else {
			bcolor = Color.WHITE;
			fcolor = Color.BLACK;
		}
		component.setBackground(bcolor);
		component.setForeground(fcolor);
	}
	
	public boolean DigitCheck(String value) {
		char[] digits = value.toCharArray();
		
		for(char digit : digits) {
			if(!Character.isDigit(digit) && digit != '.') {
				return false;
			}
		}
		return true;
	}
	
	public String GetTotalAmount(double a, double b) {
		try {
			double totalAmount = a * b;
			
			String value = String.valueOf(totalAmount);
			String[] splitValue = value.split("\\.");
			
			char[] tens = splitValue[0].toCharArray();
			
			StringBuilder totalValue = new StringBuilder();
			
			for(int i = tens.length - 1, ctr = 0; i >= 0; i--, ctr++) {
				if(ctr % 2 == 0 && ctr != 0 && i != 0) {
					totalValue.append(tens[i] + ",");
					ctr = -1;
				}
				else {
					totalValue.append(tens[i]);
				}
			}
			totalValue.reverse();
			totalValue.append("." + splitValue[1]);
			
			return totalValue.toString();
		}
		catch(Exception ee) {
			allowed = false;
			AlertMessage("Invalid Input");
		}
		return "null";
	}
}
