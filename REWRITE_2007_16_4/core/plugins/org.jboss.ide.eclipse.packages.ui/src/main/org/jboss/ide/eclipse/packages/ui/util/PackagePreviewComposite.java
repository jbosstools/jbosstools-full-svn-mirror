package org.jboss.ide.eclipse.packages.ui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class PackagePreviewComposite extends Composite {

	public PackagePreviewComposite (Composite parent, int style)
	{
		super(parent, style);
	}
	
	public PackagePreviewComposite (Composite parent)
	{
		this(parent, SWT.NONE);
	}
	
	
}
