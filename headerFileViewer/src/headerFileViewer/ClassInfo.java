package headerFileViewer;

import java.util.ArrayList;

public class ClassInfo  {
    public String className;
    // 다음은 String 배열의 배열리스트입니다. 각각 String[]은 크기 3의 스트링 배열인데
    // 0인덱스에는 메소드의 이름, 1인덱스에는 메소드의 반환형, 2인덱스에는 접근지시자가 들어갑니다. 
    public ArrayList<String[]> tableArray = new ArrayList<String[]>();
    public ArrayList<String> declarationTokens;
    
    // 다음 두 배열리스트들은 Tree모델 구현에 쓰입니다. 
    public ArrayList<MethodInfo> methodList;
    public ArrayList<VariableInfo> variableList;
    
    public KeyWords keyWords = new KeyWords();
    public String currentAccessSpecifier = new String();
    public String currentType = new String();
    
    // MethodInfo에 쓰일 멤버 변수의 리스트입니다. 
    public ArrayList<String> memberVariables = new ArrayList<String>();
    
    public ClassInfo(ArrayList<String> declarationTokens){
        this.declarationTokens = declarationTokens;
        className = declarationTokens.get(1);
        processTokens();
        addMembersToList();
    }

    public String getClassName(){
        return className;
    }
    
    // 테이블용
    public void processTokens(){
    	// 클래스 정의 내부부터 시작하기
    	for (int i = 2; i < declarationTokens.size(); i++) {
    		
    		if (isAccessSpecifier(declarationTokens.get(i))) {
    			currentAccessSpecifier = declarationTokens.get(i);
    		}
    		
    		else if (isTypeKeyWord(declarationTokens.get(i))) {
    			currentType = declarationTokens.get(i);
    		}
    		
    		else if (isConstructorOrDestructor(declarationTokens.get(i))) {
    			String[] tableRow = new String[3];
    			currentType = "void";
    			tableRow[0] = formattedName(declarationTokens.get(i));
    			tableRow[1] = currentType;
    			tableRow[2] = currentAccessSpecifier;
    			tableArray.add(tableRow);
    		}
    		// 포인터 변수
    		else if (isPointer(declarationTokens.get(i))) {
    			String[] tableRow = new String[3];
    			tableRow[0] = declarationTokens.get(i).substring(1); // *ptr의 *를 없애주기
    			tableRow[1] = currentType + "*"; // int* 처럼 어떤 형식의 포인터인지 알려줌 
    			tableRow[2] = currentAccessSpecifier;
    			tableArray.add(tableRow);
    		}
    		
    		else {
    			String[] tableRow = new String[3];
    			tableRow[0] = formattedName(declarationTokens.get(i));
    			tableRow[1] = currentType;
    			tableRow[2] = currentAccessSpecifier;
    			tableArray.add(tableRow);
    			
    		}
    	}
    }
    
    public String formattedName(String name) {
    	String temp = new String();
    	boolean parenthesisOpen = false;
    	for (int i = 0; i < name.length(); i++) {
    		if (name.charAt(i) == '(') {
    			parenthesisOpen = true;
    			temp += name.charAt(i);
    		}
    		
    		else if (name.charAt(i) == ')') {
    			parenthesisOpen = false;
    			//temp += name.charAt(i);
    		}
    		if (parenthesisOpen) {
    			continue;
    		}
    		else
    			temp += name.charAt(i);
    	}  	
    	return temp;
    }
    
    public String getName(int index) {
    	return tableArray.get(index)[0];
    }
    
    public String getType(int index) {
    	return tableArray.get(index)[1];
    }
    
    public String getAccess(int index) {
    	return tableArray.get(index)[2];
    }
    
    public boolean isAccessSpecifier(String token) {
    	for (int k = 0; k < keyWords.getAccessSpecifiers().length; k++) {
    		if (token.equals(keyWords.getAccessSpecifiers()[k]))
    				return true;
    	}
    	return false;
    }
    
    public boolean isTypeKeyWord(String token) {
    	for (int k = 0; k < keyWords.getTypes().length; k++) {
    		if (token.equals(keyWords.getTypes()[k]))
    				return true;
    	}
    	return false;
    }
    
    public boolean isConstructorOrDestructor(String token) {
    	if (token.equals(className + "()") || token.equals("~" + className + "()"))
    		return true;
    	return false;
    }
    
    public boolean isPointer(String token) {
    	if (token.charAt(0) == '*') {
    		return true;
    	}
    	return false;
    }
    
    public void addMembersToList() {
    	for (int i = 0; i < tableArray.size(); i++) {
    		if (stringIsMethodName(tableArray.get(i)[0])) {
    			continue;
    		}
    		else
    			memberVariables.add(tableArray.get(i)[0]);
    	}
    }
    
    public ArrayList<String> getMemberVariables(){
    	return memberVariables;
    }
    public boolean stringIsMethodName(String name) {
    	if (name.charAt(name.length() - 2) == '(' && name.charAt(name.length() - 1) == ')') {
    		return true;
    	}
    	else
    		return false;
    }
    
    // testing method
    public void printTable() {
    	for (String[] row : tableArray) {
    		for (int i = 0; i < row.length; i++) {
    			System.out.print(row[i] + " ");
    		}
    		System.out.println();
    	}
    }
}
