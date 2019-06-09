package headerFileViewer;

import java.util.ArrayList;

public class Tokenizer {
    public ArrayList<String> declarationTokens = new ArrayList<String>();
    public ArrayList<String> definitionTokens = new ArrayList<String>();
    public int index = 0 ;
    public int bracketNum = 0;
    public boolean noChar = false;
    public boolean commentMode = false;
    public boolean parenthesisOpen = false;
    public KeyWords keyWords = new KeyWords();
    
    public class BracketStack{
    	private ArrayList<Character> brackets = new ArrayList<Character>();
    	private void addBracket(char c) {
    		brackets.add(c);
    	}
    	
    	private boolean bracketClosed() {
    		int openNum = 0;
    		int closedNum = 0;
    		for (int i = 0; i < brackets.size(); i++) {
    			if (brackets.get(i) == '{') {
    				openNum++;
    			}
    			else if (brackets.get(i) == '}') {
    				closedNum++;
    			}
    		}    		
    		if (openNum == closedNum) {
    			return true;
    		}
    		else 
    			return false;
    	}    	
    	private void clear() {
    		brackets.clear();
    	}
    }
    
    public BracketStack bracketStack = new BracketStack();
      
    public Tokenizer() {
    }
    
    public Tokenizer(StringBuffer mainTextBuffer){
        processDeclarations(mainTextBuffer);
        processDefinitions(mainTextBuffer);
    } 
    
    public boolean declarationEnded(StringBuffer mainTextBuffer, int index) {
    	int subIndex = 0;
    	if (mainTextBuffer.charAt(index) == '}') {
    		if (mainTextBuffer.charAt(index + 1) == ';')
    			return true;
    		else {
    		while (mainTextBuffer.substring(index + 1).charAt(subIndex + 1) != ';') {
    			if (mainTextBuffer.substring(index + 1).charAt(subIndex + 1) != ' ')
    				
    				return false;    	
    			else
    				subIndex++;
    			}
    		return true;
    		}
		}
    	return false;
    }

    // this is the solution for the parenthesis bug.
    public boolean parenthesisAhead(StringBuffer mainTextBuffer, int index){
    	int i = 0;
    	while (mainTextBuffer.substring(index).charAt(i++) == ' '){
    		if (mainTextBuffer.substring(index).charAt(i) == '(' || mainTextBuffer.substring(index).charAt(i) == '{')
    			return true;
		}
    	return false;
	}

