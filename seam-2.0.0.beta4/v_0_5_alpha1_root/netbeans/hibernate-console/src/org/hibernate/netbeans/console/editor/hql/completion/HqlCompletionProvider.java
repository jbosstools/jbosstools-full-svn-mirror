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
package org.hibernate.netbeans.console.editor.hql.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.hibernate.netbeans.console.editor.hql.HqlDocument;
import org.hibernate.netbeans.console.editor.EditorContentHelper;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.ide.completion.HQLCodeAssist;
import org.hibernate.tool.ide.completion.HQLCompletionProposal;
import org.hibernate.tool.ide.completion.IHQLCodeAssist;
import org.hibernate.tool.ide.completion.IHQLCompletionRequestor;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.ErrorManager;

/**
 * @author leon
 */
public class HqlCompletionProvider implements CompletionProvider {
    
    public HqlCompletionProvider() {
    }

    public CompletionTask createTask(int queryType, JTextComponent component) {
        if (queryType == CompletionProvider.COMPLETION_QUERY_TYPE) {
            return new AsyncCompletionTask(new HqlCompletionQuery(), component);
        }
        return null;
    }

    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 0;
    }
    
    private static class HqlCompletionRequestor implements IHQLCompletionRequestor {
        
        private Collection<HqlResultItem> items;
        
        public HqlCompletionRequestor(Collection<HqlResultItem> items) {
            this.items = items;
        }
        
        public boolean accept(HQLCompletionProposal proposal) {
            int kind = proposal.getCompletionKind();
            String completion = proposal.getCompletion();
            if (completion == null || completion.length() == 0) {
                return false;
            }
            if (kind == HQLCompletionProposal.ENTITY_NAME || kind == HQLCompletionProposal.ALIAS_REF) {
                if (proposal.getEntityName().equals(proposal.getShortEntityName())) {
                    // it's most probably the FQN of the entity
                    items.add(new HqlResultItem(HqlResultItem.CLASS_ICON, completion, proposal.getEntityName(), null, HqlResultItem.PRIORITY_QUALIFIED_NAME));
                } else {
                    items.add(new HqlResultItem(HqlResultItem.CLASS_ICON, completion, proposal.getSimpleName(), null, HqlResultItem.PRIORITY_SIMPLE_NAME));
                }
                return true;
            } else if (kind == HQLCompletionProposal.PROPERTY) {
                items.add(new HqlResultItem(HqlResultItem.PROPERTY_ICON, completion, proposal.getPropertyName(), null, HqlResultItem.PRIORITY_PROPERTY));
                return true;
            }
            return false;
        }

        public void completionFailure(String string) {
        }
        
    }

    private static class HqlCompletionQuery extends AsyncCompletionQuery {

        private String filterPrefix;

        private int anchorOffset;

        private List<HqlResultItem> queryResult;
        
        protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
            try {
                anchorOffset = -1;
                queryResult = null;
                if (!(doc instanceof HqlDocument)) {
                    return;
                }
                HqlDocument bd = (HqlDocument) doc;
                Configuration cfg = bd.getSessionFactoryNode().getDescriptor().getConfiguration();
                if (cfg == null) {
                    return;
                }
                int[] bounds = EditorContentHelper.getCurrentQueryBounds(bd, caretOffset);
                if (bounds == null) {
                    return;
                }
                String query = String.valueOf(bd.getChars(bounds));
                IHQLCodeAssist assist = new HQLCodeAssist(bd.getSessionFactoryNode().getDescriptor().getConfiguration());
                int statementOffset = caretOffset - bounds[0];
                queryResult = new ArrayList<HqlResultItem>();
                assist.codeComplete(query, statementOffset, new HqlCompletionRequestor(queryResult));
                if (queryResult.size() > 0) {
                    anchorOffset = caretOffset;
                }
                resultSet.addAllItems(queryResult);
            } catch (BadLocationException ex) {
                ErrorManager.getDefault().notify(ex);
            } finally {
                resultSet.finish();
            }
        }

        protected boolean canFilter(JTextComponent component) {
            int caretOffset = component.getCaretPosition();
            HqlDocument doc = (HqlDocument) component.getDocument();
            filterPrefix = null;
            if (caretOffset >= anchorOffset && anchorOffset > -1) {
                try {
                    filterPrefix = doc.getText(anchorOffset, caretOffset - anchorOffset);
                    if (filterPrefix.endsWith(".")) {
                        filterPrefix = null;
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                    // filterPrefix stays null -> no filtering
                }
            }
            return (filterPrefix != null);
        }

        protected void filter(CompletionResultSet resultSet) {
            try {
                if (filterPrefix != null && queryResult != null) {
                    resultSet.setAnchorOffset(anchorOffset);
                    List<HqlResultItem> l = filter(queryResult);
                    if (l.size() == 1 && l.get(0).getText().equals(filterPrefix)) {
                        Completion.get().hideAll();
                    } else {
                        resultSet.addAllItems(filter(queryResult));
                    }
                }
            } finally {
                resultSet.finish();
            }
        }

        private List<HqlResultItem> filter(List<HqlResultItem> results) {
            List<HqlResultItem> l = new ArrayList<HqlResultItem>();
            if (filterPrefix == null) {
                return results;
            }
            for (HqlResultItem result : results) {
                if (result.acceptsPrefix(filterPrefix)) {
                    l.add(result);
                    result.setPrefixLength(filterPrefix.length());
                }
            }
            return l;
        }

    }




}

