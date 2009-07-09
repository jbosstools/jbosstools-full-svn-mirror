/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor;

import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.jboss.tools.vpe.editor.menu.ITextNodeSplitter;

/**
 * @author Daniel
 *
 */
public class TextNodeSplitterImpl implements ITextNodeSplitter  {
	private Text node;
	private Node parent;
	private IndexedRegion region;
	
	private int offset1 = 0, offset2=0;
	private boolean split1=true,split2=true;


	public TextNodeSplitterImpl(Point range, Text node){
		this.node = node;
		parent = node.getParentNode();
		region = (IndexedRegion)node;
		offset1 = range.x-region.getStartOffset();
		if(offset1 == 0)
			split1 = false;
		else if(offset1 > 0){
			String str = node.getNodeValue().substring(0,offset1);
			if(isSpacing(str))split1 = false;
		}
		offset2 = range.y;
		if(region.getEndOffset()-(range.x+range.y) == 0)
			split2 = false;
		else if(region.getEndOffset()-(range.x+range.y) > 0){
			String str = node.getNodeValue().substring(offset1+range.y,node.getNodeValue().length());
			if(isSpacing(str))split2 = false;
		}
		
	}
	
	public int getSplitIndex(int index) {
		int nodeIndex = getIndex(parent, node);
		if(nodeIndex == index){
			if(split1)return index+1;
			else return index;
		}else if(nodeIndex == index+1){
			if(split1 && split2) return index+1;
			else return index;
		}
		return index+1;
	}
	
	public void nodeSplit(int type) {
		Node node1=null, node2=null;
		
		//if(!next){
			if(type == INSERT_AROUND){
				if(split1 && split2){
					node1 = ((Text)node).splitText(offset1);
				}else if(split1){
					node1 = ((Text)node).splitText(offset1);
				}else{
					if(split2)node1 = ((Text)node).splitText(offset1+offset2);
				}
			}else if(type == INSERT_BEFORE){
				if(split1)node1 = ((Text)node).splitText(offset1);
			}else if(type == INSERT_AFTER){
				if(split2)node1 = ((Text)node).splitText(offset1+offset2);
			}
	}
	
	private boolean isSpacing(String str){
		if("".endsWith(str.trim())) return true; //$NON-NLS-1$
		else return false;
	}
	
	private int getIndex(Node parentNode, Node child) {
		NodeList nodeList = parentNode.getChildNodes();
		int index = -1;
		int size = nodeList.getLength();
		for (int i = 0; i < size; i++) {
			if (nodeList.item(i) == child) {
				index = i;
				break;
			}
		}
		return index;
	}
	
}
