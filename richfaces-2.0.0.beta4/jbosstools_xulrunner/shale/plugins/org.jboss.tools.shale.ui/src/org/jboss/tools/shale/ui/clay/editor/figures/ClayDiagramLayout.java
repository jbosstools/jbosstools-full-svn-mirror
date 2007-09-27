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
package org.jboss.tools.shale.ui.clay.editor.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.jboss.tools.shale.ui.clay.editor.edit.ClayComponentEditPart;
import org.jboss.tools.shale.ui.clay.editor.model.IClayOptions;
import org.jboss.tools.shale.ui.clay.editor.model.IComponent;

import java.util.*;

public class ClayDiagramLayout extends AbstractLayout{
	Dimension dim = new Dimension(0,0);
	Hashtable<IComponent,Rectangle> figureDim = new Hashtable<IComponent,Rectangle>();
	Hashtable<String,IComponent> figureNames;
	int figureW = 114;//55;
	int figureH = 21;//40;
	int border = 30;
	IClayOptions options;
	IFigure container;
	
	private int getGridX(){
		return options.getHorizontalSpacing();
	}

	private int getGridY(){
		return options.getVerticalSpacing();
	}
	
	private Vector getDefinitions(){
		return ((DiagramFigure)container).getEditPart().getClayModel().getComponentList().getElements();
	}
	
	private ComponentFigure getFigure(IComponent definition){
		ClayComponentEditPart part = (ClayComponentEditPart)((DiagramFigure)container).getEditPart().getViewer().getEditPartRegistry().get(definition);
		if(part == null)return null;
		else return part.getDefinitionFigure();
	}
	
	public ClayDiagramLayout(IClayOptions options){
		super();
		this.options = options;
	}
	
	public Dimension calculatePreferredSize(IFigure container,int wHint, int hHint){
		calculateDim();
		return this.dim;
	}
	
	private void calculateDim(){
		int x = 0;
		int y = 0;
		for (Iterator i = figureDim.keySet().iterator(); i.hasNext();) {
			Rectangle fb = (Rectangle)figureDim.get(i.next());
			x = Math.max(x,fb.x+fb.width);
			y = Math.max(y,fb.y+fb.height);
		}
		this.dim.width = x;
		this.dim.height = y;
	}
	
	public void layout(IFigure container){
		this.container = container;
		
		figureDim.clear();
		
		List l = getDefinitions();
		figureNames = new Hashtable<String,IComponent>();
		Vector<IComponent> figures = new Vector<IComponent>(); 
		for (int i = 0; i < l.size(); i++) {
			IComponent tf = (IComponent)l.get(i);
			figureNames.put(tf.getName(),tf);
			figures.add(tf);
		}
		
		TreeSet lines = createLine(figures,figureNames);
		Dimension lineSize = new Dimension(0,0);
		for (Iterator i = lines.iterator(); i.hasNext();) {	
			Hashtable hs = (Hashtable)i.next();
			int ret = layoutLine(hs,lineSize,figureNames);
			lineSize.width = 0;
			lineSize.height=ret;
			lineSize.height += getGridY();
		}		
		
		for (int i = 0; i < figures.size(); i++) {
			IComponent tf = (IComponent)figures.get(i);
			IFigure fig = getFigure(tf);
			if(fig != null){
				Rectangle r = (Rectangle)figureDim.get(tf);
				if(r!=null)fig.setBounds(r);
			}
		}
		calculateDim();
	}

	public int empty(int y, TreeSet ts){
		int maxY = 0;
		
		for(Iterator i = ts.iterator(); i.hasNext();){
			Rectangle ry = (Rectangle)i.next();
			if(ry.y>maxY)maxY=ry.y;
		}
		int ok = y;
		boolean q = false;
		for(int i=y; i<=maxY; i=i+getGridY()){
			for(Iterator it = ts.iterator(); it.hasNext();){
				Rectangle ry = (Rectangle)it.next();
				if(ry.contains(ry.x+ry.width/2,i+ry.height/2)){
					q = false;
					break;
				}else{
					q = true;
				}
			}
			
			ok = i;
			if(q)break;
		}
		return ok;
	}

