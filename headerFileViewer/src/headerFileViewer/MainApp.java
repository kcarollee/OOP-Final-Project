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
        for (String i : t.getTokens()){
            System.out.println(i);
        }
    }
}

