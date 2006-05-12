/*
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;
import org.hibernate.netbeans.console.Icons;
import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.error.ErrorMultiViewDescription;
import org.hibernate.netbeans.console.output.error.ErrorMultiViewElement;
import org.hibernate.netbeans.console.output.result.QueryResultMultiViewDescription;
import org.hibernate.netbeans.console.output.sql.SqlMultiViewDescription;
import org.netbeans.core.api.multiview.MultiViewHandler;
import org.netbeans.core.api.multiview.MultiViewPerspective;
import org.netbeans.core.api.multiview.MultiViews;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.util.Utilities;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class SessionFactoryOutput {
    
    private static WeakHashMap<SessionFactoryDescriptor, TopComponent> descr2Tab =
            new WeakHashMap<SessionFactoryDescriptor, TopComponent>();
    
    private static WeakHashMap<SessionFactoryDescriptor, ErrorMultiViewDescription> descr2Error =
            new WeakHashMap<SessionFactoryDescriptor, ErrorMultiViewDescription>();

    private static WeakHashMap<SessionFactoryDescriptor, OutputMultiViewElement> descr2ActiveView =
            new WeakHashMap<SessionFactoryDescriptor, OutputMultiViewElement>();
    
    private static NameChangeListener nameChangeListener = new NameChangeListener();
    
    private static final long serialVersionUID = 1L;

    private SessionFactoryOutput() {
    }
    
    public static void clearError(SessionFactoryDescriptor desc) {
        ErrorMultiViewElement element = getErrorElement(desc, false);
        if (element != null) {
            element.clearError();
        }
    }
    
    public static void showError(SessionFactoryDescriptor desc, Throwable ex) {
        ErrorMultiViewElement element = getErrorElement(desc, true);
        element.showError(ex);
    }
    
    public static void closeOutputTab(SessionFactoryDescriptor descr) {
        TopComponent comp = descr2Tab.get(descr);
        if (comp != null) {
            comp.close();
        }
    } 
    
    private static ErrorMultiViewElement getErrorElement(SessionFactoryDescriptor sfDesc, boolean requestActive) {
        TopComponent comp = showOutputTab(sfDesc, false);
        MultiViewHandler handler = MultiViews.findMultiViewHandler(comp);
        ErrorMultiViewDescription desc = descr2Error.get(sfDesc);
        if (requestActive) {
            MultiViewPerspective[] perspectives = handler.getPerspectives();
            handler.requestActive(perspectives[perspectives.length - 1]);
        }
        return (ErrorMultiViewElement) desc.getElement();
    }
    
    public static TopComponent showOutputTab(final SessionFactoryDescriptor descr, boolean requestActive) {
        TopComponent comp = descr2Tab.get(descr);
        if (comp == null) {
            List<MultiViewDescription> multiViewDescrs = new ArrayList<MultiViewDescription>();
            MultiViewDescription result = new QueryResultMultiViewDescription(descr);
            multiViewDescrs.add(result);
            MultiViewDescription sql = new SqlMultiViewDescription(descr);
            multiViewDescrs.add(sql);
            OutputMultiViewDescription[] otherDescriptions = OutputFactory.createDescription(descr);
            if (otherDescriptions != null && otherDescriptions.length > 0) {
                multiViewDescrs.addAll(Arrays.asList(otherDescriptions));
            }
            ErrorMultiViewDescription error = new ErrorMultiViewDescription(descr);
            multiViewDescrs.add(error);
            comp = MultiViewFactory.createMultiView(
                    multiViewDescrs.toArray(new MultiViewDescription[multiViewDescrs.size()]),
                    result);
            comp.setIcon(Utilities.loadImage(Icons.HIBERNATE_SMALL));
            comp.setName(descr.getName());
            descr2Tab.put(descr, comp);
            descr2Error.put(descr, error);
            descr.removePropertyChangeListener(nameChangeListener);
            descr.addPropertyChangeListener(nameChangeListener);
        }
        if (!comp.isOpened()) {
            Mode m = WindowManager.getDefault().findMode("output");
            if (m == null) {
                m = WindowManager.getDefault().findMode("sessionFactoryOutput");
            }
            if (m != null) {
                m.dockInto(comp);
            }
            comp.open();
        }
        comp.requestVisible();
        if (requestActive) {
            comp.requestActive();
        }
        return comp;
    }
    
    private static class NameChangeListener implements PropertyChangeListener {
        
        public NameChangeListener() {
        }
        
        public void propertyChange(PropertyChangeEvent evt) {
            if (SessionFactoryDescriptor.PROPERTY_NAME.equals(evt.getPropertyName())) {
                SessionFactoryDescriptor descr = (SessionFactoryDescriptor) evt.getSource();
                TopComponent tabComponent = descr2Tab.get(descr);
                tabComponent.setName((String) evt.getNewValue());
            }
        }
    }

    public static void setCurrentNonErrorView(SessionFactoryDescriptor descr, OutputMultiViewElement view) {
        if (view != getErrorElement(descr, false)) {
            descr2ActiveView.put(descr, view);
        }
    }

    public static OutputMultiViewElement getCurrentNonErrorView(SessionFactoryDescriptor descr) {
        return descr2ActiveView.get(descr);
    }



}
