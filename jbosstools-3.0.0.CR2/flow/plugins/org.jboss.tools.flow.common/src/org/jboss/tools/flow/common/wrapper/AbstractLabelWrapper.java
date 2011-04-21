package org.jboss.tools.flow.common.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

public abstract class AbstractLabelWrapper extends AbstractWrapper implements
		LabelWrapper {

    private transient List<ModelListener> listeners = new ArrayList<ModelListener>();
    private Point location = null;
    private Wrapper owner = null;
    
	public Point getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLocation(Point location) {
		// TODO Auto-generated method stub

	}

	public void setText(String text) {
		// TODO Auto-generated method stub

	}

	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}

}
