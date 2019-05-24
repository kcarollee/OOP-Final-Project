package headerFileViewer;

import java.io.*;
import java.util.StringTokenizer;

public class Parser {
    public StringBuffer mainTextBuffer = new StringBuffer();

    public int mainTextIndex = 0;
    FileInputStream file = null;

    public Parser(String fileAddress){
        try {
            file = new FileInputStream(fileAddress);
            mainTextIndex = file.read();
            while (mainTextIndex != -1){
                mainTextBuffer.append((char) mainTextIndex);
                mainTextIndex = file.read();
            }

        } catch (FileNotFoundException e){
            System.out.println("FILE NOT FOUND EXCEPTION");
        } catch(IOException e){
            System.out.println("INPUT ERROR");
        }
    }

    public StringBuffer getTextBuffer(){
        return mainTextBuffer;
    }
//안녕하세요
}
