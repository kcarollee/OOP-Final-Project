package headerFileViewer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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

public class Gui extends JFrame	implements ActionListener{
	public JTree tree;
	Parser p= new Parser("bin\\Stack.h");
	Tokenizer t= new Tokenizer(p.getTextBuffer());
	ClassInfo c= new ClassInfo(t.getDeclarationTokens(),t.getDefinitionTokens());
	JTable table;
	CardLayout card;
	JTextArea textarea;
	JMenuItem exit;
	// 以묒슂!
	ArrayList<VarTableModel> varTableModelList = new ArrayList<VarTableModel>(); // �뿬湲곗뿉 紐⑤뜽�뱾�쓣 �떞�쓬
	ArrayList<JTable> varTableList = new ArrayList<JTable>(); // �뿬湲곗뿉 紐⑤뜽�쓣 �궗�슜�빐 �깮�꽦�맂 �뀒�씠釉붾뱾�쓣 �떞�쓬
	
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
	//占쏙옙占쏙옙 占쏙옙占싱븝옙 占쏙옙占쏙옙 占쏙옙占쏙옙
	//////////////////////////////////////////////////////
	// 蹂��닔 �뀒�씠釉� 紐⑤뜽�쓣 �븯�굹濡� 吏��젙
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
	// �븘�슂�뾾�뒗 �겢�옒�뒪 �떎 吏���
	
	public Gui() {
		JPanel treepanel = new JPanel();
		JPanel usepanel = new JPanel();
		JPanel cardpanel = new JPanel();
		JLabel uselabel = new JLabel("Use");
		JTextArea usetextarea = new JTextArea();
		Font font = new Font("custom",Font.PLAIN,20);
		JSplitPane varpanel = new JSplitPane(1,false,usetextarea,new JPanel());
		
		JMenuBar menubar = new JMenuBar();
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		JMenu menu = new JMenu("File");
		menu.add(new JMenuItem("Open"));
		menu.add(new JMenuItem("Save"));
		menu.add(exit);
		menubar.add(menu);
		setJMenuBar(menubar);
		
		varpanel.setDividerLocation(200);
		setTitle("C++ class viewer");
		setSize(1000,450);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		card = new CardLayout();
	    cardpanel.setLayout(card);
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
		/*DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)tree.getCellRenderer();
		Icon leaficon = new ImageIcon("leaf.png");
		renderer.setLeafIcon(leaficon);*/
		
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
        table.setRowHeight(30);
        
        ////////////////////////////////////////////////////////
        for (int i = 0; i < c.getVariableCount(); i++) {
        	VarTableModel tempVarTableModel = new VarTableModel(i);
        	JTable tempVarTable = new JTable(tempVarTableModel);
        	tempVarTable.setSize(550, 300);
        	cardpanel.add("var" + i, new JScrollPane(tempVarTable));
        	tempVarTable.setRowHeight(30);
        	
        }
        // SizeTableModel, TopTableModel �벑�벑�� �떎 吏���.
        //////////////////////////////////////////////////////////////////
        textarea = new JTextArea(30,500);
        textarea.setFont(font);
        textarea.setEditable(true);
        textarea.setCaretPosition(textarea.getDocument().getLength());
        usetextarea.setFont(font);
        
        //아무거나
        cardpanel.add(new JScrollPane(table),"table");
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
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if(source.equals(exit))
			System.exit(0);
	}
}