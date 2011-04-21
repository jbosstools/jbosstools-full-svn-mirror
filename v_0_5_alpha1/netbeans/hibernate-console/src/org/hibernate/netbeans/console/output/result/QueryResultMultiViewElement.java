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


package org.hibernate.netbeans.console.output.result;

import bsh.EvalError;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import org.hibernate.HibernateException;
import org.hibernate.netbeans.console.BshCode;
import org.hibernate.netbeans.console.Icons;
import org.hibernate.netbeans.console.util.HibernateExecutor;
import org.hibernate.netbeans.console.HqlQuery;
import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.option.Options;
import org.hibernate.netbeans.console.util.UIUtilities;
import org.hibernate.netbeans.console.output.OutputMultiViewElement;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.hibernate.Query;
import org.hibernate.Session;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.Utilities;

/**
 * @author leon
 */
public class QueryResultMultiViewElement extends OutputMultiViewElement implements ObjectNavigationHandler, Serializable {

    private JComponent toolBar;

    private QueryResultPanel resultPanel;

    private JButton backButton;
    
    // Strangely, we have to use a filler, otherwise toolbar components are not aligned to the left
    private Component toolBarFiller;
    
    private final static ImageIcon BACK_ICON = new ImageIcon(Utilities.loadImage(Icons.BACK));
    
    public QueryResultMultiViewElement(SessionFactoryDescriptor descr) {
        super(descr);
        resultPanel = new QueryResultPanel(this);
        toolBar = createToolBar();
        registerKeyboardActions();
    }
    
