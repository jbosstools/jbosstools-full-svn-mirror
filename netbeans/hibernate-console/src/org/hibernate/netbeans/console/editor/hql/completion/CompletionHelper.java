package org.hibernate.netbeans.console.editor.hql.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.ArrayType;
import org.hibernate.type.AssociationType;
import org.hibernate.type.BagType;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.IdentifierBagType;
import org.hibernate.type.ListType;
import org.hibernate.type.MapType;
import org.hibernate.type.SetType;
import org.hibernate.type.Type;

/**
 * @author leon
 */
public class CompletionHelper {
    
    private CompletionHelper() {
    }
    
    public static String getCanonicalPath(List<QueryTable> qts, String name) {
        Map<String, String> alias2Type = new HashMap<String, String>();
        for (QueryTable qt : qts) {
            alias2Type.put(qt.getAlias(), qt.getType());
        }
        if (qts.size() == 1) {
            QueryTable visible = qts.get(0);
            if (name.equals(visible.getAlias())) {
                return visible.getType();
            } else {
                return visible.getType() + "/" + name;
            }
        } else {
            return getCanonicalPath(new HashSet<String>(), alias2Type, name);
        }
    }
    
    public static List<HqlResultItem> getMappedClasses(SessionFactory sf, String prefix) {
        List<HqlResultItem> l = new ArrayList<HqlResultItem>();
        @SuppressWarnings("unchecked")
        Collection<ClassMetadata> classMetas = sf.getAllClassMetadata().values();
        for (ClassMetadata md : classMetas) {
            String entityName = md.getEntityName();
            String simpleName;
            int idx = entityName.lastIndexOf(".");
            if (idx == -1) {
                simpleName = entityName;
            } else {
                simpleName = entityName.substring(idx + 1, entityName.length());
            }
            if ((prefix == null || prefix.length() == 0) && simpleName.length() > 0) {
                l.add(new HqlResultItem(HqlResultItem.CLASS_ICON, simpleName, prefix.length(), null));
            } else if (prefix != null && prefix.length() > 0) {
                if (simpleName.startsWith(prefix)) {
                    l.add(new HqlResultItem(HqlResultItem.CLASS_ICON, simpleName, prefix.length(), null));
                } else {
                    // Maybe camel case
                    int length = simpleName.length();
                }
            }
        }
        return l;
    }
    
    private static String getCanonicalPath(Set<String> resolved, Map<String, String> alias2Type, String name) {
        if (resolved.contains(name)) {
            // To prevent a stack overflow
            return name;
        }
        resolved.add(name);
        String type = alias2Type.get(name);
        if (type != null) {
            return name.equals(type) ? name : getCanonicalPath(resolved, alias2Type, type);
        }
        int idx = name.lastIndexOf('.');
        if (idx == -1) {
            return type != null ? type : name;
        }
        String baseName = name.substring(0, idx);
        String prop = name.substring(idx + 1);
        if (isAliasNown(alias2Type, baseName)) {
            return getCanonicalPath(resolved, alias2Type, baseName) + "/" + prop;
        } else {
            return name;
        }
    }
    
    private static boolean isAliasNown(Map<String, String> alias2Type, String alias) {
        if (alias2Type.containsKey(alias)) {
            return true;
        }
        int idx = alias.lastIndexOf('.');
        if (idx == -1) {
            return false;
        }
        return isAliasNown(alias2Type, alias.substring(0, idx));
    }
    
    private static ClassMetadata getClassMeta(SessionFactory sf, String name) {
        ClassMetadata cmd = sf.getClassMetadata(name);
        if (cmd == null && name.indexOf('.') == -1) {
            // Not found, maybe we can get it through the simple name
            @SuppressWarnings("unchecked")
            Collection<ClassMetadata> allMetas = sf.getAllClassMetadata().values();
            for (ClassMetadata md : allMetas) {
                String en = md.getEntityName();
                int idx = en.lastIndexOf('.');
                if (idx != -1 && en.substring(idx + 1).equals(name)) {
                    cmd = md;
                    break;
                }
            }
        }
        return cmd;
    }
    
    private static void addProperties(SessionFactory sf, String canonicalPath, String prefix, List<HqlResultItem> props) {
        int idx = canonicalPath.indexOf('/');
        if (idx == -1) {
            ClassMetadata cmd = getClassMeta(sf, canonicalPath);
            if (cmd == null) {
                return;
            }
            addPropertiesToList(sf, cmd, prefix, props);
            Type idType = cmd.getIdentifierType();
            if (idType != null && idType instanceof ComponentType) {
                addPropertiesToList(sf, (ComponentType) idType, prefix, props);
            }
        } else {
            Type type = unwrapType(
                    sf,
                    getAttributeType(
                    sf,
                    canonicalPath.substring(0, idx),
                    canonicalPath.substring(idx + 1)));
            if (type == null) {
                return;
            }
            int nextIdx = canonicalPath.indexOf('/', idx + 1);
            if (nextIdx == -1 || type == null) {
                if (type instanceof ComponentType) {
                    addPropertiesToList(sf, (ComponentType) type, prefix, props);
                } else {
                    addProperties(sf, getDisplayableTypeName(sf, type, false), prefix, props);
                }
            } else {
                addProperties(sf, getDisplayableTypeName(sf, type, false) + canonicalPath.substring(nextIdx), prefix, props);
            }
        }
    }
    
