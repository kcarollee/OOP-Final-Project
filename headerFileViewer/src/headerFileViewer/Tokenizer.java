package headerFileViewer;

import java.util.ArrayList;

public class Tokenizer {
    public ArrayList<String> declarationTokens = new ArrayList<String>();
    public ArrayList<String> definitionTokens = new ArrayList<String>();
    public int index = 0 ;
    public int bracketNum = 0;
    public boolean noChar = true;
    public boolean commentMode = false;
    public boolean parenthesisOpen = false;
    public KeyWords keyWords = new KeyWords();
    
    
    // ������ �´ٴ� �����Ͽ��� �۵��Ǵ� ���ȣ ���� Ŭ���� 
    public class BracketStack{
    	private ArrayList<Character> brackets = new ArrayList<Character>();
    	private void addBracket(char c) {
    		Character temp = c;
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
    
    
    // 선언부가 끝났는지 판별하는 메소드 
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
    
   
    // �� �޼ҵ�� Table�����뿡 ���� ��ũ�������Դϴ�.
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
            // Ŭ���� ���� ���� ���� Ȯ��
            if (declarationEnded(mainTextBuffer, i)) {
            	index++;
                break;
            }
            // ���� ���ᰡ �ȵƴٸ�
            else {
                // �ּ����� Ȯ��
                if ((mainTextBuffer.charAt(i) == '/' && mainTextBuffer.charAt(i + 1) == '/')) {
                
                	commentMode = true;
                }
                // �ּ��̶��
                if (commentMode) {
                    if (mainTextBuffer.charAt(i) == '\r') {
                    	index++;
                    	// �������� ���� �����̹Ƿ� commentMode�� false�� ��ȯ.
                        commentMode = false;
                    }
                    else {
                    	index++;
                    	// ������ ĳ���� '\r'�� ���������� �����ϱ�
                        continue;
                    }
                }
                // �ּ��� �ƴ϶��
                else {
                    // ���ȣ ó��. ù��° ���ȣ�� �� �ʿ�. �ι�° ���ȣ��
                    // ������ �޼ҵ� ���� ��Ʈ���� �ֱ� 
                    if (bracketNum > 1){
                        if (mainTextBuffer.charAt(i) != '}') {
                        	index++;
                        	if (!addedToDefinitionTokens) {
                        		definitionTokens.add(temp);
                        		addedToDefinitionTokens = true;
                        	}
                        	if (mainTextBuffer.charAt(i) != '{')
                        		methodContentWithin += mainTextBuffer.charAt(i);
                        	continue;
                        }
                        else {
                        	definitionTokens.add(methodContentWithin);
                        	methodContentWithin = "";
                        	index++;
                            bracketNum--;
                        }
                    }
                    else {
                    	// �Ұ�ȣ�� ���ȴٸ�
                        if (mainTextBuffer.charAt(i) == '(') {
                        	index++;
                        	// ��ȣ ��ü�� ��ū�� �����մϴ�. 
                        	temp += mainTextBuffer.charAt(i);
                            // ���� �÷��׸� true�� ����
                            parenthesisOpen = true;
                        }
                        else {
                        	// �Ұ�ȣ�� �����ִ� ���¶��
                            if (parenthesisOpen) {
                            	// �Ұ�ȣ�� �����ٸ�
                                if (mainTextBuffer.charAt(i) == ')') {
                                	index++;
                                	// ��ȣ ��ü�� ��ū�� ���� �մϴ�. 
                                	temp += mainTextBuffer.charAt(i);
                                	// ���� �÷��׸� false�� ����
                                    parenthesisOpen = false;
                                }
                                // �Լ��� ������ �������� ���. 
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
                                	// ��ȣ�� ���������� �����ϱ�.
                                    continue;
                                	}
                                }
                            }
                            // �Ұ�ȣ ����� ���ص� �� ���
                            else {
                                if (isCharOrParenthesis(mainTextBuffer.charAt(i))) {
                                	index++;
                                	temp += mainTextBuffer.charAt(i);
                                    noChar = true;
                                }
                                else {
                                    if (noChar) {
                                    	index++;
                                    	noChar = false;
                                        declarationTokens.add(temp);
                                        temp = "";
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
    
    // �޼ҵ� �ҽ� �ڵ� ǥ�ÿ� ���� �޼ҵ�. 
    public void processDefinitions(StringBuffer mainTextBuffer){
    	commentMode = false;
    	boolean blockInitiated = false;
    	boolean gettingMethodName = false;
    	boolean methodNameParsed = false; 
    	String temp = "";
        String temp2 = "";
        String temp3 = ""; // used for method information tokens: split by \r
    	for (int i = index; i < mainTextBuffer.length(); i++) {
    		
    		 if ((mainTextBuffer.charAt(i) == '/' && mainTextBuffer.charAt(i + 1) == '/')) {
             	commentMode = true;
             }
             // �ּ��̶��
             if (commentMode) {
                 if (mainTextBuffer.charAt(i) == '\r') {
                 	
                 	// �������� ���� �����̹Ƿ� commentMode�� false�� ��ȯ.
                     commentMode = false;
                 }
                 else {
                 	
                 	// ������ ĳ���� '\r'�� ���������� �����ϱ�
                     continue;
                 }
             }
             // �ּ��� �ƴ϶��
             else {
            	 // ::���ķκ��� �޼ҵ� �̸� �ޱ� 
            	 if ((mainTextBuffer.charAt(i) == ':' && mainTextBuffer.charAt(i + 1) == ':')) {
            		 gettingMethodName = true;
            	 }
            	 
            	 if (gettingMethodName) {
            		// �Ұ�ȣ�� ���ȴٸ�
                     if (mainTextBuffer.charAt(i) == '(') {
                     	
                     	// ��ȣ ��ü�� ��ū�� �����մϴ�. 
                     	temp += mainTextBuffer.charAt(i);
                         // ���� �÷��׸� true�� ����
                         parenthesisOpen = true;
                     }
                     else {
                     	// �Ұ�ȣ�� �����ִ� ���¶��
                         if (parenthesisOpen) {
                         	// �Ұ�ȣ�� �����ٸ�
                             if (mainTextBuffer.charAt(i) == ')') {
                             	
                             	// ��ȣ ��ü�� ��ū�� ���� �մϴ�. 
                             	temp += mainTextBuffer.charAt(i);
                             	// ���� �÷��׸� false�� ����
                                 parenthesisOpen = false;
                                 methodNameParsed = true;
                             }
                             // �Լ��� ������ �������� ���. 
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
                             	else {
                             	
                             	// ��ȣ�� ���������� �����ϱ�.
                                 continue;
                             	}
                             }
                         }
                         // �Ұ�ȣ ����� ���ص� �� ���
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
            		 else {            			
            			 continue;
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