package headerFileViewer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.*;

class ViewerTreeModel implements TreeModel{
    public ClassInfo c;

    public ViewerTreeModel(ClassInfo c){
        this.c = c;
    }

    public String getChild(Object parent, int index){
        if (index < c.getMethodCount()){
            return c.getMethodName(index);
        }
        else {
        	return c.getFormattedVariableName(index-c.getMethodCount());
        }
    }
    public int getChildCount(Object parent){
        return c.getMethodCount() + c.getVariableCount();
    }
    public int getIndexOfChild(Object parent, Object child){
        int index = 0;
        for (int i = 0; i < c.methodList.size(); i++){
            if (child.equals(c.methodList.get(i).getName())) {
                index++;
                return index;
            }
            else
                index++;
        }
        for (int i = 0; i < c.variableList.size(); i++){
            index++;
            if(child.equals(c.variableList.get(i).getName()))
                return index;
        }
        return index;
    }

    public Object getRoot(){
        return c.getClassName();
    }

    public boolean isLeaf(Object node){
        if(node.equals(c.getClassName()))
            return false;
        return true;
    }

    public void addTreeModelListener(TreeModelListener i){ }
    public void removeTreeModelListener(TreeModelListener i){}
    public void valueForPathChanged(TreePath path, Object newValue){}

}

public class Gui extends JFrame	{
	public JTree tree;
	Parser p= new Parser("bin\\Stack.h");
	Tokenizer t= new Tokenizer(p.getTextBuffer());
	ClassInfo c= new ClassInfo(t.getDeclarationTokens(),t.getDefinitionTokens());
	JTable table,sizetable,toptable,ptrtable;
	CardLayout card;
	JTextArea textarea;
	
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
	//변수 테이블 각각 생성
	class SizeTableModel extends AbstractTableModel{
		Object[][] data = new Object[c.getVariableCount()][2];
		SizeTableModel(){
			data[0][0]=c.getVariableName(0);
			data[0][1]=c.getMethodsThatUseVariable(0);
		}
		String[] columnName = {"Name","methods"};
		public int getColumnCount() {return columnName.length;}
		public int getRowCount() {return data.length;}
		public String getColumnName(int col) {return columnName[col];}
		public Object getValueAt(int row,int col) {return data[row][col];}
		public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
	}
	
	class TopTableModel extends AbstractTableModel{
		Object[][] data = new Object[c.getVariableCount()][2];
		TopTableModel(){
			data[0][0]=c.getVariableName(1);
			data[0][1]=c.getMethodsThatUseVariable(1);
		}
		String[] columnName = {"Name","methods"};
		public int getColumnCount() {return columnName.length;}
		public int getRowCount() {return data.length;}
		public String getColumnName(int col) {return columnName[col];}
		public Object getValueAt(int row,int col) {return data[row][col];}
		public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
		
	}
	
	class PtrTableModel extends AbstractTableModel{
		Object[][] data = new Object[c.getVariableCount()][2];
		PtrTableModel(){
			data[0][0]=c.getVariableName(2);
			data[0][1]=c.getMethodsThatUseVariable(2);
		}
		String[] columnName = {"Name","methods"};
		public int getColumnCount() {return columnName.length;}
		public int getRowCount() {return data.length;}
		public String getColumnName(int col) {return columnName[col];}
		public Object getValueAt(int row,int col) {return data[row][col];}
		public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
		
	}
	
