/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import java.util.Arrays;

import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class CompletionProposalPopup
{
   /** Description of the Field */
   protected Shell fParentShell, fProposalShell;

   /** Description of the Field */
   protected TableViewer fProposalTableViewer;

   /** Description of the Field */
   protected String[] helptexts;

   /** Description of the Field */
   protected ListenerList listenerList = new ListenerList();

   /** Description of the Field */
   protected Object[] proposals = new Object[0];

   /** Description of the Field */
   public final static int HEIGHT_IN_ITEMS = 5;

   /** Description of the Field */
   public final static char KEY_ACTION_CHAR = 13;

   /** Description of the Field */
   public final static int WIDTH_IN_PIXELS = 150;

   /**
    * Constructor for CompletionProposalPopup.
    *
    * @param parentShell  Description of the Parameter
    */
   public CompletionProposalPopup(Shell parentShell)
   {
      if (parentShell == null)
      {
         throw new IllegalArgumentException();
      }
      this.fParentShell = parentShell;
   }

   /**
    * Adds a feature to the ProposalListener attribute of the CompletionProposalPopup object
    *
    * @param listener  The feature to be added to the ProposalListener attribute
    */
   public void addProposalListener(IProposalListener listener)
   {
      listenerList.add(listener);
   }

   /**
    * Returns the helptexts.
    *
    * @return   String[]
    */
   public String[] getHelptexts()
   {
      return helptexts;
   }

   /**
    * Returns the parentShell.
    *
    * @return   Shell
    */
   public Shell getParentShell()
   {
      return fParentShell;
   }

   /**
    * Gets the proposalListener attribute of the CompletionProposalPopup object
    *
    * @return   The proposalListener value
    */
   public IProposalListener[] getProposalListener()
   {
      return (IProposalListener[]) Arrays.asList(listenerList.getListeners()).toArray(
            new IProposalListener[listenerList.size()]);
   }

   /**
    * Method getProposalShell.
    *
    * @return   The proposalShell value
    */
   public Shell getProposalShell()
   {
      return fProposalShell;
   }

   /**
    * Returns the proposals.
    *
    * @return   Object[]
    */
   public Object[] getProposals()
   {
      return proposals;
   }

   /**
    * Method getViewer.
    *
    * @return   TableViewer
    */
   public TableViewer getTableViewer()
   {
      return fProposalTableViewer;
   }

   /** Description of the Method */
   public void removeAllProposalListeners()
   {
      listenerList.clear();
   }

   /**
    * Description of the Method
    *
    * @param listener  Description of the Parameter
    */
   public void removeProposalListener(IProposalListener listener)
   {
      listenerList.remove(listener);
   }

   /**
    * Description of the Method
    *
    * @param proposals  Description of the Parameter
    * @param helptexts  Description of the Parameter
    * @param point      Description of the Parameter
    * @param sorter     Description of the Parameter
    */
   public void showFreshPopup(Object[] proposals, String[] helptexts, Point point, ViewerSorter sorter)
   {
      if (proposals == null || proposals.length == 0)
      {
         return;
      }
      if (helptexts != null && proposals.length != helptexts.length)
      {
         throw new IllegalArgumentException();
      }
      if (fProposalShell != null && !fProposalShell.isDisposed())
      {
         fProposalShell.dispose();
      }
      init();
      fProposalShell.setLocation(point);
      fProposalShell.setVisible(true);
      fProposalTableViewer.setSorter(sorter);
      this.proposals = proposals;
      this.helptexts = helptexts;
      fProposalTableViewer.refresh();
      fProposalTableViewer.setSelection(new StructuredSelection(fProposalTableViewer.getElementAt(0)));
   }

   /** Description of the Method */
   protected void doCloseWithoutProposal()
   {
      fireChange(null);
      fProposalShell.dispose();
   }

   /** Description of the Method */
   protected void doSelection()
   {
      IStructuredSelection selection = ((IStructuredSelection) fProposalTableViewer.getSelection());
      if (!selection.isEmpty())
      {
         fProposalShell.dispose();
         fireChange(selection.toArray());
      }
   }

   /** Description of the Method */
   protected void init()
   {
      fProposalShell = new Shell(fParentShell, SWT.ON_TOP | SWT.RESIZE);
      GridLayout layout = new GridLayout();
      layout.marginWidth = 0;
      layout.marginHeight = 0;
      fProposalShell.setLayout(layout);
      fProposalTableViewer = new TableViewer(fProposalShell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
      GridData data = new GridData(GridData.FILL_BOTH);
      data.heightHint = fProposalTableViewer.getTable().getItemHeight() * HEIGHT_IN_ITEMS;
      data.widthHint = WIDTH_IN_PIXELS;
      fProposalTableViewer.getTable().setLayoutData(data);
      fProposalTableViewer.setLabelProvider(new LocalLableProvider());
      fProposalTableViewer.setContentProvider(new LocalContentProvider());
      fProposalTableViewer.setInput(new Object());
      fProposalShell.pack();
      fProposalShell.open();
      fProposalTableViewer.getTable().addMouseListener(new MouseAdapter()
      {
         public void mouseDoubleClick(MouseEvent e)
         {
            doSelection();
         }
      });
      fProposalTableViewer.getTable().addFocusListener(new FocusListener()
      {
         public void focusGained(FocusEvent e)
         {
         }

         public void focusLost(FocusEvent e)
         {
            doCloseWithoutProposal();
         }
      });
      fProposalTableViewer.getTable().addKeyListener(new KeyAdapter()
      {
         public void keyPressed(KeyEvent e)
         {
            if (e.character == KEY_ACTION_CHAR)
            {
               doSelection();
            }
         }
      });
      fProposalShell.addDisposeListener(new DisposeListener()
      {
         /**
          * @param e  Description of the Parameter
          * @see      org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
          */
         public void widgetDisposed(DisposeEvent e)
         {
            // System.out.println("DISP " + proposals[0]);
         }
      });
   }

   /**
    * Description of the Method
    *
    * @param proposals  Description of the Parameter
    */
   private void fireChange(Object[] proposals)
   {
      Object[] listeners = listenerList.getListeners();
      if (listeners != null)
      {
         ProposalEvent event = null;
         if (proposals != null)
         {
            event = new ProposalEvent(this, proposals);
         }
         for (int i = 0; i < listeners.length; i++)
         {
            IProposalListener listener = (IProposalListener) listeners[i];
            if (proposals != null)
            {
               listener.handleProposal(event);
            }
            else
            {
               listener.closedWithoutProposals();
            }
         }
      }
   }

   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   17 mai 2003
    */
   protected class LocalContentProvider implements IStructuredContentProvider
   {
      /** Description of the Method */
      public void dispose()
      {
      }

      /**
       * Gets the elements attribute of the LocalContentProvider object
       *
       * @param inputElement  Description of the Parameter
       * @return              The elements value
       */
      public Object[] getElements(Object inputElement)
      {
         return proposals;
      }

      /**
       * Description of the Method
       *
       * @param viewer    Description of the Parameter
       * @param oldInput  Description of the Parameter
       * @param newInput  Description of the Parameter
       */
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }
   }

   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   17 mai 2003
    */
   protected class LocalLableProvider implements ITableLabelProvider
   {
      /**
       * Adds a feature to the Listener attribute of the LocalLableProvider object
       *
       * @param listener  The feature to be added to the Listener attribute
       */
      public void addListener(ILabelProviderListener listener)
      {
      }

      /** Description of the Method */
      public void dispose()
      {
      }

      /**
       * Gets the columnImage attribute of the LocalLableProvider object
       *
       * @param element      Description of the Parameter
       * @param columnIndex  Description of the Parameter
       * @return             The columnImage value
       */
      public Image getColumnImage(Object element, int columnIndex)
      {
         return ImageStore.getImage(element);
      }

      /**
       * Gets the columnText attribute of the LocalLableProvider object
       *
       * @param element      Description of the Parameter
       * @param columnIndex  Description of the Parameter
       * @return             The columnText value
       */
      public String getColumnText(Object element, int columnIndex)
      {
         return element.toString();
      }

      /**
       * Gets the labelProperty attribute of the LocalLableProvider object
       *
       * @param element   Description of the Parameter
       * @param property  Description of the Parameter
       * @return          The labelProperty value
       */
      public boolean isLabelProperty(Object element, String property)
      {
         return false;
      }

      /**
       * Description of the Method
       *
       * @param listener  Description of the Parameter
       */
      public void removeListener(ILabelProviderListener listener)
      {
      }
   }

}
