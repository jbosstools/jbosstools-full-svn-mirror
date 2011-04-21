/**
 * 
 */
package org.jboss.tools.smooks.ui.gef.commandprocessor;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.ui.editors.SmooksFormEditor;
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.SmooksExtensionPointConstants;

/**
 * @author Dart
 * 
 */
public class CommandProcessorFactory {

	private static CommandProcessorFactory instance;

	private HashMap<String, IConfigurationElement> processorMap = new HashMap<String, IConfigurationElement>();

	private CommandProcessorFactory() {
		loadExtensions();
	}

	private void loadExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint ep = registry
				.getExtensionPoint(SmooksExtensionPointConstants.EXTENTION_POINT_COMMAND_PROCESSOR);
		IConfigurationElement[] elements = ep.getConfigurationElements();
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			if (!element
					.getName()
					.equals(
							SmooksExtensionPointConstants.EXTENTION_POINT_ELEMENT_COMMAND_PROCESSOR))
				continue;
			String sourceId = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_SOURCEID);
			String targetId = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_TARGETID);
			if (sourceId != null && targetId != null) {
				processorMap.put(generateKey(sourceId, targetId), element);
			}
		}
	}

	private String generateKey(String sourceId, String targetId) {
		return sourceId + ":" + targetId;
	}

	public synchronized static CommandProcessorFactory getInstance() {
		if (instance == null) {
			instance = new CommandProcessorFactory();
		}
		return instance;
	}
	
	public boolean processGEFCommand(Command command, EditPart editPart) throws CoreException {
		if(editPart != null && editPart instanceof GraphicalEditPart){
			DefaultEditDomain domain = (DefaultEditDomain)((GraphicalViewer)((GraphicalEditPart)editPart).getViewer()).getEditDomain();
			IEditorPart pa = domain.getEditorPart();
			if(pa instanceof SmooksGraphicalFormPage){
				SmooksGraphicalFormPage page = (SmooksGraphicalFormPage)pa;
				if(page != null){
					String sourceId = page.getSourceDataTypeID();
					String targetId = page.getTargetDataTypeID();
					return processGEFCommand(command, sourceId,targetId,page.getSmooksConfigurationFileGenerateContext());
				}
			}
		}
		return true;
	}

	public boolean processGEFCommand(Command command, String sourceId,
			String targetId ,  SmooksConfigurationFileGenerateContext context) throws CoreException {
		ICommandProcessor pro = getCommandProcessor(sourceId, targetId);
		if (pro != null) {
			return pro.processGEFCommand(command , context);
		}
		return true;
	}

	public ICommandProcessor getCommandProcessor(String sourceId,
			String targetId) throws CoreException {
		if(sourceId == null || targetId == null) return null;
		IConfigurationElement element = processorMap.get(generateKey(sourceId,
				targetId));
		if (element != null) {
			String clazz = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CLASS);
			if (clazz != null) {
				Object obj = element.createExecutableExtension(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CLASS);
				if (obj instanceof ICommandProcessor) {
					return (ICommandProcessor) obj;
				}
			}
		}
		return null;
	}
}
