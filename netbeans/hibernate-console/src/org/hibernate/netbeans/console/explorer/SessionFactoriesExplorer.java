package org.hibernate.netbeans.console.explorer;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.hibernate.netbeans.console.ConfigureSessionFactoryAction;
import org.hibernate.netbeans.console.Icons;
import org.hibernate.netbeans.console.NewSessionFactoryAction;
import org.hibernate.netbeans.console.DeleteSessionFactoryAction;
import org.hibernate.netbeans.console.SessionFactoriesNode;
import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.util.UIUtilities;
import org.openide.ErrorManager;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.explorer.view.NodeTreeModel;
import org.openide.nodes.Node;
import org.openide.nodes.NodeAdapter;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
final class SessionFactoriesExplorer extends TopComponent implements ExplorerManager.Provider {
    
    private static final long serialVersionUID = 1L;
    
    private static SessionFactoriesExplorer instance;
    
    private static final String PREFERRED_ID = "SessionFactoriesExplorer";
    
    private ExplorerManager explorerManager;
    
    private BeanTreeView treeView;
    
    private SessionFactoriesNode sessionFactoriesNode;

    private JButton newButton;
    
    private JButton configureButton;
    
    private JButton removeButton;
    
    private SessionFactoriesExplorer() {
        initComponents();
        postInit();
    }
    
    private void postInit() {
        setName(NbBundle.getMessage(SessionFactoriesExplorer.class, "CTL_SessionFactoriesExplorer"));
        setToolTipText(NbBundle.getMessage(SessionFactoriesExplorer.class, "HINT_SessionFactoriesExplorer"));
        //
        newButton = 
                UIUtilities.createToolBarButton("", new ImageIcon(Utilities.loadImage(Icons.NEW)));
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                NewSessionFactoryAction act = (NewSessionFactoryAction) SystemAction.get(NewSessionFactoryAction.class);
                act.performAction();
                treeView.getViewport().getView().requestFocusInWindow();
            }
        });
        newButton.setToolTipText("New Session Factory");
        configureButton =
                UIUtilities.createToolBarButton("", new ImageIcon(Utilities.loadImage(Icons.CONFIGURE)));
        configureButton.setEnabled(false);
        configureButton.setToolTipText("Configure Session Factory");
        configureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                ConfigureSessionFactoryAction act = (ConfigureSessionFactoryAction) SystemAction.get(ConfigureSessionFactoryAction.class);
                act.performAction(explorerManager.getSelectedNodes());
                treeView.getViewport().getView().requestFocusInWindow();
            }
        });
        removeButton = 
                UIUtilities.createToolBarButton("", new ImageIcon(Utilities.loadImage(Icons.DELETE)));
        removeButton.setEnabled(false);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                DeleteSessionFactoryAction act = (DeleteSessionFactoryAction) SystemAction.get(DeleteSessionFactoryAction.class);
                act.performAction(explorerManager.getSelectedNodes());
            }
        });
        toolBar.add(newButton);
        toolBar.add(configureButton);
        toolBar.add(removeButton);
        //
        sessionFactoriesNode = new SessionFactoriesNode();
        sessionFactoriesNode.getChildren().add(SessionFactoryNode.listAll());
        
        // Explorer
        explorerManager = new ExplorerManager();
        explorerManager.setRootContext(sessionFactoriesNode);
        
        treeView = new SessionFactoriesView();
        treeView.setRootVisible(false);
        ActionMap map = this.getActionMap ();
        associateLookup(ExplorerUtils.createLookup(explorerManager, map));
        add(treeView, BorderLayout.CENTER);
        
        explorerManager.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if (ExplorerManager.PROP_SELECTED_NODES.equals(propertyChangeEvent.getPropertyName())) {
                    setButtonState(explorerManager.getSelectedNodes());
                }
            }
        });
        setIcon(Utilities.loadImage(Icons.HIBERNATE_SMALL));
    }
    
    private void setButtonState(Node[] nodes) {
        int sz = nodes.length;
        configureButton.setEnabled(sz == 1);
        removeButton.setEnabled(sz >= 1);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        toolBar = new javax.swing.JToolBar();

        setLayout(new java.awt.BorderLayout());

        toolBar.setFloatable(false);
        toolBar.setMargin(new java.awt.Insets(2, 0, 0, 0));
        add(toolBar, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized SessionFactoriesExplorer getDefault() {
        if (instance == null) {
            instance = new SessionFactoriesExplorer();
        }
        return instance;
    }
    
    /**
     * Obtain the SessionFactoriesExplorer instance. Never call {@link #getDefault} directly!
     */
    public static synchronized SessionFactoriesExplorer findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find SessionFactories component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SessionFactoriesExplorer) {
            return (SessionFactoriesExplorer)win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }
    
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }
    
    public void componentOpened() {
    }
    
    public void componentClosed() {
    }
    
    /** replaces this in object stream */
    public Object writeReplace() {
        return new ResolvableHelper();
    }
    
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }
    
    final static class ResolvableHelper implements Serializable {
        private static final long serialVersionUID = 1L;
        
        public Object readResolve() {
            return SessionFactoriesExplorer.getDefault();
        }
    }
    
    private class SessionFactoriesView extends BeanTreeView {
        
        public SessionFactoriesView() {
            SessionFactoryDescriptor.addCreationListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SessionFactoryNode node = new SessionFactoryNode((SessionFactoryDescriptor) e.getSource());
                    Node[] newNodes = new Node[] { node };
                    sessionFactoriesNode.getChildren().add(newNodes);
                    try {
                        explorerManager.setSelectedNodes(newNodes);
                    } catch (PropertyVetoException ex) {
                        ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
                    }
                }
            });
        }
        
        protected NodeTreeModel createModel() {
            return new NodeTreeModel(sessionFactoriesNode);
        }
    }

    public void requestActive() {
        super.requestActive();
        treeView.getViewport().getView().requestFocusInWindow();
    }
    
}
