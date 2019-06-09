package headerFileViewer;

import java.util.ArrayList;

public class MethodInfo extends Tokenizer {
	public String name;
	public ArrayList<String> pureStringTokens = new ArrayList<String>();
	public ArrayList<String> memberVariables = new ArrayList<String>();
	public String methodContents;
	
	// each token is a single line of code from a method definition.
	// this is what we'll ultimately use. 
	public ArrayList<String> methodSplitByNewLine = new ArrayList<String>();
	
	public MethodInfo(String name, String methodContents) {
		this.name = name;
		this.methodContents = methodContents;
		formatMethodContents();
		processTextBuffer(methodContents);
	}
	
	public void formatMethodContents() {
				
		String temp = "";
		// iterate through method definition by character
		for (int i = 0; i < methodContents.length(); i++) {
			if (methodContents.charAt(i) == '\r' || methodContents.charAt(i) == '\n' || 
					(i == methodContents.length() - 1)) {
				if (methodContents.charAt(i) == ';') {
					temp += methodContents.charAt(i);
					methodSplitByNewLine.add(temp);
					temp = "";
				}
				if (!temp.equals("")) {
					methodSplitByNewLine.add(temp);
					temp = "";
				}
			}
			else {
				temp += methodContents.charAt(i);
			}
		}
				
		for (int i = 0; i < methodSplitByNewLine.size(); i++) {
			boolean bracketOpened = false;
			int firstNonSpaceChar = 0;
			while (methodSplitByNewLine.get(i).charAt(firstNonSpaceChar++) == ' ') {
				
			}
			// check if the previous line contains an opening bracket 
			if (i > 0) {
				for (int j = 0; j < methodSplitByNewLine.get(i - 1).length(); j++) {
					if ( methodSplitByNewLine.get(i - 1).charAt(j) == '{') {
						bracketOpened = true;
					}
					if (bracketOpened && methodSplitByNewLine.get(i - 1).charAt(j) == '}')
						bracketOpened = false;
				}
			}
			if (bracketOpened) // add 4 spaces
				methodSplitByNewLine.set(i, "    ".concat(methodSplitByNewLine.get(i).substring(firstNonSpaceChar - 1)));			
			else // add no spaces
				methodSplitByNewLine.set(i, methodSplitByNewLine.get(i).substring(firstNonSpaceChar - 1));
		}
		
	}
	
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
	

}