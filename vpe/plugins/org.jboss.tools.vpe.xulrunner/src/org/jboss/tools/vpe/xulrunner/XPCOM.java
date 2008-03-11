/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.xulrunner;

/**
 * @author Sergey Vasilyev (svasilyev@exadel.com)
 *
 */
public final class XPCOM {
	private XPCOM() {}
	/*
	 * Contract IDs
	 */
	public static final String NS_DRAGSERVICE_CONTRACTID = "@mozilla.org/widget/dragservice;1"; //$NON-NLS-1$
	public static final String NS_TRANSFERABLE_CONTRACTID = "@mozilla.org/widget/transferable;1"; //$NON-NLS-1$
	public static final String NS_WINDOWWATCHER_CONTRACTID = "@mozilla.org/embedcomp/window-watcher;1"; //$NON-NLS-1$
	public static final String NS_PREFSERVICE_CONTRACTID = "@mozilla.org/preferences-service;1"; //$NON-NLS-1$
	public static final String NS_SUPPORTSSTRING_CONTRACTID = "@mozilla.org/supports-string;1"; //$NON-NLS-1$
	public static final String NS_SUPPORTSARRAY_CONTRACTID = "@mozilla.org/supports-array;1"; //$NON-NLS-1$
	
	public static final String IN_FLASHER_CONTRACTID = "@mozilla.org/inspector/flasher;1"; //$NON-NLS-1$
	
	public static final String NS_IWEBBROWSER_CID = "F1EAC761-87E9-11d3-AF80-00A024FFC08C"; //$NON-NLS-1$
	public static final String NS_IAPPSHELL_CID = "2d96b3df-c051-11d1-a827-0040959a28c9"; //$NON-NLS-1$
}
