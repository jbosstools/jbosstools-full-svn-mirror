package org.jboss.tools.bpmn2.process.diagram.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.jboss.tools.bpmn2.process.diagram.providers.Bpmn2ElementTypes;

/**
 * @generated
 */
public class Bpmn2PaletteFactory {

	/**
	 * @generated
	 */
	public void fillPalette(PaletteRoot paletteRoot) {
		paletteRoot.add(createNodes1Group());
		paletteRoot.add(createLinks2Group());
	}

	/**
	 * Creates "Nodes" palette tool group
	 * @generated
	 */
	private PaletteContainer createNodes1Group() {
		PaletteGroup paletteContainer = new PaletteGroup(
				Messages.Nodes1Group_title);
		paletteContainer.setId("createNodes1Group"); //$NON-NLS-1$
		paletteContainer.add(createActivities1Group());
		paletteContainer.add(createGateways2Group());
		paletteContainer.add(createEvents3Group());
		paletteContainer.add(createArtifacts4Group());
		return paletteContainer;
	}

	/**
	 * Creates "Links" palette tool group
	 * @generated
	 */
	private PaletteContainer createLinks2Group() {
		PaletteGroup paletteContainer = new PaletteGroup(
				Messages.Links2Group_title);
		paletteContainer.setId("createLinks2Group"); //$NON-NLS-1$
		paletteContainer.add(createSequenceFlowLinks1Group());
		paletteContainer.add(createAssociationLinks2Group());
		return paletteContainer;
	}

