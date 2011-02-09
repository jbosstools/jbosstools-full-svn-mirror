package org.jboss.tools.bpmn2.process.diagram.edit.parts;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry;

/**
 * @generated
 */
public class Bpmn2EditPartFactory implements EditPartFactory {

	/**
	 * @generated
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof View) {
			View view = (View) model;
			switch (Bpmn2VisualIDRegistry.getVisualID(view)) {

			case ProcessEditPart.VISUAL_ID:
				return new ProcessEditPart(view);

			case UserTaskEditPart.VISUAL_ID:
				return new UserTaskEditPart(view);

			case UserTaskNameEditPart.VISUAL_ID:
				return new UserTaskNameEditPart(view);

			case ScriptTaskEditPart.VISUAL_ID:
				return new ScriptTaskEditPart(view);

			case ScriptTaskNameEditPart.VISUAL_ID:
				return new ScriptTaskNameEditPart(view);

			case ServiceTaskEditPart.VISUAL_ID:
				return new ServiceTaskEditPart(view);

			case ServiceTaskNameEditPart.VISUAL_ID:
				return new ServiceTaskNameEditPart(view);

			case BusinessRuleTaskEditPart.VISUAL_ID:
				return new BusinessRuleTaskEditPart(view);

			case BusinessRuleTaskNameEditPart.VISUAL_ID:
				return new BusinessRuleTaskNameEditPart(view);

			case StartEventEditPart.VISUAL_ID:
				return new StartEventEditPart(view);

			case StartEvent2EditPart.VISUAL_ID:
				return new StartEvent2EditPart(view);

			case IntermediateCatchEventEditPart.VISUAL_ID:
				return new IntermediateCatchEventEditPart(view);

			case IntermediateCatchEvent2EditPart.VISUAL_ID:
				return new IntermediateCatchEvent2EditPart(view);

			case IntermediateCatchEvent3EditPart.VISUAL_ID:
				return new IntermediateCatchEvent3EditPart(view);

			case IntermediateThrowEventEditPart.VISUAL_ID:
				return new IntermediateThrowEventEditPart(view);

			case EndEventEditPart.VISUAL_ID:
				return new EndEventEditPart(view);

			case EndEvent2EditPart.VISUAL_ID:
				return new EndEvent2EditPart(view);

			case EndEvent3EditPart.VISUAL_ID:
				return new EndEvent3EditPart(view);

			case ExclusiveGatewayEditPart.VISUAL_ID:
				return new ExclusiveGatewayEditPart(view);

			case ParallelGatewayEditPart.VISUAL_ID:
				return new ParallelGatewayEditPart(view);

			case SubProcessEditPart.VISUAL_ID:
				return new SubProcessEditPart(view);

			case DataObjectEditPart.VISUAL_ID:
				return new DataObjectEditPart(view);

			case DataObjectNameEditPart.VISUAL_ID:
				return new DataObjectNameEditPart(view);

			case TextAnnotationEditPart.VISUAL_ID:
				return new TextAnnotationEditPart(view);

			case TextAnnotationTextEditPart.VISUAL_ID:
				return new TextAnnotationTextEditPart(view);

			case UserTask2EditPart.VISUAL_ID:
				return new UserTask2EditPart(view);

			case UserTaskName2EditPart.VISUAL_ID:
				return new UserTaskName2EditPart(view);

			case ScriptTask2EditPart.VISUAL_ID:
				return new ScriptTask2EditPart(view);

			case ScriptTaskName2EditPart.VISUAL_ID:
				return new ScriptTaskName2EditPart(view);

			case ServiceTask2EditPart.VISUAL_ID:
				return new ServiceTask2EditPart(view);

			case ServiceTaskName2EditPart.VISUAL_ID:
				return new ServiceTaskName2EditPart(view);

			case BusinessRuleTask2EditPart.VISUAL_ID:
				return new BusinessRuleTask2EditPart(view);

			case BusinessRuleTaskName2EditPart.VISUAL_ID:
				return new BusinessRuleTaskName2EditPart(view);

			case StartEvent3EditPart.VISUAL_ID:
				return new StartEvent3EditPart(view);

			case StartEvent4EditPart.VISUAL_ID:
				return new StartEvent4EditPart(view);

			case IntermediateCatchEvent4EditPart.VISUAL_ID:
				return new IntermediateCatchEvent4EditPart(view);

			case IntermediateCatchEvent5EditPart.VISUAL_ID:
				return new IntermediateCatchEvent5EditPart(view);

			case IntermediateCatchEvent6EditPart.VISUAL_ID:
				return new IntermediateCatchEvent6EditPart(view);

			case IntermediateThrowEvent2EditPart.VISUAL_ID:
				return new IntermediateThrowEvent2EditPart(view);

			case EndEvent4EditPart.VISUAL_ID:
				return new EndEvent4EditPart(view);

			case EndEvent5EditPart.VISUAL_ID:
				return new EndEvent5EditPart(view);

			case EndEvent6EditPart.VISUAL_ID:
				return new EndEvent6EditPart(view);

			case ExclusiveGateway2EditPart.VISUAL_ID:
				return new ExclusiveGateway2EditPart(view);

			case ParallelGateway2EditPart.VISUAL_ID:
				return new ParallelGateway2EditPart(view);

			case SubProcess2EditPart.VISUAL_ID:
				return new SubProcess2EditPart(view);

			case DataObject2EditPart.VISUAL_ID:
				return new DataObject2EditPart(view);

			case DataObjectName2EditPart.VISUAL_ID:
				return new DataObjectName2EditPart(view);

			case TextAnnotation2EditPart.VISUAL_ID:
				return new TextAnnotation2EditPart(view);

			case TextAnnotationText2EditPart.VISUAL_ID:
				return new TextAnnotationText2EditPart(view);

			case SequenceFlowEditPart.VISUAL_ID:
				return new SequenceFlowEditPart(view);

			case AssociationEditPart.VISUAL_ID:
				return new AssociationEditPart(view);

			}
		}
		return createUnrecognizedEditPart(context, model);
	}

	/**
	 * @generated
	 */
	private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
		// Handle creation of unrecognized child node EditParts here
		return null;
	}

	/**
	 * @generated
	 */
	public static CellEditorLocator getTextCellEditorLocator(
			ITextAwareEditPart source) {
		if (source.getFigure() instanceof WrappingLabel)
			return new TextCellEditorLocator((WrappingLabel) source.getFigure());
		else {
			return new LabelCellEditorLocator((Label) source.getFigure());
		}
	}

	/**
	 * @generated
	 */
	static private class TextCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private WrappingLabel wrapLabel;

		/**
		 * @generated
		 */
		public TextCellEditorLocator(WrappingLabel wrapLabel) {
			this.wrapLabel = wrapLabel;
		}

		/**
		 * @generated
		 */
		public WrappingLabel getWrapLabel() {
			return wrapLabel;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getWrapLabel().getTextBounds().getCopy();
			getWrapLabel().translateToAbsolute(rect);
			if (!text.getFont().isDisposed()) {
				if (getWrapLabel().isTextWrapOn()
						&& getWrapLabel().getText().length() > 0) {
					rect.setSize(new Dimension(text.computeSize(rect.width,
							SWT.DEFAULT)));
				} else {
					int avr = FigureUtilities.getFontMetrics(text.getFont())
							.getAverageCharWidth();
					rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT,
							SWT.DEFAULT)).expand(avr * 2, 0));
				}
			}
			if (!rect.equals(new Rectangle(text.getBounds()))) {
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	/**
	 * @generated
	 */
	private static class LabelCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private Label label;

		/**
		 * @generated
		 */
		public LabelCellEditorLocator(Label label) {
			this.label = label;
		}

		/**
		 * @generated
		 */
		public Label getLabel() {
			return label;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getLabel().getTextBounds().getCopy();
			getLabel().translateToAbsolute(rect);
			if (!text.getFont().isDisposed()) {
				int avr = FigureUtilities.getFontMetrics(text.getFont())
						.getAverageCharWidth();
				rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT,
						SWT.DEFAULT)).expand(avr * 2, 0));
			}
			if (!rect.equals(new Rectangle(text.getBounds()))) {
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}
}
