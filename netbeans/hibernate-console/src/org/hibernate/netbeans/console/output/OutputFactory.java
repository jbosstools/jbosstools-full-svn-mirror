/*
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

package org.hibernate.netbeans.console.output;

import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.oracle.ExplainPlanMultiViewDescription;

/**
 * @author leon
 */
public class OutputFactory {

    private final static OutputMultiViewDescription[] EMPTY_ARRAY = new OutputMultiViewDescription[0];
    
    private OutputFactory() {
    }
    
    public static OutputMultiViewDescription[] createDescription(SessionFactoryDescriptor descr) {
        if ("org.hibernate.dialect.Oracle9Dialect".equals(descr.getHibernateDialect())) {
            OutputMultiViewDescription[] descrs = new OutputMultiViewDescription[1];
            descrs[0] = new ExplainPlanMultiViewDescription(descr);
            return descrs;
        }
        return EMPTY_ARRAY;
    }
    
}
