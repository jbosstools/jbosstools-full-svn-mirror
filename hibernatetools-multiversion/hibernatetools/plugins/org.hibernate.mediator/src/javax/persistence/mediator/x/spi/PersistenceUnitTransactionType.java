package javax.persistence.mediator.x.spi;

import org.hibernate.mediator.base.HObject;

public class PersistenceUnitTransactionType extends HObject {

	public static final String CL = "javax.persistence.spi.PersistenceUnitTransactionType"; //$NON-NLS-1$
	
	public static final PersistenceUnitTransactionType RESOURCE_LOCAL =
		new PersistenceUnitTransactionType(readStaticFieldValue(CL, "RESOURCE_LOCAL"));
	
	public static final PersistenceUnitTransactionType JTA =
		new PersistenceUnitTransactionType(readStaticFieldValue(CL, "JTA"));

	protected PersistenceUnitTransactionType(Object persistenceUnitTransactionType) {
		super(persistenceUnitTransactionType, CL);
	}

}
