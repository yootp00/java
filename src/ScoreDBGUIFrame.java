

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;

//javafinalexam mysql db
public class ScoreDBGUIFrame extends JFrame implements ActionListener, MouseListener{
	static Connection con=null;
	static String dbTableName="score201958091";
	PreparedStatement pstmt;
	ArrayList<String> list;
	public JTable table;
	private DefaultTableModel model;
	private Vector title;
	private Vector result;
	ResultSet resultdata;
	String[] lbText = {"�й�","�а�(��)��","�̸�","����","����","����"};
	public JLabel[] lb = new JLabel[lbText.length];
	public JTextField[] tf = new JTextField[lbText.length];
	String[] btnText = {"����(Insert)","����(Delete)","����(Update)","�ʱ�ȭ(Clear)","����(Prev.)",
			"����(Next)","������ ����(Reset)","������(Dev.)"};
	public JButton[] btn = new JButton[btnText.length];
	
	String[] lbTextSouth = {"�̸�","����","���","�ְ���","������"};
	public JLabel[] lbSouth = new JLabel[lbTextSouth.length];
	public JTextField[] tfSouth = new JTextField[lbTextSouth.length];
	
	public JPanel panelWest = new JPanel();
	public JPanel panelSouth = new JPanel();
	
	public ScoreDBGUIFrame(){
		con = ConnectionScoreDB.makeConnection();
		ConnectionScoreDB.createTable(con, dbTableName);
		list = ConnectionScoreDB.getDataFromTable(con, dbTableName);
		ConnectionScoreDB.printList(list);
		
		this.setTitle("Score DB GUI : 201958091 ������");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(250,300);
		
		// 1�ܰ� : JTable --> JFrame
		result = new Vector();
		title =  new Vector();
		
		for(int i=0;i<lbText.length;i++)
		{
			title.add(lbText[i]);
		}
		
		result = selectFromDB(con,dbTableName); // select * from ...
		
		model = new DefaultTableModel(result,title);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		this.add(sp,BorderLayout.CENTER);
		
		
		// 2�ܰ� : PanelWest --> JFrame
		panelWest.setBorder(BorderFactory.createTitledBorder("����ó��"));
		panelWest.setLayout(new GridLayout(0,2));
		
		for(int i=0;i<lbText.length;i++)
		{
			lb[i] = new JLabel(lbText[i], JLabel.CENTER);
			tf[i] = new JTextField(15);
			panelWest.add(lb[i]);
			panelWest.add(tf[i]);
		}
		for(int i=0; i<btnText.length;i++)
		{
			btn[i] = new JButton(btnText[i]);
			panelWest.add(btn[i]);
			btn[i].addActionListener(this);
		}
		// 3�ܰ� : PanelSouth --> JFrame
		for(int i=0;i<lbTextSouth.length;i++)
		{
			lbSouth[i] = new JLabel(lbTextSouth[i], JLabel.CENTER);
			tfSouth[i] = new JTextField(10);
			panelSouth.add(lbSouth[i]);
			panelSouth.add(tfSouth[i]);
		}
		
		table.addMouseListener(this);
		this.add(panelWest,BorderLayout.WEST);
		this.add(panelSouth,BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	private Vector selectFromDB(Connection con, String dbTableName) {
		// TODO Auto-generated method stub
		Vector data = new Vector();
		
		String selectDataSQL="SELECT std_id,dept,name,kor,eng,com FROM "+dbTableName+";";
		
		PreparedStatement selectTable;
		try {
			selectTable = con.prepareStatement(selectDataSQL);
			resultdata = selectTable.executeQuery();
			while(resultdata.next()) {
				Vector<String> in = new Vector<String>();
				in.add(resultdata.getString("std_id"));
				in.add(resultdata.getString("dept"));
				in.add(resultdata.getString("name"));
				in.add(resultdata.getString("kor"));
				in.add(resultdata.getString("eng"));
				in.add(resultdata.getString("com"));
				data.add(in);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return data;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==btn[0]) { //Insert Button clicked
			ConnectionScoreDB.insertDataOnTable(con, dbTableName, 
					Integer.parseInt(tf[0].getText()), tf[1].getText(), 
					tf[2].getText(), Integer.parseInt(tf[3].getText()),
					Integer.parseInt(tf[4].getText()),Integer.parseInt(tf[5].getText()));
			try {
				result = selectFromDB(con,dbTableName); // select * from ...
				model.setDataVector(result, title);
				list = ConnectionScoreDB.getDataFromTable(con, dbTableName);
				System.out.println("Inserted Data Update OK!");
				JOptionPane.showMessageDialog(null,"�й� : "+ tf[0].getText()+ " ������ ���� �Ϸ�!");
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("Inserted Data Update ERROR!");
				JOptionPane.showMessageDialog(null,"�й� : "+ tf[0].getText()+ " ������ ���� ����!");
			}
			ConnectionScoreDB.printList(list);
		}
		if(e.getSource()==btn[1]) { //Delete Button clicked
			ConnectionScoreDB.deleteFromTable(con, dbTableName, Integer.parseInt(tf[0].getText()));
			try {
				result = selectFromDB(con,dbTableName); // select * from ...
				model.setDataVector(result, title);
				list = ConnectionScoreDB.getDataFromTable(con, dbTableName);
				System.out.println("Deleted Data Update OK!");
				JOptionPane.showMessageDialog(null,"�й� : "+ tf[0].getText()+ " ������ ���� �Ϸ�!");
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("Deleted Data Update ERROR!");
				JOptionPane.showMessageDialog(null,"�й� : "+ tf[0].getText()+ " ������ ���� ����!");
			}
			ConnectionScoreDB.printList(list);
		}
		//replace�� �ߺ��� �����͸� ���������� �����߰��ϴ� ����̰� update�� �������� �ʰ� �����͸� �����ϴ� ����̴�.
		if(e.getSource()==btn[2]) { //Update Button clicked
			ConnectionScoreDB.updateDataOnTable(con, dbTableName, 
					Integer.parseInt(tf[0].getText()), tf[1].getText(), 
					tf[2].getText(), Integer.parseInt(tf[3].getText()),
					Integer.parseInt(tf[4].getText()),Integer.parseInt(tf[5].getText()));
			try {
				result = selectFromDB(con,dbTableName); // select * from ...
				model.setDataVector(result, title);
				list = ConnectionScoreDB.getDataFromTable(con, dbTableName);
				System.out.println("Updated Data Update OK!");
				JOptionPane.showMessageDialog(null,"�й� : "+ tf[0].getText()+" ������ ���� �Ϸ�!");
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("Updated Data Update ERROR!");
				JOptionPane.showMessageDialog(null,"�й� : "+ tf[0].getText()+ " ������ ���� ����!");
			}
			ConnectionScoreDB.printList(list);
			
		}
		if(e.getSource()==btn[3]) { //Clear Button clicked
			for(int i=0; i<lbText.length;i++)
			{
				tf[i].setText("");
			}
			for(int i=0; i<lbTextSouth.length;i++)
			{
				tfSouth[i].setText("");
			}
			
		}
		if(e.getSource()==btn[4]) { //Previous Button clicked
			int kor_int=0;
			int eng_int=0;
			int com_int=0;
			try {
				if(resultdata.previous()) {
					kor_int= resultdata.getInt("kor");
					eng_int = resultdata.getInt("eng");
					com_int = resultdata.getInt("com");
					tf[0].setText(""+resultdata.getInt("std_id"));
					tf[1].setText(""+resultdata.getString("dept"));
					tf[2].setText(""+resultdata.getString("name"));
					tf[3].setText(""+resultdata.getInt("kor"));
					tf[4].setText(""+resultdata.getInt("eng"));
					tf[5].setText(""+resultdata.getInt("com"));
					
					tfSouth[0].setText(""+resultdata.getString("name"));
					tfSouth[1].setText(""+(kor_int+eng_int+com_int));
					tfSouth[2].setText(""+((kor_int+eng_int+com_int)/3));
					tfSouth[3].setText(""+(Math.max( Math.max( kor_int, eng_int ), com_int )));
					tfSouth[4].setText(""+(Math.min( Math.min( kor_int, eng_int ), com_int )));
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(e.getSource()==btn[5]) { //Next Button clicked
			int kor_int=0;
			int eng_int=0;
			int com_int=0;
			
			try {
				if(resultdata.next()) {
					kor_int= resultdata.getInt("kor");
					eng_int = resultdata.getInt("eng");
					com_int = resultdata.getInt("com");
					tf[0].setText(""+resultdata.getInt("std_id"));
					tf[1].setText(""+resultdata.getString("dept"));
					tf[2].setText(""+resultdata.getString("name"));
					tf[3].setText(""+resultdata.getInt("kor"));
					tf[4].setText(""+resultdata.getInt("eng"));
					tf[5].setText(""+resultdata.getInt("com"));
					
					tfSouth[0].setText(""+resultdata.getString("name"));
					tfSouth[1].setText(""+(kor_int+eng_int+com_int));
					tfSouth[2].setText(""+((kor_int+eng_int+com_int)/3));
					tfSouth[3].setText(""+(Math.max( Math.max( kor_int, eng_int ), com_int )));
					tfSouth[4].setText(""+(Math.min( Math.min( kor_int, eng_int ), com_int )));
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource()==btn[6]) { //Reset Button clicked
			for(int i=0; i<lbText.length;i++)
			{
				tf[i].setText("");
			}
			for(int i=0; i<lbTextSouth.length;i++)
			{
				tfSouth[i].setText("");
			}
			ConnectionScoreDB.dropTable(con, dbTableName);
			ConnectionScoreDB.createTable(con, dbTableName);
			try {
				result = selectFromDB(con,dbTableName); // select * from ...
				model.setDataVector(result, title);
				list = ConnectionScoreDB.getDataFromTable(con, dbTableName);
				System.out.println("Reset Data Update OK!");
				JOptionPane.showMessageDialog(null, "��ü������ �ʱ�ȭ �Ϸ�!");
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("Reset Data Update ERROR!");
				JOptionPane.showMessageDialog(null, "��ü������ �ʱ�ȭ ����!");
			}
			ConnectionScoreDB.printList(list);
		}
		if(e.getSource()==btn[7]) { //Dev Button clicked	
			JOptionPane.showMessageDialog(null, "������ : 201958091, ������ Ver.1.0 \n 2021.12.14");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int index = table.getSelectedRow();
		try {
			resultdata.absolute(index+1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Vector<String> in = new Vector<String>();
		in = (Vector<String>) result.get(index);
		
		int kor_int = Integer.parseInt(in.get(3));
		int eng_int = Integer.parseInt(in.get(4));
		int com_int = Integer.parseInt(in.get(5));
		
		for(int i=0; i<tf.length;i++)
		{
			tf[i].setText(in.get(i));
		}
		tfSouth[0].setText(in.get(2));
		tfSouth[1].setText(""+(kor_int+eng_int+com_int));
		tfSouth[2].setText(""+((kor_int+eng_int+com_int)/3));
		tfSouth[3].setText(""+(Math.max( Math.max( kor_int, eng_int ), com_int )));
		tfSouth[4].setText(""+(Math.min( Math.min( kor_int, eng_int ), com_int )));
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