    private void registerKeyboardActions() {
        InputMap im = resultPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = resultPanel.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "back-navigation");
        am.put("back-navigation", new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                goBack();
            }
        });
    }
    
    private JComponent createToolBar() {
        JToolBar tb = new JToolBar();
        tb.setRollover(true);
        tb.add(UIUtilities.createToolBarSpacer());
        backButton = UIUtilities.createToolBarButton("", BACK_ICON);
        backButton.setEnabled(false);
        backButton.setVisible(false);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                goBack();
            }
        });
        tb.add(backButton);
        Dimension d = new Dimension(Integer.MAX_VALUE, 1);
        toolBarFiller = new Box.Filler(d, d, d);
        tb.add(toolBarFiller);
        return tb;
    }

    private void goBack() {
        Component[] comps = toolBar.getComponents();
        for (int i = comps.length - 1; i >= 0; i--) {
            Component comp = comps[i];
            if (comp instanceof HyperlinkLabel) {
                ((HyperlinkLabel) comp).performAction();
                break;
            }
        }
    }
    
    public JComponent getVisualRepresentation() {
        return resultPanel;
    }

    public JComponent getToolbarRepresentation() {
        return toolBar;
    }
    
    public void executeBshCode(final BshCode cc) {
        final SessionFactoryDescriptor desc = getDescriptor();
        final Session s = desc.getSession();
        HibernateExecutor.execute(new Runnable() {
            public void run() {
                ProgressHandle ph = ProgressHandleFactory.createHandle("Executing Java Code"); // TODO - i18n
                Thread currentThread = Thread.currentThread();
                ClassLoader currentClassLoader = currentThread.getContextClassLoader();
                boolean success = false;
                String code = cc.getCode();
                try {
                    ph.start();
                    ph.progress("Java Code: " + code);
                    EventQueue.invokeAndWait(new Runnable() {
                        public void run() {
                            resultPanel.setResults(desc.getSession(), null, null, -1);
                            removeAllToolBarLabels();
                            requestVisible(false, false);
                        }
                    });
                    final long l = System.currentTimeMillis();
                    final List<Object> results = JavaCodeInterpreter.getInterpreter().getResults(code, desc);
                    if (results != null) {
                        EventQueue.invokeAndWait(new Runnable() {
                            public void run() {
                                resultPanel.setResults(desc.getSession(), null, results, System.currentTimeMillis() - l);
                                requestVisible(true, true);
                            }
                        });
                    }
                } catch (final EvalError err) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            Throwable t = err.getCause();
                            if (t == null) {
                                t = err;
                            }
                            SessionFactoryOutput.showError(desc, t);
                        }
                    });
                } catch (final Exception ex) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            SessionFactoryOutput.showError(desc, ex);
                        }
                    });
                } finally {
                    currentThread.setContextClassLoader(currentClassLoader);
                    ph.finish();
                }
            }
        });
    }

    public void executeQuery(final HqlQuery hqlq) {
        SessionFactoryDescriptor desc = getDescriptor();
        final Session s = desc.getSession();
        HibernateExecutor.execute(new Runnable() {
            public void run() {
                ProgressHandle ph = ProgressHandleFactory.createHandle("Executing Query", new Cancellable() {
                    public boolean cancel() {
                        try {
                            s.cancelQuery();
                        } catch (HibernateException ex) {
                            // Ignore it
                        }
                        return true;
                    }
                });
                Thread currentThread = Thread.currentThread();
                ClassLoader currentClassLoader = currentThread.getContextClassLoader();
                boolean success = false;
                try {
                    final String hql = hqlq.getHql();
                    ph.start();
                    ph.progress("HQL: " + hql);
                    EventQueue.invokeAndWait(new Runnable() {
                        public void run() {
                            resultPanel.setResults(null, hql, null, -1);
                            removeAllToolBarLabels();
                        }
                    });
                    currentThread.setContextClassLoader(getDescriptor().getClassLoader());
                    long l = System.currentTimeMillis();
                    final Query q = s.createQuery(hql);
                    q.setMaxResults(Options.get().getMaxQueryResults());
                    s.clear();
                    EventQueue.invokeAndWait(new Runnable() {
                        public void run() {
                            requestVisible(false, false);
                        }
                    });
                    String uHql = hql.toUpperCase();
                    final List results;
                    if (uHql.startsWith("DELETE") || uHql.startsWith("UPDATE")) {
                        results = Collections.singletonList(new Integer(q.executeUpdate()));
                    } else {
                        results = q.list();
                    }
                    final long duration = System.currentTimeMillis() - l;
                    success = true;
                    ph.finish();
                    EventQueue.invokeAndWait(new Runnable() {
                        public void run() {
                            resultPanel.setResults(s, hql, results, duration);
                            requestVisible(true, true);
                        }
                    });
                } catch (final Exception ex) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            SessionFactoryOutput.showError(getDescriptor(), ex);
                        }
                    });
                } finally {
                    currentThread.setContextClassLoader(currentClassLoader);
                    ph.finish();
                }
            }
        });
    }

    public void addNavigationAction(Action action) {
        toolBar.add(new HyperlinkLabel(new NavigationAction(action)), toolBar.getComponentCount() - 1);
        backButton.setEnabled(true);
        backButton.setVisible(true);
    }
    
    private void removeAllToolBarLabels() {
        Component comps[] = toolBar.getComponents();
        for (int i = 0; i < comps.length; i++) {
            Component comp = comps[i];
            if (comp instanceof HyperlinkLabel) {
                toolBar.remove(comp);
            }
        }
        backButton.setEnabled(false);
        backButton.setVisible(false);
        toolBar.repaint();
    }
    
    private void removeToolBarLabels(HyperlinkLabel fromLabel) {
        Component[] comps = toolBar.getComponents();
        boolean remove = false;
        for (int i = 0; i < comps.length; i++) {
            Component comp = comps[i];
            if (!remove && comp == fromLabel) {
                remove = true;
            }
            if (remove && comp instanceof HyperlinkLabel) {
                toolBar.remove(comp);
            }
        }
        // Not get the new component count: if it's 3 (the button and the 2 fillers) disable the button
        if (toolBar.getComponentCount() == 3) {
            backButton.setEnabled(false);
            backButton.setVisible(false);
        }
        toolBar.repaint();
    }

    public void requestFocus() {
        resultPanel.requestFocusInWindow();
    }
    
    private class NavigationAction extends AbstractAction {

        private Action action;
        
        public NavigationAction(Action a) {
            super((String) a.getValue(Action.NAME));
            this.action = a;
        }
        
        public void actionPerformed(ActionEvent actionEvent) {
            action.actionPerformed(actionEvent);
            HyperlinkLabel label = (HyperlinkLabel) actionEvent.getSource();
            removeToolBarLabels(label);
        }
        
    }

    public void componentClosed() {
        super.componentClosed();
        resultPanel.clear();
    }

    
}
