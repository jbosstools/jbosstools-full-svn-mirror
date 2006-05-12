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
package org.hibernate.netbeans.console;

import java.awt.Dialog;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Action;
import org.hibernate.netbeans.console.editor.EditorContentCookie;
import org.hibernate.netbeans.console.editor.bsh.BshMultiViewDescription;
import org.hibernate.netbeans.console.editor.hql.HqlMultiViewDescription;
import org.hibernate.netbeans.console.editor.hql.HqlMultiViewElement;
import org.hibernate.netbeans.console.editor.hql.HqlEditorCookie;
import org.hibernate.netbeans.console.option.Options;
import org.hibernate.netbeans.console.output.LoggingOutput;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.netbeans.core.api.multiview.MultiViewHandler;
import org.netbeans.core.api.multiview.MultiViews;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.ErrorManager;
import org.openide.actions.OpenAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class SessionFactoryNode extends AbstractNode implements SessionFactoryCookie, HqlEditorCookie, EditorContentCookie, CookieSet.Factory {

    private Thread shutdownHook;

    private SessionFactoryDescriptor descriptor;

    private TopComponent editorTopComponent;
    
    private final static Image ICON = Utilities.loadImage(Icons.HIBERNATE_SMALL);
    
    private HqlMultiViewDescription hqlDescription;
    
    private BshMultiViewDescription bshDescription;

    public SessionFactoryNode(SessionFactoryDescriptor descriptor) {
        super(Children.LEAF);
        this.descriptor = descriptor;
        CookieSet cs = getCookieSet();
        cs.add(SessionFactoryCookie.class, this);
        cs.add(HqlEditorCookie.class, this);
        descriptor.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if (SessionFactoryDescriptor.PROPERTY_NAME.equals(propertyChangeEvent.getPropertyName())) {
                    setName((String) propertyChangeEvent.getNewValue());
                }
            }
        });
        setName(descriptor.getName());
        shutdownHook = new Thread(new Runnable() {
            public void run() {
                if (hqlDescription != null) {
                    hqlDescription.save();
                }
                if (bshDescription != null) {
                    bshDescription.save();
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public Image getIcon(int type) {
        return ICON;
    }


    public Action getPreferredAction() {
        return SystemAction.get(OpenAction.class);
    }
    
    public Action[] getActions(boolean context) {
        return new Action[] {
            SystemAction.get(OpenReloadAction.class),
            SystemAction.get(ConfigureSessionFactoryAction.class),
            null,
            SystemAction.get(UpdateSchemaAction.class),
            null,
            SystemAction.get(CopySessionFactoryAction.class),
            SystemAction.get(DeleteSessionFactoryAction.class),
        };
    }
    
    public static SessionFactoryNode[] listAll() {
        SessionFactoryDescriptor[] descs = SessionFactoryDescriptor.listAll();
        SessionFactoryNode nodes[] = new SessionFactoryNode[descs.length];
        for (int i = 0; i < descs.length; i++) {
            nodes[i] = new SessionFactoryNode(descs[i]);
        }
        return nodes;
    }

    public void delete() {
        try {
            descriptor.getStorageFile().delete();
            destroy();
        } catch (IOException ex) {
            ErrorManager.getDefault().notify(ex);
        }
    }

    public void destroy() throws IOException {
        super.destroy();
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
    }

    public void reload() {
        if (editorTopComponent == null) {
            open();
        } else {
            descriptor.reload();
            editorTopComponent.open();
            editorTopComponent.requestActive();
        }
    }

    public boolean configure() {
        SessionFactoryConfigurationPanel cfgPanel = new SessionFactoryConfigurationPanel(descriptor);
        DialogDescriptor dialDesc = new DialogDescriptor(cfgPanel, NbBundle.getMessage(SessionFactoryNode.class, "LBL_ConfigureSessionFactory"), true, // NOI18N
                DialogDescriptor.OK_CANCEL_OPTION, null, null);
        Dialog dial = DialogDisplayer.getDefault().createDialog(dialDesc);
        dial.setVisible(true);
        if (DialogDescriptor.OK_OPTION.equals(dialDesc.getValue())) {
            try {
                SessionFactoryDescriptor desc = cfgPanel.updateOrCreateDescriptor();
                desc.persist();
                return true;
            } catch (Exception ex) {
                ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
            }
        } 
        return false;
    }

    public void open() {
        if (editorTopComponent == null) {
            hqlDescription = new HqlMultiViewDescription(this);
            bshDescription = new BshMultiViewDescription(this);
            editorTopComponent = MultiViewFactory.createMultiView(
                    new MultiViewDescription[] { hqlDescription, bshDescription }, 
                    hqlDescription);
            editorTopComponent.setDisplayName(getDisplayName());
            final TopComponent.Registry registry = editorTopComponent.getRegistry();
            registry.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (TopComponent.Registry.PROP_OPENED.equals(propertyChangeEvent.getPropertyName()) &&
                            !registry.getOpened().contains(editorTopComponent)) {
                        registry.removePropertyChangeListener(this);
                        SessionFactoryOutput.closeOutputTab(descriptor);
                        // TODO - close the logging window maybe
                        descriptor.close();
                        hqlDescription.save();
                        bshDescription.save();
                        editorTopComponent = null;
                        closeLoggingWindowEventually();
                    }
                }
            });
        }
        editorTopComponent.open();
        descriptor.openSession();
        SessionFactoryOutput.showOutputTab(descriptor, false);
        editorTopComponent.requestActive();
        if (Options.get().isShowLoggingWindow()) {
            LoggingOutput.showLoggingWindow(false);
        }
    }
    
    private void closeLoggingWindowEventually() {
        Set comps = TopComponent.getRegistry().getOpened();
        boolean editorOpen = false;
        for (Iterator it = comps.iterator(); it.hasNext();) {
            TopComponent comp = (TopComponent) it.next();
            HqlMultiViewElement c = (HqlMultiViewElement) comp.getLookup().lookup(HqlMultiViewElement.class);
            if (c != null) {
                editorOpen = true;
                break;
            }
        }
        if (editorOpen) {
            return;
        }
        LoggingOutput.closeLoggingWindow();
    }        

    public boolean close() {
        return false;
    }

    public Node.Cookie createCookie(Class klass) {
        if (SessionFactoryCookie.class.equals(klass)) {
            return this;
        } else if (HqlEditorCookie.class.equals(klass)) {
            return this;
        }
        return null;
    }
    
    public SessionFactoryDescriptor getDescriptor() {
        return descriptor;
    }

    public boolean isSessionOpen() {
        return descriptor.isSessionOpen();
    }

    public void updateSchema() {
        descriptor.updateSchema();
    }

    public void convertHql2Java() {
        if (hqlDescription == null) {
            return;
        }
        hqlDescription.convertHql2Java();
    }
    
    public void convertJava2Hql() {
        if (hqlDescription == null) {
            return;
        }
        hqlDescription.convertJava2Hql();
    }

    public void copy() {
        try {
            descriptor.copy();
        } catch (Exception ex) {
            ErrorManager.getDefault().notify(ex);
        }
    }

    public boolean canRename() {
        return true;
    }

    public void setName(String s) {
        if (s == null || s.trim().length() == 0) {
            s = org.openide.util.NbBundle.getBundle(SessionFactoryNode.class).getString("LBL_AnonSessionFactory");
        }
        s = s.trim();
        setDisplayName(s);
        if (editorTopComponent != null) {
            editorTopComponent.setDisplayName(s);
        }
        if (!s.equals(descriptor.getName())) {
            descriptor.setName(s);
            try {
                descriptor.persist();
            } catch (Exception ex) {
                ErrorManager.getDefault().notify(ex);
            }
        }
    }

    public String getName() {
        return descriptor.getName();
    }

    public String getDisplayName() {
        return descriptor.getName();
    }

    public String getActiveContent() {
        MultiViewHandler mvh = MultiViews.findMultiViewHandler(editorTopComponent);
        if (mvh.getSelectedPerspective().preferredID().equals(hqlDescription.preferredID())) {
            return hqlDescription.getCurrentQuery();
        } else {
            return bshDescription.getCurrentQuery();
        }
    }

    public void doRunWithContent(String content) {
        MultiViewHandler mvh = MultiViews.findMultiViewHandler(editorTopComponent);
        if (mvh.getSelectedPerspective().preferredID().equals(hqlDescription.preferredID())) {
            // HQL
            if (!descriptor.isSessionOpen()) {
                hqlDescription.showWarning(NbBundle.getMessage(SessionFactoryNode.class, "LBL_SessionNotOpenWarning"));
                return;
            }
            if (content != null && content.length() > 0) {
                SessionFactoryOutput.showOutputTab(descriptor, true);
                descriptor.invokeHqlProcessors(content);
            }
        } else {
            // Beanshell
            if (!descriptor.isSessionOpen()) {
                bshDescription.showWarning(NbBundle.getMessage(SessionFactoryNode.class, "LBL_SessionNotOpenWarning"));
                return;
            }
            SessionFactoryOutput.showOutputTab(descriptor, true);
            if (content != null && content.length() > 0) {
                SessionFactoryOutput.showOutputTab(descriptor, true);
                descriptor.invokeBshProcessors(content);
            }
        }
        
    }

}
