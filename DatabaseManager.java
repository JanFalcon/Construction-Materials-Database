package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DatabaseManager {
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;
	private Statement statement;
	private Connection con;
	
	private Object[] row;
	
	public void GetConnection() {
		try {
			con = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\Asus\\Desktop\\Construction_Materials_Database.accdb");
			statement = con.createStatement();
		}
		catch(Exception ee) {
			ee.printStackTrace();
		}
	}
	
	public void CloseConnection(boolean prepare) {
		if(con != null) {
			try {
				con.close();
				if(prepare) {
					preparedStatement.close();
				}
			} 
			catch (SQLException ee) {
				JOptionPane.showMessageDialog(null, "Error Closing Database : " + ee.getMessage());
			}
		}
	}
	
	public void GetDatabase(DefaultTableModel table, LinkedList<String> list) {
		try {
			GetConnection();
			
//			resultSet = pr
//			resultSet = statement.executeQuery("Select * from Database ORDER BY _TimeRecorded");
			String sql = "Select * from Database ORDER BY _Date DESC";
			
			preparedStatement = con.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			
			row = new Object[11];
			
			while(resultSet.next()) {
//				row[0] = resultSet.getString(12);
				row[0] = resultSet.getDate(12);
				row[1] = resultSet.getString(2);
				row[2] = resultSet.getString(3);
				row[3] = resultSet.getString(4);
				row[4] = resultSet.getString(5);
				row[5] = resultSet.getString(6);
				row[6] = resultSet.getString(7);
				row[7] = resultSet.getString(8);
				row[8] = resultSet.getString(9);
				row[9] = resultSet.getString(10);
				row[10] = resultSet.getString(11);
				list.add(resultSet.getString(12));
				table.addRow(row);
			}
			CloseConnection(false);
		}
		catch (Exception ee) {
			JOptionPane.showMessageDialog(null, "Error Getting Database : " + ee.getMessage());
		}
	}
	
	public void AddDatabase(DefaultTableModel modelicious, String time, LinkedList<String> list) {
		int columns = modelicious.getColumnCount();
		int addedRow = modelicious.getRowCount();
		
		String[] row = new String[columns];
		
		for(int i = 0; i < columns; i++) {
			row[i] = modelicious.getValueAt(addedRow - 1, i).toString();
		}
		
		try {
			GetConnection();
			
			String sql = "INSERT INTO Database  (_Date, _JobSite, _DR, _ItemCode, _ItemDescription, "
					+ "_Quantity, _UOM, _DeliveredBy, _ReceivedBy, _Amount, _TotalAmount, _TimeRecorded)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			preparedStatement = con.prepareStatement(sql);
			
			for(int i = 1; i <= columns; i++) {
				preparedStatement.setString(i, row[i - 1]);
			}
			
			time = time.replace(" ", "");
			
			String[] timeValue = time.split("\\|");
			
			timeValue[2] = timeValue[2].replace("/", "");
			timeValue[3]  = timeValue[3].replace(":", "");
			timeValue[3]  = timeValue[3].replace("AM", "1");
			timeValue[3]  = timeValue[3].replace("PM", "2");

			String dataBaseTime = timeValue[2] + timeValue[3];
			list.add(dataBaseTime);
			
			preparedStatement.setString(12, dataBaseTime);
			preparedStatement.executeUpdate();
		}
		catch(Exception ee) {
			JOptionPane.showMessageDialog(null, "Error Inserting Data : " + ee.getMessage());
		}
		CloseConnection(true);
	}
	
	public boolean DeleteDatabase(String index) {
		GetConnection();
		
		try {
			String sql = "DELETE FROM Database WHERE _TimeRecorded = " + index;
			
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.executeUpdate();
			return true;
		}
		catch(Exception ee) {
			JOptionPane.showMessageDialog(null, "Error Deleting Database : " + ee.getMessage());
			ee.printStackTrace();
		}
		finally {
			CloseConnection(true);
			
		}
		return false;
	}
	
	public boolean EditDatabase(DefaultTableModel modelicious, String data, int index) {
		GetConnection();
		
		int columns = modelicious.getColumnCount();
		
		String[] row = new String[columns];
		
		String sql = "UPDATE Database SET _Date = ?, _JobSite = ?, _DR = ?, "
				+ "_ItemCode = ?, _ItemDescription = ?, _Quantity = ?, _UOM = ?, _DeliveredBy = ?, "
				+ "_ReceivedBy = ?, _Amount = ?, _TotalAmount = ? WHERE _TimeRecorded = " + data;
		
		try {
			preparedStatement = con.prepareStatement(sql);
			
			for(int i = 0; i < columns; i++) {
				row[i] = modelicious.getValueAt(index, i).toString();
				preparedStatement.setString(i + 1, row[i]);
			}
			preparedStatement.executeUpdate();
			
			return true;
		} 
		catch (Exception ee) {
			JOptionPane.showMessageDialog(null, "Error Editing Database : " + ee.getMessage());
		}
		finally {
			CloseConnection(true);
		}
		return false;
	}
	
	public void GetSpecificDatabase(String name, String value, DefaultTableModel table, LinkedList<String> list) {
		GetConnection();
		
		try {
			Object[] row = new Object[11];
			String sql = "SELECT * FROM Database WHERE " + name + " = ? ORDER BY _TimeRecorded";
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, value);
			
			resultSet = preparedStatement.executeQuery();
			
			
			while(resultSet.next()) {
				row[0] = resultSet.getString(1);
				row[1] = resultSet.getString(2);
				row[2] = resultSet.getString(3);
				row[3] = resultSet.getString(4);
				row[4] = resultSet.getString(5);
				row[5] = resultSet.getString(6);
				row[6] = resultSet.getString(7);
				row[7] = resultSet.getString(8);
				row[8] = resultSet.getString(9);
				row[9] = resultSet.getString(10);
				row[10] = resultSet.getString(11);
				list.add(resultSet.getString(12));
				table.addRow(row);
			}
			
		}
		catch(Exception ee) {
			JOptionPane.showMessageDialog(null, "Error Getting Specific Database : " + ee.getMessage());
		}
		finally {
			CloseConnection(true);
		}
		
	}
}
