package mapping.component.cascading.collection;

/**
 * {@inheritDoc}
 *
 * @author Steve Ebersole
 */
public class Value {
	private Long id;
	private Definition definition;
	private LocalizedStrings localizedStrings = new LocalizedStrings();

	private Value() {
	}

	public Value(Definition definition) {
		this();
		this.definition = definition;
		definition.getValues().add( this );
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Definition getDefinition() {
		return definition;
	}

	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

	public LocalizedStrings getLocalizedStrings() {
		return localizedStrings;
	}

	public void setLocalizedStrings(LocalizedStrings localizedStrings) {
		this.localizedStrings = localizedStrings;
	}
}
