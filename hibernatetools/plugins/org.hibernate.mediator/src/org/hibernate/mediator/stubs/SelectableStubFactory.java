package org.hibernate.mediator.stubs;

public class SelectableStubFactory {
	@SuppressWarnings("unchecked")
	public static SelectableStub createSelectableStub(Object selectable) {
		if (selectable == null) {
			return null;
		}
		final Class cl = selectable.getClass();
		if (0 == "org.hibernate.mapping.Column".compareTo(cl.getName())) { //$NON-NLS-1$
			return new ColumnStub(selectable);
		} else if (0 == "org.hibernate.mapping.Formula".compareTo(cl.getName())) { //$NON-NLS-1$
			return new FormulaStub(selectable);
		}
		return null;
	}
}
