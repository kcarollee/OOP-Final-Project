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
		
		Object[][] data = {
				{c.getName(0),c.getType(0),c.getAccess(0)},
				{c.getName(1),c.getType(1),c.getAccess(1)},
				{c.getName(2),c.getType(2),c.getAccess(2)},
				{c.getName(3),c.getType(3),c.getAccess(3)}
			};
		String[] columnName = {"Name","Type","Access"};
		public int getColumnCount() {return columnName.length;}
		public int getRowCount() {return data.length;}
		public String getColumnName(int col) {return columnName[col];}
		public Object getValueAt(int row,int col) {return data[row][col];}
		public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
			
	}
}