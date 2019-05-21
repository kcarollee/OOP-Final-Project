package headerFileViewer;

import java.util.ArrayList;

public class Tokenizer {
    public ArrayList<String> mainTextTokens = new ArrayList<String>();
    public ArrayList<String> declarationTokens = new ArrayList<String>();
    public ArrayList<String> definitionTokens = new ArrayList<String>();
    public int index = 0 ;

    public int bracketNum = 0;
    public boolean noChar = true;
    public boolean commentMode = false;
    public boolean parenthesisOpen = false;
    public Tokenizer(StringBuffer mainTextBuffer){
        processDeclarations(mainTextBuffer);
        processTextBuffer(mainTextBuffer);
        processDefinitions(mainTextBuffer, index);
    }

    // 일반적인 토크나이저입니다. 순수 스트링을 받음.
    public void processTextBuffer(StringBuffer mainTextBuffer){
        String temp = "";
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
                        mainTextTokens.add(temp);
                        temp = "";
                    }
                }
            }
        }
    }

    // 이 메소드는 Table구조용에 쓰일 토크나이저입니다.
    public void processDeclarations(StringBuffer mainTextBuffer){
        String temp = "";
        for (int i = 0; i < mainTextBuffer.length(); i++){
            if (mainTextBuffer.charAt(i) == '{'){
                bracketNum++;
            }
            // 클래스 변수 선언 종료 확인
            if (mainTextBuffer.charAt(i) == '}' && mainTextBuffer.charAt(i + 1) == ';')
                break;
            // 아직 종료가 안됐다면
            else {
                // 주석인지 확인
                if ((mainTextBuffer.charAt(i) == '/' && mainTextBuffer.charAt(i + 1) == '/')) {
                    commentMode = true;
                }
                // 주석이라면
                if (commentMode) {
                    if (mainTextBuffer.charAt(i) == '\r')
                        // 다음줄을 읽을 차례이므로 commentMode는 false로 변환.
                        commentMode = false;
                    else
                        // 다음줄 캐릭터 '\r'을 읽을때까지 무시하기
                        continue;
                }
                // 주석이 아니라면
                else {
                    // 대괄호 처리. 첫번째 대괄호는 꼭 필요. 두번째 대괄호가
                    // 열린 순간 그 괄호가 닫힐때까지의 내용은 불필요.
                    if (bracketNum > 1){
                        if (mainTextBuffer.charAt(i) != '}')
                            continue;
                        else
                            bracketNum--;
                    }
                    else {
                    	// 소괄호가 열렸다면
                        if (mainTextBuffer.charAt(i) == '(') {
                            // 괄호 자체는 토큰에 들어가야합니다. 
                        	temp += mainTextBuffer.charAt(i);
                            // 열림 플래그를 true로 설정
                            parenthesisOpen = true;
                        }
                        else {
                        	// 소괄호가 열려있는 상태라면
                            if (parenthesisOpen) {
                            	// 소괄호가 닫혔다면
                                if (mainTextBuffer.charAt(i) == ')') {
                                    // 괄호 자체는 토큰에 들어가야 합니다. 
                                	temp += mainTextBuffer.charAt(i);
                                	// 열림 플래그를 false로 설정
                                    parenthesisOpen = false;
                                }
                                else
                                	// 괄호가 닫힐때까지 무시하기.
                                    continue;
                            }
                            // 소괄호 고려를 안해도 될 경우
                            else {
                                if (isCharOrParenthesis(mainTextBuffer.charAt(i))) {
                                    temp += mainTextBuffer.charAt(i);
                                    noChar = true;
                                }
                                else {
                                    if (noChar) {
                                        noChar = false;
                                        declarationTokens.add(temp);
                                        temp = "";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // 메소드 소스 코드 표시에 쓰일 메소드. 
    public void processDefinitions(StringBuffer mainTextBuffer, int index){
    	
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

    public ArrayList<String> getTokens(){
        return mainTextTokens;
    }

    public ArrayList<String> getDeclarationTokens(){
        return declarationTokens;
    }

    public ArrayList<String> getDefinitionTokens(){
        return definitionTokens;
    }
}
