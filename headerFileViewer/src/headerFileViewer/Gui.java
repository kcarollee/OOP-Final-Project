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
import java.util.ArrayList;

class ViewerTreeModel implements TreeModel{
    public ClassInfo c;

    public ViewerTreeModel(ClassInfo c){
        this.c = c;
    }

    public String getChild(Object parent, int index){
        if (index < c.getMethodCount()){
        	System.out.println("TREE TEST 1: " + index);
            return c.getMethodName(index);
        }
        else {
        	System.out.println("TREE TEST 2: " + index);
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
                System.out.println("TREE TEST " + index);
                return index;
            }
            else
                index++;
        }
        for (int i = 0; i < c.variableList.size(); i++){
            index++;
            if(child.equals(c.variableList.get(i).getName())) {
            	index++;
            	System.out.println("TREE TEST " + index);
            	return index;
            }
            else
                index++;
                
        }
        System.out.println("TREE TEST " + index);
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
	
	// 중요!
	ArrayList<VarTableModel> varTableModelList = new ArrayList<VarTableModel>(); // 여기에 모델들을 담음
	ArrayList<JTable> varTableList = new ArrayList<JTable>(); // 여기에 모델을 사용해 생성된 테이블들을 담음
	
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
	//���� ���̺� ���� ����
	//////////////////////////////////////////////////////
	// 변수 테이블 모델을 하나로 지정
	class VarTableModel extends AbstractTableModel{
		Object[][] data = new Object[c.getVariableCount()][2];
		public VarTableModel(int index){
			data[0][0]=c.getVariableName(index);
			data[0][1]=c.getMethodsThatUseVariable(index);
		}
		String[] columnName = {"Name","methods"};
		public int getColumnCount() {return columnName.length;}
		public int getRowCount() {return data.length;}
		public String getColumnName(int col) {return columnName[col];}
		public Object getValueAt(int row,int col) {return data[row][col];}
		public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
	}
	//////////////////////////////////////////////////////
	// 필요없는 클래스 다 지움
	
	public Gui() {
		JPanel treepanel = new JPanel();
		JPanel usepanel = new JPanel();
		JPanel cardpanel = new JPanel();
		JLabel uselabel = new JLabel("Use");
		//�޼ҵ尡 ����ϴ� ������ ǥ���ϴ� ���ø��г� ����. ���ø����� ���� ������ �ܼ��� ũ�� ������ ���ؼ���.
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
        
        
        card = new CardLayout();
        cardpanel.setLayout(card);
        
        ////////////////////////////////////////////////////////
        for (int i = 0; i < c.getVariableCount(); i++) {
        	VarTableModel tempVarTableModel = new VarTableModel(i);
        	JTable tempVarTable = new JTable(tempVarTableModel);
        	tempVarTable.setSize(550, 300);
        	cardpanel.add("var" + i, tempVarTable);
        	
        }
        // SizeTableModel, TopTableModel 등등은 다 지움.
        //////////////////////////////////////////////////////////////////
        textarea = new JTextArea(30,500);
        textarea.setEditable(true);
        textarea.setCaretPosition(textarea.getDocument().getLength());
        
        
        cardpanel.add(table,"table");
        cardpanel.add(textarea,"textarea");
        cardpanel.setVisible(false);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			public void valueChanged(TreeSelectionEvent e) {
				Object o = e.getPath().getLastPathComponent().toString();
				if(o==treemodel.getRoot()) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "table");
				}
				else {
					for (int i=0;i<c.getMethodCount()+c.getVariableCount();i++) {
						if(i<c.getMethodCount()) {
							if(o==treemodel.getChild(treemodel.getRoot(),i)) {
								cardpanel.setVisible(true);
								card.show(cardpanel, "textarea");
								textarea.setText(c.getFormattedMethodContents(i));
								usepanel.setVisible(true);
								usetextarea.setText(c.getVariablesUsedByMethod(i));
								System.out.println(i);
								break;
							}
						}
						
						else if( o.equals(treemodel.getChild(treemodel.getRoot(), i))) {
							cardpanel.setVisible(true);	
							usepanel.setVisible(false);
							card.show(cardpanel, "var" + (i - c.getMethodCount()));
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