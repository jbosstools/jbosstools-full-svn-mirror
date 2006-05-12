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

package org.hibernate.netbeans.console.editor.hql.completion;

import java.util.Comparator;

/**
 * @author leon
 */
public class CompletionComparator implements Comparator<HqlResultItem> {
    
    public CompletionComparator() {
    }

    public int compare(HqlResultItem o1, HqlResultItem o2) {
        int r = o1.getSortPriority() - o2.getSortPriority();
        if (r != 0) {
            return r;
        }
        return String.valueOf(o1.getSortText()).compareTo(String.valueOf(o2.getSortText()));
    }
    
}
