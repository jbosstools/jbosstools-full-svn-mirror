package org.hibernate.netbeans.console;

import java.io.File;
import java.util.StringTokenizer;

class TokenizingFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
    
    private String[] extensions;
    
    private String description;

    public TokenizingFileFilter(String exts, String description) {
        StringTokenizer tok = new StringTokenizer(exts, ",");
        extensions = new String[tok.countTokens()];
        int i = 0;
        while (tok.hasMoreTokens()) {
            String s = tok.nextToken().trim();
            if (!s.startsWith(".")) {
                s = "." + s;
            }
            extensions[i++] = s;
        }
        this.description = description;
    }

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String name = file.getName();
        for(String ext : extensions) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return description;
    }
    
}