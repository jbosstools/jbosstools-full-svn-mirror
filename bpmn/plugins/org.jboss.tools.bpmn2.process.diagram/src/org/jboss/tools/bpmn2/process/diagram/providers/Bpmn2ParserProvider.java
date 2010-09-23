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
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObjectName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObjectNameEditPart;
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
	private IParser userTaskName_5001Parser;

	/**
	 * @generated
	 */
	private IParser getUserTaskName_5001Parser() {
		if (userTaskName_5001Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			userTaskName_5001Parser = parser;
		}
		return userTaskName_5001Parser;
	}

	/**
	 * @generated
	 */
	private IParser serviceTaskName_5002Parser;

	/**
	 * @generated
	 */
	private IParser getServiceTaskName_5002Parser() {
		if (serviceTaskName_5002Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			serviceTaskName_5002Parser = parser;
		}
		return serviceTaskName_5002Parser;
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
	private IParser userTaskName_5005Parser;

	/**
	 * @generated
	 */
	private IParser getUserTaskName_5005Parser() {
		if (userTaskName_5005Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			userTaskName_5005Parser = parser;
		}
		return userTaskName_5005Parser;
	}

	/**
	 * @generated
	 */
	private IParser serviceTaskName_5006Parser;

	/**
	 * @generated
	 */
	private IParser getServiceTaskName_5006Parser() {
		if (serviceTaskName_5006Parser == null) {
			EAttribute[] features = new EAttribute[] { Bpmn2Package.eINSTANCE
					.getFlowElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			serviceTaskName_5006Parser = parser;
		}
		return serviceTaskName_5006Parser;
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
			return getUserTaskName_5001Parser();
		case ServiceTaskNameEditPart.VISUAL_ID:
			return getServiceTaskName_5002Parser();
		case DataObjectNameEditPart.VISUAL_ID:
			return getDataObjectName_5003Parser();
		case TextAnnotationTextEditPart.VISUAL_ID:
			return getTextAnnotationText_5004Parser();
		case UserTaskName2EditPart.VISUAL_ID:
			return getUserTaskName_5005Parser();
		case ServiceTaskName2EditPart.VISUAL_ID:
			return getServiceTaskName_5006Parser();
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
