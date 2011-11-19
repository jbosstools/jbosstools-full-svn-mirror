//$Id: CustomPersister.java 11061 2007-01-19 12:55:07Z steve.ebersole@jboss.com $
package mapping.legacy;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;

import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MappingException;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.entry.CacheEntryStructure;
import org.hibernate.cache.spi.entry.UnstructuredCacheEntry;
import org.hibernate.engine.internal.TwoPhaseLoad;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.ValueInclusion;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PreLoadEvent;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.hibernate.internal.util.compare.EqualsHelper;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.sql.QuerySelect;
import org.hibernate.sql.Select;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.hibernate.tuple.entity.EntityTuplizer;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.type.VersionType;

public class CustomPersister implements EntityPersister {

	private static final Hashtable INSTANCES = new Hashtable();
	private static final IdentifierGenerator GENERATOR = new UUIDHexGenerator();

	private SessionFactoryImplementor factory;

	public CustomPersister(
			PersistentClass model,
			CacheConcurrencyStrategy cache,
			SessionFactoryImplementor factory,
			Mapping mapping) {
		this.factory = factory;
	}

	public boolean hasLazyProperties() {
		return false;
	}

	private void checkEntityMode(EntityMode entityMode) {
		if ( EntityMode.POJO != entityMode ) {
			throw new IllegalArgumentException( "Unhandled EntityMode : " + entityMode ); //$NON-NLS-1$
		}
	}

	public boolean isInherited() {
		return false;
	}

	public SessionFactoryImplementor getFactory() {
		return factory;
	}

	public Class getMappedClass() {
		return Custom.class;
	}

	public void postInstantiate() throws MappingException {}

	public String getEntityName() {
		return Custom.class.getName();
	}

	public boolean isSubclassEntityName(String entityName) {
		return Custom.class.getName().equals(entityName);
	}

	public boolean hasProxy() {
		return false;
	}

	public boolean hasCollections() {
		return false;
	}

	public boolean hasCascades() {
		return false;
	}

	public boolean isMutable() {
		return true;
	}

	public boolean isSelectBeforeUpdateRequired() {
		return false;
	}

	public boolean isIdentifierAssignedByInsert() {
		return false;
	}

	public Boolean isTransient(Object object, SessionImplementor session) {
		return new Boolean( ( (Custom) object ).id==null );
	}

	public Object[] getPropertyValuesToInsert(Object object, Map mergeMap, SessionImplementor session)
	throws HibernateException {
		return getPropertyValues( object, session.getEntityPersister(null, object).getEntityMode() );
	}

	public void processInsertGeneratedProperties(Serializable id, Object entity, Object[] state, SessionImplementor session) {
	}

	public void processUpdateGeneratedProperties(Serializable id, Object entity, Object[] state, SessionImplementor session) {
	}

	public void retrieveGeneratedProperties(Serializable id, Object entity, Object[] state, SessionImplementor session) {
		throw new UnsupportedOperationException();
	}

	public Class getMappedClass(EntityMode entityMode) {
		checkEntityMode( entityMode );
		return Custom.class;
	}

	public boolean implementsLifecycle(EntityMode entityMode) {
		checkEntityMode( entityMode );
		return false;
	}

	public boolean implementsValidatable(EntityMode entityMode) {
		checkEntityMode( entityMode );
		return false;
	}

	public Class getConcreteProxyClass(EntityMode entityMode) {
		checkEntityMode( entityMode );
		return Custom.class;
	}

	public void setPropertyValues(Object object, Object[] values, EntityMode entityMode) throws HibernateException {
		checkEntityMode( entityMode );
		setPropertyValue( object, 0, values[0], entityMode );
	}

	public void setPropertyValue(Object object, int i, Object value, EntityMode entityMode) throws HibernateException {
		checkEntityMode( entityMode );
		( (Custom) object ).setName( (String) value );
	}

	public Object[] getPropertyValues(Object object, EntityMode entityMode) throws HibernateException {
		checkEntityMode( entityMode );
		Custom c = (Custom) object;
		return new Object[] { c.getName() };
	}

