/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.console;

/**
 * 
 */
public interface ConsoleConfigurationListener {
	
	public void queryPageCreated(QueryPage qp);
	
	/** 
	 * called when the factory has just been created 
	 */	 
	public void sessionFactoryBuilt(ConsoleConfiguration ccfg);
	
	/**
	 * Called when this sessionFactory is about to be closed. Used for listeners to clean up resources related to this sessionfactory (such as closing sessions)
	 * @param configuration
	 */
	public void sessionFactoryClosing(ConsoleConfiguration configuration);

	public void configurationBuilt(ConsoleConfiguration ccfg);

	public void configurationReset(ConsoleConfiguration ccfg);
	
}
