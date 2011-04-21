package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.jboss.tools.flow.jpdl4.model.CancelEndEvent;
import org.jboss.tools.flow.jpdl4.model.ErrorEndEvent;
import org.jboss.tools.flow.jpdl4.model.ExclusiveGateway;
import org.jboss.tools.flow.jpdl4.model.ForkParallelGateway;
import org.jboss.tools.flow.jpdl4.model.HqlTask;
import org.jboss.tools.flow.jpdl4.model.HumanTask;
import org.jboss.tools.flow.jpdl4.model.JavaTask;
import org.jboss.tools.flow.jpdl4.model.JoinParallelGateway;
import org.jboss.tools.flow.jpdl4.model.Process;
import org.jboss.tools.flow.jpdl4.model.ProcessNode;
import org.jboss.tools.flow.jpdl4.model.ScriptTask;
import org.jboss.tools.flow.jpdl4.model.SequenceFlow;
import org.jboss.tools.flow.jpdl4.model.ServiceTask;
import org.jboss.tools.flow.jpdl4.model.SqlTask;
import org.jboss.tools.flow.jpdl4.model.StartEvent;
import org.jboss.tools.flow.jpdl4.model.SuperState;
import org.jboss.tools.flow.jpdl4.model.TerminateEndEvent;
import org.jboss.tools.flow.jpdl4.model.WaitTask;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class JpdlSerializer {
	
	private static TransformerFactory transformerFactory = TransformerFactory.newInstance();
	private static Transformer transformer = null;
	
	static {
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		} catch (TransformerConfigurationException e) {				
			Logger.logError("Error while creating XML tranformer.", e);	
		}
	}
	
    public void serialize(Wrapper wrapper, OutputStream os) throws IOException {
    	StringBuffer buffer = new StringBuffer();
    	appendToBuffer(buffer, wrapper, 0);
    	Writer writer = new OutputStreamWriter(os);
    	writer.write(buffer.toString());
    	writer.close();
    }
    
    private void appendToBuffer(StringBuffer buffer, Wrapper wrapper, int level) {
    	Object object = wrapper.getElement();
        if (!(object instanceof Element)) return;
       	appendOpening(buffer, wrapper, level);
       	StringBuffer body = new StringBuffer();
       	appendBody(body, wrapper, level);
       	if (body.length() > 0) {
       		buffer.append(">");
       		buffer.append(body);
       		appendClosing(buffer, wrapper, level);
       	} else {
       		buffer.append("/>"); 
       	}
    }
    
    
    
    private void appendNodeList(StringBuffer buffer, ArrayList<Node> nodeList) {
    	if (transformer == null) {
    		Logger.logInfo("Skipping append nodes as transformer is not initialized.");
    		return;
    	}
    	DOMSource domSource = new DOMSource();
    	for (Node node : nodeList) {
        	StringWriter writer = new StringWriter();
        	domSource.setNode(node);
        	Result result = new StreamResult(writer);
        	try {
				transformer.transform(domSource, result);
			} catch (TransformerException e) {
				Logger.logError("Exception while transforming xml.", e);
			}
    		buffer.append(writer.getBuffer());
    	}
    }
    
    interface WrapperSerializer {
    	void appendOpening(StringBuffer buffer, Wrapper wrapper, int level);
    }
    
    abstract class AbstractWrapperSerializer implements WrapperSerializer {
    	protected abstract List<String> getAttributesToSave();
    	protected abstract void appendAttributeToSave(String nodeName, StringBuffer buffer, Wrapper wrapper);
    	@SuppressWarnings("unchecked")
		protected void appendLeadingNodes(StringBuffer buffer, Wrapper wrapper, int level) {
        	ArrayList<Node> leadingNodeList = (ArrayList<Node>)wrapper.getElement().getMetaData("leadingNodes");
        	boolean appendLeadingNodes = leadingNodeList != null && !leadingNodeList.isEmpty();
        	if (appendLeadingNodes) {
        		appendNodeList(buffer, leadingNodeList);
        	} else {
        		buffer.append("\n");
        		appendPadding(buffer, level);
        	}
    	}
    	protected void appendDefaultAttribute(StringBuffer buffer, Node node) {
    		buffer.append(" " + node.getNodeName() + "=\"" + node.getNodeValue() + "\"");
    	}
    	protected void appendAttributes(StringBuffer buffer, Wrapper wrapper, int level) {
    		Element element = wrapper.getElement();
    		if (element == null) return;
    		NamedNodeMap attributes = (NamedNodeMap)element.getMetaData("attributes");
    		List<String> attributeNames = getAttributesToSave();
    		if (attributes != null) {
	     		for (int i = 0; i < attributes.getLength(); i++) {
	    			String nodeName = attributes.item(i).getNodeName();
	    			if (attributeNames.contains(nodeName)) {
	    				appendAttributeToSave(nodeName, buffer, wrapper);
	    				attributeNames.remove(nodeName);
	    			} else {
	    				appendDefaultAttribute(buffer, attributes.item(i));
	    			}
	    		}
    		}
     		for (int i = 0; i < attributeNames.size(); i++) {
     			appendAttributeToSave(attributeNames.get(i), buffer, wrapper);
     		}
    	}
    	public void appendOpening(StringBuffer buffer, Wrapper wrapper, int level) {
    		appendLeadingNodes(buffer, wrapper, level);
    		buffer.append("<" + getNodeName(wrapper.getElement()));
    		appendAttributes(buffer, wrapper, level);
    	}
    	public void appendClosing(StringBuffer buffer, Wrapper wrapper) {
    		buffer.append("</" + getNodeName(wrapper.getElement()) + ">");
    	}
    }
    
    private String getNodeName(Element element) {
    	IConfigurationElement configuration = (IConfigurationElement)element.getMetaData("configurationElement");
    	String elementId = configuration.getAttribute("id");
		if ("org.jboss.tools.flow.jpdl4.process".equals(elementId)) return "process";
		else if ("org.jboss.tools.flow.jpdl4.startEvent".equals(elementId)) return "start";
		else if ("org.jboss.tools.flow.jpdl4.terminateEndEvent".equals(elementId)) return "end";
		else if ("org.jboss.tools.flow.jpdl4.errorEndEvent".equals(elementId)) return "end-error";
		else if ("org.jboss.tools.flow.jpdl4.cancelEndEvent".equals(elementId)) return "end-cancel";
		else if ("org.jboss.tools.flow.jpdl4.waitTask".equals(elementId)) return "state";
		else if ("org.jboss.tools.flow.jpdl4.hqlTask".equals(elementId)) return "hql";
		else if ("org.jboss.tools.flow.jpdl4.sqlTask".equals(elementId)) return "sql";
		else if ("org.jboss.tools.flow.jpdl4.javaTask".equals(elementId)) return "java";
		else if ("org.jboss.tools.flow.jpdl4.scriptTask".equals(elementId)) return "script";
		else if ("org.jboss.tools.flow.jpdl4.serviceTask".equals(elementId)) return "esb";
		else if ("org.jboss.tools.flow.jpdl4.humanTask".equals(elementId)) return "task";
		else if ("org.jboss.tools.flow.jpdl4.exclusiveGateway".equals(elementId)) return "exclusive";
		else if ("org.jboss.tools.flow.jpdl4.parallelJoinGateway".equals(elementId)) return "join";
		else if ("org.jboss.tools.flow.jpdl4.parallelForkGateway".equals(elementId)) return "fork";
		else if ("org.jboss.tools.flow.jpdl4.sequenceFlow".equals(elementId)) return "flow";
		else return null;
    }
    
    class SequenceFlowWrapperSerializer extends AbstractWrapperSerializer {
    	protected List<String> getAttributesToSave() {
    		ArrayList<String> result = new ArrayList<String>();
    		result.add("to");
    		result.add("g");
    		return result;
    	}
    	protected void appendAttributeToSave(String attributeName, StringBuffer buffer, Wrapper wrapper) {
    		if (!(wrapper instanceof ConnectionWrapper)) return;
    		Element element = wrapper.getElement();
    		if (!(element instanceof SequenceFlow)) return;
    		if ("to".equals(attributeName)) {
				appendTo(buffer, (SequenceFlow)element);
			} else if ("g".equals(attributeName)) {
				appendGraphics(buffer, (ConnectionWrapper)wrapper);
    		}
    	}
		protected void appendTo(StringBuffer buffer, SequenceFlow sequenceFlow) {
			if (sequenceFlow.getTo() == null) return;
			String value = sequenceFlow.getTo().getName();
			if (value == null || "".equals(value)) return;
    		buffer.append(" to=\"" + value + "\"");
		}
		protected void appendGraphics(StringBuffer buffer, ConnectionWrapper wrapper) {
	    	List<Point> bendPoints = wrapper.getBendpoints();
	    	if (bendPoints == null || bendPoints.size() == 0) return;
	    	buffer.append(" g=\"");
	    	for (int i = 0; i < bendPoints.size(); i++) {
	    		buffer.append(bendPoints.get(i).x);
	    		buffer.append(",");
	    		buffer.append(bendPoints.get(i).y);
	    		if (i < bendPoints.size() - 1) buffer.append(";");
	    	}
	    	buffer.append("\"");
		}
    }
    
    class ProcessNodeWrapperSerializer extends AbstractWrapperSerializer {
    	protected List<String> getAttributesToSave() {
    		ArrayList<String> result = new ArrayList<String>();
    		result.add("name");
    		result.add("g");
    		return result;
    	}
    	protected void appendAttributeToSave(String attributeName, StringBuffer buffer, Wrapper wrapper) {
    		if (!(wrapper instanceof NodeWrapper)) return;
    		Element element = wrapper.getElement();
    		if (!(element instanceof ProcessNode)) return;
    		if ("name".equals(attributeName)) {
				appendName(buffer, (ProcessNode)element);
			} else if ("g".equals(attributeName)) {
				appendGraphics(buffer, (NodeWrapper)wrapper);
    		}
    	}
    	protected void appendName(StringBuffer buffer, ProcessNode processNode) {
			String value = processNode.getName();
			if (value == null || "".equals(value)) return;
    		buffer.append(" name=\"" + value + "\"");
     	}
    	protected void appendGraphics(StringBuffer buffer, NodeWrapper wrapper) {
        	Rectangle constraint = wrapper.getConstraint();
        	buffer.append(" g=\"");
        	buffer.append(constraint.x);
        	buffer.append(",");
        	buffer.append(constraint.y);
        	buffer.append(",");
        	buffer.append(constraint.width);
        	buffer.append(",");
        	buffer.append(constraint.height);
        	buffer.append("\"");
    	}
    }
    
    class ProcessWrapperSerializer extends AbstractWrapperSerializer {
    	public void appendOpening(StringBuffer buffer, Wrapper wrapper, int level) {
    		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
    		buffer.append("<" + getNodeName(wrapper.getElement()));
    		appendAttributes(buffer, wrapper, level);
    	}
    	protected List<String> getAttributesToSave() {
    		ArrayList<String> result = new ArrayList<String>();
    		result.add("xmlns");
    		result.add("name");
    		result.add("initial");
    		return result;
    	}
    	protected void appendAttributeToSave(String attributeName, StringBuffer buffer, Wrapper wrapper) {
    		if (!(wrapper instanceof FlowWrapper)) return;
    		Element element = wrapper.getElement();
    		if (element == null || !(element instanceof Process)) return;
    		if ("xmlns".equals(attributeName)) {
	    		buffer.append(" xmlns=\"http://jbpm.org/4/jpdl\"");    	    		
			} else if ("name".equals(attributeName)) {
				appendName(buffer, (Process)element);
			} else if ("initial".equals(attributeName)) {
				appendInitial(buffer, (Process)element);
    		}
    	}
    	protected void appendName(StringBuffer buffer, Process process) {
			String value = process.getName();
			if (value == null || "".equals(value)) return;
			buffer.append(" name=\"" + value + "\"");
     	}
    	protected void appendInitial(StringBuffer buffer, Process process) {
    		if (process.getInitial() == null) return;
			String value = process.getInitial().getName();
			if (value == null || "".equals(value)) return;
			buffer.append(" initial=\"" + value + "\"");
    	}
    }
    
	private void appendOpening(StringBuffer buffer, Wrapper wrapper, int level) {
    	Element element = (Element)wrapper.getElement();
//    	ArrayList<Node> leadingNodeList = (ArrayList<Node>)element.getMetaData("leadingNodes");
//    	boolean appendLeadingNodes = leadingNodeList != null && !leadingNodeList.isEmpty();
//    	if (appendLeadingNodes) {
//    		appendNodeList(buffer, leadingNodeList);
//    	} else {
//    		buffer.append("\n");
//    		appendPadding(buffer, level);
//    	}
    	if (element instanceof SequenceFlow) {
    		new SequenceFlowWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		SequenceFlow transition = (SequenceFlow)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<flow");
//    		if (transition.getTo() != null) {
//    			buffer.append(" ");
//        		String value = transition.getTo().getName();
//        		value = value == null ? "" : value;
//        		buffer.append("to=\"" + value + "\"");
//    		}
//    		appendConnectionGraphics(buffer, (ConnectionWrapper)wrapper);
    	} else if (element instanceof TerminateEndEvent) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		TerminateEndEvent terminateEndEvent = (TerminateEndEvent)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<end");
//    		if (!isEmpty(terminateEndEvent.getName())) {
//    			buffer.append(" ");
//    			String value = terminateEndEvent.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ErrorEndEvent) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		ErrorEndEvent errorEndEvent = (ErrorEndEvent)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<end-error");
//    		if (!isEmpty(errorEndEvent.getName())) {
//    			buffer.append(" ");
//    			String value = errorEndEvent.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof CancelEndEvent) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		CancelEndEvent cancelEndEvent = (CancelEndEvent)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<end-cancel");
//    		if (!isEmpty(cancelEndEvent.getName())) {
//    			buffer.append(" ");
//    			String value = cancelEndEvent.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof StartEvent) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		StartEvent startEvent = (StartEvent)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<start");
//    		if (!isEmpty(startEvent.getName())) {
//    			buffer.append(" ");
//    			String value = startEvent.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof SuperState) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		SuperState superState = (SuperState)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<super-state");
//    		if (!isEmpty(superState.getName())) {
//    			buffer.append(" ");
//    			String value = superState.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
    	} else if (element instanceof WaitTask) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		WaitTask waitTask = (WaitTask)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<state");
