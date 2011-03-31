package org.jboss.tools.internal.deltacloud.ui.utils;

import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;

/**
 * A listener that shows/hides given control decorations on changes in the
 * validation status it listens to.
 * 
 * @author André Dietisheim
 */
public class ControlDecorationAdapter implements IValueChangeListener {
	private ControlDecoration decoration;

	public ControlDecorationAdapter(ControlDecoration decoration, IStatus initialStatus) {
		this.decoration = decoration;
		setDecorationVisible(initialStatus);
	}

	@Override
	public void handleValueChange(ValueChangeEvent event) {
		if (event.diff.getNewValue() instanceof IStatus) {
			IStatus status = (IStatus) event.diff.getNewValue();
			setDecorationVisible(status);
		}
	}

	private void setDecorationVisible(IStatus status) {
		if (!status.isOK()) {
			decoration.show();
		} else {
			decoration.hide();
		}
	}
}
