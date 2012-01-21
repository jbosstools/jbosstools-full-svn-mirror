/******************************************************************************* 
 * Copyright (c) 2009-2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.tests;

import org.eclipse.core.resources.IMarker;

/**
 * @author Alexey Kazakov
 */
public interface IMarkerFilter {

	boolean accept(IMarker marker);
}