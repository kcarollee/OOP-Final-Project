package headerFileViewer;

import javax.swing.*;
import javax.swing.table.*;

public class InfoTable extends JFrame {
	JTable table;
	Parser p = new Parser("bin\\Stack.h");
	Tokenizer t = new Tokenizer(p.getTextBuffer());
	ClassInfo c = new ClassInfo(t.getDeclarationTokens(),t.getDefinitionTokens());
	
	public InfoTable() {
		super("Information");
		TableModel model = new TableModel();
		table = new JTable(model);
		getContentPane().add(new JScrollPane(table),"Center");
		setSize(500,300);
		setVisible(true);
		
		}
	
	class TableModel extends AbstractTableModel{
		
		Object[][] data = new Object[c.getMethodCount()+c.getVariableCount()][3];
		TableModel(){
			for(int i=0;i<c.getMethodCount()+c.getVariableCount();i++) {
				for(int j=0;j<3;j++) {
					if(j==0) data[i][j]=c.getName(i);
					else if(j==1) data[i][j]=c.getType(i);
					else data[i][j]=c.getAccess(i);
				}
			}
		}
		String[] columnName = {"Name","Type","Access"};
		public int getColumnCount() {return columnName.length;}
		public int getRowCount() {return data.length;}
		public String getColumnName(int col) {return columnName[col];}
		public Object getValueAt(int row,int col) {return data[row][col];}
		public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
			
	}
}