    private static Type unwrapType(SessionFactory sf, Type t) {
        if (t instanceof CollectionType) {
            return unwrapType(sf, ((CollectionType) t).getElementType((SessionFactoryImplementor) sf));
        } else {
            return t;
        }
    }
    
    private static String getShortCollectionTypeName(CollectionType ct) {
        if (ct instanceof ArrayType) {
            return "Array";
        } else if (ct instanceof BagType) {
            return "Bag";
        } else if (ct instanceof IdentifierBagType) {
            return "IdBag";
        } else if (ct instanceof ListType) {
            return "List";
        } else if (ct instanceof MapType) {
            return "Map";
        } else if (ct instanceof SetType) {
            return "Set";
        } else {
            return "Collection";
        }
        
    }
    
    private static String getDisplayableTypeName(SessionFactory sf, Type t, boolean simple) {
        String name;
        if (t instanceof CollectionType) {
            name = getDisplayableTypeName(sf, unwrapType(sf, t), simple);
        } else if (t instanceof AssociationType) {
            name = ((AssociationType) t).getAssociatedEntityName((SessionFactoryImplementor) sf);
        } else {
            name = t.getReturnedClass().getName();
        }
        if (name == null) {
            return name;
        }
        if (!simple) {
            return getEventualCollectionName(t, name);
        }
        int idx = name.lastIndexOf('.');
        if (idx == -1) {
            return getEventualCollectionName(t, name);
        }
        return getEventualCollectionName(t, name.substring(idx + 1));
    }
    
    private static String getEventualCollectionName(Type t, String name) {
        if (t instanceof CollectionType) {
            return getShortCollectionTypeName((CollectionType) t) + "&lt;" + name + "&gt;";
        } else {
            return name;
        }
    }
    
    private static void addPropertiesToList(SessionFactory sf, ComponentType t, String prefix, List<HqlResultItem> l) {
        if (t == null) {
            return;
        }
        String[] props = t.getPropertyNames();
        Type[] types = t.getSubtypes();
        int i = 0;
        for (String candidate : props) {
            if (prefix == null || prefix.length() == 0 || candidate.startsWith(prefix)) {
                l.add(new HqlResultItem(
                        HqlResultItem.PROPERTY_ICON,
                        candidate,
                        prefix.length(),
                        getDisplayableTypeName(sf, types[i], true)));
            }
            i++;
        }
    }
    
    private static void addPropertiesToList(SessionFactory sf, ClassMetadata cmd, String prefix, List<HqlResultItem> l) {
        if (cmd == null) {
            return;
        }
        String[] props = cmd.getPropertyNames();
        for (String candidate : props) {
            if (prefix == null || prefix.length() == 0 || candidate.startsWith(prefix)) {
                l.add(new HqlResultItem(
                        HqlResultItem.PROPERTY_ICON, candidate, prefix.length(), getDisplayableTypeName(sf, cmd.getPropertyType(candidate), true)));
            }
        }
        String identPropName = cmd.getIdentifierPropertyName();
        if (identPropName != null && (prefix == null || prefix.length() == 0 || identPropName.startsWith(prefix))) {
            l.add(new HqlResultItem(
                    HqlResultItem.PROPERTY_ICON, identPropName, prefix.length(), getDisplayableTypeName(sf, cmd.getIdentifierType(), true)));
        }
    }
    
    public static List<HqlResultItem> getProperties(SessionFactory sf, String canonicalPath, String prefix) {
        List<HqlResultItem> l = new ArrayList<HqlResultItem>();
        addProperties(sf, canonicalPath, prefix, l);
        return l;
    }
    
    private static Type getAttributeType(SessionFactory sf, String type, String attributeList) {
        ClassMetadata cmd = getClassMeta(sf, type);
        if (cmd == null) {
            return null;
        }
        String attribute;
        int idx = attributeList.indexOf('/');
        if (idx == -1) {
            attribute = attributeList;
        } else {
            attribute = attributeList.substring(0, idx);
        }
        String idName = cmd.getIdentifierPropertyName();
        if (attribute.equals(idName)) {
            return cmd.getIdentifierType();
        }
        Type propType = cmd.getPropertyType(attribute);
        return propType;
    }
    
    
}
