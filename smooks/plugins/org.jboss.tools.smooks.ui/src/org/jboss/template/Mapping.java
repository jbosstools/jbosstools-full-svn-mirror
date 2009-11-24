/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006, JBoss Inc.
 */
package org.jboss.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.w3c.dom.Node;

/**
 * Model template mapping.
 * <p/>
 * Represents a successful mapping. It also tells if the mapping requires
 * other model nodes to be hidden, so as to restrict mappings to these nodes.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class Mapping {

    private String srcPath;
    private Node mappingNode;
    private List<Node> hideNodes;
    private List<Node> showNodes;

    /**
     * Public constructor.
     * @param srcPath Source path.
     * @param mappingNode The mapping node.
     */
    public Mapping(String srcPath, Node mappingNode) {
        Assert.isNotNull(srcPath, "srcPath");
        Assert.isNotNull(mappingNode, "mappingNode");
        this.srcPath = srcPath;
        this.mappingNode = mappingNode;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public Node getMappingNode() {
        return mappingNode;
    }

    public List<Node> getHideNodes() {
        return hideNodes;
    }

    public List<Node> getShowNodes() {
        return showNodes;
    }

    public void addHideNode(Node node) {
    	Assert.isNotNull(node, "node");

        if(hideNodes == null) {
            hideNodes = new ArrayList<Node>();
        }

        hideNodes.add(node);
    }

    public void addShowNode(Node node) {
    	Assert.isNotNull(node, "node");

        if(showNodes == null) {
            showNodes = new ArrayList<Node>();
        }

        showNodes.add(node);
    }
}
