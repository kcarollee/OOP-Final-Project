package headerFileViewer;

import java.util.ArrayList;

public class ClassInfo  {
    public String className;

    public int methodCount = 0;
    public int variableCount = 0;
    // 다음은 String 배열의 배열리스트입니다. 각각 String[]은 크기 3의 스트링 배열인데
    // 0인덱스에는 메소드의 이름, 1인덱스에는 메소드의 반환형, 2인덱스에는 접근지시자가 들어갑니다.
    public ArrayList<String[]> tableArray = new ArrayList<String[]>();
    public ArrayList<String> declarationTokens;
    public ArrayList<String> definitionTokens;

    // 다음 두 배열리스트들은 Tree모델 구현에 쓰입니다.
    public ArrayList<MethodInfo> methodList = new ArrayList<MethodInfo>();
    public ArrayList<VariableInfo> variableList = new ArrayList<VariableInfo>();

    public KeyWords keyWords = new KeyWords();
    public String currentAccessSpecifier = new String();
    public String currentType = new String();

    // MethodInfo에 쓰일 멤버 변수의 리스트입니다.
    public ArrayList<String> memberVariables = new ArrayList<String>();

    public ClassInfo(ArrayList<String> declarationTokens, ArrayList<String> definitionTokens){
        this.declarationTokens = declarationTokens;
        this.definitionTokens = definitionTokens;
        className = declarationTokens.get(1);
        processDeclarationTokens();
        addMembersToList();
        processDefinitionTokens();
        addVariableInfo();
        addMethodsToVariableInfo();
    }

    public String getClassName(){
        return className;
    }

    // 테이블용
    public void processDeclarationTokens(){
        // 클래스 정의 내부부터 시작하기
        for (int i = 2; i < declarationTokens.size(); i++) {

            if (isAccessSpecifier(declarationTokens.get(i))) {
                currentAccessSpecifier = declarationTokens.get(i);
            }

            else if (isTypeKeyWord(declarationTokens.get(i))) {
                currentType = declarationTokens.get(i);
            }

            else if (isConstructorOrDestructor(formattedName(declarationTokens.get(i)))) {
                String[] tableRow = new String[3];
                currentType = "void";
                tableRow[0] = declarationTokens.get(i);
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
                tableRow[0] = declarationTokens.get(i);
                tableRow[1] = currentType;
                tableRow[2] = currentAccessSpecifier;
                tableArray.add(tableRow);

            }
        }
    }
    // 트리 및 메소드 선택용
    public void processDefinitionTokens() {
        for (int i = 0; i < definitionTokens.size(); i += 2) {
            // i가 짝수일 경우 메소드 이름, 홀수일 경우 메소드 내용.
            MethodInfo method = new MethodInfo(definitionTokens.get(i), definitionTokens.get(i + 1));
            methodList.add(method);
            methodCount++;
            compareMethodTokensWithMemberVariables(method);
        }
    }

    public int getMethodCount(){
        return methodCount;
    }


    // 자료 선택용
    public void addVariableInfo() {
        for (String[] s : tableArray) {
            if (stringIsMethodName(s[0]))
                continue;
            else {
                VariableInfo var = new VariableInfo(s[0]);
                var.addVariableType(s[1]);
                variableList.add(var);
                variableCount++;
            }
        }
    }

    public int getVariableCount(){
        return variableCount;
    }
    // 자료 선택시 비교용
    public void addMethodsToVariableInfo() {
        for (VariableInfo variable : variableList) {
            for (MethodInfo method : methodList) {
                for (String token : method.getPureStringTokens()) {
                    if (variable.getName().equals(token)) {
                        variable.addToUsedMethodList(method);
                        break;
                    }
                }
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

    //----------------------------IMPORTANT GETTERS------------------------------//
    public String getName(int index) {
        return tableArray.get(index)[0];
    }

    public String getType(int index) {
        return tableArray.get(index)[1];
    }

    public String getAccess(int index) {
        return tableArray.get(index)[2];
    }

    public String getMethodName(int index) {
        return methodList.get(index).getName();
    }

    public String getVariableName(int index) {
        return variableList.get(index).getName();
    }

    public String getFormattedVariableName(int index) {
        return variableList.get(index).getFormattedName();
    }

    public String getFormattedMethodContents(int index) {
        String temp = "";
        for (int i = 0; i < methodList.get(index).methodSplitByNewLine.size(); i++) {
            temp += (methodList.get(index).methodSplitByNewLine.get(i));
            temp += '\n';
        }
        return temp;
    }

    public String getVariablesUsedByMethod(int index) {
        String temp = "";
        for (int i = 0; i < methodList.get(index).memberVariables.size(); i++) {
            temp += (methodList.get(index).memberVariables.get(i));
            temp += '\n';
        }
        return temp;
    }

    public String getMethodsThatUseVariable(int index) {
        String temp = "";
        for (int i = 0; i < variableList.get(index).usedMethods.size(); i++) {
            if (i == variableList.get(index).usedMethods.size() - 1)
                temp += (variableList.get(index).usedMethods.get(i).getName());
            else {
                temp += (variableList.get(index).usedMethods.get(i).getName());
                temp += ", ";
            }
        }
        return temp;
    }

//---------------------------------------------------------------------------//

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

    public void compareMethodTokensWithMemberVariables(MethodInfo method) {

        boolean[] memberAdded = new boolean[memberVariables.size()];
        for (int i = 0; i < memberVariables.size(); i++) {
            memberAdded[i] = false;
        }

        for (int i = 0; i < memberVariables.size(); i++) {
            for (int j = 0; j < method.getPureStringTokens().size(); j++) {

                if (memberVariables.get(i).equals(method.getPureStringTokens().get(j))) {
                    if (memberAdded[i]) {
                        continue;
                    }
                    else {
                        method.addToMemberVariables(memberVariables.get(i));
                        memberAdded[i] = true;
                    }
                }
            }
        }


    }
    public boolean stringIsMethodName(String name) {

        boolean hasOpeningParen = false;
        boolean hasClosingParen = false;

        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '(')
                hasOpeningParen = true;
            if (name.charAt(i) == ')')
                hasClosingParen = true;
        }

        if (hasOpeningParen && hasClosingParen)
            return true;
        return false;

    	/*

    	if (name.charAt(name.length() - 2) == '(' && name.charAt(name.length() - 1) == ')') {
    		return true;
    	}
    	else
    		return false;
    	*/
    }



    // testing table
    public void printTable() {
        for (String[] row : tableArray) {
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i] + " ");
            }
            System.out.println();
        }
    }

    // testing methodInfo
    public void printMethods() {
        for (MethodInfo m : methodList) {
            System.out.println("------------------Method Name-------------------");
            System.out.println(m.getName());
            System.out.println("----------------Method Contents 2-----------------");
            m.showMethodContents();
            System.out.println("-----------------Method Tokens---------------------");
            m.showPureStringTokens();
            System.out.println("------------Method's Member Variables--------------");
            m.showUsedMemberVariables();
        }
    }

    // testing variableInfo
    public void printVariables() {
        for (VariableInfo v : variableList) {
            System.out.println("----------------Variable Name--------------------");
            System.out.println(v.getName());
            System.out.println("--------Methods this variable is used in---------");
            v.showUsedMethods();
        }
    }
}
