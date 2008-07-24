package mapping.any;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * todo: describe ${NAME}
 *
 * @author Steve Ebersole
 */
public class ComplexPropertyValue implements PropertyValue {
	private Long id;
	private Map subProperties = new HashMap();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map getSubProperties() {
		return subProperties;
	}

	public void setSubProperties(Map subProperties) {
		this.subProperties = subProperties;
	}

	public String asString() {
		return "complex[" + keyString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private String keyString() {
		StringBuffer buff = new StringBuffer();
		Iterator itr = subProperties.keySet().iterator();
		while ( itr.hasNext() ) {
			buff.append( itr.next() );
			if ( itr.hasNext() ) {
				buff.append( ", " ); //$NON-NLS-1$
			}
		}
		return buff.toString();
	}
}
