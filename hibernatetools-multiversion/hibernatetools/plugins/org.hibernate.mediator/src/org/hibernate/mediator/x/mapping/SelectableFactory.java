package org.hibernate.mediator.x.mapping;


public class SelectableFactory {
	@SuppressWarnings("unchecked")
	public static Selectable createSelectableStub(Object selectable) {
		if (selectable == null) {
			return null;
		}
		final Class cl = selectable.getClass();
		if (0 == Column.CL.compareTo(cl.getName())) {
			return new Column(selectable);
		} else if (0 == Formula.CL.compareTo(cl.getName())) {
			return new Formula(selectable);
		}
		return null;
	}
}
