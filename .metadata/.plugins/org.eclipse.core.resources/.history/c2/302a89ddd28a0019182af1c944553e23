//테이블의 헤더가 왜 안 나오는지 보기 위해 따로 만든 클래스. Gui 클래스에 있는걸 복붙. 테이블만 따로 만들어 실행하면 헤더가 제대로 뜨는 것을 볼 수 있음.
//즉, 테이블 코드가 문제가 아니라 이 테이블을 gui상의 패널에 넣을 때 모종의 이유로 헤더가 잘리는 것 같음. 변수 테이블도 마찬가지로 헤더가 안 나옴.
package headerFileViewer;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class TestInfoTable extends JFrame {
	JTable table;
	Parser p = new Parser("bin\\Stack.h");
	Tokenizer t = new Tokenizer(p.getTextBuffer());
	ClassInfo c = new ClassInfo(t.getDeclarationTokens(),t.getDefinitionTokens());

	public TestInfoTable() {
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
	public static void main(String[] args) {
		new TestInfoTable();
	}
}