package headerFileViewer;

import java.util.ArrayList;

public class VariableInfo {
	public String name;
	public String type;
	public ArrayList<MethodInfo> usedMethods = new ArrayList<MethodInfo>();
	
	public VariableInfo(String name) {
		this.name = name;
	}
	
	public void addVariableType(String type) {
		this.type = type;
	}
	
	public void addToUsedMethodList(MethodInfo method) {
		usedMethods.add(method);
	}
	
	public String getName() {
		return name;
	}
	
	public String getFormattedName() {
		return name + " : " + type;
	}
	
	
	// testing method
	public void showUsedMethods() {
		for (MethodInfo m : usedMethods) {
			System.out.print(m.name + ' ');
		}
		System.out.println();
	}
	
	
}
