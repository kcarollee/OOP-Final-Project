package headerFileViewer;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainApp extends JFrame{
    public MainApp(){
    	
    }
    public static void main(String[] args){
        Parser p = new Parser("bin\\Stack.h");
        Tokenizer t = new Tokenizer(p.getTextBuffer());
        System.out.println("---------------------Testing declaration tokens-----------------");
        for (String i : t.getDeclarationTokens()){
            System.out.println(i);
        }
        ClassInfo c = new ClassInfo(t.getDeclarationTokens(), t.getDefinitionTokens());
        System.out.println("---------------------Testing table------------------------------");
        c.printTable();
        System.out.println("---------------------Testing member variables-------------------");
        for (String i : c.getMemberVariables()) {
        	System.out.println(i);
        }
        System.out.println("---------------------Testing definition tokens------------------");
        for (String i : t.getDefinitionTokens()){
            System.out.println(i);
            System.out.println("-------------------------------------------------------------");
        }
        
        System.out.println("---------------------Testing methodInfo--------------------------");
        c.printMethods();
        System.out.println("------------------Testing VariableInfo---------------------------");
        c.printVariables();
    }
}


