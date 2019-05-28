package headerFileViewer;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/************************************************************************************************************************************************
 *                                                    GETTER MANUAL
 * 
 * Class                                              Method                               Description 
 * Parser(String address)                             getTextBuffer()                      address의 주소에 있는 텍스트를 StringBuffer에 담아 그것을 반환
 * 
 * Tokenizer(StringBuffer mainText)                   getDeclarationTokens()               Stack.h의 선언 필드의 텍스트를 분석 및 토큰화하여 ArrayString 반환.
 *                                                    getDefinitionTokens()                Stack.h의 함수 정의 필드의 텍스트를 분석 및 토큰화하여 ArrayString 반환.
 * 
 * ClassInfo(ArrayString<String> decText,             getClassName()           		            클래스의 이름 반환
 *           ArrayString<String> defText)             
 *                                                    getName(int index)                   테이블 구조에 필요한 index번째에 있는 속한 메소드 및 변수 이름 스트링을 반환
 *                                                    getType(int index)                   테이블 구조에 필요한 index번째에 있는 타입 이름 스트링을 반환
 *                                                    getAccess(int index)                 테이블 구조에 필요한 index번째에 있는 속한 접근 지시자 스트링을 반환
 *                                                    (위 세 메소드의 index는 0부터 시작하고 같은 행에 있는 것들의 index는 같아야 한다는 것에 주의)
 *                                                    
 *                                                    getMethodName(int index)             트리 구조에 필요한 index번째에 있는 메소드의 이름 스트링을 반환
 *                                                    getFormattedMethodContents(int index)index번째에 있는 메소드의 내용 스트링을 반환
 *                                                    getVariablesUsedByMethod(int index)  index번째에 있는 메소드에 쓰인  변수 리스트를 반환
 *                                                    
 *                                                    getFormattedVariableName(int index)  트리 구조에 필요한 index번째에 있는 타입이 딸린 변수의 이름 스트링을 반환
 *                                                    getVariableName(int index)           index번째에 있는 변수의 이름 스트링을 반환
 *                                                    getMethodsThatUseVariable(int index) index번째에 있는 변수를 사용하는 메소드의 리스트를 반환
 *////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class MainApp extends JFrame{
    public MainApp(){
    	
    }
    public static void main(String[] args){
        Parser p = new Parser("bin\\Stack.h");
        Tokenizer t = new Tokenizer(p.getTextBuffer());
        System.out.println("---------------------Testing declaration tokens-----------------");
        for (String i : t.getDeclarationTokens()){
            System.out.println(i);
        }
        ClassInfo c = new ClassInfo(t.getDeclarationTokens(), t.getDefinitionTokens());
        System.out.println("---------------------Testing table------------------------------");
        c.printTable();
        System.out.println("---------------------Testing member variables-------------------");
        for (String i : c.getMemberVariables()) {
        	System.out.println(i);
        }
        System.out.println("---------------------Testing definition tokens------------------");
        for (String i : t.getDefinitionTokens()){
            System.out.println(i);
            System.out.println("-------------------------------------------------------------");
        }
        
        System.out.println("---------------------Testing methodInfo--------------------------");
        c.printMethods();
        System.out.println("------------------Testing VariableInfo---------------------------");
        c.printVariables();
        
        
        System.out.println("***********************FINAL TEST********************************");
        System.out.println("********************* TESTING TABLE******************************");
        for (int i = 0; i < 9; i++) {
        	System.out.println(c.getName(i) + " " + c.getType(i)+ " " + c.getAccess(i));
        }
        System.out.println("***********************TESTING TREE******************************");
        System.out.println("Class Name: " + c.getClassName());
        for (int i = 0; i < 6; i++) {
        	System.out.println("Method Name at index " + i + ": " + c.getMethodName(i));
        }
        for (int i = 0; i < 3; i++) {
        	System.out.println("Formatted variable name at index " + i + ": " + c.getFormattedVariableName(i));
        }
        System.out.println("******************TESTING METHOD CONTENTS************************");
        for (int i = 0; i < 6; i++) {
        	System.out.println("Formatted content of method at index " + i + ": ");
        	System.out.println(c.getFormattedMethodContents(i) + "\n");
        	System.out.println("Variables used in method at index " + i + ": ");
        	System.out.println(c.getVariablesUsedByMethod(i));
        	System.out.println("--------------------------------------------------------------");    
        }
        System.out.println("*******************TESTING VARIABLES*****************************");
        for (int i = 0; i < 3; i++) {
        	System.out.println("Variable name at index " + i + ": " + c.getVariableName(i));
        	System.out.println("Methods this variable is used in: ");
        	System.out.println(c.getMethodsThatUseVariable(i));
        	System.out.println("--------------------------------------------------------------");   
        }
        
    }
}


