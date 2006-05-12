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

package org.hibernate.netbeans.console.option;

import java.util.prefs.Preferences;

/**
 * @author leon
 */
public class Options {

    private static final String HBM_EXTENSIONS = "hbmExtensions";

    private static final String DEF_HBM_EXTENSIONS = "hbm.xml,hbm";

    private static final String MAX_QUERY_RESULTS = "maxQueryResults";

    private static final String SHOW_LOGGING_WINDOW = "showLoggingWindow";

    private static final int DEF_MAX_QUERY_RESULTS = 100;

    private static final boolean DEF_SHOW_LOGGING_WINDOW = true;

    private static Options options;


    private Options() {
    }

    public static Options get() {
        if (options == null) {
            options = new Options();
        }
        return options;
    }

    public String getHbmExtensions() {
        return Preferences.userNodeForPackage(Options.class).get(HBM_EXTENSIONS, DEF_HBM_EXTENSIONS);
    }

    public void setHbmExtensions(String str) {
        if (str.trim().length() == 0) {
            str = "hbm.xml,hbm";
        }
        Preferences.userNodeForPackage(Options.class).put(HBM_EXTENSIONS, str);
    }

    public int getMaxQueryResults() {
        return Preferences.userNodeForPackage(Options.class).getInt(MAX_QUERY_RESULTS, DEF_MAX_QUERY_RESULTS);
    }

    public void setMaxQueryResults(int i) {
        if (i < 1 || i > 1000) {
            i = DEF_MAX_QUERY_RESULTS;
        }
        Preferences.userNodeForPackage(Options.class).putInt(MAX_QUERY_RESULTS, i);
    }

    public boolean isShowLoggingWindow() {
        return Preferences.userNodeForPackage(Options.class).getBoolean(SHOW_LOGGING_WINDOW, DEF_SHOW_LOGGING_WINDOW);
    }

    public void setShowLoggingWindow(boolean b) {
        Preferences.userNodeForPackage(Options.class).putBoolean(SHOW_LOGGING_WINDOW, b);
    }
    
}
