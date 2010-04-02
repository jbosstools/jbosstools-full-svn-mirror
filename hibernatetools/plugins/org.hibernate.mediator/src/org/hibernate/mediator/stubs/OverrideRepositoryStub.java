package org.hibernate.mediator.stubs;

import java.io.File;

import org.hibernate.cfg.reveng.OverrideRepository;
import org.hibernate.mediator.Messages;

public class OverrideRepositoryStub {
	public static final String CL = "org.hibernate.cfg.reveng.OverrideRepository"; //$NON-NLS-1$

	protected OverrideRepository overrideRepository;

	protected OverrideRepositoryStub(Object overrideRepository) {
		if (overrideRepository == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.overrideRepository = (OverrideRepository)overrideRepository;
	}
	
	public static OverrideRepositoryStub newInstance() {
		return new OverrideRepositoryStub(new OverrideRepository());
	}

	public void addFile(File file) {
		overrideRepository.addFile(file);
	}

	public ReverseEngineeringStrategyStub getReverseEngineeringStrategy(
			ReverseEngineeringStrategyStub res) {
		Object obj = overrideRepository.getReverseEngineeringStrategy(res.reverseEngineeringStrategy);
		if (obj == null) {
			return null;
		}
		return new ReverseEngineeringStrategyStub(obj);
	}

	public void addTableFilter(TableFilterStub tf) {
		overrideRepository.addTableFilter(tf.tableFilter);
	}

}
