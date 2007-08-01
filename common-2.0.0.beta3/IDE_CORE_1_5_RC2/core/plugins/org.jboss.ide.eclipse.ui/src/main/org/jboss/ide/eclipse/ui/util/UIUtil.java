package org.jboss.ide.eclipse.ui.util;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class UIUtil {
	   public static void setEnabledRecursive (Composite composite, boolean enabled)
	   {
		   Control[] controls = composite.getChildren();

		   for (int i = 0; i < controls.length; i++)
		   {
			   if (controls[i] instanceof Composite)
			   {
				   setEnabledRecursive((Composite) controls[i], enabled);
			   }
			 controls[i].setEnabled(enabled);
		   }
	   }
}
