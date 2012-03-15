/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.ui;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Common constants and methods used with JBoss Tool's ModeShape Tools project.
 */
public final class UiUtils {

    /**
     * An empty string constant.
     */
    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    /**
     * The column will be packed using the header text.
     * 
     * @param viewerColumn the viewer column (cannot be <code>null</code>)
     * @param labelProvider the column label provider (cannot be <code>null</code>
     * @param headerText the header text (cannot be <code>null</code>)
     * @param headerToolTip (can be <code>null</code>)
     * @param moveable a flag indicating if the column can be moved
     * @param resizable a flag indicating if the column can be resized
     */
    public static void configureColumn( final TableViewerColumn viewerColumn,
                                        final CellLabelProvider labelProvider,
                                        final String headerText,
                                        final String headerToolTip,
                                        final boolean moveable,
                                        final boolean resizable ) {
        viewerColumn.setLabelProvider(labelProvider);

        // configure column
        final TableColumn column = viewerColumn.getColumn();
        column.setText(headerText);
        column.setToolTipText(headerToolTip);
        column.setMoveable(false);
        column.setResizable(resizable);
        column.pack();
    }

    /**
     * @param stringBeingChecked the string being checked (can be <code>null</code> or empty)
     * @return <code>true</code> if <code>null</code> or empty
     */
    public static boolean isEmpty( final String stringBeingChecked ) {
        return ((stringBeingChecked == null) || stringBeingChecked.isEmpty());
    }

    /**
     * @param viewers the viewers whose columns will be packed (cannot be <code>null</code>)
     */
    public static void pack( final TableViewer... viewers ) {
        for (final TableViewer viewer : viewers) {
            for (final TableColumn column : viewer.getTable().getColumns()) {
                column.pack();
            }
        }
    }

    /**
     * Don't allow construction.
     */
    private UiUtils() {
        // nothing to do
    }
}
