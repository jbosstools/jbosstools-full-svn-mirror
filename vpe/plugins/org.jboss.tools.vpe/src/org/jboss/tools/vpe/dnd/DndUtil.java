/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/


package org.jboss.tools.vpe.dnd;

import org.eclipse.swt.events.TypedEvent;
import org.jboss.tools.common.model.ui.editors.dnd.context.DropContext;
import org.jboss.tools.common.model.ui.editors.dnd.context.IDNDTextEditor;
import org.jboss.tools.vpe.editor.util.VpeDebugUtil;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDragService;
import org.mozilla.interfaces.nsIDragSession;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;


/**
 * The Class DndUtil.
 * 
 * @author Eugene Stherbin
 */
public class DndUtil {
    

    /** The Constant kTextMime. */
    public static final String kTextMime = "text/plain"; //$NON-NLS-1$

    /** The Constant kUnicodeMime. */
    public static final String kUnicodeMime = "text/unicode"; //$NON-NLS-1$

    /** The Constant kHTMLMime. */
    public static final String kHTMLMime = "text/html"; //$NON-NLS-1$

    /** The Constant kAOLMailMime. */
    public static final String kAOLMailMime = "AOLMAIL"; //$NON-NLS-1$

    /** The Constant kPNGImageMime. */
    public static final String kPNGImageMime = "image/png"; //$NON-NLS-1$

    /** The Constant kJPEGImageMime. */
    public static final String kJPEGImageMime = "image/jpg"; //$NON-NLS-1$

    /** The Constant kGIFImageMime. */
    public static final String kGIFImageMime = "image/gif"; //$NON-NLS-1$

    /** The Constant kFileMime. */
    public static final String kFileMime = "application/x-moz-file"; //$NON-NLS-1$

    /** The Constant kURLMime. */
    public static final String kURLMime = "text/x-moz-url"; //$NON-NLS-1$

    /** The Constant kURLDataMime. */
    public static final String kURLDataMime = "text/x-moz-url-data"; //$NON-NLS-1$

    /** The Constant kURLDescriptionMime. */
    public static final String kURLDescriptionMime = "text/x-moz-url-desc"; //$NON-NLS-1$

    /** The Constant kNativeImageMime. */
    public static final String kNativeImageMime = "application/x-moz-nativeimage"; //$NON-NLS-1$

    /** The Constant kNativeHTMLMime. */
    public static final String kNativeHTMLMime = "application/x-moz-nativehtml"; //$NON-NLS-1$

    /** The Constant kFilePromiseURLMime. */
    public static final String kFilePromiseURLMime = "application/x-moz-file-promise-url"; //$NON-NLS-1$

    /** The Constant kFilePromiseMime. */
    public static final String kFilePromiseMime = "application/x-moz-file-promise"; //$NON-NLS-1$

    /** The Constant kFilePromiseDirectoryMime. */
    public static final String kFilePromiseDirectoryMime = "application/x-moz-file-promise-dir"; //$NON-NLS-1$

    /** The Constant FLAVORS. */
    public static final String[] FLAVORS = { kTextMime, kUnicodeMime, kHTMLMime, kAOLMailMime, kPNGImageMime, kJPEGImageMime,
            kGIFImageMime, kFileMime, kURLMime, kURLDataMime, kURLDescriptionMime, kNativeImageMime, kNativeHTMLMime, kFilePromiseURLMime,
            kFilePromiseMime, kFilePromiseDirectoryMime };

    /**
     * The Constructor.
     */
    private DndUtil() {
        super();
    }

    /**
     * Fire dn D event.
     * 
     * @param dropContext the drop context
     * @param event the event
     * @param textEditor the text editor
     */
    public static void fireDnDEvent(DropContext dropContext, IDNDTextEditor textEditor, TypedEvent event) {

        dropContext.runDropCommand(textEditor, event);
    }

    /**
     * Gets the dn D file.
     * 
     * @param event the event
     * 
     * @return the dn D file
     */
    public static nsISupports getDnDValue(nsIDOMEvent event) {
        nsIServiceManager serviceManager = Mozilla.getInstance().getServiceManager();
        nsIComponentManager componentManager = Mozilla.getInstance().getComponentManager();
        nsIDragService dragService = (nsIDragService) serviceManager.getServiceByContractID("@mozilla.org/widget/dragservice;1", //$NON-NLS-1$
                nsIDragService.NS_IDRAGSERVICE_IID);
        final nsIDragSession dragSession = dragService.getCurrentSession();

        final nsITransferable iTransferable = (nsITransferable) componentManager.createInstanceByContractID(
                XPCOM.NS_TRANSFERABLE_CONTRACTID, null, nsITransferable.NS_ITRANSFERABLE_IID);

        for (String flavor1 : FLAVORS) {
            if (dragSession.isDataFlavorSupported(flavor1)) {
                iTransferable.addDataFlavor(flavor1);
            }
        }
        String[] aFlavor = { "" }; //$NON-NLS-1$
        long[] aDataLen = { 0 };
        nsISupports[] aData = { null };
        
        dragSession.getData(iTransferable, 0);
        iTransferable.getAnyTransferData(aFlavor, aData, aDataLen);
        return aData[0];
    }

}
