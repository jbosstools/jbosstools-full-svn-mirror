/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.wizards;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class VpeImportExportWizardsUtils {

	/**
	 * Updates visual table with tags templates.
	 * 
	 * @param tagsTable updated tags list
	 * @param tagsList current tags list
	 * @param clearTagsTable clears current tags table
	 */
	public static void updateTagsTable(Table tagsTable, List<VpeAnyData> tagsList, boolean clearTagsTable) {
		/*
		 * Return when visual table hasn't been initialized.
		 */
		if(tagsTable == null || tagsTable.isDisposed() || tagsList == null) {
			return;
		}
		/*
		 * Remember current selection index 
		 * and restore it at the end.
		 */
		int selectionIndex = tagsTable.getSelectionIndex();
		/*
		 * Clear current visual table.
		 */
		if (clearTagsTable) {
			tagsTable.clearAll();
			tagsTable.update();
		}
		TableItem tableItem = null;
		/*
		 * Sort the templates
		 */
		Collections.sort(tagsList, new Comparator<VpeAnyData>() {
			public int compare(VpeAnyData o1, VpeAnyData o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		for (int i = 0; i < tagsList.size(); i++) {
			if(tagsTable.getItemCount() > i) {
				/*
				 * Use existed items
				 */
				tableItem = tagsTable.getItem(i);
			} else {
				/*
				 * Add new item
				 */
				tableItem = new TableItem(tagsTable, SWT.BORDER, i);
			}
			/*
			 * Fill in columns.
			 */
			String[] itemColumnsData = new String[tagsTable.getColumnCount()];
			for (int j = 0; j < itemColumnsData.length; j++) {
				/*
				 * Getting values from tagList
				 */
				itemColumnsData[j] = toVisualValue(getValueAt(tagsList, i, j));
			}
			/*
			 * Set cells text
			 */
			tableItem.setText(itemColumnsData);
		}
		
		/*
		 * Restoring selection index
		 */
		if (selectionIndex > 0 ) {
			 try {
					tagsTable.setSelection(selectionIndex);
				} catch (SWTException e) {
					VpePlugin.getDefault().logError(
							VpeUIMessages.COULD_NOT_SET_TABLE_SELECTION, e);
				}
		}
	}
	
	private static String getValueAt(List<VpeAnyData> tagsList, int row, int column) {
		String result = VpeUIMessages.LIST_IS_EMPTY;
		if ((null != tagsList) && ((row >= 0) && (tagsList.size() > 0) && (row < tagsList.size()))) {
			VpeAnyData tagItem = (VpeAnyData)tagsList.get(row);
			switch(column){
			case 0:
				result = tagItem.getName();
				break;
			case 1:
				result = tagItem.getTagForDisplay();
				break;
			case 2:
				result = tagItem.getUri();
				break;
			case 3:
				if(tagItem.isChildren()) {
					result = VpeUIMessages.TemplatesTableProvider_Yes;
				} else {
					result = VpeUIMessages.TemplatesTableProvider_No;
				}
				break;
			}
		}
		return result;
	}
	
	private static String toVisualValue(String v) {
		if(v == null) return ""; //$NON-NLS-1$
		if(v.indexOf('\n') >= 0) v = v.replace('\n', ' ');
		if(v.indexOf('\t') >= 0) v = v.replace('\t', ' ');
		if(v.indexOf('\r') >= 0) v = v.replace('\r', ' ');
		return v;
	}
	
}