	public int layoutLine(Hashtable h, Dimension fSize, Hashtable names){
		int ret = fSize.height;
		int www=0;
		if(h.isEmpty())return ret;
		Dimension s = new Dimension(fSize);

		TreeSet<Object> ts = new TreeSet<Object>(new WeightComparator(h));
		
		for (Iterator i = h.keySet().iterator(); i.hasNext();) {
			String name = (String)i.next();
			ts.add(name);	
		}
		
		for (Iterator i = ts.iterator(); i.hasNext();) {	
			String name = (String)i.next();
			Hashtable ht = (Hashtable)h.get(name);
			
			IComponent tf = (IComponent)names.get(name);
			if(tf!=null){
				IFigure fig = getFigure(tf);
				if(fig != null){
					if(figureDim.containsKey(tf)){
						figureDim.remove(tf);
					}
					www = 20;
					//if(s.width == 0) www=40;
					figureDim.put(tf,new Rectangle(new Point(s.width*getGridX()+border,s.height+getGridY()+border+s.width*20),new Dimension(figureW,figureH)));
				
					//s.height += getGridY()+www;
					s.width++;
					ret = layoutLine(ht,s,names);
				}else continue;
			}
			
			s.height += getGridY()+www;
			s.width = fSize.width;
			int ss = Math.max(s.height,ret);
			s.height = ss;
		}
		
		return s.height;
	}
	
	public Hashtable<String,Hashtable> getChilds(IComponent tf, Vector figures, Set<IComponent> usedFigures){
		usedFigures.add(tf);
		Hashtable<String,Hashtable> childs = new Hashtable<String,Hashtable>();
		for (int i = 0; i < figures.size(); i++) {
			IComponent tf1 = (IComponent)figures.get(i);
			if(usedFigures.contains(tf1)) continue;
			String ex = tf1.getExtends();
			if(tf.getName().equals(ex)){
				childs.put(tf1.getName(),getChilds(tf1,figures, usedFigures));			
			}
		}
		return childs;
	}
	
	public int getWeight(Hashtable ht){
		int j=ht.size();
		for (Iterator i = ht.keySet().iterator(); i.hasNext();) {
			Hashtable h = (Hashtable)ht.get(i.next());
			if(h!=null){
				j = j+getWeight(h);
			}
		}
		return j;
	}
	
	public TreeSet createLine(Vector figures, Hashtable figureNames){
		TreeSet<Object> lines = new TreeSet<Object>(new WeightComparator());
		Set<IComponent> usedFigures = new HashSet<IComponent>();
		
		for (int i = 0; i < figures.size(); i++) {
			IComponent tf = (IComponent)figures.get(i);
//			String name = 
				tf.getName();
			String ex = tf.getExtends();
			if(ex==null||"".equals(ex)){
				Hashtable<String,Hashtable<String,Hashtable>> line = new Hashtable<String,Hashtable<String,Hashtable>>();
				line.put(tf.getName(),getChilds(tf,figures, usedFigures));
				lines.add(line);
			}else{
				Object o = figureNames.get(ex);
				if(o==null){
					Hashtable<String,Hashtable<String,Hashtable>> line = new Hashtable<String,Hashtable<String,Hashtable>>();
					line.put(tf.getName(),getChilds(tf,figures,usedFigures));
					lines.add(line);
				}
			}
		}
		while(usedFigures.size() < figures.size()) {
			IComponent tf = null;
			for (int i = 0; i < figures.size() && tf == null; i++) {
				tf = (IComponent)figures.get(i);
				if(usedFigures.contains(tf)) tf = null;
			}
			if(tf == null) break;
			Hashtable<String,Hashtable<String,Hashtable>> line = new Hashtable<String,Hashtable<String,Hashtable>>();
			line.put(tf.getName(),getChilds(tf,figures, usedFigures));
			lines.add(line);
		}
		return lines;
	}
	
	public class WeightComparator implements Comparator<Object> {
		Hashtable table;
		
		public WeightComparator(Hashtable table){
			this.table = table; 
		}
		
		public WeightComparator(){}
		
		public int compare(Object o1, Object o2){
			int a = 0;
			int b = 0; 
			if(o1 instanceof Hashtable&&o2 instanceof Hashtable){
				a = getWeight((Hashtable)o1);
				b = getWeight((Hashtable)o2);
			}else{
				a = getWeight((Hashtable)this.table.get(o1));
				b = getWeight((Hashtable)this.table.get(o2));
			} 
			return (a <= b) ? 1 : -1;
		}
		public boolean equals(Object obj){
			return true;
		}
	}
}
