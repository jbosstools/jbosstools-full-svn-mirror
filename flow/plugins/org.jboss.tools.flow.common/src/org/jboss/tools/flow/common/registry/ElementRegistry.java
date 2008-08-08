package org.jboss.tools.flow.common.registry;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.requests.CreationFactory;
import org.jboss.tools.flow.common.Logger;
import org.jboss.tools.flow.common.model.Container;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.strategy.AcceptsElementStrategy;
import org.jboss.tools.flow.common.strategy.AcceptsIncomingConnectionStrategy;
import org.jboss.tools.flow.common.strategy.AcceptsOutgoingConnectionStrategy;
import org.jboss.tools.flow.common.wrapper.DefaultConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultContainerWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultFlowWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultNodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class ElementRegistry {
	
	private static final String elementsExtensionPointId = "org.jboss.tools.flow.common.elements";
	private static HashMap<String, IConfigurationElement> elementMap = null;
	
	private static void initializeRegistry() {
		elementMap = new HashMap<String, IConfigurationElement>();
		IConfigurationElement[] configurationElements = 
			Platform.getExtensionRegistry().getConfigurationElementsFor(elementsExtensionPointId);
		for (IConfigurationElement configurationElement: configurationElements) {
			elementMap.put(configurationElement.getAttribute("id"), configurationElement);
		}
	}
	
	private static Wrapper createWrapper(IConfigurationElement configurationElement) 
			throws CoreException {
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children.length != 1) return null;
		String type = children[0].getName();
		if ("flow".equals(type)) {
			return createFlow(configurationElement); 
		} else if ("container".equals(type)) {
			return createContainer(configurationElement);
		} else if ("node".equals(type)) {
			return createNode(configurationElement);
		} else if ("connection".equals(type)) {
			return createConnection(configurationElement);
		} else {
			return null;
		}
	}
	
	private static Wrapper createConnection(IConfigurationElement configurationElement)
			throws CoreException {
		return new DefaultConnectionWrapper();
	}
	
	private static Wrapper createNode(IConfigurationElement configurationElement)
			throws CoreException {
		Object element = configurationElement.createExecutableExtension("class");
		if (!(element instanceof Node)) {
			String message = "Expecting to instantiate a org.jboss.tools.flow.common.model.Node instance.";
			Logger.logError(message, new RuntimeException(message));
			return null;
		}
		DefaultNodeWrapper result = new DefaultNodeWrapper();
		result.setElement(element);
		AcceptsIncomingConnectionStrategy acceptsIncomingConnectionStrategy = createAcceptsIncomingConnectionStrategy(configurationElement);
		if (acceptsIncomingConnectionStrategy != null) {
			acceptsIncomingConnectionStrategy.setNode((Node)element);
			result.setAcceptsIncomingConnectionStrategy(acceptsIncomingConnectionStrategy);
		}
		AcceptsOutgoingConnectionStrategy acceptsOutgoingConnectionStrategy = createAcceptsOutgoingConnectionStrategy(configurationElement);
		if (acceptsOutgoingConnectionStrategy != null) {
			acceptsOutgoingConnectionStrategy.setNode((Node)element);
			result.setAcceptsOutgoingConnectionStrategy(acceptsOutgoingConnectionStrategy);
		}
		((Node)element).setName(configurationElement.getAttribute("name"));
		return result;
	}
	
	private static Wrapper createContainer(IConfigurationElement configurationElement) 
			throws CoreException {
		Object element = configurationElement.createExecutableExtension("class");
		if (!(element instanceof Container)) {
			String message = "Expecting to instantiate a org.jboss.tools.flow.common.model.Container instance.";
			Logger.logError(message, new RuntimeException(message));
			return null;
		}
		DefaultContainerWrapper result = new DefaultContainerWrapper();
		result.setElement(element);
		AcceptsElementStrategy acceptsElementStrategy = createAcceptsElementStrategy(configurationElement);
		if (acceptsElementStrategy != null) {
			acceptsElementStrategy.setContainer((Container)element);
			result.setAcceptsElementStrategy(acceptsElementStrategy);
		}
		AcceptsIncomingConnectionStrategy acceptsIncomingConnectionStrategy = createAcceptsIncomingConnectionStrategy(configurationElement);
		if (acceptsIncomingConnectionStrategy != null) {
			acceptsIncomingConnectionStrategy.setNode((Node)element);
			result.setAcceptsIncomingConnectionStrategy(acceptsIncomingConnectionStrategy);
		}
		AcceptsOutgoingConnectionStrategy acceptsOutgoingConnectionStrategy = createAcceptsOutgoingConnectionStrategy(configurationElement);
		if (acceptsOutgoingConnectionStrategy != null) {
			acceptsOutgoingConnectionStrategy.setNode((Node)element);
			result.setAcceptsOutgoingConnectionStrategy(acceptsOutgoingConnectionStrategy);
		}
		if (element instanceof Node) {
			((Node)element).setName(configurationElement.getAttribute("name"));
		}
		return result;
	}
	
	private static AcceptsIncomingConnectionStrategy createAcceptsIncomingConnectionStrategy(IConfigurationElement configurationElement) 
			throws CoreException {
		Object result = null;
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children[0].getAttribute("acceptsIncomingConnectionStrategy") != null) {
			result = children[0].createExecutableExtension("acceptsIncomingConnectionStrategy");
		}
		return (AcceptsIncomingConnectionStrategy)result;
	}
	
	private static AcceptsOutgoingConnectionStrategy createAcceptsOutgoingConnectionStrategy(IConfigurationElement configurationElement) 
			throws CoreException {
		Object result = null;
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children[0].getAttribute("acceptsOutgoingConnectionStrategy") != null) {
			result = children[0].createExecutableExtension("acceptsOutgoingConnectionStrategy");
		}
		return (AcceptsOutgoingConnectionStrategy)result;
	}
	
	private static AcceptsElementStrategy createAcceptsElementStrategy(IConfigurationElement configurationElement) 
			throws CoreException {
		Object result = null;
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children[0].getAttribute("acceptsElementStrategy") != null) {
			result = children[0].createExecutableExtension("acceptsElementStrategy");
		}
		return (AcceptsElementStrategy)result;
	}
	
	private static Wrapper createFlow(IConfigurationElement configurationElement) 
			throws CoreException {
		Object element = configurationElement.createExecutableExtension("class");
		if (!(element instanceof Container)) {
			String message = "Expecting to instantiate a org.jboss.tools.flow.common.model.Container instance.";
			Logger.logError(message, new RuntimeException(message));
			return null;
		}
		DefaultFlowWrapper result = new DefaultFlowWrapper();		
		result.setElement(element);
		AcceptsElementStrategy acceptsElementStrategy = createAcceptsElementStrategy(configurationElement);
		if (acceptsElementStrategy != null) {
			acceptsElementStrategy.setContainer((Container)result.getElement());
			result.setAcceptsElementStrategy(acceptsElementStrategy);
		}
		if (element instanceof Flow) {
			((Flow)element).setName(configurationElement.getAttribute("name"));
		}
		return result;
	}
	
	public static Wrapper createWrapper(String elementId) {
		if (elementMap == null) {
			initializeRegistry();
		}
		IConfigurationElement configurationElement = elementMap.get(elementId);
		if (configurationElement != null) {
			try {
				return createWrapper(configurationElement);
			} catch (CoreException e) {
				Logger.logError("Creating a wrapper for " + elementId + " failed.", e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static CreationFactory getCreationFactory(final String elementId) {
		return new CreationFactory() {
			public Object getNewObject() {
				return createWrapper(elementId);
			}
			public Object getObjectType() {
				return elementId;
			}			
		};
	}

}
