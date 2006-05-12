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
