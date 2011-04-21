/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package demo;

import javax.persistence.Entity;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Role;
import org.jboss.seam.annotations.Roles;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;

/**
 * Created by JBoss Developer Studio
 */

@Name("myUser")
@Scope(ScopeType.APPLICATION)
@Entity
@Install(precedence=Install.FRAMEWORK)
@Role(name="roleName1", scope=ScopeType.SESSION)
@Roles({
	@Role(name="roleName2", scope=ScopeType.SESSION),
	@Role(name="roleName3", scope=ScopeType.SESSION),
	@Role(name="roleName4", scope=ScopeType.SESSION)
})

public class User {
	
	@Name("inner_JBIDE_1374")
	public static class Inner {
		private String innerName;
		
		public String getInnerName() {
			return innerName;
		}
		
		public void setInnerName(String s) {
			innerName = s;
		}
	}

	private String name;
	
	@Out
	private String address = "";
	
	@In
	private String payment = "";

	/**
	 * @return User Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param User Name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getPayment() {
		return payment;
	}
	
	@Unwrap
	public User unwrapMethod() {
		return new User();
	}
	
	@Create @Destroy
	public void createAndDestroyMethod(Component c) {
	}
	
	@Factory(value="myFactory", scope=ScopeType.SESSION)
	public User testFactory() {
		return new User();
	}
	
	@Factory
	public User getMyFactory2() {
		return new User();
	}

}
