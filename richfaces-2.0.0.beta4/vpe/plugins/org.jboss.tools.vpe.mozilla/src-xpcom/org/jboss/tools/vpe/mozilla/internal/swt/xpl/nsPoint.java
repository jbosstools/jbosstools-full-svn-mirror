package org.jboss.tools.vpe.mozilla.internal.swt.xpl;

/**
 * Java implementation of Mozilla's nsPoint class
 * for more information see
 * http://lxr.mozilla.org/seamonkey/source/gfx/public/nsPoint.h
 */
public class nsPoint {
	private int handle;

	public nsPoint(int aX, int aY) {
		handle = XPCOM.nsPoint_new(aX,aY);
	}

	public void MoveTo(int aX, int aY) {
		XPCOM.nsPoint_moveTo(handle, aX, aY);
	}

	public void MoveBy(int aX, int aY) {
		XPCOM.nsPoint_moveBy(handle, aX, aY);
	}
	
	public int GetX() {
		return XPCOM.nsPoint_getX(handle);
	}
	
	public int GetY() {
		return XPCOM.nsPoint_getY(handle);
	}

	public int getAddress() {
		return handle;
	}
	
	public void dispose() {
		if (handle == 0) return;			
		XPCOM.nsPoint_delete(handle);
		handle = 0; 	
	}
	
	public String toString() {
		return "Coordinates: X = " + GetX() + " Y = " + GetY();
	}
}
