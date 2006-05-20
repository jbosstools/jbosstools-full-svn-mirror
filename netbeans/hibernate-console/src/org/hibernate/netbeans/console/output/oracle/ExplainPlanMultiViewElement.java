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


package org.hibernate.netbeans.console.output.oracle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.hibernate.netbeans.console.BshCode;
import org.hibernate.netbeans.console.HqlQuery;
import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.OutputMultiViewElement;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.openide.ErrorManager;

/**
 * @author leon
 */
public class ExplainPlanMultiViewElement extends OutputMultiViewElement {
    
    private JTree explainPlanTree;
    
    private JScrollPane scrollPane;

    private final static TreeModel EMPTY_TREE_MODEL = new DefaultTreeModel(
            new DefaultMutableTreeNode("No execution plan available"));

    private JToolBar toolBar;

    private Connection connection;

    private String tableName;

    public ExplainPlanMultiViewElement(final SessionFactoryDescriptor descr) {
        super(descr);
        tableName = getDescriptor().getStorageFile().getName();
        int idx = tableName.lastIndexOf("-");
        if (idx != -1) {
            tableName = tableName.substring(idx + 1, tableName.length());
        }
        tableName = "PLAN_" + tableName.toUpperCase();
        descr.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if (SessionFactoryDescriptor.PROPERTY_SESSION_OPEN.equals(propertyChangeEvent.getPropertyName())) {
                    if (!descr.isSessionOpen() && connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException ex) {
                            ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
                        }
                    }
                }
            }
        });
    }

    public void executeQuery(HqlQuery q) {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getDescriptor().getClassLoader());
            requestVisible(false, false);
            explainPlanTree.setModel(EMPTY_TREE_MODEL);
            ensurePlanTableExists();
            createPlan(q);
            requestVisible(true, false);
        } catch (Exception ex) {
            SessionFactoryOutput.showError(getDescriptor(), ex);
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
    }
    
    private void createPlan(HqlQuery hql) throws Exception {
        updateConnection();
        Statement stm = connection.createStatement();
        try {
            try {
                stm.executeUpdate("explain plan into " + tableName + " for " + getDescriptor().toSql(hql.getHql()));
                ResultSet rs = stm.executeQuery(
                        "select " +
                        "level, operation, options, object_name " +
                        "from " + tableName + " " +
                        "start with id = 0 " +
                        "connect by prior id = parent_id");
                Map<Integer, DefaultMutableTreeNode> level2TreeNode = new HashMap<Integer, DefaultMutableTreeNode>();
                while (rs.next()) {
                    ExplainPlanNode epNode = new ExplainPlanNode(rs.getString(2), rs.getString(3), rs.getString(4));
                    int level = rs.getInt(1);
                    Integer parentLevel = new Integer(level - 1);
                    DefaultMutableTreeNode parent = level2TreeNode.get(parentLevel);
                    if (parent == null) {
                        parent = new DefaultMutableTreeNode();
                        level2TreeNode.put(parentLevel, parent);
                    }
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(epNode);
                    parent.add(child);
                    level2TreeNode.put(new Integer(level), child);
                }
                explainPlanTree.setModel(new DefaultTreeModel(level2TreeNode.get(new Integer(0))));
                expandPlanTree();
            } finally {
                stm.executeUpdate("delete from " + tableName);
                connection.commit();
            }
        } finally {
            try {
                stm.close();
            } catch (SQLException ex) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
            }
        }
    }

    public void expandPlanTree() {
        TreeNode root = (TreeNode) explainPlanTree.getModel().getRoot();
        expandPlanTree(new TreePath(root));
    }

    private void expandPlanTree(TreePath parent) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandPlanTree(path);
            }
        }
        explainPlanTree.expandPath(parent);
    }

    private void updateConnection() throws Exception, SQLException {
        if (connection == null || connection.isClosed()) {
            connection = getDescriptor().openConnection();
        }
    }

    private void ensurePlanTableExists() throws Exception {
        updateConnection();
        Statement stm = connection.createStatement();
        try {
            ResultSet rs = stm.executeQuery("select count(*) from user_tables where table_name = '" + tableName + "'");
            rs.next();
            int i = rs.getInt(1);
            if (i == 1) {
                return;
            }
            String sql =
                    "create table " + tableName + "(" +
                    "statement_id varchar2(30), " +
                    "timestamp date, " +
                    "remarks varchar2(80), " +
                    "operation varchar2(30), " +
                    "options varchar2(30), " +
                    "object_node varchar2(128), " +
                    "object_owner varchar2(30), " +
                    "object_name varchar2(30), " +
                    "object_instance number(38), " +
                    "object_type varchar2(30), " +
                    "optimizer varchar2(255), " +
                    "search_columns number, " +
                    "id number(38), " +
                    "parent_id number(38), " +
                    "position number(38), " +
                    "cost number(38), " +
                    "cardinality number(38), " +
                    "bytes number(38), " +
                    "other_tag varchar2(255), " +
                    "partition_start varchar2(255), " +
                    "partition_stop varchar2(255), " +
                    "partition_id number(38), " +
                    "other long, " +
                    "distribution varchar2(30))";
            stm.executeUpdate(sql);
        } finally {
            try {
                stm.close();
            } catch (SQLException ex) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
            }
        }
    }
    
    public void executeBshCode(BshCode cc) {
        if (explainPlanTree != null) {
            explainPlanTree.setModel(EMPTY_TREE_MODEL);
        }
    }

    public void requestFocus() {
        if (explainPlanTree != null) {
            explainPlanTree.requestFocusInWindow();
        }
    }

    public JComponent getVisualRepresentation() {
        if (scrollPane == null) {
            explainPlanTree = new JTree(EMPTY_TREE_MODEL);
            explainPlanTree.setRootVisible(false);
            scrollPane = new JScrollPane(explainPlanTree);
            scrollPane.setBorder(null);
        }
        return scrollPane;
    }

    public JComponent getToolbarRepresentation() {
        if (toolBar != null) {
            toolBar = new JToolBar();
        }
        return toolBar;
    }

    
}
