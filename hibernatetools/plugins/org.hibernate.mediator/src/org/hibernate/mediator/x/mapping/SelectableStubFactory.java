package org.hibernate.mediator.x.mapping;


public class SelectableStubFactory {
	@SuppressWarnings("unchecked")
	public static SelectableStub createSelectableStub(Object selectable) {
		if (selectable == null) {
			return null;
		}
		final Class cl = selectable.getClass();
		if (0 == ColumnStub.CL.compareTo(cl.getName())) {
			return new ColumnStub(selectable);
		} else if (0 == FormulaStub.CL.compareTo(cl.getName())) {
			return new FormulaStub(selectable);
		}
		return null;
	}
}