	/**
	 * Creates "Activities" palette tool group
	 * @generated
	 */
	private PaletteContainer createActivities1Group() {
		PaletteStack paletteContainer = new PaletteStack(
				Messages.Activities1Group_title, null, null);
		paletteContainer.setId("createActivities1Group"); //$NON-NLS-1$
		paletteContainer.add(createUserTask1CreationTool());
		paletteContainer.add(createServiceTask2CreationTool());
		paletteContainer.add(createScriptTask3CreationTool());
		paletteContainer.add(createSubProcess4CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "Start Events" palette tool group
	 * @generated
	 */
	private PaletteContainer createStartEvents1Group() {
		PaletteStack paletteContainer = new PaletteStack(
				Messages.StartEvents1Group_title, null, null);
		paletteContainer.setId("createStartEvents1Group"); //$NON-NLS-1$
		paletteContainer.add(createStart1CreationTool());
		paletteContainer.add(createMessageStart2CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "Events" palette tool group
	 * @generated
	 */
	private PaletteContainer createEvents3Group() {
		PaletteGroup paletteContainer = new PaletteGroup(
				Messages.Events3Group_title);
		paletteContainer.setId("createEvents3Group"); //$NON-NLS-1$
		paletteContainer.add(createStartEvents1Group());
		paletteContainer.add(createIntermediateEvents2Group());
		paletteContainer.add(createEndEvents3Group());
		return paletteContainer;
	}

	/**
	 * Creates "Artifacts" palette tool group
	 * @generated
	 */
	private PaletteContainer createArtifacts4Group() {
		PaletteGroup paletteContainer = new PaletteGroup(
				Messages.Artifacts4Group_title);
		paletteContainer.setId("createArtifacts4Group"); //$NON-NLS-1$
		paletteContainer.add(createDataObject1CreationTool());
		paletteContainer.add(createTextAnnotation2CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "Intermediate Events" palette tool group
	 * @generated
	 */
	private PaletteContainer createIntermediateEvents2Group() {
		PaletteStack paletteContainer = new PaletteStack(
				Messages.IntermediateEvents2Group_title, null, null);
		paletteContainer.setId("createIntermediateEvents2Group"); //$NON-NLS-1$
		paletteContainer.add(createMessageCatch1CreationTool());
		paletteContainer.add(createMessageThrow2CreationTool());
		paletteContainer.add(createTimerCatch3CreationTool());
		paletteContainer.add(createErrorCatch4CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "End Events" palette tool group
	 * @generated
	 */
	private PaletteContainer createEndEvents3Group() {
		PaletteStack paletteContainer = new PaletteStack(
				Messages.EndEvents3Group_title, null, null);
		paletteContainer.setId("createEndEvents3Group"); //$NON-NLS-1$
		paletteContainer.add(createEnd1CreationTool());
		paletteContainer.add(createMessageEnd2CreationTool());
		paletteContainer.add(createTerminate3CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "Gateways" palette tool group
	 * @generated
	 */
	private PaletteContainer createGateways2Group() {
		PaletteStack paletteContainer = new PaletteStack(
				Messages.Gateways2Group_title, null, null);
		paletteContainer.setId("createGateways2Group"); //$NON-NLS-1$
		paletteContainer.setDescription(Messages.Gateways2Group_desc);
		paletteContainer.add(createExclusive1CreationTool());
		paletteContainer.add(createParallel2CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "Sequence Flow Links" palette tool group
	 * @generated
	 */
	private PaletteContainer createSequenceFlowLinks1Group() {
		PaletteGroup paletteContainer = new PaletteGroup(
				Messages.SequenceFlowLinks1Group_title);
		paletteContainer.setId("createSequenceFlowLinks1Group"); //$NON-NLS-1$
		paletteContainer.add(createSequenceFlow1CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "Association Links" palette tool group
	 * @generated
	 */
	private PaletteContainer createAssociationLinks2Group() {
		PaletteGroup paletteContainer = new PaletteGroup(
				Messages.AssociationLinks2Group_title);
		paletteContainer.setId("createAssociationLinks2Group"); //$NON-NLS-1$
		paletteContainer.add(createAssociation1CreationTool());
		return paletteContainer;
	}

	/**
	 * @generated
	 */
	private ToolEntry createUserTask1CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.UserTask_2001);
		types.add(Bpmn2ElementTypes.UserTask_3002);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.UserTask1CreationTool_title, null, types);
		entry.setId("createUserTask1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.UserTask_2001));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createServiceTask2CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.ServiceTask_2002);
		types.add(Bpmn2ElementTypes.ServiceTask_3004);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.ServiceTask2CreationTool_title, null, types);
		entry.setId("createServiceTask2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.ServiceTask_2002));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createScriptTask3CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.ScriptTask_3016);
		types.add(Bpmn2ElementTypes.ScriptTask_2017);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.ScriptTask3CreationTool_title, null, types);
		entry.setId("createScriptTask3CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.ScriptTask_3016));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createSubProcess4CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.SubProcess_2016);
		types.add(Bpmn2ElementTypes.SubProcess_3001);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.SubProcess4CreationTool_title, null, types);
		entry.setId("createSubProcess4CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.SubProcess_2016));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createDataObject1CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.DataObject_2014);
		types.add(Bpmn2ElementTypes.DataObject_3014);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.DataObject1CreationTool_title, null, types);
		entry.setId("createDataObject1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.DataObject_2014));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createTextAnnotation2CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.TextAnnotation_2015);
		types.add(Bpmn2ElementTypes.TextAnnotation_3015);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.TextAnnotation2CreationTool_title, null, types);
		entry.setId("createTextAnnotation2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.TextAnnotation_2015));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createExclusive1CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.ExclusiveGateway_2005);
		types.add(Bpmn2ElementTypes.ExclusiveGateway_3007);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.Exclusive1CreationTool_title, null, types);
		entry.setId("createExclusive1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.ExclusiveGateway_2005));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createParallel2CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.ParallelGateway_2006);
		types.add(Bpmn2ElementTypes.ParallelGateway_3008);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.Parallel2CreationTool_title, null, types);
		entry.setId("createParallel2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.ParallelGateway_2006));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createStart1CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.StartEvent_2003);
		types.add(Bpmn2ElementTypes.StartEvent_3003);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.Start1CreationTool_title, null, types);
		entry.setId("createStart1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.StartEvent_2003));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createMessageStart2CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.StartEvent_2007);
		types.add(Bpmn2ElementTypes.StartEvent_3005);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.MessageStart2CreationTool_title, null, types);
		entry.setId("createMessageStart2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.StartEvent_2007));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createMessageCatch1CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2010);
		types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3011);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.MessageCatch1CreationTool_title, null, types);
		entry.setId("createMessageCatch1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.IntermediateCatchEvent_2010));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createMessageThrow2CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.IntermediateThrowEvent_2011);
		types.add(Bpmn2ElementTypes.IntermediateThrowEvent_3012);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.MessageThrow2CreationTool_title, null, types);
		entry.setId("createMessageThrow2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.IntermediateThrowEvent_2011));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createTimerCatch3CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2012);
		types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3013);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.TimerCatch3CreationTool_title,
				Messages.TimerCatch3CreationTool_desc, types);
		entry.setId("createTimerCatch3CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.IntermediateCatchEvent_2012));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createErrorCatch4CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(
				Messages.ErrorCatch4CreationTool_title,
				null,
				Collections
						.singletonList(Bpmn2ElementTypes.IntermediateCatchEvent_2013));
		entry.setId("createErrorCatch4CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.IntermediateCatchEvent_2013));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createEnd1CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.EndEvent_2004);
		types.add(Bpmn2ElementTypes.EndEvent_3006);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.End1CreationTool_title, null, types);
		entry.setId("createEnd1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.EndEvent_2004));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createMessageEnd2CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.EndEvent_2008);
		types.add(Bpmn2ElementTypes.EndEvent_3009);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.MessageEnd2CreationTool_title, null, types);
		entry.setId("createMessageEnd2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.EndEvent_2008));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createTerminate3CreationTool() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.EndEvent_2009);
		types.add(Bpmn2ElementTypes.EndEvent_3010);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.Terminate3CreationTool_title, null, types);
		entry.setId("createTerminate3CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.EndEvent_2009));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createSequenceFlow1CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(
				Messages.SequenceFlow1CreationTool_title, null,
				Collections.singletonList(Bpmn2ElementTypes.SequenceFlow_4001));
		entry.setId("createSequenceFlow1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.SequenceFlow_4001));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createAssociation1CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(
				Messages.Association1CreationTool_title, null,
				Collections.singletonList(Bpmn2ElementTypes.Association_4002));
		entry.setId("createAssociation1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(Bpmn2ElementTypes
				.getImageDescriptor(Bpmn2ElementTypes.Association_4002));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private static class NodeToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List<IElementType> elementTypes;

		/**
		 * @generated
		 */
		private NodeToolEntry(String title, String description,
				List<IElementType> elementTypes) {
			super(title, description, null, null);
			this.elementTypes = elementTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}

	/**
	 * @generated
	 */
	private static class LinkToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List<IElementType> relationshipTypes;

		/**
		 * @generated
		 */
		private LinkToolEntry(String title, String description,
				List<IElementType> relationshipTypes) {
			super(title, description, null, null);
			this.relationshipTypes = relationshipTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
}
