package org.hibernate.netbeans.console.editor.bsh;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.modules.editor.plain.PlainKit;

/**
 * @author Leon
 */
public class BshEditorKit extends PlainKit {
    
    public static final String MIME_TYPE = "text/x-hbsh"; // NOI18N

    public BshEditorKit() {
    }

    public String getContentType() {
        return MIME_TYPE;
    }

    
}
