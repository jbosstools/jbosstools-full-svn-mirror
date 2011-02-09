package org.jboss.tools.bpmn2.process.diagram.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.BusinessRuleTask;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2DiagramEditorPlugin;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2ProcessDiagramEditorPlugin;

/**
 * @generated
 */
public class ElementInitializers {

	protected ElementInitializers() {
		// use #getInstance to access cached instance
	}

	/**
	 * @generated
	 */
	public void init_UserTask_2001(UserTask instance) {
		try {
			Object value_0 = id_UserTask_2001(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_BusinessRuleTask_2018(BusinessRuleTask instance) {
		try {
			Object value_0 = id_BusinessRuleTask_2018(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_ServiceTask_2002(ServiceTask instance) {
		try {
			Object value_0 = id_ServiceTask_2002(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_StartEvent_2003(StartEvent instance) {
		try {
			Object value_0 = id_StartEvent_2003(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_StartEvent_2007(StartEvent instance) {
		try {
			Object value_0 = id_StartEvent_2007(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_StartEvent_2007(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_EndEvent_2004(EndEvent instance) {
		try {
			Object value_0 = id_EndEvent_2004(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_ExclusiveGateway_2005(ExclusiveGateway instance) {
		try {
			Object value_0 = id_ExclusiveGateway_2005(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_ParallelGateway_2006(ParallelGateway instance) {
		try {
			Object value_0 = id_ParallelGateway_2006(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_EndEvent_2008(EndEvent instance) {
		try {
			Object value_0 = id_EndEvent_2008(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_EndEvent_2008(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_EndEvent_2009(EndEvent instance) {
		try {
			Object value_0 = id_EndEvent_2009(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_EndEvent_2009(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_IntermediateCatchEvent_2010(IntermediateCatchEvent instance) {
		try {
			Object value_0 = id_IntermediateCatchEvent_2010(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_IntermediateCatchEvent_2010(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_IntermediateThrowEvent_2011(IntermediateThrowEvent instance) {
		try {
			Object value_0 = id_IntermediateThrowEvent_2011(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_IntermediateThrowEvent_2011(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_IntermediateCatchEvent_2012(IntermediateCatchEvent instance) {
		try {
			Object value_0 = id_IntermediateCatchEvent_2012(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_IntermediateCatchEvent_2012(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_IntermediateCatchEvent_2013(IntermediateCatchEvent instance) {
		try {
			Object value_0 = id_IntermediateCatchEvent_2013(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_IntermediateCatchEvent_2013(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_DataObject_2014(DataObject instance) {
		try {
			Object value_0 = id_DataObject_2014(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_TextAnnotation_2015(TextAnnotation instance) {
		try {
			Object value_0 = id_TextAnnotation_2015(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_SubProcess_2016(SubProcess instance) {
		try {
			Object value_0 = id_SubProcess_2016(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_ScriptTask_2017(ScriptTask instance) {
		try {
			Object value_0 = id_ScriptTask_2017(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_SubProcess_3001(SubProcess instance) {
		try {
			Object value_0 = id_SubProcess_3001(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_UserTask_3002(UserTask instance) {
		try {
			Object value_0 = id_UserTask_3002(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_ServiceTask_3004(ServiceTask instance) {
		try {
			Object value_0 = id_ServiceTask_3004(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_BusinessRuleTask_3017(BusinessRuleTask instance) {
		try {
			Object value_0 = id_BusinessRuleTask_3017(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_StartEvent_3003(StartEvent instance) {
		try {
			Object value_0 = id_StartEvent_3003(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_StartEvent_3005(StartEvent instance) {
		try {
			Object value_0 = id_StartEvent_3005(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_StartEvent_3005(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_EndEvent_3006(EndEvent instance) {
		try {
			Object value_0 = id_EndEvent_3006(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_ExclusiveGateway_3007(ExclusiveGateway instance) {
		try {
			Object value_0 = id_ExclusiveGateway_3007(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_ParallelGateway_3008(ParallelGateway instance) {
		try {
			Object value_0 = id_ParallelGateway_3008(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_EndEvent_3009(EndEvent instance) {
		try {
			Object value_0 = id_EndEvent_3009(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_EndEvent_3009(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_EndEvent_3010(EndEvent instance) {
		try {
			Object value_0 = id_EndEvent_3010(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_EndEvent_3010(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_IntermediateCatchEvent_3011(IntermediateCatchEvent instance) {
		try {
			Object value_0 = id_IntermediateCatchEvent_3011(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_IntermediateCatchEvent_3011(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_IntermediateThrowEvent_3012(IntermediateThrowEvent instance) {
		try {
			Object value_0 = id_IntermediateThrowEvent_3012(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_IntermediateThrowEvent_3012(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_IntermediateCatchEvent_3013(IntermediateCatchEvent instance) {
		try {
			Object value_0 = id_IntermediateCatchEvent_3013(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_IntermediateCatchEvent_3013(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_IntermediateCatchEvent_3018(IntermediateCatchEvent instance) {
		try {
			Object value_0 = id_IntermediateCatchEvent_3018(instance);
			instance.setId((String) value_0);
			Object value_1 = eventDefinitions_IntermediateCatchEvent_3018(instance);
			if (value_1 instanceof Collection) {
				instance.getEventDefinitions().clear();
				instance.getEventDefinitions().addAll(((Collection) value_1));
			} else {
				instance.getEventDefinitions().add((EventDefinition) value_1);
			}
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_DataObject_3014(DataObject instance) {
		try {
			Object value_0 = id_DataObject_3014(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_TextAnnotation_3015(TextAnnotation instance) {
		try {
			Object value_0 = id_TextAnnotation_3015(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_ScriptTask_3016(ScriptTask instance) {
		try {
			Object value_0 = id_ScriptTask_3016(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_SequenceFlow_4001(SequenceFlow instance) {
		try {
			Object value_0 = id_SequenceFlow_4001(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_Association_4002(Association instance) {
		try {
			Object value_0 = id_Association_4002(instance);
			instance.setId((String) value_0);
		} catch (RuntimeException e) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated not
	 */
	private String id_UserTask_2001(UserTask self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_BusinessRuleTask_2018(BusinessRuleTask self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_ServiceTask_2002(ServiceTask self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_StartEvent_2003(StartEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_StartEvent_2007(StartEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_StartEvent_2007(
			StartEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createMessageEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_EndEvent_2004(EndEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_ExclusiveGateway_2005(ExclusiveGateway self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_ParallelGateway_2006(ParallelGateway self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_EndEvent_2008(EndEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_EndEvent_2008(EndEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createMessageEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_EndEvent_2009(EndEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_EndEvent_2009(EndEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createTerminateEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_IntermediateCatchEvent_2010(IntermediateCatchEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_IntermediateCatchEvent_2010(
			IntermediateCatchEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createMessageEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_IntermediateThrowEvent_2011(IntermediateThrowEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_IntermediateThrowEvent_2011(
			IntermediateThrowEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createMessageEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_IntermediateCatchEvent_2012(IntermediateCatchEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_IntermediateCatchEvent_2012(
			IntermediateCatchEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createTimerEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_IntermediateCatchEvent_2013(IntermediateCatchEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_IntermediateCatchEvent_2013(
			IntermediateCatchEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createErrorEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_DataObject_2014(DataObject self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_TextAnnotation_2015(TextAnnotation self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_SubProcess_2016(SubProcess self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_ScriptTask_2017(ScriptTask self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_SubProcess_3001(SubProcess self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_UserTask_3002(UserTask self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_ServiceTask_3004(ServiceTask self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated
	 */
	private String id_BusinessRuleTask_3017(BusinessRuleTask self) {
		// TODO: implement this method to return value  
		// for org.eclipse.bpmn2.Bpmn2Package.eINSTANCE.getBaseElement_Id()
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException(
				"No user java implementation provided in 'id_BusinessRuleTask_3017' operation"); //$NON-NLS-1$
	}

	/**
	 * @generated not
	 */
	private String id_StartEvent_3003(StartEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_StartEvent_3005(StartEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_StartEvent_3005(
			StartEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createMessageEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_EndEvent_3006(EndEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_ExclusiveGateway_3007(ExclusiveGateway self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_ParallelGateway_3008(ParallelGateway self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_EndEvent_3009(EndEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_EndEvent_3009(EndEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createMessageEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_EndEvent_3010(EndEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_EndEvent_3010(EndEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createTerminateEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_IntermediateCatchEvent_3011(IntermediateCatchEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_IntermediateCatchEvent_3011(
			IntermediateCatchEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createMessageEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_IntermediateThrowEvent_3012(IntermediateThrowEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_IntermediateThrowEvent_3012(
			IntermediateThrowEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createMessageEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated not
	 */
	private String id_IntermediateCatchEvent_3013(IntermediateCatchEvent self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private List<EventDefinition> eventDefinitions_IntermediateCatchEvent_3013(
			IntermediateCatchEvent self) {
		ArrayList<EventDefinition> result = new ArrayList<EventDefinition>();
		EventDefinition eventDefinition = Bpmn2Factory.eINSTANCE
				.createTimerEventDefinition();
		eventDefinition.setId(EcoreUtil.generateUUID());
		result.add(eventDefinition);
		return result;
	}

	/**
	 * @generated
	 */
	private String id_IntermediateCatchEvent_3018(IntermediateCatchEvent self) {
		// TODO: implement this method to return value  
		// for org.eclipse.bpmn2.Bpmn2Package.eINSTANCE.getBaseElement_Id()
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException(
				"No user java implementation provided in 'id_IntermediateCatchEvent_3018' operation"); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private List eventDefinitions_IntermediateCatchEvent_3018(
			IntermediateCatchEvent self) {
		// TODO: implement this method to return value  
		// for org.eclipse.bpmn2.Bpmn2Package.eINSTANCE.getCatchEvent_EventDefinitions()
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException(
				"No user java implementation provided in 'eventDefinitions_IntermediateCatchEvent_3018' operation"); //$NON-NLS-1$
	}

	/**
	 * @generated not
	 */
	private String id_DataObject_3014(DataObject self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_TextAnnotation_3015(TextAnnotation self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_ScriptTask_3016(ScriptTask self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated not
	 */
	private String id_SequenceFlow_4001(SequenceFlow self) {
		return EcoreUtil.generateUUID();

	}

	/**
	 * @generated not
	 */
	private String id_Association_4002(Association self) {
		return EcoreUtil.generateUUID();
	}

	/**
	 * @generated
	 */
	public static ElementInitializers getInstance() {
		ElementInitializers cached = Bpmn2ProcessDiagramEditorPlugin
				.getInstance().getElementInitializers();
		if (cached == null) {
			Bpmn2ProcessDiagramEditorPlugin.getInstance()
					.setElementInitializers(cached = new ElementInitializers());
		}
		return cached;
	}
}
