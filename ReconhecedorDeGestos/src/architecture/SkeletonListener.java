package architecture;

import processing.core.PApplet;

public abstract class SkeletonListener {
	
	private PApplet main;
	private SkeletonAnalyzer skeletonAnalyzer;
	
	private RegisteredAction onAction;
	private RegisteredAction offAction;
	private RegisteredAction updateAction;
	
	public SkeletonListener(PApplet main, SkeletonAnalyzer skeletonAnalyzer) {
		this.main = main;
		this.skeletonAnalyzer = skeletonAnalyzer;
		
		this.skeletonAnalyzer.registerOnAction("onAction", this);
		this.skeletonAnalyzer.registerOffAction("offAction", this);
		//this.skeletonAnalyzer.registerUpdateAction("updateAction", this, new Class[1]);
	}

	public void registerOnAction(String method, Object o){
		this.onAction = new RegisteredAction(method, o);
	}
	
	public void registerOffAction(String method, Object o){
		this.offAction = new RegisteredAction(method, o);
	}
	
	public void registerUpdateAction(String method, Object o, Class[] cargs){
		this.updateAction = new RegisteredAction(method, o, cargs);
	}
	
	public void onAction(){
		this.onAction.invoke();
	}
	
	public void offAction(){
		this.offAction.invoke();
	}
	
	public void updateAction(Object[] oargs){
		this.updateAction.invoke(oargs);
	}
	
	public abstract void update();

}
