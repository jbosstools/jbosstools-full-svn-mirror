package org.hibernate.netbeans.console.option;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.hibernate.netbeans.console.Icons;
import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

public final class HCOptionsCategory extends OptionsCategory {
    
    private final static Icon ICON = new ImageIcon(Utilities.loadImage(Icons.HIBERNATE_LARGE));
    
    public Icon getIcon() {
        return ICON;
    }
    
    public String getCategoryName() {
        return NbBundle.getMessage(HCOptionsCategory.class, "OptionsCategory_Name");
    }
    
    public String getTitle() {
        return NbBundle.getMessage(HCOptionsCategory.class, "OptionsCategory_Title");
    }
    
    public OptionsPanelController create() {
        return new HCOptionsPanelController();
    }
    
}
