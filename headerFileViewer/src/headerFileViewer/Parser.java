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

            /* // the following code is for testing purposes
            System.out.println(mainTextBuffer);
            System.out.println(mainTextBuffer.length());
            System.out.println(mainTextBuffer.charAt(11));

            char[] temp = new char[mainTextBuffer.length()];
            for (int i = 0; i < mainTextBuffer.length(); i++){
                temp[i] = mainTextBuffer.charAt(i);
            }
            int test1 = 0;
            int test2 = 0;
            for (int i = 0; i < mainTextBuffer.length(); i++){
                if (temp[i] == ' ' || temp[i] == '\r'){
                    System.out.print("X");
                    test1++;
                }
                else {
                    System.out.print(temp[i]);
                    test2++;
                }
            }
            */


        } catch (FileNotFoundException e){
            System.out.println("FILE NOT FOUND EXCEPTION");
        } catch(IOException e){
            System.out.println("INPUT ERROR");
        }
    }

    public StringBuffer getTextBuffer(){
        return mainTextBuffer;
    }
}

