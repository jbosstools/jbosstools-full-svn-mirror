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
package org.hibernate.eclipse.hqleditor;

import java.util.Comparator;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

/** 
 * Sort a comparator according to its display string.
 * Use the INSTANCE variable to save memory/time.
 */ 
public class DisplayStringProposalComparator implements Comparator {
	
	static public Comparator INSTANCE = new DisplayStringProposalComparator();
	
	public int compare( Object o1, Object o2 ) {
        ICompletionProposal c1 = (ICompletionProposal) o1;
        ICompletionProposal c2 = (ICompletionProposal) o2;
        return c1.getDisplayString().compareTo( c2.getDisplayString() );
    }
}