	public Object getPropertyValue(Object object, int i, EntityMode entityMode) throws HibernateException {
		checkEntityMode( entityMode );
		return ( (Custom) object ).getName();
	}

	public Object getPropertyValue(Object object, String propertyName, EntityMode entityMode) throws HibernateException {
		checkEntityMode( entityMode );
		return ( (Custom) object ).getName();
	}

	public Serializable getIdentifier(Object object, EntityMode entityMode) throws HibernateException {
		checkEntityMode( entityMode );
		return ( (Custom) object ).id;
	}

	public void setIdentifier(Object object, Serializable id, EntityMode entityMode) throws HibernateException {
		checkEntityMode( entityMode );
		( (Custom) object ).id = (String) id;
	}

	public Object getVersion(Object object, EntityMode entityMode) throws HibernateException {
		checkEntityMode( entityMode );
		return null;
	}

	public Object instantiate(Serializable id, EntityMode entityMode) throws HibernateException {
		checkEntityMode( entityMode );
		Custom c = new Custom();
		c.id = (String) id;
		return c;
	}

	public boolean isInstance(Object object, EntityMode entityMode) {
		checkEntityMode( entityMode );
		return object instanceof Custom;
	}

	public boolean hasUninitializedLazyProperties(Object object, EntityMode entityMode) {
		checkEntityMode( entityMode );
		return false;
	}

	public void resetIdentifier(Object entity, Serializable currentId, Object currentVersion, EntityMode entityMode) {
		checkEntityMode( entityMode );
		( ( Custom ) entity ).id = ( String ) currentId;
	}

	public EntityPersister getSubclassEntityPersister(Object instance, SessionFactoryImplementor factory, EntityMode entityMode) {
		checkEntityMode( entityMode );
		return this;
	}

	public int[] findDirty(
		Object[] x,
		Object[] y,
		Object owner,
		SessionImplementor session
	) throws HibernateException {
		if ( !EqualsHelper.equals( x[0], y[0] ) ) {
			return new int[] { 0 };
		}
		else {
			return null;
		}
	}

	public int[] findModified(
		Object[] x,
		Object[] y,
		Object owner,
		SessionImplementor session
	) throws HibernateException {
		if ( !EqualsHelper.equals( x[0], y[0] ) ) {
			return new int[] { 0 };
		}
		else {
			return null;
		}
	}

	/**
	 * @see EntityPersister#hasIdentifierProperty()
	 */
	public boolean hasIdentifierProperty() {
		return true;
	}

	/**
	 * @see EntityPersister#isVersioned()
	 */
	public boolean isVersioned() {
		return false;
	}

	/**
	 * @see EntityPersister#getVersionType()
	 */
	public VersionType getVersionType() {
		return null;
	}

	/**
	 * @see EntityPersister#getVersionProperty()
	 */
	public int getVersionProperty() {
		return 0;
	}

	/**
	 * @see EntityPersister#getIdentifierGenerator()
	 */
	public IdentifierGenerator getIdentifierGenerator()
	throws HibernateException {
		return GENERATOR;
	}

	/**
	 * @see EntityPersister#load(Serializable, Object, LockMode, SessionImplementor)
	 */
	public Object load(
		Serializable id,
		Object optionalObject,
		LockMode lockMode,
		SessionImplementor session
	) throws HibernateException {

		// fails when optional object is supplied

		Custom clone = null;
		Custom obj = (Custom) INSTANCES.get(id);
		if (obj!=null) {
			clone = (Custom) obj.clone();
			TwoPhaseLoad.addUninitializedEntity(
					new EntityKey( id, session.getEntityPersister(null, clone), null ),
					clone,
					this,
					LockMode.NONE,
					false,
					session
				);
			TwoPhaseLoad.postHydrate(
					this, id,
					new String[] { obj.getName() },
					null,
					clone,
					LockMode.NONE,
					false,
					session
				);
			TwoPhaseLoad.initializeEntity(
					clone,
					false,
					session,
					new PreLoadEvent( (EventSource) session ),
					new PostLoadEvent( (EventSource) session )
				);
		}
		return clone;
	}

