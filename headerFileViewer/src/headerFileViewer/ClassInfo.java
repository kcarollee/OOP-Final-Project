package headerFileViewer;

import java.util.ArrayList;

public class ClassInfo  {
    public String className;

    public int methodCount = 0;
    public int variableCount = 0;
    public ArrayList<String[]> tableArray = new ArrayList<String[]>();
    public ArrayList<String> declarationTokens;
    public ArrayList<String> definitionTokens;
    public ArrayList<MethodInfo> methodList = new ArrayList<MethodInfo>();
    public ArrayList<VariableInfo> variableList = new ArrayList<VariableInfo>();

    public KeyWords keyWords = new KeyWords();
    public String currentAccessSpecifier = new String();
    public String currentType = new String();

    
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

    
    public void processDeclarationTokens(){
      
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
           
            else if (isPointer(declarationTokens.get(i))) {
                String[] tableRow = new String[3];
                tableRow[0] = declarationTokens.get(i).substring(1); 
                tableRow[1] = currentType + "*";
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
  
    public void processDefinitionTokens() {
        for (int i = 0; i < definitionTokens.size(); i += 2) {
           
            MethodInfo method = new MethodInfo(definitionTokens.get(i), definitionTokens.get(i + 1));
            methodList.add(method);
            methodCount++;
            compareMethodTokensWithMemberVariables(method);
        }
    }

    public int getMethodCount(){
        return methodCount;
    }


    
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

  
    }

}