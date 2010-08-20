package mapping.connections;

/**
 * @author Steve Ebersole
 */
public class Other {
	private Long id;
	private String name;

	public Other() {
	}

	public Other(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