	/**
	 * @see EntityPersister#lock(Serializable, Object, Object, LockMode, SessionImplementor)
	 */
	public void lock(
		Serializable id,
		Object version,
		Object object,
		LockMode lockMode,
		SessionImplementor session
	) throws HibernateException {

		throw new UnsupportedOperationException();
	}

	public void insert(
		Serializable id,
		Object[] fields,
		Object object,
		SessionImplementor session
	) throws HibernateException {

		INSTANCES.put(id, ( (Custom) object ).clone() );
	}

	public Serializable insert(Object[] fields, Object object, SessionImplementor session)
	throws HibernateException {

		throw new UnsupportedOperationException();
	}

	public void delete(
		Serializable id,
		Object version,
		Object object,
		SessionImplementor session
	) throws HibernateException {

		INSTANCES.remove(id);
	}

	/**
	 * @see EntityPersister
	 */
	public void update(
		Serializable id,
		Object[] fields,
		int[] dirtyFields,
		boolean hasDirtyCollection,
		Object[] oldFields,
		Object oldVersion,
		Object object,
		Object rowId,
		SessionImplementor session
	) throws HibernateException {

		INSTANCES.put( id, ( (Custom) object ).clone() );

	}

	private static final Type[] TYPES = new Type[] { StringType.INSTANCE };
	private static final String[] NAMES = new String[] { "name" }; //$NON-NLS-1$
	private static final boolean[] MUTABILITY = new boolean[] { true };
	private static final boolean[] GENERATION = new boolean[] { false };

	/**
	 * @see EntityPersister#getPropertyTypes()
	 */
	public Type[] getPropertyTypes() {
		return TYPES;
	}

	/**
	 * @see EntityPersister#getPropertyNames()
	 */
	public String[] getPropertyNames() {
		return NAMES;
	}

	/**
	 * @see EntityPersister#getPropertyCascadeStyles()
	 */
	public CascadeStyle[] getPropertyCascadeStyles() {
		return null;
	}

	/**
	 * @see EntityPersister#getIdentifierType()
	 */
	public Type getIdentifierType() {
		return StringType.INSTANCE;
	}

	/**
	 * @see EntityPersister#getIdentifierPropertyName()
	 */
	public String getIdentifierPropertyName() {
		return "id"; //$NON-NLS-1$
	}

	/**
	 * @see EntityPersister#hasCache()
	 */
	public boolean hasCache() {
		return false;
	}

	/**
	 * @see EntityPersister#getCache()
	 */
	public CacheConcurrencyStrategy getCache() {
		return null;
	}

	/**
	 * @see EntityPersister#getRootEntityName()
	 */
	public String getRootEntityName() {
		return "CUSTOMS"; //$NON-NLS-1$
	}

	public Serializable[] getPropertySpaces() {
		return new String[] { "CUSTOMS" }; //$NON-NLS-1$
	}

	public Serializable[] getQuerySpaces() {
		return new String[] { "CUSTOMS" }; //$NON-NLS-1$
	}

	/**
	 * @see EntityPersister#getClassMetadata()
	 */
	public ClassMetadata getClassMetadata() {
		return null;
	}

	public boolean[] getPropertyUpdateability() {
		return MUTABILITY;
	}

	public boolean[] getPropertyCheckability() {
		return MUTABILITY;
	}

	/**
	 * @see EntityPersister#getPropertyInsertability()
	 */
	public boolean[] getPropertyInsertability() {
		return MUTABILITY;
	}


	public boolean canExtractIdOutOfEntity() {
		return true;
	}

	public boolean isBatchLoadable() {
		return false;
	}

	public Type getPropertyType(String propertyName) {
		throw new UnsupportedOperationException();
	}

	public Object getPropertyValue(Object object, String propertyName)
		throws HibernateException {
		throw new UnsupportedOperationException();
	}

