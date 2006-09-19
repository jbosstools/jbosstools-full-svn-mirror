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

package org.hibernate.netbeans.console;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import org.hibernate.netbeans.console.option.Options;
import org.hibernate.netbeans.console.util.DelegatingListModel;
import org.openide.util.HelpCtx;

/**
 * @author  leon
 */
public class SessionFactoryConfigurationPanel extends javax.swing.JPanel {

    public static final HelpCtx HELP_CTX = new HelpCtx("org.hibernate.netbeans.console.sf-config");
    
    private List<File> mappingLocations;
    
    private List<File> classPathEntries;
    
    private SessionFactoryDescriptor descriptor;
    
    private DelegatingListModel<File> mappingLocationsModel;
    
    private DelegatingListModel<File> classPathEntriesModel;
    
    private static JFileChooser fileChooser;
        
    private static TokenizingFileFilter classPathFileFilter = new TokenizingFileFilter(
            ".jar", 
            org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_DirectoriesAndJarFiles"));
    
    /** Creates new form SessionFactoryConfigurationPanel */
    public SessionFactoryConfigurationPanel(SessionFactoryDescriptor descriptor) {
        this.descriptor = descriptor;
        initComponents();
        postInit();
    }

    private void postInit() {
        initDialectCombo();
        // Locations
        List<File> loc = null;
        List<File> cp = null;
        if (descriptor != null) {
            loc = descriptor.getMappingLocations();
            cp = descriptor.getClassPathEntries();
        }
        if (loc != null) {
            mappingLocations = new ArrayList<File>(loc);
        } else {
            mappingLocations = new ArrayList<File>();
        }
        if (cp != null) {
            classPathEntries = new ArrayList<File>(cp);
        } else {
            classPathEntries = new ArrayList<File>();
        }
        mappingLocationsModel = new DelegatingListModel<File>(mappingLocations);
        classPathEntriesModel = new DelegatingListModel<File>(classPathEntries);
        mappingLocationsList.setModel(mappingLocationsModel);
        classPathList.setModel(classPathEntriesModel);
        if (descriptor != null) {
            int dialectIndex = ConnectionDescriptor.indexByDialect(descriptor.getHibernateDialect());
            if (dialectIndex != -1) {
                dialectComboBox.setSelectedIndex(dialectIndex);
            }
            dialectComboBox.getEditor().setItem(descriptor.getHibernateDialect());
            nameField.setText(descriptor.getName());
            userField.setText(descriptor.getDatabaseUser());
            passwordField.setText(descriptor.getDatabasePassword());
            driverComboBox.getEditor().setItem(descriptor.getDatabaseDriverName());
            connectionUrlCombo.getEditor().setItem(descriptor.getDatabaseUrl());
            importsTextArea.setText(descriptor.getUserImports());
            Properties props = descriptor.getExtraProperties();
            if (props != null) {
                StringBuffer buff = new StringBuffer();
                for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
                    String propName = (String) en.nextElement();
                    buff.append(propName).append("=").append(props.getProperty(propName)).append("\n");
                }
                propertiesTextArea.setText(buff.toString());
            }
        }
    }
    
    private void initDialectCombo() {
        for (ConnectionDescriptor cd : ConnectionDescriptor.ALL_DESCRIPTORS) {
            dialectComboBox.addItem(cd);
        }
        dialectComboBox.setRenderer(new DialectNameRenderer());
    }
        