	public Gui() {
		JPanel treepanel = new JPanel();
		JPanel usepanel = new JPanel();
		JPanel cardpanel = new JPanel();
		JLabel uselabel = new JLabel("Use");
		//메소드가 사용하는 변수를 표시하는 스플릿패널 생성. 스플릿으로 만든 이유는 단순히 크기 조절을 위해서임.
		JTextArea usetextarea = new JTextArea();
		JSplitPane varpanel = new JSplitPane(1,false,usetextarea,new JPanel());
		varpanel.setDividerLocation(200);
		setTitle("C++ class viewer");
		setSize(1000,420);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		add(treepanel);
		treepanel.setBounds(10, 20, 390, 250);
		add(usepanel);
		usepanel.setBounds(10, 270, 390, 100);
		usepanel.setLayout(new BorderLayout());
		usepanel.add(uselabel,BorderLayout.NORTH);
		usepanel.add(varpanel,BorderLayout.CENTER);
		add(cardpanel);
		cardpanel.setBounds(420, 50, 550, 300);
		LineBorder border = new LineBorder(Color.black);
		//EmptyBorder emptyborder = new EmptyBorder(5,10,10,190);
		//varpanel.setPreferredSize(new Dimension(50, 50));
		treepanel.setBorder(border);
		usepanel.setBorder(border);
		varpanel.setBorder(border);
		cardpanel.setBorder(border);
		treepanel.setBackground(Color.white);
		setVisible(true);
		setResizable(true);
		
		ViewerTreeModel treemodel = new ViewerTreeModel(c);

        tree = new JTree(treemodel);
        tree.setModel(treemodel);
        
        treepanel.add(tree);
        tree.setSize(390,250);
        treepanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        TableModel tablemodel = new TableModel();
        table = new JTable(tablemodel);
        table.setSize(550, 300);
        
        SizeTableModel sizetablemodel = new SizeTableModel();
	    sizetable = new JTable(sizetablemodel);
	    sizetable.setSize(550,300);
	    
	    TopTableModel toptablemodel = new TopTableModel();
	    toptable = new JTable(toptablemodel);
	    toptable.setSize(550,300);
	    
	    PtrTableModel ptrtablemodel = new PtrTableModel();
	    ptrtable = new JTable(ptrtablemodel);
	    ptrtable.setSize(550,300);
        
        textarea = new JTextArea(30,500);
        textarea.setEditable(true);
        textarea.setCaretPosition(textarea.getDocument().getLength());
        
        
        card = new CardLayout();
        cardpanel.setLayout(card);
        cardpanel.add(table,"table");
        cardpanel.add(textarea,"textarea");
        cardpanel.add(sizetable,"sizetable");
        cardpanel.add(toptable,"toptable");
        cardpanel.add(ptrtable,"ptrtable");
        cardpanel.setVisible(false);
        
       
        tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			public void valueChanged(TreeSelectionEvent e) {
				Object o = e.getPath().getLastPathComponent().toString();
				if(o==treemodel.getRoot()) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "table");
				}
				
				// 변수 리스너 발동을 위한 발악의 잔재
				/*else if(o==treemodel.getChild(treemodel.getRoot(),6)) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "sizetable");
				}
				
				else if(o==treemodel.getChild(treemodel.getRoot(),7)) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "toptable");
				}
				
				else if(o==treemodel.getChild(treemodel.getRoot(),8)) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "ptrtable");
				}*/
				// for loop으로 수정,else if들은 이 또한 발악의 잔재. index가 6,7,8일때 getchild가 제대로 작동하지 않는게 원인인듯?
				else {
					for (int i=0;i<c.getMethodCount()+c.getVariableCount();i++) {
						if(i<c.getMethodCount()) {
							if(o==treemodel.getChild(treemodel.getRoot(),i)) {
								cardpanel.setVisible(true);
								card.show(cardpanel, "textarea");
								textarea.setText(c.getFormattedMethodContents(i));
								//메소드가 사용하는 변수를 리스너에 등록
								usetextarea.setText(c.getVariablesUsedByMethod(i));
								break;
							}
						}
						else if(i==6 && o==treemodel.getChild(treemodel.getRoot(), 6)) {
							cardpanel.setVisible(true);
							card.show(cardpanel, "sizetable");
							break;
						}
					
						else if(i==7 && o==treemodel.getChild(treemodel.getRoot(),7)) {
							cardpanel.setVisible(true);
							card.show(cardpanel, "toptable");
							break;
						}
					
						else /*if(i==8 && o==treemodel.getChild(treemodel.getRoot(),8))*/ {
							System.out.println("이것도 아니지롱!");
							System.out.println("ㅋㅋ!");
							cardpanel.setVisible(true);
							card.show(cardpanel, "ptrtable");
							break;
						}	
					}
				}
				
			}
		});
	}
	public static void main(String[] args) {
		new Gui();
	}

}