	public Object createProxy(Serializable id, SessionImplementor session)
		throws HibernateException {
		throw new UnsupportedOperationException("no proxy for this class"); //$NON-NLS-1$
	}

	public Object getCurrentVersion(
		Serializable id,
		SessionImplementor session)
		throws HibernateException {

		return INSTANCES.get(id);
	}

	public Object forceVersionIncrement(Serializable id, Object currentVersion, SessionImplementor session)
			throws HibernateException {
		return null;
	}

	public EntityMode guessEntityMode(Object object) {
		if ( !isInstance(object, EntityMode.POJO) ) {
			return null;
		}
		else {
			return EntityMode.POJO;
		}
	}

	public boolean[] getPropertyNullability() {
		return MUTABILITY;
	}

	public boolean isDynamic() {
		return false;
	}

	public boolean isCacheInvalidationRequired() {
		return false;
	}

	public void applyFilters(QuerySelect select, String alias, Map filters) {
	}

	public void applyFilters(Select select, String alias, Map filters) {
	}


	public void afterInitialize(Object entity, boolean fetched, SessionImplementor session) {
	}

	public void afterReassociate(Object entity, SessionImplementor session) {
	}

	public Object[] getDatabaseSnapshot(Serializable id, SessionImplementor session)
	throws HibernateException {
		return null;
	}

	public boolean[] getPropertyVersionability() {
		return MUTABILITY;
	}

	public CacheEntryStructure getCacheEntryStructure() {
		return new UnstructuredCacheEntry();
	}

	public boolean hasSubselectLoadableCollections() {
		return false;
	}

	public int[] getNaturalIdentifierProperties() {
		return null;
	}

	public Type[] getNaturalIdentifierTypes() {
		return null;
	}

	public boolean hasNaturalIdentifier() {
		return false;
	}

	public boolean hasMutableProperties() {
		return false;
	}

	public boolean isInstrumented(EntityMode entityMode) {
		return false;
	}

	public boolean hasInsertGeneratedProperties() {
		return false;
	}

	public boolean hasUpdateGeneratedProperties() {
		return false;
	}

	public boolean[] getPropertyLaziness() {
		return null;
	}

	public boolean isLazyPropertiesCacheable() {
		return true;
	}

	public boolean hasGeneratedProperties() {
		return false;
	}

	public boolean isVersionPropertyGenerated() {
		return false;
	}

	public Object[] getNaturalIdentifierSnapshot(Serializable id, SessionImplementor session) throws HibernateException {
		return null;
	}

	public Comparator getVersionComparator() {
		return null;
	}

	public boolean[] getPropertyInsertGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean[] getPropertyUpdateGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasIdentifierPropertyOrEmbeddedCompositeIdentifier() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EntityRegionAccessStrategy getCacheAccessStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityMetamodel getEntityMetamodel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getIdentifier(Object arg0, SessionImplementor arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueInclusion[] getPropertyInsertGenerationInclusions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueInclusion[] getPropertyUpdateGenerationInclusions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object instantiate(Serializable arg0, SessionImplementor arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object load(Serializable arg0, Object arg1, LockOptions arg2,
			SessionImplementor arg3) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void lock(Serializable arg0, Object arg1, Object arg2,
			LockOptions arg3, SessionImplementor arg4)
			throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetIdentifier(Object arg0, Serializable arg1, Object arg2,
			SessionImplementor arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIdentifier(Object arg0, Serializable arg1,
			SessionImplementor arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInstrumented() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean implementsLifecycle() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class getConcreteProxyClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPropertyValues(Object object, Object[] values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPropertyValue(Object object, int i, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getPropertyValues(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPropertyValue(Object object, int i)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getIdentifier(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getVersion(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInstance(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasUninitializedLazyProperties(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EntityPersister getSubclassEntityPersister(Object instance,
			SessionFactoryImplementor factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityMode getEntityMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityTuplizer getEntityTuplizer() {
		// TODO Auto-generated method stub
		return null;
	}

}