    private static File[] openFiles(JComponent parent, TokenizingFileFilter filter) {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setFileHidingEnabled(true);
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setDragEnabled(false);
        }
        fileChooser.resetChoosableFileFilters();
        fileChooser.setFileFilter(filter);
        // This is needed to clear the name field
        fileChooser.setSelectedFile(new File(""));
        int val = fileChooser.showOpenDialog(parent);
        if (val == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            // XXX - This is a strange behaviour of the filechooser
            // Sometimes the name field remains filled out, after you enter a directory,
            // so you basically get to select the file dir1/dir2/dir2
            if (selectedFiles.length == 1 && !selectedFiles[0].exists()) {
                selectedFiles[0] = selectedFiles[0].getParentFile();
            }
            return selectedFiles;
        } else {
            return new File[0];
        }
    }
        
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        mappingLocationsLabel = new javax.swing.JLabel();
        mappingLocationsScrollPane = new javax.swing.JScrollPane();
        mappingLocationsList = new javax.swing.JList();
        classPathLabel = new javax.swing.JLabel();
        classPathScrollPane = new javax.swing.JScrollPane();
        classPathList = new javax.swing.JList();
        addMappingLocationButton = new javax.swing.JButton();
        removeMappingLocationButton = new javax.swing.JButton();
        upMappingLocationButton = new javax.swing.JButton();
        downMappingLocationButton = new javax.swing.JButton();
        addClassPathButton = new javax.swing.JButton();
        removeClassPathButton = new javax.swing.JButton();
        upClassPathButton = new javax.swing.JButton();
        downClassPathButton = new javax.swing.JButton();
        extraConfigurationTabbedPane = new javax.swing.JTabbedPane();
        simpleConfigurationPanel = new javax.swing.JPanel();
        userLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        driverComboBox = new javax.swing.JComboBox();
        driverLabel = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        dialectLabel = new javax.swing.JLabel();
        dialectComboBox = new javax.swing.JComboBox();
        connectionUrlLabel = new javax.swing.JLabel();
        connectionUrlCombo = new javax.swing.JComboBox();
        passwordField = new javax.swing.JPasswordField();
        advancedConfigurationPanel = new javax.swing.JPanel();
        propertiesScrollPane = new javax.swing.JScrollPane();
        propertiesTextArea = new javax.swing.JTextArea();
        importsPanel = new javax.swing.JPanel();
        importsScrollPane = new javax.swing.JScrollPane();
        importsTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        nameLabel.setLabelFor(nameField);
        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_DisplayName")); // NOI18N

        mappingLocationsLabel.setLabelFor(mappingLocationsList);
        org.openide.awt.Mnemonics.setLocalizedText(mappingLocationsLabel, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_MappingLocations")); // NOI18N

        mappingLocationsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                mappingLocationsListValueChanged(evt);
            }
        });

        mappingLocationsScrollPane.setViewportView(mappingLocationsList);

        classPathLabel.setLabelFor(classPathList);
        org.openide.awt.Mnemonics.setLocalizedText(classPathLabel, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_ClassPath")); // NOI18N

        classPathList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                classPathListValueChanged(evt);
            }
        });

        classPathScrollPane.setViewportView(classPathList);

        org.openide.awt.Mnemonics.setLocalizedText(addMappingLocationButton, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_AddMappingLocation")); // NOI18N
        addMappingLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMappingLocationButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeMappingLocationButton, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_RemoveMappingLocation")); // NOI18N
        removeMappingLocationButton.setEnabled(false);
        removeMappingLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeMappingLocationButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(upMappingLocationButton, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_UpMappingLocation")); // NOI18N
        upMappingLocationButton.setEnabled(false);
        upMappingLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upMappingLocationButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(downMappingLocationButton, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_DownMappingLocation")); // NOI18N
        downMappingLocationButton.setEnabled(false);
        downMappingLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downMappingLocationButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(addClassPathButton, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_AddClassPath")); // NOI18N
        addClassPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClassPathButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeClassPathButton, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_RemoveClassPath")); // NOI18N
        removeClassPathButton.setEnabled(false);
        removeClassPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeClassPathButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(upClassPathButton, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_UpClassPath")); // NOI18N
        upClassPathButton.setEnabled(false);
        upClassPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upClassPathButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(downClassPathButton, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_DownClassPath")); // NOI18N
        downClassPathButton.setEnabled(false);
        downClassPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downClassPathButtonActionPerformed(evt);
            }
        });

        userLabel.setLabelFor(userField);
        org.openide.awt.Mnemonics.setLocalizedText(userLabel, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_UserName")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(passwordLabel, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_Password")); // NOI18N

        driverComboBox.setEditable(true);

        driverLabel.setLabelFor(driverComboBox);
        org.openide.awt.Mnemonics.setLocalizedText(driverLabel, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_Driver")); // NOI18N

        dialectLabel.setLabelFor(dialectComboBox);
        org.openide.awt.Mnemonics.setLocalizedText(dialectLabel, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_HibernateDialect")); // NOI18N

        dialectComboBox.setEditable(true);
        dialectComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialectComboBoxActionPerformed(evt);
            }
        });

        connectionUrlLabel.setLabelFor(connectionUrlCombo);
        org.openide.awt.Mnemonics.setLocalizedText(connectionUrlLabel, org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_DatabaseUrl")); // NOI18N

        connectionUrlCombo.setEditable(true);

        org.jdesktop.layout.GroupLayout simpleConfigurationPanelLayout = new org.jdesktop.layout.GroupLayout(simpleConfigurationPanel);
        simpleConfigurationPanel.setLayout(simpleConfigurationPanelLayout);
        simpleConfigurationPanelLayout.setHorizontalGroup(
            simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(simpleConfigurationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(dialectLabel)
                    .add(passwordLabel)
                    .add(simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(driverLabel)
                        .add(simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(userLabel)
                            .add(connectionUrlLabel))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(dialectComboBox, 0, 231, Short.MAX_VALUE)
                    .add(driverComboBox, 0, 231, Short.MAX_VALUE)
                    .add(userField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                    .add(connectionUrlCombo, 0, 231, Short.MAX_VALUE)
                    .add(passwordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
                .addContainerGap())
        );
        simpleConfigurationPanelLayout.setVerticalGroup(
            simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(simpleConfigurationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialectLabel)
                    .add(dialectComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(driverLabel)
                    .add(driverComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(connectionUrlLabel)
                    .add(connectionUrlCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(userLabel)
                    .add(userField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(simpleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordLabel)
                    .add(passwordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        extraConfigurationTabbedPane.addTab(org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_SimpleConfiguration"), simpleConfigurationPanel); // NOI18N

        propertiesTextArea.setColumns(20);
        propertiesTextArea.setRows(5);
        propertiesScrollPane.setViewportView(propertiesTextArea);

        org.jdesktop.layout.GroupLayout advancedConfigurationPanelLayout = new org.jdesktop.layout.GroupLayout(advancedConfigurationPanel);
        advancedConfigurationPanel.setLayout(advancedConfigurationPanelLayout);
        advancedConfigurationPanelLayout.setHorizontalGroup(
            advancedConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(advancedConfigurationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(propertiesScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addContainerGap())
        );
        advancedConfigurationPanelLayout.setVerticalGroup(
            advancedConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, advancedConfigurationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(propertiesScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addContainerGap())
        );
        extraConfigurationTabbedPane.addTab(org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_AdvancedConfiguration"), advancedConfigurationPanel); // NOI18N

        importsTextArea.setColumns(20);
        importsTextArea.setRows(5);
        importsScrollPane.setViewportView(importsTextArea);

        org.jdesktop.layout.GroupLayout importsPanelLayout = new org.jdesktop.layout.GroupLayout(importsPanel);
        importsPanel.setLayout(importsPanelLayout);
        importsPanelLayout.setHorizontalGroup(
            importsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(importsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(importsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addContainerGap())
        );
        importsPanelLayout.setVerticalGroup(
            importsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(importsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(importsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addContainerGap())
        );
        extraConfigurationTabbedPane.addTab(org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_BshImports"), importsPanel); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/hibernate/netbeans/console/resources/warning.png")));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(SessionFactoryConfigurationPanel.class, "SessionFactoryConfigurationPanel.jLabel1.text")); // NOI18N
        jLabel1.setIconTextGap(6);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/hibernate/netbeans/console/resources/warning.png")));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(SessionFactoryConfigurationPanel.class, "SessionFactoryConfigurationPanel.jLabel2.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mappingLocationsLabel)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(classPathScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(classPathLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel2))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, mappingLocationsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(nameLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(nameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                            .add(extraConfigurationTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(downClassPathButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(upClassPathButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .add(removeClassPathButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .add(addClassPathButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, addMappingLocationButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, removeMappingLocationButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, upMappingLocationButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, downMappingLocationButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {addClassPathButton, addMappingLocationButton, downClassPathButton, downMappingLocationButton, removeClassPathButton, removeMappingLocationButton, upClassPathButton, upMappingLocationButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameLabel)
                    .add(nameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(mappingLocationsLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(addMappingLocationButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removeMappingLocationButton)
                        .add(10, 10, 10)
                        .add(upMappingLocationButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(downMappingLocationButton))
                    .add(mappingLocationsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(classPathLabel)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(classPathScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(extraConfigurationTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(addClassPathButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removeClassPathButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(upClassPathButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(downClassPathButton)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fillConnectionCombo() {
        Object dialect = dialectComboBox.getSelectedItem();
        if (!(dialect instanceof ConnectionDescriptor)) {
            return;
        }
        ConnectionDescriptor desc = (ConnectionDescriptor) dialect;
        ComboBoxEditor cbEditor = connectionUrlCombo.getEditor();
        String currentValue = (String) cbEditor.getItem();
        connectionUrlCombo.setModel(new DefaultComboBoxModel(desc.connectionUrls));
        if (currentValue != null) {
            cbEditor.setItem(currentValue);
        }
    }

    private void dialectComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialectComboBoxActionPerformed
        fillDriverCombo();
        fillConnectionCombo();
    }//GEN-LAST:event_dialectComboBoxActionPerformed

    private void fillDriverCombo() {
        Object dialect = dialectComboBox.getSelectedItem();
        if (!(dialect instanceof ConnectionDescriptor)) {
            return;
        }
        ConnectionDescriptor desc = (ConnectionDescriptor) dialect;
        ComboBoxEditor editor = driverComboBox.getEditor();
        Object item = editor.getItem();
        driverComboBox.setModel(new DefaultComboBoxModel(desc.drivers));
        if (item != null) {
            editor.setItem(item);
        }
    }

    private void removeClassPathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeClassPathButtonActionPerformed
        classPathEntriesModel.removeElements(classPathList.getSelectedIndices());
    }//GEN-LAST:event_removeClassPathButtonActionPerformed

    private void removeMappingLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeMappingLocationButtonActionPerformed
        mappingLocationsModel.removeElements(mappingLocationsList.getSelectedIndices());
    }//GEN-LAST:event_removeMappingLocationButtonActionPerformed

    private void downClassPathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downClassPathButtonActionPerformed
        moveSelectionDown(classPathList, classPathEntriesModel);
    }//GEN-LAST:event_downClassPathButtonActionPerformed

    private void upClassPathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upClassPathButtonActionPerformed
        moveSelectionUp(classPathList, classPathEntriesModel);
    }//GEN-LAST:event_upClassPathButtonActionPerformed

    private void classPathListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_classPathListValueChanged
        if (!evt.getValueIsAdjusting()) {
            adjustUpDownButtons(classPathList, upClassPathButton, downClassPathButton);
            removeClassPathButton.setEnabled(classPathList.getSelectedIndex() != -1);
        }
    }//GEN-LAST:event_classPathListValueChanged
    
    private void downMappingLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downMappingLocationButtonActionPerformed
        moveSelectionDown(mappingLocationsList, mappingLocationsModel);
    }//GEN-LAST:event_downMappingLocationButtonActionPerformed
    
    private void mappingLocationsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_mappingLocationsListValueChanged
        if (!evt.getValueIsAdjusting()) {
            adjustUpDownButtons(mappingLocationsList, upMappingLocationButton, downMappingLocationButton);
            removeMappingLocationButton.setEnabled(mappingLocationsList.getSelectedIndex() != -1);
        }
    }//GEN-LAST:event_mappingLocationsListValueChanged
    
    private void adjustUpDownButtons(JList list, JButton upButton, JButton downButton) {
        int[] indices = list.getSelectedIndices();
        upButton.setEnabled(list.getMinSelectionIndex() > 0);
        downButton.setEnabled(list.getMaxSelectionIndex() < list.getModel().getSize() - 1);
    }
    
    private void upMappingLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upMappingLocationButtonActionPerformed
        moveSelectionUp(mappingLocationsList, mappingLocationsModel);
    }//GEN-LAST:event_upMappingLocationButtonActionPerformed

    private void addClassPathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClassPathButtonActionPerformed
        File[] selectedEntries = openFiles(this, classPathFileFilter);
        classPathEntriesModel.addElements(selectedEntries);
    }//GEN-LAST:event_addClassPathButtonActionPerformed

    private void addMappingLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMappingLocationButtonActionPerformed
        File[] selectedHbms = openFiles(this,
                new TokenizingFileFilter(
                        Options.get().getHbmExtensions(),
                        org.openide.util.NbBundle.getBundle(SessionFactoryConfigurationPanel.class).getString("LBL_DirectoriesAndMappingFiles")));
        mappingLocationsModel.addElements(selectedHbms);
    }//GEN-LAST:event_addMappingLocationButtonActionPerformed
    
    private void moveSelectionUp(JList list, DelegatingListModel model) {
        int indices[] = list.getSelectedIndices();
        if (indices.length == 0 || indices[0] == 0) {
            return;
        }
        model.moveElementsUp(indices);
        list.setSelectedIndices(indices);
        model.refresh();
        int firstIndex = indices[0];
        list.scrollRectToVisible(list.getCellBounds(firstIndex, firstIndex));
    } 
    
    public void moveSelectionDown(JList list, DelegatingListModel model) {
        int indices[] = list.getSelectedIndices();
        if (indices.length == 0 || indices[indices.length - 1] == model.getSize() - 1) {
            return;
        }
        model.moveElementsDown(indices);
        list.setSelectedIndices(indices);
        model.refresh();
        int lastIndex = indices[indices.length - 1];
        list.scrollRectToVisible(list.getCellBounds(lastIndex, lastIndex));
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addClassPathButton;
    private javax.swing.JButton addMappingLocationButton;
    private javax.swing.JPanel advancedConfigurationPanel;
    private javax.swing.JLabel classPathLabel;
    private javax.swing.JList classPathList;
    private javax.swing.JScrollPane classPathScrollPane;
    private javax.swing.JComboBox connectionUrlCombo;
    private javax.swing.JLabel connectionUrlLabel;
    private javax.swing.JComboBox dialectComboBox;
    private javax.swing.JLabel dialectLabel;
    private javax.swing.JButton downClassPathButton;
    private javax.swing.JButton downMappingLocationButton;
    private javax.swing.JComboBox driverComboBox;
    private javax.swing.JLabel driverLabel;
    private javax.swing.JTabbedPane extraConfigurationTabbedPane;
    private javax.swing.JPanel importsPanel;
    private javax.swing.JScrollPane importsScrollPane;
    private javax.swing.JTextArea importsTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel mappingLocationsLabel;
    private javax.swing.JList mappingLocationsList;
    private javax.swing.JScrollPane mappingLocationsScrollPane;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JScrollPane propertiesScrollPane;
    private javax.swing.JTextArea propertiesTextArea;
    private javax.swing.JButton removeClassPathButton;
    private javax.swing.JButton removeMappingLocationButton;
    private javax.swing.JPanel simpleConfigurationPanel;
    private javax.swing.JButton upClassPathButton;
    private javax.swing.JButton upMappingLocationButton;
    private javax.swing.JTextField userField;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
    
    public SessionFactoryDescriptor updateOrCreateDescriptor() throws Exception {
        if (descriptor == null) {
            descriptor = new SessionFactoryDescriptor();
        }
        descriptor.setName(nameField.getText());
        descriptor.setMappingLocations(mappingLocations);
        descriptor.setClassPathEntries(classPathEntries);
        descriptor.setDatabaseUser(userField.getText());
        descriptor.setDatabasePassword(String.valueOf(passwordField.getPassword()));
        descriptor.setDatabaseDriverName((String) driverComboBox.getEditor().getItem());
        descriptor.setDatabaseUrl((String) connectionUrlCombo.getEditor().getItem());
        descriptor.setHibernateDialect(String.valueOf(dialectComboBox.getEditor().getItem()));
        descriptor.setUserImports(importsTextArea.getText());
        // TODO - set the extra properties
        Properties props = new Properties();
        ByteArrayInputStream bis = new ByteArrayInputStream(propertiesTextArea.getText().getBytes("ISO-8859-1")); // NOI18N
        props.load(bis);
        descriptor.setExtraProperties(props);
        return descriptor;
    }

    private static class ConnectionDescriptor {

        private final static List<ConnectionDescriptor> ALL_DESCRIPTORS;
        
        private final static String[] EMPTY_STRING_ARRAY = new String[0];
        
        static {
            ALL_DESCRIPTORS = new ArrayList<ConnectionDescriptor>();
            
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.DB2390Dialect", 
                    "DB2390Dialect", 
                    EMPTY_STRING_ARRAY,
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.DB2400Dialect", 
                    "DB2400Dialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.DB2Dialect", 
                    "DB2Dialect", 
                    new String[] {
                        "com.ibm.db2.jcc.DB2Driver",
                        "COM.ibm.db2.jdbc.app.DB2Driver",
                        "COM.ibm.db2.jdbc.net.DB2Driver",
                    },
                    new String[] {
                        "jdbc:db2://server:port/database",
                        "jdbc:db2:[DATABASE_NAME]",
                        "jdbc:db2://[HOST_NAME]:[PORT_NUMBER]/[DATABASE_NAME]"
                    }));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.DerbyDialect", 
                    "DerbyDialect", 
                    new String[] {
                        "org.apache.derby.jdbc.ClientDriver",
                        "org.apache.derby.jdbc.EmbeddedDriver",
                    },
                    new String[] {
                        "jdbc:derby://<server>[:<port>]/databaseName[;URLAttributes=<value>[;...]]",
                        "jdbc:derby:databaseName;URLAttributes"
                    }));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.FirebirdDialect", 
                    "FirebirdDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.FrontBaseDialect", 
                    "FrontBaseDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.GenericDialect", 
                    "GenericDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.HSQLDialect", 
                    "HSQLDialect",
                    new String[] { "org.hsqldb.jdbcDriver" },
                    new String[] {
                        "jdbc:hsqldb:file:path",
                        "jdbc:hsqldb:res:path",
                        "jdbc:hsqldb:hsql:host",
                        "jdbc:hsqldb:hsqls://host[:port][/<alias>][<key-value-pairs>]",
                        "jdbc:hsqldb:http://host[:port][/alias]",
                        "jdbc:hsqldb:https://host[:port][/alias]"
                    }));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.InformixDialect", 
                    "InformixDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.IngresDialect", 
                    "IngresDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.InterbaseDialect", 
                    "InterbaseDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.MckoiDialect", 
                    "MckoiDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.MySQLDialect", 
                    "MySQLDialect", 
                    new String[] {
                        "com.mysql.jdbc.Driver",
                        "org.gjt.mm.mysql.Driver" },
                    new String[] {
                        "jdbc:mysql://[host][,failoverhost...][:port]/[database][?propertyName1][=propertyValue1][&propertyName2][=propertyValue2]",
                    }));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.MySQLMyISAMDialect", 
                    "MySQLMyISAMDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.Oracle9Dialect", 
                    "Oracle9Dialect", 
                    "oracle.jdbc.driver.OracleDriver", 
                    "jdbc:oracle:thin:@<host>:<port>:<SID>"));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.OracleDialect", 
                    "OracleDialect", 
                    "oracle.jdbc.driver.OracleDriver", 
                    "jdbc:oracle:thin:@<host>:<port>:<SID>")); 
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.PointbaseDialect", 
                    "PointbaseDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.PostgreSQLDialect", 
                    "PostgreSQLDialect", 
                    new String[] { "org.postgresql.Driver" },
                    new String[] { "jdbc:postgresql:database", "jdbc:postgresql://host/database", "jdbc:postgresql://host:port/database" }));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.SAPDBDialect", 
                    "SAPDBDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.SQLServer7Dialect", 
                    "SQLServer7Dialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.SQLServerDialect", 
                    "SQLServerDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.SybaseAnywhereDialect", 
                    "SybaseAnywhereDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.SybaseDialect", 
                    "SybaseDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
            ALL_DESCRIPTORS.add(new ConnectionDescriptor(
                    "org.hibernate.dialect.TimesTenDialect", 
                    "TimesTenDialect", 
                    EMPTY_STRING_ARRAY, 
                    EMPTY_STRING_ARRAY));
        }
        
        private String displayName;
        
        private String dialect;
        
        private String[] drivers;
        
        private String[] connectionUrls;

        public ConnectionDescriptor(String dialect, String displayName, String[] drivers, String[] connectionUrls) {
            this.dialect = dialect;
            this.displayName = displayName;
            this.drivers = drivers;
            this.connectionUrls = connectionUrls;
        }

        public ConnectionDescriptor(String dialect, String displayName, String databaseDriver, String connectionUrl) {
            this.dialect = dialect;
            this.displayName = displayName;
            if (databaseDriver != null) {
                this.drivers = new String[] { databaseDriver };
            } else {
                this.drivers = EMPTY_STRING_ARRAY;
            }
            if (connectionUrl != null) {
                this.connectionUrls = new String[] { connectionUrl };
            } else {
                this.connectionUrls = EMPTY_STRING_ARRAY;
            }
        }
        
        public static int indexByDialect(String dialect) {
            if (dialect == null || dialect.length() == 0) {
                return -1;
            }
            int sz = ALL_DESCRIPTORS.size();
            for (int i = 0; i < sz; i++) {
                ConnectionDescriptor elem = ALL_DESCRIPTORS.get(i);

                if (elem.dialect.equals(dialect)) {
                    return i;
                }
            }
            return -1;
        }
        
        public String toString() {
            return dialect;
        }
        
    }
    
    private static class DialectNameRenderer extends DefaultListCellRenderer {
        
        public Component getListCellRendererComponent(JList list, Object item, int i, boolean b1, boolean b2) {
            return super.getListCellRendererComponent(list, ((ConnectionDescriptor) item).displayName, i, b1, b2);
        }
        
    }
    
}
