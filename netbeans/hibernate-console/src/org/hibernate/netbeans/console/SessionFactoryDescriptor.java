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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.hibernate.netbeans.console.option.Options;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.hibernate.netbeans.console.util.HibernateExecutor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.query.HQLQueryPlan;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.ErrorManager;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.Repository;

/**
 * @author leon
 */
public class SessionFactoryDescriptor implements Serializable {
    
    private final static long serialVersionUID = -1;

    private final static String DEFAULT_IMPORTS = "import org.hibernate.*;\nimport org.hibernate.criterion.*;\n";
            
    // TODO - get them from the options
    private transient static final int MAX_HISTORY_ENTRIES = 50;
    
    private transient static final String SESSION_FACTORIES_FOLDER = "Hibernate3SessionFactories";
    
    public transient static final String PROPERTY_NAME = "name";
    
    public transient static final String PROPERTY_CLASS_PATH_ENTRIES = "classPathEntries";
    
    public transient static final String PROPERTY_MAPPING_LOCATIONS = "mappingLocations";
    
    public transient static final String PROPERTY_DATABASE_USER = "databaseUser";
    
    public transient static final String PROPERTY_DATABASE_PASSWORD = "databasePassword";
    
    public transient static final String PROPERTY_DATABASE_URL = "databaseUrl";
    
    public transient static final String PROPERTY_DATABASE_DRIVER_NAME = "databaseDriverName";
    
    public transient static final String PROPERTY_HIBERNATE_DIALECT = "hibernateDialect";
    
    public transient static final String PROPERTY_EXTRA_PROPERTIES = "extraProperties";
    
    public transient static final String PROPERTY_SESSION_OPEN = "sessionOpen";
    
    private transient FileObject storageFile;
    
    private transient static Map<String, SessionFactoryDescriptor> fileObjectName2Instance =
            new HashMap<String, SessionFactoryDescriptor>();
    
    private transient static List<ActionListener> creationListeners = new ArrayList<ActionListener>();
    
    private transient PropertyChangeSupport propertyChangeSupport;
    
    private transient SessionFactoryClassLoader classLoader;
    
    private transient ArrayList<HqlProcessor> queryProcessors;
    
    private transient ArrayList<BshCodeProcessor> bshProcessors;
    
    private transient Session session;

    private LinkedList<HistoryEntry> historyEntries;
    
    private String name;
    
    private List<File> classPathEntries;
    
    private List<File> mappingLocations;
    
    private String databaseUser;
    
    private String databasePassword;
    
    private String databaseUrl;
    
    private String databaseDriverName;
    
    private String hibernateDialect;

    private String userImports;
    
    private Properties extraProperties;
    
    private transient Connection connection;
    
    private Configuration configuration;
    
    private static FileObject ensureFolderExists() throws Exception {
        FileSystem fs = Repository.getDefault().getDefaultFileSystem();
        FileObject folder = fs.findResource(SESSION_FACTORIES_FOLDER);
        if (folder == null || !folder.isValid()) {
            folder = fs.getRoot().createFolder(SESSION_FACTORIES_FOLDER);
        }
        return folder;
    }
    
    private void createStorageFile() throws Exception {
        FileObject folder = ensureFolderExists();
        String name = getNewName();
        fileObjectName2Instance.put(name, this);
        FileObject fo = folder.createData(name);
        storageFile = fo;
    }

    private static String getNewName() {
        long l = System.currentTimeMillis();
        return "SF" + new UUID(l, l).toString();
    }
    
