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

import bsh.BshClassManager;
import bsh.Interpreter;
import bsh.NameSpace;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.hibernate.netbeans.console.SessionFactoryDescriptor;

/**
 * @author leon
 */
public class JavaCodeInterpreter {
    
    private static JavaCodeInterpreter interpreter;
    
    private Interpreter bsh;
    
    private JavaCodeInterpreter() {
        bsh = new Interpreter();
    }

    public static JavaCodeInterpreter getInterpreter() {
        if (interpreter == null) {
            interpreter = new JavaCodeInterpreter();
        }
        return interpreter;
    }
    
    public List<Object> getResults(String code, SessionFactoryDescriptor descr) throws Exception {
        BshClassManager classManager = new BshClassManager();
        List<File> classPathLocations = descr.getClassPathEntries();
        for (File f : classPathLocations) {
            if (f.exists()) {
                classManager.addClassPath(f.toURL());
            }
        }
        classManager.setClassLoader(descr.getClassLoader());
        NameSpace ns = new NameSpace(classManager, descr.getName());
        StringTokenizer tok = new StringTokenizer(descr.getImplicitImports(), ";");
        while (tok.hasMoreTokens()) {
            String imp = tok.nextToken().trim();
            if (imp.length() == 0) {
                continue;
            }
            if (imp.indexOf("import ") != -1) {
                imp = imp.substring("import ".length()).trim();
            }
            if (imp.endsWith(".*")) {
                ns.importPackage(imp.substring(0, imp.length() - 2));
            } else {
                ns.importClass(imp);
            }
        }
        bsh.setNameSpace(ns);
        bsh.set("session", descr.getSession());
        Object o = bsh.eval(code);
        if (o instanceof List) {
            return (List<Object>) o;
        } else if (o != null) {
            List<Object> l = new ArrayList<Object>();
            l.add(o);
            return l;
        } else {
            return null;
        }
    }
    
    
    
}
