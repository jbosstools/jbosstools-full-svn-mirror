/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.preferences;

import java.util.List;

import org.jboss.tools.common.model.ui.objecteditor.*;
import org.eclipse.swt.graphics.*;

import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class TemplatesTableProvider implements XTableProvider, XTableImageProvider {
	static String[] COLUMNS = new String[] {
		VpeUIMessages.TemplatesTableProvider_TagName, 
		VpeUIMessages.TemplatesTableProvider_TagForDisplay,
		VpeUIMessages.TemplatesTableProvider_URI,
		VpeUIMessages.TemplatesTableProvider_Children};

	static int[] WIDTH = new int[]{200,150, 150,100};
	List dataList;
	
	public TemplatesTableProvider(List dataList){
		this.dataList = dataList;
	}

	public int getColumnCount() {
		return COLUMNS.length;
	}

	public int getRowCount() {
		if(dataList == null) return 0;
		return dataList.size();
	}

	public String getColumnName(int c) {
		return COLUMNS[c];
	}

	public String getValueAt(int r, int c) {
		VpeAnyData data = (VpeAnyData)dataList.get(r);
		switch(c){
			case 0:
			    return data.getName();
			case 1:
			    return data.getTagForDisplay();
			case 2:
			    return data.getUri();
			case 3:
				if(data.isChildren()) return VpeUIMessages.TemplatesTableProvider_Yes;
				else return VpeUIMessages.TemplatesTableProvider_No;
		}
		return "x"; //$NON-NLS-1$
	}

	public Object getDataAt(int r) {
		return null;
	}

	public Color getColor(int r) {
		return null;
	}

	public int getWidthHint(int c) {
		return WIDTH[c];
	}

	public void dispose() {
	}

	public Image getImage(int r) {
		return null;
	}

}
