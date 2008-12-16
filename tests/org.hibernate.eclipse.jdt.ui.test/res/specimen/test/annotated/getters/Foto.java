/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package test.annotated.getters;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Foto {

	private Long fid;
	
	private Short id;

	private Person person;

	private Short width_IDtest;

	private Short height_testID;
	
	public Foto() {
	}

	public Long getFid() {
		return fid;
	}

	public void setFid(Long fid) {
		this.fid = fid;
	}

	@Id @GeneratedValue
	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	@OneToOne(mappedBy="foto")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Short getWidth_IDtest() {
		return width_IDtest;
	}

	public void setWidth_IDtest(Short width_IDtest) {
		this.width_IDtest = width_IDtest;
	}

	public Short getHeight_testID() {
		return height_testID;
	}

	public void setHeight_testID(Short height_testID) {
		this.height_testID = height_testID;
	}
	
}
