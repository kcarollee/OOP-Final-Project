package headerFileViewer;


public class KeyWords {
    public String[] AccessSpecifiers = {"public", "protected", "private"}; // 접근 지시자들
    public String[] Types = {"void", "bool", "int"}; // 변수형/반환형
    public String[] getAccessSpecifiers(){
        return AccessSpecifiers;
    }
   
    public String[] getTypes(){
        return Types;
    }
}
