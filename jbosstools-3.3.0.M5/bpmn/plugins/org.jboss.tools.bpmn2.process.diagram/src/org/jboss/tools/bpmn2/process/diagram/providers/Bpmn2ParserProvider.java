package org.jboss.tools.bpmn2.process.diagram.providers;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTaskName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTaskNameEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObjectName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObjectNameEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTaskName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTaskNameEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTaskName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTaskNameEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotationText2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotationTextEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTaskName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTaskNameEditPart;
import org.jboss.tools.bpmn2.process.diagram.parsers.MessageFormatParser;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry;

/**
 * @generated
 */
public class Bpmn2ParserProvider extends AbstractProvider implements
		IParserProvider {

	/**
	 * @generated
	 */
	private IParser userTaskName_5009Parser;

	/**
	 * @generated
	 */
	private IParser getUserTaskName_5009Parser() {
		if (userTaskName_5009Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			userTaskName_5009Parser = parser;
		}
		return userTaskName_5009Parser;
	}

	/**
	 * @generated
	 */
	private IParser scriptTaskName_5011Parser;

	/**
	 * @generated
	 */
	private IParser getScriptTaskName_5011Parser() {
		if (scriptTaskName_5011Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			scriptTaskName_5011Parser = parser;
		}
		return scriptTaskName_5011Parser;
	}

	/**
	 * @generated
	 */
	private IParser serviceTaskName_5012Parser;

	/**
	 * @generated
	 */
	private IParser getServiceTaskName_5012Parser() {
		if (serviceTaskName_5012Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			serviceTaskName_5012Parser = parser;
		}
		return serviceTaskName_5012Parser;
	}

	/**
	 * @generated
	 */
	private IParser businessRuleTaskName_5013Parser;

	/**
	 * @generated
	 */
	private IParser getBusinessRuleTaskName_5013Parser() {
		if (businessRuleTaskName_5013Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			businessRuleTaskName_5013Parser = parser;
		}
		return businessRuleTaskName_5013Parser;
	}

	/**
	 * @generated
	 */
	private IParser dataObjectName_5003Parser;

	/**
	 * @generated
	 */
	private IParser getDataObjectName_5003Parser() {
		if (dataObjectName_5003Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			dataObjectName_5003Parser = parser;
		}
		return dataObjectName_5003Parser;
	}

	/**
	 * @generated
	 */
	private IParser textAnnotationText_5004Parser;

	/**
	 * @generated
	 */
	private IParser getTextAnnotationText_5004Parser() {
		if (textAnnotationText_5004Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getTextAnnotation_Text() };
			MessageFormatParser parser = new MessageFormatParser(features);
			textAnnotationText_5004Parser = parser;
		}
		return textAnnotationText_5004Parser;
	}

	/**
	 * @generated
	 */
	private IParser userTaskName_5010Parser;

	/**
	 * @generated
	 */
	private IParser getUserTaskName_5010Parser() {
		if (userTaskName_5010Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			userTaskName_5010Parser = parser;
		}
		return userTaskName_5010Parser;
	}

	/**
	 * @generated
	 */
	private IParser scriptTaskName_5014Parser;

	/**
	 * @generated
	 */
	private IParser getScriptTaskName_5014Parser() {
		if (scriptTaskName_5014Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			scriptTaskName_5014Parser = parser;
		}
		return scriptTaskName_5014Parser;
	}

	/**
	 * @generated
	 */
	private IParser serviceTaskName_5015Parser;

	/**
	 * @generated
	 */
	private IParser getServiceTaskName_5015Parser() {
		if (serviceTaskName_5015Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			serviceTaskName_5015Parser = parser;
		}
		return serviceTaskName_5015Parser;
	}

	/**
	 * @generated
	 */
	private IParser businessRuleTaskName_5016Parser;

	/**
	 * @generated
	 */
	private IParser getBusinessRuleTaskName_5016Parser() {
		if (businessRuleTaskName_5016Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			businessRuleTaskName_5016Parser = parser;
		}
		return businessRuleTaskName_5016Parser;
	}

	/**
	 * @generated
	 */
	private IParser dataObjectName_5007Parser;

	/**
	 * @generated
	 */
	private IParser getDataObjectName_5007Parser() {
		if (dataObjectName_5007Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			dataObjectName_5007Parser = parser;
		}
		return dataObjectName_5007Parser;
	}

	/**
	 * @generated
	 */
	private IParser textAnnotationText_5008Parser;

	/**
	 * @generated
	 */
	private IParser getTextAnnotationText_5008Parser() {
		if (textAnnotationText_5008Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getTextAnnotation_Text() };
			MessageFormatParser parser = new MessageFormatParser(features);
			textAnnotationText_5008Parser = parser;
		}
		return textAnnotationText_5008Parser;
	}

	/**
	 * @generated
	 */
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case UserTaskNameEditPart.VISUAL_ID:
			return getUserTaskName_5009Parser();
		case ScriptTaskNameEditPart.VISUAL_ID:
			return getScriptTaskName_5011Parser();
		case ServiceTaskNameEditPart.VISUAL_ID:
			return getServiceTaskName_5012Parser();
		case BusinessRuleTaskNameEditPart.VISUAL_ID:
			return getBusinessRuleTaskName_5013Parser();
		case DataObjectNameEditPart.VISUAL_ID:
			return getDataObjectName_5003Parser();
		case TextAnnotationTextEditPart.VISUAL_ID:
			return getTextAnnotationText_5004Parser();
		case UserTaskName2EditPart.VISUAL_ID:
			return getUserTaskName_5010Parser();
		case ScriptTaskName2EditPart.VISUAL_ID:
			return getScriptTaskName_5014Parser();
		case ServiceTaskName2EditPart.VISUAL_ID:
			return getServiceTaskName_5015Parser();
		case BusinessRuleTaskName2EditPart.VISUAL_ID:
			return getBusinessRuleTaskName_5016Parser();
		case DataObjectName2EditPart.VISUAL_ID:
			return getDataObjectName_5007Parser();
		case TextAnnotationText2EditPart.VISUAL_ID:
			return getTextAnnotationText_5008Parser();
		}
		return null;
	}

	/**
	 * Utility method that consults ParserService
	 * @generated
	 */
	public static IParser getParser(IElementType type, EObject object,
			String parserHint) {
		return ParserService.getInstance().getParser(
				new HintAdapter(type, object, parserHint));
	}

	/**
	 * @generated
	 */
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null) {
			return getParser(Bpmn2VisualIDRegistry.getVisualID(vid));
		}
		View view = (View) hint.getAdapter(View.class);
		if (view != null) {
			return getParser(Bpmn2VisualIDRegistry.getVisualID(view));
		}
		return null;
	}

	/**
	 * @generated
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (Bpmn2ElementTypes.getElement(hint) == null) {
				return false;
			}
			return getParser(hint) != null;
		}
		return false;
	}

	/**
	 * @generated
	 */
	private static class HintAdapter extends ParserHintAdapter {

		/**
		 * @generated
		 */
		private final IElementType elementType;

		/**
		 * @generated
		 */
		public HintAdapter(IElementType type, EObject object, String parserHint) {
			super(object, parserHint);
			assert type != null;
			elementType = type;
		}

		/**
		 * @generated
		 */
		public Object getAdapter(Class adapter) {
			if (IElementType.class.equals(adapter)) {
				return elementType;
			}
			return super.getAdapter(adapter);
		}
	}

}
