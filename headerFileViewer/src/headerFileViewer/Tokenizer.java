package headerFileViewer;

import java.util.ArrayList;

public class Tokenizer {
    public ArrayList<String> mainTextTokens = new ArrayList<String>();
    public int tokenNum = 0 ;
    public boolean noChar = true;
    public boolean commentMode = false;
    public Tokenizer(StringBuffer mainTextBuffer){
        processTextBuffer(mainTextBuffer);
    }

    public void processTextBuffer(StringBuffer mainTextBuffer){
        String temp = "";
        for (int i = 0; i < mainTextBuffer.length(); i++){
            if ((mainTextBuffer.charAt(i) == '/' && mainTextBuffer.charAt(i + 1) == '/')){
                commentMode = true;
            }
            if (commentMode){
                if (mainTextBuffer.charAt(i) == '\r')
                    commentMode = false;
                else
                    continue;
            }
            else {
                if (isChar(mainTextBuffer.charAt(i))) {
                    temp += mainTextBuffer.charAt(i);
                    noChar = true;
                }
                else {
                    if (noChar) {
                        noChar = false;
                        mainTextTokens.add(temp);
                        temp = "";
                    }
                }
            }
        }
    }

    public boolean isChar(char c){
        if (c == '*' || c == '~' ||
                (65 <= (int) c && (int) c <= 90) ||
                (97 <= (int) c && (int) c <= 122)){
            return true;
        }
        return false;
    }

    public ArrayList<String> getTokens(){
        return mainTextTokens;
    }
}
