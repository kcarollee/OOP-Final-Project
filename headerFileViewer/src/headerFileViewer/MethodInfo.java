package headerFileViewer;

import java.util.ArrayList;

public class MethodInfo {
	public String name;
	public ArrayList<String> memberVariables;
	public String methodContents;
	
	public MethodInfo(String name, String methodContents) {
		this.name = name;
		this.methodContents = methodContents;
		formatMethodContents();
	}
	
	public void formatMethodContents() {
		
	}
	
	public String getMethodContents() {
		return methodContents;
	}
	
	public String getName() {
		return name;
	}
}

