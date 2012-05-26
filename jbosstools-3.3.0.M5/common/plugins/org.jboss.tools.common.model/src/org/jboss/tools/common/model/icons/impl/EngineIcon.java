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
package org.jboss.tools.common.model.icons.impl;

import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.model.*;

public class EngineIcon implements ImageComponent {

    public EngineIcon() {}

    public int getHash(XModelObject obj) {
        return (true) ? 72614 : 37158;
    }


    public Image getImage(XModelObject obj) {
            boolean b = true;
            String s = (b) ? "engine.running" : "engine.stopped"; //$NON-NLS-1$ //$NON-NLS-2$
            return obj.getModelEntity().getMetaModel().getIconList().getImage(s, "default.unknown"); //$NON-NLS-1$
    }

}