    public void processDeclarations(StringBuffer mainTextBuffer){
        String temp = "";
        String temp2 = "";
        String methodContentWithin = "";
        boolean addedToDefinitionTokens = false;
        for (int i = 0; i < mainTextBuffer.length(); i++){
            if (mainTextBuffer.charAt(i) == '{'){
                bracketNum++;
                index++;
            }
            if (declarationEnded(mainTextBuffer, i)) {
            	index++;
                break;
            }
            else {
                if ((mainTextBuffer.charAt(i) == '/' && mainTextBuffer.charAt(i + 1) == '/')) {              
                	commentMode = true;
                }
                if (commentMode) {
                    if (mainTextBuffer.charAt(i) == '\r') {
                    	index++;
                        commentMode = false;
                    }
                    else {
                    	index++;                        
                    }
                }
                else {

                    if (bracketNum > 1){
                        if (mainTextBuffer.charAt(i) != '}') {
                        	index++;
                        	if (!addedToDefinitionTokens) {
                        		definitionTokens.add(temp);
                        		addedToDefinitionTokens = true;
                        	}
                        	if (mainTextBuffer.charAt(i) != '{')
                        		methodContentWithin += mainTextBuffer.charAt(i);                       	
                        }
                        else {
                        	definitionTokens.add(methodContentWithin);
                        	methodContentWithin = "";
                        	index++;
                            bracketNum--;
                        }
                    }
                    else {

                        if (mainTextBuffer.charAt(i) == '(') {
                        	index++;
                        	temp += mainTextBuffer.charAt(i);
                            parenthesisOpen = true;
                        }
                        else {
                            if (parenthesisOpen) {
                                if (mainTextBuffer.charAt(i) == ')') {
                                	index++;
                                	temp += mainTextBuffer.charAt(i);
                                    parenthesisOpen = false;
                                }

                                else {
                                	if (isChar(mainTextBuffer.charAt(i))) {
                                		if (!isTypeKeyWord(temp2)){
                                			index++;
                                			temp2 += mainTextBuffer.charAt(i);
                                		}
                                		else {
                                			index++;
                                			temp += temp2;
                                			temp2 = "";
                                		}
                                	}
                                	else {
                                	index++;                                 
                                	}
                                }
                            }
                            else {
                                if (isCharOrParenthesis(mainTextBuffer.charAt(i))) {
                                	index++;
                                	temp += mainTextBuffer.charAt(i);
                                    noChar = true;
                                }
                                else {
                                    if (noChar) {
                                    	if (parenthesisAhead(mainTextBuffer, i)){
                                    		index++;
                                    		noChar = true;
										}
                                    	else {
											index++;
											noChar = false;
											declarationTokens.add(temp);
											temp = "";
										}
                                    }
                                    else
                                    	index++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    

    public void processDefinitions(StringBuffer mainTextBuffer){
    	commentMode = false;
    	boolean blockInitiated = false;
    	boolean gettingMethodName = false;
    	boolean methodNameParsed = true; 
    	String temp = "";
        String temp2 = "";
        String temp3 = ""; // used for method information tokens: split by \r
    	for (int i = index; i < mainTextBuffer.length(); i++) {
    		
    		 if ((mainTextBuffer.charAt(i) == '/' && mainTextBuffer.charAt(i + 1) == '/')) {
             	commentMode = true;
             }

             if (commentMode) {
                 if (mainTextBuffer.charAt(i) == '\r') {              
                     commentMode = false;
                 }
                 else continue;                
             }             
             else {

            	 if ((mainTextBuffer.charAt(i) == ':' && mainTextBuffer.charAt(i + 1) == ':')) {
            		 gettingMethodName = true;
            	 }            	 
            	 if (gettingMethodName) {
                     if (mainTextBuffer.charAt(i) == '(') {                    
                     	temp += mainTextBuffer.charAt(i);
                         parenthesisOpen = true;
                     }
                     else {
                         if (parenthesisOpen) {
                             if (mainTextBuffer.charAt(i) == ')') {                             
                             	temp += mainTextBuffer.charAt(i);
                                 parenthesisOpen = false;
                                 methodNameParsed = true;
                             }
                             else {
                             	if (isChar(mainTextBuffer.charAt(i))) {
                             		if (!isTypeKeyWord(temp2)){
                             			temp2 += mainTextBuffer.charAt(i);
                             		}
                             		else {
                             			temp += temp2;
                             			temp2 = "";
                             		}
                             	}
                             }
                         }
                         else {
                             if (isCharOrParenthesis(mainTextBuffer.charAt(i))) {
                             	temp += mainTextBuffer.charAt(i);                                                                
                             }                                                      
                             else {
                            	 if (!methodNameParsed) {
                            		 if (mainTextBuffer.charAt(i) == ' ') {                               	                                 	
                            			 definitionTokens.add(temp);                 
                            			 temp = "";
                            		 }  
                            	 }
                                 else {
                                	 if (mainTextBuffer.charAt(i) == '{' && !blockInitiated) {
                                		 definitionTokens.add(temp);
                                		 gettingMethodName = false;
                                		 temp = "";
                                     
                                		 gettingMethodName = false;
                                		 blockInitiated = true;	
                                		 bracketStack.addBracket(mainTextBuffer.charAt(i));      
                                	 }
                        	 	 }
                             }
                         }
                     }
            	 }
            	 // not getting methodName -> look for opening bracket
            	 else {           		 
            		 if (blockInitiated) { 
            			 if (mainTextBuffer.charAt(i) == '{' || mainTextBuffer.charAt(i) == '}') {
            				 bracketStack.addBracket(mainTextBuffer.charAt(i));
            				 if (bracketStack.bracketClosed()) {            					 
            					 bracketStack.clear();
            					 definitionTokens.add(temp3);
            					 temp3 = "";
                   				 blockInitiated = false;
                   			 }
            				 else {
            					 temp3 += mainTextBuffer.charAt(i);
            				 }
           				 }
            			 else {
            				 temp3 += mainTextBuffer.charAt(i);
            			 }
            		 }           		 
            	 }
             }
    	}
    }
    
    public boolean isTypeKeyWord(String token) {
    	for (int k = 0; k < keyWords.getTypes().length; k++) {
    		if (token.equals(keyWords.getTypes()[k]))
    				return true;
    	}
    	return false;
    }

    public boolean isChar(char c){
        if (c == '*' || c == '~' ||
                (65 <= (int) c && (int) c <= 90) ||
                (97 <= (int) c && (int) c <= 122)){
            return true;
        }
        return false;
    }

    public boolean isCharOrParenthesis(char c){
        if (c == '*' || c == '~' ||
                c == '(' || c == ')'||
                (65 <= (int) c && (int) c <= 90) ||
                (97 <= (int) c && (int) c <= 122)){
            return true;
        }
        return false;
    }

    public ArrayList<String> getDeclarationTokens(){
        return declarationTokens;
    }

    public ArrayList<String> getDefinitionTokens(){
        return definitionTokens;
    }
}