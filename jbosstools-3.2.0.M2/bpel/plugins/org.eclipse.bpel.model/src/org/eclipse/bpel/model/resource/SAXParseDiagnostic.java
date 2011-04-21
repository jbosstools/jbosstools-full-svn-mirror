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
package org.eclipse.bpel.model.resource;

import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.xml.sax.SAXParseException;

/*
 * This class represents a SAX parser diagnostic. These are added to the EMF resource.
 *
 * @see https://jira.jboss.org/browse/JBIDE-6825
 * @author Bob Brodt
 * @date Aug 13, 2010
 */
public class SAXParseDiagnostic implements Diagnostic
{
	protected SAXParseException exception;
	protected int severity;
	
	public static final int WARNING = 1;
	public static final int ERROR = 2;
	public static final int FATAL_ERROR = 3;
	
	SAXParseDiagnostic(SAXParseException exception, int severity)
	{
		this.exception = exception;
		this.severity = severity;
	}

	public int getColumn() {
		return exception.getColumnNumber();
	}

	public int getLine() {
		return exception.getLineNumber();
	}

	public String getLocation() {
		return exception.getPublicId();
	}

	public String getMessage() {
		return exception.getLocalizedMessage();
	}
	
	public int getSeverity() {
		return severity;
	}
}