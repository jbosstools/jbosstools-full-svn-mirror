package org.hibernate.netbeans.console.output;

import java.awt.Image;
import java.io.Serializable;
import javax.swing.Icon;
import org.hibernate.netbeans.console.Icons;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.Utilities;

/**
 * @author leon
 */
public abstract class OutputMultiViewDescription implements MultiViewDescription, Serializable {

    private OutputMultiViewElement element;

    private static final Image ICON = Utilities.loadImage(Icons.HIBERNATE_SMALL);
    
    public OutputMultiViewDescription() {
    }

    public final MultiViewElement createElement() {
        if (element == null) {
            element = createOutputElement();
        }
        return element;
    }
    
    public OutputMultiViewElement getElement() {
        return element;
    }

    public Image getIcon() {
        return ICON;
    }

    public abstract OutputMultiViewElement createOutputElement();
    
}
