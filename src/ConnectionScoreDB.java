

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class ConnectionScoreDB {
	static Connection con= null;
	static String dbTableName = "score201958091";
	static String[] dbTableField = {"std_id","dept","name","kor","eng","com"};

	public static void dropTable(Connection con, String dbTableName) {
		// TODO Auto-generated method stub
		String dropTableSQL = "DROP TABLE if exists " + dbTableName + ";";
		try {
			PreparedStatement dropTable = con.prepareStatement(dropTableSQL);
			dropTable.execute();
			System.out.println("Drop Table (" +dbTableName+") OK!");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Drop Table (" +dbTableName+") ERROR!");
		}
	}

	public static void printList(ArrayList<String> list) {
		// TODO Auto-generated method stub
		for(String item: list)
		{
			System.out.println(item);
		}
	}

	public static ArrayList<String> getDataFromTable(Connection con, String dbTableName) {
		// TODO Auto-generated method stub
		String selectDataSQL="SELECT std_id,dept,name,kor,eng,com FROM "+dbTableName+";";
		ArrayList<String> list=new ArrayList<String>();
		
		try {
			PreparedStatement selectTable = con.prepareStatement(selectDataSQL);
			ResultSet result = selectTable.executeQuery();
			while(result.next()) {
				list.add(dbTableField[0]+": "+result.getInt(dbTableField[0]) +"\t"+
						dbTableField[1]+": " +result.getString(dbTableField[1]) + "\t"+
						dbTableField[2]+": " +result.getString(dbTableField[2]) +"\t"+
						dbTableField[3]+": "+result.getInt(dbTableField[3]) +"\t"+
						dbTableField[4]+": "+result.getInt(dbTableField[4]) +"\t"+
						dbTableField[5]+": "+result.getInt(dbTableField[5]) +"\t"
				);
				
			}
			System.out.println("Select Data from DB Table"+dbTableName+ " OK!");
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\nSelect SQL Error!");
		}
		
		return null;
	}

//	public static void insertDataOnTable(Connection con,String dbTableName, int std_id, String dept, String name, String phone) {
//		// TODO Auto-generated method stub
//		String insertTableSQL="replace INTO "+dbTableName +
//			" (std_id, dept, name, phone) "+
//			" VALUES "+
//			"('"+title+"','"+publisher+"','"+year+"',"+price+");";
//		try {
//			PreparedStatement insertTable = con.prepareStatement(insertTableSQL);
//			insertTable.executeUpdate(); //주의!
//			System.out.println("Insert SQL OK!");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("\nInsert SQL Error!");
//		}
//	}
	public static void insertDataOnTable(Connection con,String dbTableName, int std_id, String dept, String name, int kor,int eng,int com) {
		// TODO Auto-generated method stub
		String insertTableSQL="replace INTO "+dbTableName +
			" (std_id, dept, name, kor, eng, com) values(?,?,?,?,?,?); ";
		try {
			PreparedStatement insertTable = con.prepareStatement(insertTableSQL);
			
			insertTable.setInt(1, std_id);
			insertTable.setString(2, dept);
			insertTable.setString(3, name);
			insertTable.setInt(4, kor);
			insertTable.setInt(5, eng);
			insertTable.setInt(6, com);
			
			insertTable.executeUpdate(); //주의!
			System.out.println("Insert SQL OK!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\nInsert SQL Error!");
		}
	}
	//replace는 중복된 데이터를 지워버리고 새로추가하는 방법이고 update는 삭제하지 않고 데이터를 변경하는 방법이다.
	public static void updateDataOnTable(Connection con,String dbTableName, int std_id, String dept, String name, int kor,int eng,int com) {
		// TODO Auto-generated method stub
		String updateTableSQL="update "+dbTableName + " set "+
			"dept = ?, name = ?, kor = ?, eng = ?, com = ? "+"WHERE std_id = ? ;";
		try {
			PreparedStatement updateTable = con.prepareStatement(updateTableSQL);
			
			updateTable.setString(1, dept);
			updateTable.setString(2, name);
			updateTable.setInt(3, kor);
			updateTable.setInt(4, eng);
			updateTable.setInt(5, com);
			updateTable.setInt(6, std_id);
			
			updateTable.executeUpdate(); //주의!
			System.out.println("Update SQL OK!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\nUpdate SQL Error!");
		}
	}
	public static void deleteFromTable(Connection con,String dbTableName, int std_id) {
		// TODO Auto-generated method stub
		PreparedStatement pstmt;
		String deleteSQL="DELETE FROM "+dbTableName+" WHERE std_id = ? ;";
		try {
			pstmt = con.prepareStatement(deleteSQL);
			pstmt.setInt(1,std_id);
			pstmt.executeUpdate(); //주의!
			System.out.println("delete SQL OK!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\ndelete SQL Error!");
		}
	}
	
	public static void createTable(Connection con, String dbTableNaame) {
		// TODO Auto-generated method stub
		String createTableSQL="create table if not exists "+dbTableName+" ("+
		"std_id INT not null, "+
		"dept varchar(15), "+
		"name varchar(10), "+
		"kor INT, "+
		"eng INT, "+
		"com INT, "+
		"UNIQUE INDEX (std_id), "+ //중복삽입방지
		"primary key(std_id) ) default charset=utf8;";
		//Connection con = makeConnection();
		try {
			PreparedStatement createTable = con.prepareStatement(createTableSQL);
			createTable.execute();
			System.out.println("Create Table (" +dbTableName+") OK!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Create Table (" +dbTableName+") ERROR!");
		}
		
	}

	public static Connection makeConnection() {
		// TODO Auto-generated method stub
		String driver = "com.mysql.cj.jdbc.Driver";
		String hostName  = "sql6.freemysqlhosting.net"; //본인 PC mysql --> "localhost"
		String databaseName="sql6440389";
		String utf8Connection="?useUnicode=true&characterEncoding=utf8";
		String url = "jdbc:mysql://"+hostName+":3306/"+databaseName+utf8Connection;
		String userName="sql6440389";
		String password="NCxKw5xvyv";
		
		//jdbc:mysql://sql6.freemysqlhosting.net:3306(default port)/sql6440389
		//url --> http://www.naver.com:80/index.html
		
		try {
			Class.forName(driver);
			System.out.println("Driver Load OK!");
			Connection con;
			try {
				con = DriverManager.getConnection(url,userName,password);
				System.out.println("MySQL Connection OK!");
				return con;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("\nMySQL URL,userName, password Fail!");
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\n Error Class.forName(driver)!");
		}
		
		return null;
	}

}


