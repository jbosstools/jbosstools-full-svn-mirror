package org.hibernate.netbeans.console.editor.hql;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;
import org.netbeans.editor.BaseKit;
import org.netbeans.editor.Coloring;
import org.netbeans.editor.Settings;
import org.netbeans.editor.SettingsDefaults;
import org.netbeans.editor.SettingsNames;
import org.netbeans.editor.SettingsUtil;
import org.netbeans.editor.TokenCategory;
import org.netbeans.editor.TokenContext;
import org.netbeans.editor.TokenContextPath;

/**
 * @author leon
 */
public class HqlSettingsInitializer extends Settings.AbstractInitializer {
    
    public static final String NAME = "hql-settings-initializer"; // NOI18N
    
    public HqlSettingsInitializer() {
        super(NAME);
    }
    
    public void updateSettingsMap(Class kitClass, Map settingsMap) {
        if (kitClass == BaseKit.class) {
            new HqlTokenColoringInitializer().updateSettingsMap(kitClass, settingsMap);
        }
        
        if (kitClass == HqlEditorKit.class) {
            SettingsUtil.updateListSetting(
                    settingsMap,
                    SettingsNames.TOKEN_CONTEXT_LIST,
                    new TokenContext[] { HqlTokenContext.CONTEXT }
            );
        }
    }
    
    /**
     * Class for adding syntax coloring to the editor
     */
    private static class HqlTokenColoringInitializer extends SettingsUtil.TokenColoringInitializer {
        
        private static final Font BOLD_FONT = SettingsDefaults.defaultFont.deriveFont(Font.BOLD);

        private static final Coloring KEYWORD_COLORING = new Coloring(BOLD_FONT, Coloring.FONT_MODE_APPLY_STYLE, Color.BLUE, null);

        private static final Coloring JAVA_STRING_COLORING = new Coloring(null, Color.GRAY, null);

        private static final Coloring HQL_STRING_COLORING = new Coloring(null, Color.MAGENTA, null);

        private static final Coloring BROKEN_STRING_COLORING = new Coloring(null, Color.RED, null);

        private static final Coloring NULL_COLORING = new Coloring(null, null, null);

        public HqlTokenColoringInitializer() {
            super(HqlTokenContext.CONTEXT);
        }
        
        
        public Object getTokenColoring(TokenContextPath tokenContextPath,
                TokenCategory tokenIDOrCategory, boolean printingSet) {
            if (!printingSet) {
                int tokenID = tokenIDOrCategory.getNumericID();
                switch (tokenID) {
                    case HqlTokenContext.HQL_STRING_ID:
                        return HQL_STRING_COLORING;
                    case HqlTokenContext.JAVA_STRING_ID:
                        return JAVA_STRING_COLORING;
                    case HqlTokenContext.KEYWORD_ID:
                    case HqlTokenContext.FROM_ID:
                        return KEYWORD_COLORING;
                    case HqlTokenContext.BROKEN_HQL_STRING_ID:
                    case HqlTokenContext.BROKEN_JAVA_STRING_ID:
                        return BROKEN_STRING_COLORING;
                    default:
                        return NULL_COLORING;
                }
            } else {
                return SettingsUtil.defaultPrintColoringEvaluator;
            }
        }
        
    }
    
}
