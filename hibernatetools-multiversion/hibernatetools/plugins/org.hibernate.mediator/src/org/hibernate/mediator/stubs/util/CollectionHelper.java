package org.hibernate.mediator.stubs.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class CollectionHelper {

	public static final List EMPTY_LIST = Collections.unmodifiableList( new ArrayList(0) );
	public static final Collection EMPTY_COLLECTION = Collections.unmodifiableCollection( new ArrayList(0) );
	public static final Map EMPTY_MAP = Collections.unmodifiableMap( new HashMap(0) );
	
	private CollectionHelper() {
	}

	/**
	 * Build a properly sized map, especially handling load size and load factor to prevent immediate resizing.
	 * <p/>
	 * Especially helpful for copy map contents.
	 *
	 * @param size The size to make the map.
	 * @return The sized map.
	 */
	public static Map mapOfSize(int size) {
		final int currentSize = (int) (size / 0.75f);
		return new HashMap( Math.max( currentSize+ 1, 16), 0.75f );
	}
}
