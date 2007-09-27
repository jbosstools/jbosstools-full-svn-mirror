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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.openide.ErrorManager;

/**
 * @author leon
 */
public class SessionFactoryClassLoader extends ClassLoader {

    private File[] entries;
    
    private Map<String, File> package2File = new HashMap<String, File>();
    
    public SessionFactoryClassLoader(ClassLoader parent, File[] entries) {
        super(parent);
        this.entries = entries;
    }

    protected Class loadClass(String name, boolean b) throws ClassNotFoundException {
        Class clazz = findLoadedClass(name);
        if (clazz != null) {
            return clazz;
        }
        String packageName = null;
        int lastDotIndex = name.lastIndexOf(".");
        if (lastDotIndex != -1) {
            packageName = name.substring(0, lastDotIndex);
        }
        if (packageName != null && (
                packageName.startsWith("java") || 
                packageName.startsWith("javax") || 
                packageName.startsWith("com.sun") ||
                packageName.startsWith("org.hibernate") || 
                packageName.startsWith("org.dom4j") ||
                packageName.startsWith("net.sf.cglib") || 
                packageName.startsWith("org.w3c") ||
                packageName.startsWith("antlr") ||
                packageName.startsWith("org.objectweb.asm") ||
                packageName.startsWith("org.apache.commons.collections") ||
                packageName.startsWith("net.sf.ehcache") ||
                packageName.startsWith("org.apache.commons.logging") ||
                packageName.startsWith("org.netbeans"))) {
            clazz = super.loadClass(name, b);
        }
        if (clazz != null) {
            return clazz;
        }
        if (packageName != null && packageName.startsWith("org.apache.log4j")) {
            // Throw CNFE because we use java.util.logging hander in
            // the logging output
            throw new ClassNotFoundException("Log4J is forbidden");
        }
        int dotIndex = name.indexOf(".");
        String fileName = null;
        String separator = File.separator;
        if (separator.equals("\\")) {
            separator = "\\\\";
        }
        if (dotIndex != -1) {
            fileName = name.replaceAll("\\.", separator) + ".class";
        } else {
            fileName = name + ".class";
        }
        Class loadedClass = null;
        InputStream is = getLocalResourceAsStream(packageName, fileName);
        if (is != null) {
            try {
                loadedClass = loadClass(name, is);
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    // Ignore it
                }
            }
        }
        if (loadedClass != null) {
            return loadedClass;
        }
        return super.loadClass(name, b);
    }
    
    private Class loadClass(String className, InputStream is) {
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024 * 5];
            int readBytes = 0;
            while ((readBytes = bis.read(bytes)) != -1) {
                os.write(bytes, 0, readBytes);
            }
            byte[] b = os.toByteArray();
            return defineClass(className, b, 0, b.length);
        } catch (ClassFormatError ex) {
            ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
            return null;
        } catch (IOException ex) {
            ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
            return null;
        }
    }

    protected PermissionCollection getPermissions(CodeSource codeSource) {
        Permissions perms = new Permissions();
        perms.add(new AllPermission());
        perms.setReadOnly();
        return perms;
    }
    
    private URL getLocalResource(String packageName, String name) {
        File preferred = null;
        if (packageName != null) {
            preferred = package2File.get(packageName);
        }
        List<File> files = new ArrayList<File>();
        if (preferred != null) {
            files.add(preferred);
        }
        for (File f : entries) {
            if (f == preferred) {
                continue;
            }
            files.add(f);
        }
        for (File entry : files) {
            if (entry.isDirectory() && entry.exists()) {
                File f = new File(entry, name);
                if (f.exists()) {
                    try {
                        package2File.put(packageName, entry);
                        return f.toURL();
                    } catch (MalformedURLException ex) {
                        continue;
                    }
                }
            } else {
                if (entry.isFile() && entry.exists()) {
                    try {
                        ZipFile zf = new ZipFile(entry);
                        // Zip entries are delimited by /
			name = name.replaceAll("\\\\", "/");
                        ZipEntry zipEntry = zf.getEntry(name);
                        if (zipEntry != null) {
                            // URL's can contain only /
			    String url = entry.getAbsolutePath().replaceAll("\\\\", "/");
			    if (!url.startsWith("/")) {
				url = "/" + url;
			    }
                            URL r = new URL("jar:file://" + url + "!/" + name);
                            package2File.put(packageName, entry);
                            return r;
                        }
                    } catch (ZipException ex) {
                        // continue
                    } catch (IOException ex) {
                        // continue
                    }
                    
                }
            }
        }
        return null;
    }
    
    private InputStream getLocalResourceAsStream(String packageName, String name) {
        URL res = getLocalResource(packageName, name);
        if (res == null) {
            return null;
        }
        try {
            return res.openStream();
        } catch (IOException ex) {
            return null;
        }
    }
    
    public InputStream getResourceAsStream(String name) {
        InputStream is = getLocalResourceAsStream(null, name);
        return is != null ? is : super.getResourceAsStream(name);
    }

    
}
