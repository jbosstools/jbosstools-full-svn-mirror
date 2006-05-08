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
