package headerFileViewer;

import java.util.ArrayList;

public class VariableInfo {
	public String name;
	public String type;
	public ArrayList<MethodInfo> usedMethods = new ArrayList<MethodInfo>();
	
	public VariableInfo(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public void addToUsedMethodList(MethodInfo method) {
		usedMethods.add(method);
	}
}