    private static SessionFactoryDescriptor fromFileObject(FileObject fo) throws Exception {
        SessionFactoryDescriptor desc = fileObjectName2Instance.get(fo.getName());
        if (desc != null) {
            return desc;
        }
        InputStream is = fo.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        try {
            desc = (SessionFactoryDescriptor) ois.readObject();
            desc.storageFile = fo;
            fileObjectName2Instance.put(fo.getName(), desc);
            return desc;
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
            }
        }
    }
    
    public static SessionFactoryDescriptor[] listAll() {
        List<SessionFactoryDescriptor> l = new ArrayList<SessionFactoryDescriptor>();
        FileObject folder;
        try {
            folder = ensureFolderExists();
        } catch (Exception ex) {
            ErrorManager.getDefault().notify(ErrorManager.USER, ex);
            return new SessionFactoryDescriptor[0];
        }
        FileObject[] storageFiles = folder.getChildren();
        for (int i = 0; i < storageFiles.length; i++) {
            FileObject storageFile = storageFiles[i];
            String nameExt = storageFile.getNameExt();
            if (nameExt.endsWith(".hql") || nameExt.endsWith(".hbsh")) {
                continue; // It's a editor file
            }
            try {
                l.add(fromFileObject(storageFile));
            } catch (Exception ex) {
                try {
                    storageFile.delete();
                } catch (IOException ioe) {
                    ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ioe);
                }
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
            }
        }
        return l.toArray(new SessionFactoryDescriptor[l.size()]);
    }
    
    public HistoryEntry addHistoryEntry(String statement, int state) {
        HistoryEntry e = new HistoryEntry(statement, state);
        if (historyEntries.size() >= MAX_HISTORY_ENTRIES) {
            historyEntries.removeLast();
        }
        historyEntries.addFirst(e);
        return e;
    }
    
    public List<File> getClassPathEntries() {
        return classPathEntries;
    }
    
    public List<File> getMappingLocations() {
        return mappingLocations;
    }
    
    public static class HistoryEntry implements Serializable {
        
        private static final int SUCCESSFUL_STATEMENT = 1;
        
        private static final int ERROR_STATEMENT = 2;
        
        private String statement;
        
        private int state;
        
        public HistoryEntry(String statement, int state) {
            this.statement = statement;
            this.state = state;
        }
        
        public String getQuery() {
            return statement;
        }
        
        public int getState() {
            return state;
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        PropertyChangeEvent evt = getPropertyChangeEvent(PROPERTY_NAME, this.name, name);
        this.name = name;
        maybeFireEvent(evt);
    }
    
    public String getDatabaseDriverName() {
        return databaseDriverName;
    }
    
    public void setDatabaseDriverName(String databaseDriverName) {
        PropertyChangeEvent evt = getPropertyChangeEvent(PROPERTY_DATABASE_DRIVER_NAME, this.databaseDriverName, databaseDriverName);
        this.databaseDriverName = databaseDriverName;
        maybeFireEvent(evt);
    }
    
    public String getDatabasePassword() {
        return databasePassword;
    }
    
    public void setDatabasePassword(String databasePassword) {
        PropertyChangeEvent evt = getPropertyChangeEvent(PROPERTY_DATABASE_PASSWORD, this.databasePassword, databasePassword);
        this.databasePassword = databasePassword;
        maybeFireEvent(evt);
    }
    
    public String getDatabaseUrl() {
        return databaseUrl;
    }
    
    public void setDatabaseUrl(String databaseUrl) {
        PropertyChangeEvent evt = getPropertyChangeEvent(PROPERTY_DATABASE_URL, this.databaseUrl, databaseUrl);
        this.databaseUrl = databaseUrl;
        maybeFireEvent(evt);
    }
    
    public String getDatabaseUser() {
        return databaseUser;
    }
    
    public void setDatabaseUser(String databaseUser) {
        PropertyChangeEvent evt = getPropertyChangeEvent(PROPERTY_DATABASE_USER, this.databaseUser, databaseUser);
        this.databaseUser = databaseUser;
        maybeFireEvent(evt);
    }
    
    public Properties getExtraProperties() {
        return extraProperties;
    }
    
    public void setExtraProperties(Properties extraProperties) {
        PropertyChangeEvent evt = getPropertyChangeEvent(PROPERTY_EXTRA_PROPERTIES, this.extraProperties, extraProperties);
        this.extraProperties = extraProperties;
        maybeFireEvent(evt);
    }
    
    public void setClassPathEntries(List<File> classPathEntries) {
        PropertyChangeEvent evt = getPropertyChangeEvent(PROPERTY_CLASS_PATH_ENTRIES, this.classPathEntries, classPathEntries);
        this.classPathEntries = classPathEntries;
        maybeFireEvent(evt);
    }
    
    public void setMappingLocations(List<File> mappingLocations) {
        PropertyChangeEvent evt = getPropertyChangeEvent(PROPERTY_MAPPING_LOCATIONS, this.mappingLocations, mappingLocations);
        this.mappingLocations = mappingLocations;
        maybeFireEvent(evt);
    }
    
    public String getHibernateDialect() {
        return hibernateDialect;
    }
    
    public void setHibernateDialect(String hibernateDialect) {
        PropertyChangeEvent evt = getPropertyChangeEvent(PROPERTY_HIBERNATE_DIALECT, this.hibernateDialect, hibernateDialect);
        this.hibernateDialect = hibernateDialect;
        maybeFireEvent(evt);
    }
    
    private PropertyChangeEvent getPropertyChangeEvent(String property, Object oldValue, Object newValue) {
        if (oldValue instanceof String || newValue instanceof String) {
            String oldString = (String) oldValue;
            String newString = (String) newValue;
            if (oldString == null) {
                oldString = "";
            }
            if (newString == null) {
                newString = "";
            }
            if (!oldString.equals(newString)) {
                return new PropertyChangeEvent(this, property, oldValue, newValue);
            }
        } else if (oldValue instanceof Properties || newValue instanceof Properties) {
            Properties oldProps = (Properties) oldValue;
            Properties newProps = (Properties) newValue;
            if (oldProps == null) {
                oldProps = new Properties();
            }
            if (newProps == null) {
                newProps = new Properties();
            }
            if (!oldProps.equals(newProps)) {
                return new PropertyChangeEvent(this, property, oldValue, newValue);
            }
        } else if (oldValue instanceof List || newValue instanceof List) {
            List oldList = (List) oldValue;
            List newList = (List) newValue;
            if (oldList == null) {
                oldList = new ArrayList();
            }
            if (newList == null) {
                newList = new ArrayList();
            }
            if (!oldList.equals(newList)) {
                return new PropertyChangeEvent(this, property, oldValue, newValue);
            }
        }
        return null;
    }
    
    private void maybeFireEvent(PropertyChangeEvent evt) {
        if (evt != null && propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange(evt);
        }
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    public void persist() throws Exception {
        boolean isNew = false;
        if (storageFile == null) {
            createStorageFile();
            isNew = true;
        }
        FileLock lock = storageFile.lock();
        ObjectOutputStream oos = new ObjectOutputStream(storageFile.getOutputStream(lock));
        try {
            oos.writeObject(this);
            if (isNew) {
                invokeCreationListeners(this);
            }
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
            }
            lock.releaseLock();
        }
    }
    
    public FileObject getStorageFile() {
        return storageFile;
    }
    
    public Session getSession() {
        return session;
    }
    
    public void openSession() {
        if (session != null) {
            return;
        }
        final ProgressHandle ph = ProgressHandleFactory.createHandle("Opening " + getName() + " session factory "); // TODO - i18n
        ph.start();
        HibernateExecutor.execute(new Runnable() {
            public void run() {
                try {
                    openSession(ph);
                } catch (final LinkageError ex) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            SessionFactoryOutput.showError(SessionFactoryDescriptor.this, ex);
                        }
                    });
                    classLoader = null;
                    connection = null;
                } catch (final Exception ex) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            SessionFactoryOutput.showError(SessionFactoryDescriptor.this, ex);
                        }
                    });
                    classLoader = null;
                    connection = null;
                } finally {
                    ph.finish();
                }
            }
        });
    }
    
    private Configuration createConfiguration(Properties props, ProgressHandle ph, ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class clazz = classLoader.loadClass("org.hibernate.cfg.Configuration");
        Configuration cfg = (Configuration) clazz.newInstance();
        cfg.setProperties(props);
        for (File f : getMappingFiles(ph)) {
            ph.progress("Adding mapping file " + f.getAbsolutePath());
            cfg.addFile(f);
        }
        return cfg;
    }
    
    private SessionFactoryClassLoader createClassLoader() {
        return new SessionFactoryClassLoader(
                getClass().getClassLoader(),
                (File[]) classPathEntries.toArray(new File[classPathEntries.size()]));
    }
    
    private synchronized void openSession(ProgressHandle ph) throws Exception {
        EventQueue.invokeAndWait(new Runnable() {
            public void run() {
                SessionFactoryOutput.clearError(SessionFactoryDescriptor.this);
            }
        });
        // Make sure we first close the existing session and/or connection
        close();
        classLoader = createClassLoader();
        Properties props = buildProperties();
        Thread currentThread = Thread.currentThread();
        ClassLoader threadClassLoader = currentThread.getContextClassLoader();
        SessionFactory sf = null;
        try {
            currentThread.setContextClassLoader(classLoader);
            configuration = createConfiguration(props, ph, classLoader);
            sf = configuration.buildSessionFactory();
            // Open the connection
            ph.progress("Opening database connection");
            connection = openConnection(props, classLoader);
            // Open the session
            ph.progress("Opening the session");
            session = sf.openSession(connection);
        } catch (Exception ex) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    // Ignore it
                }
            }
            if (sf != null) {
                try {
                    sf.close();
                } catch (HibernateException hex) {
                    // Ignore it
                }
            }
            throw ex;
        } finally {
            currentThread.setContextClassLoader(threadClassLoader);
        }
        
    }
    
    public Connection openConnection() throws Exception {
        return openConnection(buildProperties(), classLoader);
    }
    
    private Connection openConnection(Properties props, ClassLoader cl) throws InstantiationException, SQLException, IllegalAccessException, ClassNotFoundException {
        Class dbDriver = Class.forName(databaseDriverName, false, cl);
        // Open the connection
        Driver driver = (Driver) dbDriver.newInstance();
        DriverManager.registerDriver(driver);
        Enumeration en = DriverManager.getDrivers();
        Connection conn = null;
        while (en.hasMoreElements()) {
            Driver drv = (Driver) en.nextElement();
            if (drv.acceptsURL(databaseUrl)) {
                conn = drv.connect(databaseUrl, props);
            }
        }
        if (conn == null) {
            conn = driver.connect(databaseUrl, props);
        }
        return conn;
    }
    
    private Properties buildProperties() {
        Properties props = new Properties();
        // TODO - load the advanced properties
        props.setProperty(Environment.USER, databaseUser);
        props.setProperty(Environment.PASS, databasePassword);
        props.setProperty(Environment.DIALECT, hibernateDialect);
        props.setProperty(Environment.DRIVER, databaseDriverName);
        props.setProperty(Environment.URL, databaseUrl);
        // For DriverManager
        props.setProperty("user", databaseUser);
        props.setProperty("password", databasePassword);
        return props;
    }
    
    private List<File> getMappingFiles(ProgressHandle ph) {
        List<File> mappingFiles = new ArrayList<File>();
        if (mappingLocations != null) {
            for (File f : mappingLocations) {
                addMappingFiles(mappingFiles, f, ph);
            }
        }
        return mappingFiles;
    }
    
    private static void addMappingFiles(List<File> files, File file, ProgressHandle ph) {
        FileFilter mappingFileFilter = new TokenizingFileFilter(Options.get().getHbmExtensions(), "");
        if (file.isFile() && mappingFileFilter.accept(file)) {
            files.add(file);
            return;
        }
        if (file.isDirectory()) {
            ph.progress("Looking for mappings in " + file.getAbsolutePath()); // TODO
            File[] children = file.listFiles(mappingFileFilter);
            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                if (child.isFile()) {
                    files.add(child);
                } else {
                    addMappingFiles(files, child, ph);
                }
            }
        }
    }
        
    public void close() {
        if (session == null && connection == null && classLoader == null) {
            return;
        }
        classLoader = null;
        if (session != null) {
            try {
                session.getSessionFactory().close();
            } catch (HibernateException ex) {
                // Ignore it
            }
            try {
                session.close();
            } catch (Exception ex) {
                // Ignore it
            }
            session = null;
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ex) {
                // Ignore it
            }
            connection = null;
        }
        configuration = null;
        propertyChangeSupport.firePropertyChange(new PropertyChangeEvent(this, PROPERTY_SESSION_OPEN, Boolean.TRUE, Boolean.FALSE));
    }
    
    public boolean isSessionOpen() {
        return session != null && session.isOpen();
    }
    
    public void registerQueryProcessor(HqlProcessor qp) {
        if (queryProcessors == null) {
            queryProcessors = new ArrayList<HqlProcessor>();
        }
        synchronized (queryProcessors) {
            queryProcessors.add(qp);
        }
    }
    
    public void unregisterHqlProcessor(HqlProcessor qp) {
        if (queryProcessors == null) {
            return;
        }
        synchronized (queryProcessors) {
            queryProcessors.remove(qp);
        }
    }
    
    public void registerBshProcessor(BshCodeProcessor cp) {
        if (bshProcessors == null) {
            bshProcessors = new ArrayList<BshCodeProcessor>();
        }
        synchronized (bshProcessors) {
            bshProcessors.add(cp);
        }
    }
    
    public void unregisterBshProcessor(BshCodeProcessor cp) {
        if (bshProcessors == null) {
            return;
        }
        synchronized (bshProcessors) {
            bshProcessors.remove(cp);
        }
    }
    
    public void invokeHqlProcessors(String hql) {
        if (queryProcessors == null) {
            return;
        }
        HqlQuery q = new HqlQuery(hql);
        ArrayList<HqlProcessor> qps;
        synchronized (queryProcessors) {
            qps = new ArrayList<HqlProcessor>(queryProcessors);
        }
        for (HqlProcessor qp : qps) {
            qp.maybeExecuteQuery(q);
        }
    }
    
    public void invokeBshProcessors(String code) {
        if (bshProcessors == null) {
            return;
        }
        BshCode cc = new BshCode(code);
        ArrayList<BshCodeProcessor> ccps;
        synchronized (bshProcessors) {
            ccps = new ArrayList<BshCodeProcessor>(bshProcessors);
        }
        for (BshCodeProcessor ccp : ccps) {
            ccp.maybeExecuteBshCode(cc);
        }
    }
    
    
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    public void reload() {
        // We need to explicitly close the session, otherwise openSession 
        // will return imediately
        close();
        openSession();
    }
    
    public void updateSchema() {
        final ProgressHandle ph = ProgressHandleFactory.createHandle("Updating database schema"); // TODO
        ph.start();
        HibernateExecutor.execute(new Runnable() {
            public void run() {
                Properties props = buildProperties();
                Thread currentThread = Thread.currentThread();
                ClassLoader currentThreadClassLoader = currentThread.getContextClassLoader();
                ClassLoader sfClassLoader = createClassLoader();
                StringBuffer statements = new StringBuffer();
                try {
                    EventQueue.invokeAndWait(new Runnable() {
                        public void run() {
                            SessionFactoryOutput.clearError(SessionFactoryDescriptor.this);
                        }
                    });
                    currentThread.setContextClassLoader(sfClassLoader);
                    Configuration cfg = createConfiguration(props, ph, sfClassLoader);
                    Dialect dialect = Dialect.getDialect(props);
                    Connection conn = openConnection(props, sfClassLoader);
                    try {
                        String[] updates = cfg.generateSchemaUpdateScript(dialect, new DatabaseMetadata(conn, dialect));
                        Statement stm = conn.createStatement();
                        for (int i = 0; i < updates.length; i++) {
                            String sql = updates[i];
                            ph.progress(sql);
                            statements.append(sql).append("\n");
                            stm.executeUpdate(sql);
                        }
                        conn.commit();
                    } finally {
                        try {
                            conn.close();
                        } catch (SQLException ex) {
                            ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
                        }
                    }
                } catch (final Exception ex) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            SessionFactoryOutput.showError(SessionFactoryDescriptor.this, ex);
                        }
                    });
                } catch (final LinkageError ex) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            SessionFactoryOutput.showError(SessionFactoryDescriptor.this, ex);
                        }
                    });
                } finally {
                    currentThread.setContextClassLoader(currentThreadClassLoader);
                    ph.finish();
                }
            }
        });
    }
    
    public String toSql(String hql) {
        SessionFactoryImpl sfImpl = (SessionFactoryImpl) getSession().getSessionFactory();
        HQLQueryPlan plan = sfImpl.getQueryPlanCache().getHQLQueryPlan(hql, true, Collections.EMPTY_MAP);
        String[] str = plan.getSqlStrings();
        final StringBuffer buff = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            buff.append(str[i]).append("\n");
        }
        return buff.toString();
    }
    
    public static void addCreationListener(ActionListener al) {
        creationListeners.add(al);
    }
    
    public static void removeCreationListener(ActionListener al) {
        creationListeners.remove(al);
    }
    
    private static void invokeCreationListeners(SessionFactoryDescriptor instance) {
        ActionEvent evt = new ActionEvent(instance, ActionEvent.ACTION_PERFORMED, null);
        evt.setSource(instance);
        List<ActionListener> copy = new ArrayList<ActionListener>(creationListeners);
        for (ActionListener a : copy) {
            a.actionPerformed(evt);
        }
    }

    SessionFactoryDescriptor copy() throws Exception {
        FileObject fo = getStorageFile().copy(storageFile.getParent(), getNewName(), "");
        SessionFactoryDescriptor desc = fromFileObject(fo);
        desc.setName("Copy of " + getName());
        desc.persist();
        invokeCreationListeners(desc);
        return desc;
    }

    public String getImplicitImports() {
        if (userImports == null || userImports.trim().length() == 0) {
            return DEFAULT_IMPORTS;
        } else {
            return DEFAULT_IMPORTS + "\n" + userImports;
        }
    }

    public void setUserImports(String userImports) {
        this.userImports = userImports;
    }

    public String getUserImports() {
        return userImports;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