//    		if (!isEmpty(waitTask.getName())) {
//    			buffer.append(" ");
//    			String value = waitTask.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof HqlTask) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		HqlTask hqlTask = (HqlTask)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<hql");
//    		if (!isEmpty(hqlTask.getName())) {
//    			buffer.append(" ");
//    			String value = hqlTask.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof SqlTask) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		SqlTask sqlTask = (SqlTask)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<sql");
//    		if (!isEmpty(sqlTask.getName())) {
//    			buffer.append(" ");
//    			String value = sqlTask.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof JavaTask) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		JavaTask javaTask = (JavaTask)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<java");
//    		if (!isEmpty(javaTask.getName())) {
//    			buffer.append(" ");
//    			String value = javaTask.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ScriptTask) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		ScriptTask scriptTask = (ScriptTask)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<script");
//    		if (!isEmpty(scriptTask.getName())) {
//    			buffer.append(" ");
//    			String value = scriptTask.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ServiceTask) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		ServiceTask serviceTask = (ServiceTask)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<esb");
//    		if (!isEmpty(serviceTask.getName())) {
//    			buffer.append(" ");
//    			String value = serviceTask.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof HumanTask) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		HumanTask humanTask = (HumanTask)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<task");
//    		if (!isEmpty(humanTask.getName())) {
//    			buffer.append(" ");
//    			String value = humanTask.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ExclusiveGateway) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		ExclusiveGateway exclusiveGateway = (ExclusiveGateway)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<exclusive");
//    		if (!isEmpty(exclusiveGateway.getName())) {
//    			buffer.append(" ");
//    			String value = exclusiveGateway.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ForkParallelGateway) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		ForkParallelGateway parallelForkGateway = (ForkParallelGateway)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<fork");
//    		if (!isEmpty(parallelForkGateway.getName())) {
//    			buffer.append(" ");
//    			String value = parallelForkGateway.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof JoinParallelGateway) {
    		new ProcessNodeWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		JoinParallelGateway parallelJoinGateway = (JoinParallelGateway)element;
