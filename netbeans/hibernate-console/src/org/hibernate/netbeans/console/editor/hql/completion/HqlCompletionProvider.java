package org.hibernate.netbeans.console.editor.hql.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.hibernate.netbeans.console.editor.hql.HqlDocument;
import org.hibernate.netbeans.console.editor.EditorContentHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

    private static class HqlCompletionQuery extends AsyncCompletionQuery {

        private String filterPrefix;

        private int queryOffset;

        private int anchorOffset;

        private List<HqlResultItem> queryResult;

        protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
            try {
                queryOffset = -1;
                anchorOffset = -1;
                queryResult = null;
                if (!(doc instanceof HqlDocument)) {
                    return;
                }
                HqlDocument bd = (HqlDocument) doc;
                int[] bounds = EditorContentHelper.getCurrentQueryBounds(bd, caretOffset);
                if (bounds == null) {
                    return;
                }
                char[] chars = bd.getChars(bounds);
                int statementOffset = caretOffset - bounds[0];
                Session s = bd.getSessionFactoryNode().getDescriptor().getSession();
                SessionFactory sessionFactory = null;
                if (s != null) {
                    sessionFactory = s.getSessionFactory();
                }
                if (sessionFactory == null) {
                    return;
                }
                String prefix = HqlAnalyzer.getTableNamePrefix(chars, statementOffset);
                if (HqlAnalyzer.shouldShowTables(chars, statementOffset)) {
                    // Show tables
                    List<HqlResultItem> tables = CompletionHelper.getMappedClasses(sessionFactory, prefix);
                    if (tables.size() == 1 && tables.get(0).getText().equals(prefix)) {
                        Completion.get().hideAll();
                    } else {
                        anchorOffset = caretOffset - prefix.length();
                        queryOffset = caretOffset;
                        queryResult = tables;
                        resultSet.addAllItems(tables);
                    }
                } else {
                    // Show attributes
                    List<QueryTable> visible = HqlAnalyzer.getVisibleTables(chars, statementOffset);
                    int dotIndex = prefix.lastIndexOf(".");
                    if (dotIndex == -1) {
                        // It's a simple path, not a dot separated one
                        queryResult = new ArrayList<HqlResultItem>();
                        boolean hide = true;
                        for (QueryTable qt : visible) {
                            String alias = qt.getAlias();
                            if (!alias.equals(prefix)) {
                                if (alias.equals(qt.getType()) && visible.size() == 1) {
                                    hide = false;
                                    queryResult = CompletionHelper.getProperties(sessionFactory, alias, prefix);
                                    resultSet.addAllItems(queryResult);
                                } else if (!alias.equals(qt.getType())) {
                                    hide = false;
                                    HqlResultItem ri = new HqlResultItem(HqlResultItem.CLASS_ICON, qt.getAlias(), prefix.length(), null);
                                    queryResult.add(ri);
                                    resultSet.addItem(ri);
                                }
                            }
                            if (hide) {
                                Completion.get().hideAll();
                            } else {
                                anchorOffset = caretOffset - prefix.length();
                                queryOffset = caretOffset;
                            }
                        }
                    } else {
                        String object = CompletionHelper.getCanonicalPath(visible, prefix.substring(0, dotIndex));
                        String propertyPrefix = prefix.substring(dotIndex + 1);
                        List<HqlResultItem> results = CompletionHelper.getProperties(sessionFactory, object, propertyPrefix);
                        if (results.size() == 1 && results.get(0).getText().equals(propertyPrefix)) {
                            Completion.get().hideAll();
                        } else {
                            queryResult = results;
                            resultSet.addAllItems(results);
                            anchorOffset = caretOffset - propertyPrefix.length();
                            queryOffset = caretOffset;
                        }
                    }
                }
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
                    resultSet.setAnchorOffset(queryOffset);
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

