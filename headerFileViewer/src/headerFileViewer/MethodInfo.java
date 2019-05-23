package headerFileViewer;

import java.util.ArrayList;

public class MethodInfo extends Tokenizer {
	public String name;
	public ArrayList<String> pureStringTokens = new ArrayList<String>();
	public ArrayList<String> memberVariables = new ArrayList<String>();
	public String methodContents;
	
	public MethodInfo(String name, String methodContents) {
		this.name = name;
		this.methodContents = methodContents;
		formatMethodContents();
		processTextBuffer(methodContents);
	}
	
	public void formatMethodContents() {
		
	}
	
	// 일반적인 토크나이저입니다. 순수 스트링을 받음.
    // 메스드가 사용하는 변수를 알아낼때 사용하기. 
    public void processTextBuffer(String mainTextBuffer){
        String temp = "";
        boolean commentMode = false;
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
                        pureStringTokens.add(temp);
                        temp = "";
                    }
                }
            }
        }        
    }
    
    public ArrayList<String> getPureStringTokens(){
    	return pureStringTokens;
    }
	
	public String getMethodContents() {
		return methodContents;
	}
	
	public String getName() {
		return name;
	}
	
	// used in conjunction with comparing methods in ClassInfo
	public void addToMemberVariables(String variable) {
		memberVariables.add(variable);
	}
	
	public void showUsedMemberVariables() {
		for (String s : memberVariables) {
			System.out.println(s);
		}
	}
	public void showPureStringTokens() {
		for (String i : pureStringTokens) {
			System.out.println(i);
		}
	}
}