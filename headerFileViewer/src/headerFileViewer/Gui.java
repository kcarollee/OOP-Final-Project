package headerFileViewer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.*;

class ViewerTreeModel implements TreeModel{
    public ClassInfo c;

    public ViewerTreeModel(ClassInfo c){
        this.c = c;
    }

    public Object getChild(Object parent, int index){
        if (index < c.getMethodCount()){
            System.out.println("TREETEST");
            System.out.println(c.getMethodName(index) + " " + index);
            return c.getMethodName(index);
        }
        else {
            return c.getFormattedVariableName(index - c.getMethodCount());
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
	JTable table,vartable;
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
	
	class VarTableModel extends AbstractTableModel{
		Object[][] data = new Object[c.getVariableCount()][2];
		int index;
		VarTableModel(){
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
	
	public Gui() {
		JPanel treepanel = new JPanel();
		JPanel usepanel = new JPanel();
		JPanel cardpanel = new JPanel();
		setTitle("C++ class viewer");
		setSize(1000,420);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		add(treepanel);
		treepanel.setBounds(10, 20, 390, 250);
		add(usepanel);
		usepanel.setBounds(10, 270, 390, 100);
		add(cardpanel);
		cardpanel.setBounds(420, 50, 550, 300);
		LineBorder border = new LineBorder(Color.black);
		treepanel.setBorder(border);
		usepanel.setBorder(border);
		cardpanel.setBorder(border);
		treepanel.setBackground(Color.white);
		setVisible(true);
		setResizable(false);
		
		ViewerTreeModel treemodel = new ViewerTreeModel(c);

        tree = new JTree(treemodel);
        tree.setModel(treemodel);
        
        treepanel.add(tree);
        tree.setSize(390,250);
        treepanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        TableModel tablemodel = new TableModel();
        table = new JTable(tablemodel);
        table.setSize(550, 300);
        VarTableModel vartablemodel = new VarTableModel();
        vartable = new JTable(vartablemodel);
        vartable.setSize(550,300);
        
        textarea = new JTextArea(30,500);
        textarea.setEditable(true);
        textarea.setCaretPosition(textarea.getDocument().getLength());
        
        
        card = new CardLayout();
        cardpanel.setLayout(card);
        cardpanel.add(table,"table");
        cardpanel.add(textarea,"textarea");
        cardpanel.add(vartable,"vartable");
        cardpanel.setVisible(false);
        
       
        tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			public void valueChanged(TreeSelectionEvent e) {
				Object o = e.getPath().getLastPathComponent();
				if(o==treemodel.getRoot()) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "table");
				}
				else if(o==treemodel.getChild(treemodel.getRoot(),0)) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "textarea");
					textarea.setText(c.getFormattedMethodContents(0));
				}
				else if(o==treemodel.getChild(treemodel.getRoot(),1)) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "textarea");
					textarea.setText(c.getFormattedMethodContents(1));
				}
				else if(o==treemodel.getChild(treemodel.getRoot(),2)) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "textarea");
					textarea.setText(c.getFormattedMethodContents(2));
				}
				else if(o==treemodel.getChild(treemodel.getRoot(),3)) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "textarea");
					textarea.setText(c.getFormattedMethodContents(3));
				}
				else if(o==treemodel.getChild(treemodel.getRoot(),4)) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "textarea");
					textarea.setText(c.getFormattedMethodContents(4));
				}
				else if(o==treemodel.getChild(treemodel.getRoot(),5)) {
					cardpanel.setVisible(true);
					card.show(cardpanel, "textarea");
					textarea.setText(c.getFormattedMethodContents(5));
				}
				else if(o==treemodel.getChild(treemodel.getRoot(),6)) {
					cardpanel.setVisible(true);
					vartablemodel.index =0;
					card.show(cardpanel, "vartable");
				}
				else if(o==treemodel.getChild(treemodel.getRoot(),7)) {
					cardpanel.setVisible(true);
					vartablemodel.index =1;
					card.show(cardpanel, "vartable");
				}
				else if(o==treemodel.getChild(treemodel.getRoot(),8)) {
					cardpanel.setVisible(true);
					vartablemodel.index =2;
					card.show(cardpanel, "vartable");
				}
				else System.out.println("����");
			}
		});
	}
	public static void main(String[] args) {
		new Gui();
	}

}
