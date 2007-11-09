/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


package org.hibernate.netbeans.console.output;

import java.io.Serializable;
import javax.swing.Action;
import org.hibernate.netbeans.console.BshCode;
import org.hibernate.netbeans.console.BshCodeProcessor;
import org.hibernate.netbeans.console.HqlQuery;
import org.hibernate.netbeans.console.HqlProcessor;
import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.netbeans.core.api.multiview.MultiViewHandler;
import org.netbeans.core.api.multiview.MultiViewPerspective;
import org.netbeans.core.api.multiview.MultiViews;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public abstract class OutputMultiViewElement implements HqlProcessor, BshCodeProcessor, MultiViewElement, Serializable {

    private SessionFactoryDescriptor descriptor;

    private transient boolean showing;
    
    private transient MultiViewElementCallback callback;
    
    public OutputMultiViewElement(SessionFactoryDescriptor descr) {
        this.descriptor = descr;
    }

    public void componentOpened() {
        SessionFactoryOutput.setCurrentNonErrorView(descriptor, this);
        showing = true;
        unregisterProcessors();
        registerProcessors();
    }

    private void registerProcessors() {
        descriptor.registerBshProcessor(this);
        descriptor.registerQueryProcessor(this);
    }

    private void unregisterProcessors() {
        descriptor.unregisterHqlProcessor(this);
        descriptor.unregisterBshProcessor(this);
    }

    public void componentClosed() {
        showing = false;
        unregisterProcessors();
    }

    public void componentShowing() {
        showing = true;
        SessionFactoryOutput.setCurrentNonErrorView(descriptor, this);
        unregisterProcessors();
        registerProcessors();
    }

    public void componentHidden() {
        showing = false;
    }

    public void componentActivated() {
        SessionFactoryOutput.setCurrentNonErrorView(descriptor, this);
        showing = true;
    }

    public void componentDeactivated() {
    }

    public void maybeExecuteQuery(HqlQuery query) {
        if (showing || (!showing && isErrorElementVisible() &&
                SessionFactoryOutput.getCurrentNonErrorView(descriptor) == this && query.getException() == null)) {
            SessionFactoryOutput.clearError(descriptor);
            executeQuery(query);
        }
    }

    public void maybeExecuteBshCode(BshCode code) {
        if (showing || (!showing && isErrorElementVisible() &&
                SessionFactoryOutput.getCurrentNonErrorView(descriptor) == this && code.getException() == null)) {
            SessionFactoryOutput.clearError(descriptor);
            executeBshCode(code);
        }
    }
    

    
    public SessionFactoryDescriptor getDescriptor() {
        return descriptor;
    }

    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }


    public Action[] getActions() {
        if (callback != null) {
            return callback.createDefaultActions();
        } else {
            return new Action[0];
        }
    }

    public UndoRedo getUndoRedo() {
        return null;
    }

    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }
    
    public abstract void executeQuery(HqlQuery q);
    
    public abstract void executeBshCode(BshCode cc);

    // TODO - move this to SessionFactoryOutput
    protected boolean isErrorElementVisible() {
        TopComponent tc = callback.getTopComponent();
        MultiViewHandler handler = MultiViews.findMultiViewHandler(tc);
        MultiViewPerspective persp = handler.getSelectedPerspective();
        MultiViewPerspective[] perspectives = handler.getPerspectives();
        return perspectives[perspectives.length - 1] == persp;
    }
    
    protected void requestVisible(final boolean hideError, final boolean requestActive) {
        if (isErrorElementVisible() && !hideError) {
            return;
        }
        callback.requestVisible();
    }
    
    public abstract void requestFocus();

}
