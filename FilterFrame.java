package com.database;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class FilterFrame implements ActionListener{

	private JFrame filterFrame;
	private JPanel filterPanel;
	
	private JTextField filterBar;
	
	private JButton closeButton;
	
	private int WIDTH;
	private String value;
	
	private JTable mainTable;
	private DefaultTableModel model;
	private DefaultTableCellRenderer centerRenderer;
	private Object[] columns = {"Date", "Job Site", "DR#", "Item Code", "Item Description", "Quantity", "UOM", "Delivered By", "Received By", "Amount",  "Total Amount", "Recorded"};
//	private Object[] rows;
	private JScrollPane scrollPane;
	
	private TableColumn column[];
	
	private DefaultTableModel modelicious;
	public FilterFrame(int WIDTH, String value, DefaultTableModel modelicious) {
		this.modelicious = modelicious;
		this.WIDTH = WIDTH;
		this.value = value;
		
		initComponents();
	}
	
	private void initComponents() {
		filterFrame = new JFrame("Database");
		
		AddPanel();
		AddFilterBar();
		AddCloseButton();
		
		AddMainTable(modelicious);
		AddJFrame();
	}
	
	private void AddJFrame() {
		filterFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		filterFrame.setSize(WIDTH - 155, 600);
		filterFrame.setLocationRelativeTo(null);
		filterFrame.setResizable(true);
		filterFrame.setVisible(true);
		filterFrame.setFocusable(true);
	}
	
	
	public void AddPanel() {
		filterPanel = new JPanel();
		filterPanel.setBackground(Color.WHITE);
		filterPanel.setLayout(null);

		filterFrame.add(filterPanel);
	}
	
	public void AddFilterBar() {
		filterBar = new JTextField(value);
		
		filterBar.setBounds((WIDTH - 155) / 2 - 350, 20, 700, 25);
		filterBar.setHorizontalAlignment(SwingConstants.CENTER);
		filterBar.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		filterBar.setBackground(Color.WHITE);
		filterBar.setFont(new Font("Arial", 1, 16));
		
		filterPanel.add(filterBar);
	}
	
	public void AddCloseButton() {
		closeButton = new JButton("X");
		closeButton.setBackground(Color.RED);
		closeButton.setForeground(Color.WHITE);
		closeButton.setFont(new Font("Arial", 1, 16));
		closeButton.setBounds(WIDTH - 215, 0, 45, 45);
		closeButton.addActionListener(this);
		
		filterPanel.add(closeButton);
	}

	//Table
		@SuppressWarnings("serial")
		public void AddMainTable(DefaultTableModel modelicious) {
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
			
			scrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
			
			scrollPane.setBounds(10, 50, WIDTH - 190, 500);
			
			column = new TableColumn[columns.length];
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
			column[5].setPreferredWidth(40);
			column[6].setPreferredWidth(30);
			column[7].setPreferredWidth(140);
			column[8].setPreferredWidth(100);
			column[9].setPreferredWidth(50);
			column[10].setPreferredWidth(75);
			
			filterPanel.add(scrollPane);
			
			mainTable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int index = mainTable.getSelectedRow();
					
					filterBar.setText(model.getValueAt(index, mainTable.getSelectedColumn()).toString());
				}
			});
		}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == closeButton) {
			filterFrame.dispose();
		}
	}
}
