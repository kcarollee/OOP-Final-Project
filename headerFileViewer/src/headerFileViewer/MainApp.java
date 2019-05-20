package headerFileViewer;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainApp {
    public MainApp(){

    }
    public static void main(String[] args){
        Parser p = new Parser("C:\\Stack.h");
        Tokenizer t = new Tokenizer(p.getTextBuffer());
        for (String i : t.getDeclarationTokens()){
            System.out.println(i);
        }
        ClassInfo c = new ClassInfo(t.getDeclarationTokens());
        c.printTable();
    }
}


