/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.util;

import junit.framework.TestCase;

public class VpeStyleUtilTest extends TestCase {

	@SuppressWarnings("nls")
	public void testRemoveAllCssComments() {
		assertEquals("", VpeStyleUtil.removeAllCssComments(""));
		assertEquals(" ", VpeStyleUtil.removeAllCssComments("/*"));
		assertEquals(" ", VpeStyleUtil.removeAllCssComments("/**/"));
		assertEquals(" ", VpeStyleUtil.removeAllCssComments("/*/**/"));
		assertEquals(" ", VpeStyleUtil.removeAllCssComments("/*//**/"));
		assertEquals(" dddd ", VpeStyleUtil.removeAllCssComments("/*abcv*/dddd/**/"));
		assertEquals("bvc dddd ", VpeStyleUtil.removeAllCssComments("bvc/*abcv*/dddd/**/"));
		assertEquals("bvc dddd ", VpeStyleUtil.removeAllCssComments("bvc/*abcv*/dddd/*"));
	}

}
