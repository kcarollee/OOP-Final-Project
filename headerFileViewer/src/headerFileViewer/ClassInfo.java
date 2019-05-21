package headerFileViewer;

import java.util.ArrayList;

public class ClassInfo  {
    public String className;
    // 다음은 String 배열의 배열리스트입니다. 각각 String[]은 크기 3의 스트링 배열인데
    // 0인덱스에는 메소드의 이름, 1인덱스에는 메소드의 반환형, 2인덱스에는 접근지시자가 들어갑니다. 
    public ArrayList<String[]> tableArray = new ArrayList<String[]>();
    public ArrayList<String> declarationTokens;
    public KeyWords keyWords = new KeyWords();
    public String currentAccessSpecifier = new String();
    public String currentType = new String();

    public ClassInfo(ArrayList<String> declarationTokens){
        this.declarationTokens = declarationTokens;
        className = declarationTokens.get(1);
        processTokens();
    }

    public String getClassName(){
        return className;
    }

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
    			tableRow[0] = declarationTokens.get(i);
    			tableRow[1] = currentType;
    			tableRow[2] = currentAccessSpecifier;
    			tableArray.add(tableRow);
    		}
    		
    		else {
    			String[] tableRow = new String[3];
    			tableRow[0] = declarationTokens.get(i);
    			tableRow[1] = currentType;
    			tableRow[2] = currentAccessSpecifier;
    			tableArray.add(tableRow);
    			
    		}
    	}
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
