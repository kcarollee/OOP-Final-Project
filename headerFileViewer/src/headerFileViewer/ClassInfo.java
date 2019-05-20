package headerFileViewer;

import java.util.ArrayList;

public class ClassInfo  {
    public String className;
    public ArrayList<String> MethodNames = new ArrayList<String>();
    public ArrayList<String> ReturnTypes = new ArrayList<String>();
    public ArrayList<String> MethodAccessTypes = new ArrayList<String>();
    public ArrayList<String> declarationTokens;
    public KeyWords keyWords = new KeyWords();

    public ClassInfo(ArrayList<String> declarationTokens){
        this.declarationTokens = declarationTokens;
        className = declarationTokens.get(1);
    }

    public String getClassName(){
        return className;
    }

    public void processTokens(){
        
    }



}
