package org.jboss.tools.vpe.spring.test.springtest;

public class FormBean {
	private boolean chechbox1Selected = false;
	private boolean chechbox2Selected = true;
	private boolean chechbox3Selected = false;
	
	public boolean isChechbox1Selected() {
		return chechbox1Selected;
	}
	public void setChechbox1Selected(boolean chechbox1Selected) {
		this.chechbox1Selected = chechbox1Selected;
	}
	public boolean isChechbox2Selected() {
		return chechbox2Selected;
	}
	public void setChechbox2Selected(boolean chechbox2Selected) {
		this.chechbox2Selected = chechbox2Selected;
	}
	public boolean isChechbox3Selected() {
		return chechbox3Selected;
	}
	public void setChechbox3Selected(boolean chechbox3Selected) {
		this.chechbox3Selected = chechbox3Selected;
	}
}
