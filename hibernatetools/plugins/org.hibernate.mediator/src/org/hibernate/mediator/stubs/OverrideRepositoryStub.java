package org.hibernate.mediator.stubs;

import java.io.File;

import org.hibernate.cfg.reveng.OverrideRepository;

public class OverrideRepositoryStub {
	protected OverrideRepository overrideRepository;

	protected OverrideRepositoryStub(Object overrideRepository) {
		this.overrideRepository = (OverrideRepository)overrideRepository;
	}

	public void addFile(File file) {
		overrideRepository.addFile(file);
	}

	public ReverseEngineeringStrategyStub getReverseEngineeringStrategy(
			ReverseEngineeringStrategyStub res) {
		return new ReverseEngineeringStrategyStub(
			overrideRepository.getReverseEngineeringStrategy(res.reverseEngineeringStrategy));
	}

	public void addTableFilter(TableFilterStub tf) {
		overrideRepository.addTableFilter(tf.tableFilter);
	}

}