//    		if (!appendLeadingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
//    		buffer.append("<join");
//    		if (!isEmpty(parallelJoinGateway.getName())) {
//    			buffer.append(" ");
//    			String value = parallelJoinGateway.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
//    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof Process) {
    		new ProcessWrapperSerializer().appendOpening(buffer, wrapper, level);
//    		Process process = (Process)element;
//    		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
//    		buffer.append("<process");
//    		buffer.append(" xmlns=\"http://jbpm.org/4/jpdl\"");
//    		if (process.getInitial() != null) {
//    			buffer.append(" ");
//    			String value = process.getInitial().getName();
//    			value = value == null ? "" : value;
//    			buffer.append("initial=\"" + value + "\"");
//    		}
//    		if (!isEmpty(process.getName())) {
//    			buffer.append(" ");
//    			String value = process.getName();
//    			buffer.append("name=\"" + value + "\"");
//    		}
    	}
    	
    }
    
//    private boolean isEmpty(String str) {
//    	return str == null || "".equals(str);
//    }
    
    private void appendPadding(StringBuffer buffer, int level) {
    	for (int i = 0; i < level; i++) {
    		buffer.append("   ");
    	}
    }
    
	private void appendClosing(StringBuffer buffer, Wrapper wrapper, int level) {
    	Element element = (Element)wrapper.getElement();
//    	ArrayList<Node> trailingNodeList = (ArrayList<Node>)element.getMetaData("trailingNodes");
//    	boolean appendTrailingNodes = trailingNodeList != null && !trailingNodeList.isEmpty();
//    	if (appendTrailingNodes) {
//    		appendNodeList(buffer, trailingNodeList);
//    	}
    	if (element instanceof SequenceFlow) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</flow>");
    	} else if (element instanceof TerminateEndEvent) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</end>");
    	} else if (element instanceof ErrorEndEvent) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</end-error>");
    	} else if (element instanceof CancelEndEvent) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</end-cancel>");
    	} else if (element instanceof StartEvent) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</start>");
    	} else if (element instanceof SuperState) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</super-state>");
    	} else if (element instanceof WaitTask) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</state>");
    	} else if (element instanceof HqlTask) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</hql>");
    	} else if (element instanceof SqlTask) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</sql>");
    	} else if (element instanceof JavaTask) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</java>");
    	} else if (element instanceof ScriptTask) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</script>");
    	} else if (element instanceof ServiceTask) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</esb>");
    	} else if (element instanceof HumanTask) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</task>");
    	} else if (element instanceof ExclusiveGateway) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</exclusive>");
    	} else if (element instanceof ForkParallelGateway) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</fork>");
    	} else if (element instanceof JoinParallelGateway) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</join>");
    	} else if (element instanceof Process) {
//    		if (!appendTrailingNodes) {
//    			buffer.append("\n");
//    			appendPadding(buffer, level);
//    		}
    		buffer.append("</process>");
    	}	
    }
    
    @SuppressWarnings("unchecked")
	private void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
        if (wrapper instanceof ContainerWrapper) {
        	ContainerWrapper containerWrapper = (ContainerWrapper)wrapper;
        	List<NodeWrapper> children = containerWrapper.getElements();
        	for (NodeWrapper nodeWrapper : children) {
        		appendToBuffer(buffer, nodeWrapper, level+1);
        	}
        }
        if (wrapper instanceof NodeWrapper) {
        	NodeWrapper nodeWrapper = (NodeWrapper)wrapper;
        	List<ConnectionWrapper> children = nodeWrapper.getOutgoingConnections();
        	for (ConnectionWrapper connectionWrapper : children) {
        		appendToBuffer(buffer, connectionWrapper, level+1);
        	}
        } 
    	Element element = (Element)wrapper.getElement();
    	ArrayList<Node> trailingNodeList = (ArrayList<Node>)element.getMetaData("trailingNodes");
    	boolean appendTrailingNodes = trailingNodeList != null && !trailingNodeList.isEmpty();
    	if (appendTrailingNodes) {
    		appendNodeList(buffer, trailingNodeList);
    	} else if (buffer.length() > 0){
			buffer.append("\n");
			appendPadding(buffer, level);
    	}
    }
    
//    private void appendNodeGraphics(StringBuffer buffer, NodeWrapper wrapper) {
//    	Rectangle constraint = wrapper.getConstraint();
//    	buffer.append(" g=\"");
//    	buffer.append(constraint.x);
//    	buffer.append(",");
//    	buffer.append(constraint.y);
//    	buffer.append(",");
//    	buffer.append(constraint.width);
//    	buffer.append(",");
//    	buffer.append(constraint.height);
//    	buffer.append("\"");
//    }
//    
//    private void appendConnectionGraphics(StringBuffer buffer, ConnectionWrapper wrapper) {
//    	List<Point> bendPoints = wrapper.getBendpoints();
//    	if (bendPoints.size() == 0) return;
//    	buffer.append(" g=\"");
//    	for (int i = 0; i < bendPoints.size(); i++) {
//    		buffer.append(bendPoints.get(i).x);
//    		buffer.append(",");
//    		buffer.append(bendPoints.get(i).y);
//    		if (i < bendPoints.size() - 1) buffer.append(";");
//    	}
//    	buffer.append("\"");
//    }
//    
}
