package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.jboss.tools.flow.common.editor.GenericModelEditor;
import org.jboss.tools.flow.common.editpart.DefaultEditPartFactory;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class JpdlEditor extends GenericModelEditor {

    protected EditPartFactory createEditPartFactory() {
        return new DefaultEditPartFactory();
    }

    protected PaletteRoot createPalette() {
        return new JpdlPaletteFactory().createPalette();
    }

    protected Object createModel() {
        return ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.process");
    }
    
    protected void writeModel(OutputStream os) throws IOException {
        Object object = getModel();
        if (object instanceof Wrapper) {
        	JpdlSerializer.serialize((Wrapper)object, os);
        }
    }
    
    protected void createModel(InputStream is) {
    	setModel(JpdlDeserializer.deserialize(is));
//    	StringBuffer stringBuffer = new StringBuffer();
//    	int c = -1;
//    	do {
//    		try {
//    			c = is.read();
//    			if (c != -1) {
//    				stringBuffer.append((char)c);
//    			}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//    	} while (c != -1);
//        System.out.println(stringBuffer);
    }
    
}
