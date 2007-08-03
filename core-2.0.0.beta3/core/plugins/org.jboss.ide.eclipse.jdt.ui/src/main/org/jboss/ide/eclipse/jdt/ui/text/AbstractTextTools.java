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
package org.jboss.ide.eclipse.jdt.ui.text;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.jboss.ide.eclipse.jdt.ui.editors.ColorManager;
import org.jboss.ide.eclipse.jdt.ui.preferences.ITextStylePreferences;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AbstractTextTools
{
   /** The color manager */
   protected ColorManager colorManager;

   /** The preference store */
   protected IPreferenceStore store;

   private String[] backgroundPropertyNames;

   private String[] foregroundPropertyNames;

   private IPropertyChangeListener listener;

   private String[] properties;

   private String[] stylePropertyNames;

   private Map tokens;

   /**
    * Creates a new text tools collection.
    *
    * @param store       Description of the Parameter
    * @param properties  Description of the Parameter
    */
   public AbstractTextTools(IPreferenceStore store, String[] properties)
   {
      this.store = store;
      this.properties = properties;

      colorManager = new ColorManager();

      tokens = new HashMap();

      int length = properties.length;

      foregroundPropertyNames = new String[length];
      backgroundPropertyNames = new String[length];
      stylePropertyNames = new String[length];

      for (int i = 0; i < length; i++)
      {
         String property = properties[i];

         String foreground = property + ITextStylePreferences.SUFFIX_FOREGROUND;
         String background = property + ITextStylePreferences.SUFFIX_BACKGROUND;
         String style = property + ITextStylePreferences.SUFFIX_STYLE;

         foregroundPropertyNames[i] = foreground;
         backgroundPropertyNames[i] = background;
         stylePropertyNames[i] = style;

         RGB rgb;

         rgb = getColor(store, foreground);
         if (rgb != null)
         {
            colorManager.bindColor(foreground, rgb);
         }

         rgb = getColor(store, background);
         if (rgb != null)
         {
            colorManager.bindColor(background, rgb);
         }

         tokens.put(property, new Token(new TextAttribute(colorManager.getColor(foreground), colorManager
               .getColor(background), getStyle(store, style))));
      }

      listener = new IPropertyChangeListener()
      {
         public void propertyChange(PropertyChangeEvent event)
         {
            adaptToPreferenceChange(event);
         }
      };

      store.addPropertyChangeListener(listener);
   }

   /**
    * Determines whether the preference change encoded by the given event
    * changes the behavior of one its contained components.
    *
    * @param event  the event to be investigated
    * @return       <code>true</code> if event causes a behavioral change
    */
   public boolean affectsBehavior(PropertyChangeEvent event)
   {
      return (indexOf(event.getProperty()) >= 0);
   }

   /** Disposes all the individual tools of this tools collection. */
   public void dispose()
   {
      if (store != null)
      {
         store.removePropertyChangeListener(listener);

         store = null;
         listener = null;
      }

      if (colorManager != null)
      {
         colorManager.dispose();

         colorManager = null;
      }

      tokens = null;

      properties = null;
      foregroundPropertyNames = null;
      backgroundPropertyNames = null;
      stylePropertyNames = null;
   }

   /**
    * Returns the color manager which is used to manage any XML-specific
    * colors needed for such things like syntax highlighting.
    *
    * @return   the color manager to be used for XML text viewers
    */
   public ColorManager getColorManager()
   {
      return colorManager;
   }

   /**
    * Adapts the behavior of the contained components to the change
    * encoded in the given event.
    *
    * @param event  the event to whch to adapt
    */
   protected void adaptToPreferenceChange(PropertyChangeEvent event)
   {
      String property = event.getProperty();

      Token token = getToken(property);
      if (token != null)
      {
         if (property.endsWith(ITextStylePreferences.SUFFIX_FOREGROUND)
               || property.endsWith(ITextStylePreferences.SUFFIX_BACKGROUND))
         {
            adaptToColorChange(token, event);
         }
         else if (property.endsWith(ITextStylePreferences.SUFFIX_STYLE))
         {
            adaptToStyleChange(token, event);
         }
      }
   }

   /**
    * Gets the token attribute of the AbstractTextTools object
    *
    * @param key  Description of the Parameter
    * @return     The token value
    */
   protected Token getToken(String key)
   {
      int index = indexOf(key);
      if (index < 0)
      {
         return null;
      }

      return (Token) tokens.get(properties[index]);
   }

   /**
    * Gets the tokens attribute of the AbstractTextTools object
    *
    * @return   The tokens value
    */
   protected Map getTokens()
   {
      return tokens;
   }

   /**
    * Description of the Method
    *
    * @param token  Description of the Parameter
    * @param event  Description of the Parameter
    */
   private void adaptToColorChange(Token token, PropertyChangeEvent event)
   {
      RGB rgb = getColor(event.getNewValue());

      String property = event.getProperty();

      colorManager.unbindColor(property);
      if (rgb != null)
      {
         colorManager.bindColor(property, rgb);
      }

      Object data = token.getData();
      if (data instanceof TextAttribute)
      {
         TextAttribute old = (TextAttribute) data;

         int i = indexOf(property);

         token.setData(new TextAttribute(colorManager.getColor(foregroundPropertyNames[i]), colorManager
               .getColor(backgroundPropertyNames[i]), old.getStyle()));
      }
   }

   /**
    * Description of the Method
    *
    * @param token  Description of the Parameter
    * @param event  Description of the Parameter
    */
   private void adaptToStyleChange(Token token, PropertyChangeEvent event)
   {
      int style = getStyle((String) event.getNewValue());

      Object data = token.getData();
      if (data instanceof TextAttribute)
      {
         TextAttribute old = (TextAttribute) data;
         if (old.getStyle() != style)
         {
            token.setData(new TextAttribute(old.getForeground(), old.getBackground(), style));
         }
      }
   }

   /**
    * Gets the color attribute of the AbstractTextTools object
    *
    * @param store  Description of the Parameter
    * @param key    Description of the Parameter
    * @return       The color value
    */
   private RGB getColor(IPreferenceStore store, String key)
   {
      return getColor(store.getString(key));
   }

   /**
    * Gets the color attribute of the AbstractTextTools object
    *
    * @param value  Description of the Parameter
    * @return       The color value
    */
   private RGB getColor(Object value)
   {
      if (value instanceof RGB)
      {
         return (RGB) value;
      }

      String str = (String) value;
      if (str.length() > 0)
      {
         return StringConverter.asRGB(str);
      }

      return null;
   }

   /**
    * Gets the style attribute of the AbstractTextTools object
    *
    * @param store  Description of the Parameter
    * @param key    Description of the Parameter
    * @return       The style value
    */
   private int getStyle(IPreferenceStore store, String key)
   {
      return getStyle(store.getString(key));
   }

   /**
    * Gets the style attribute of the AbstractTextTools object
    *
    * @param value  Description of the Parameter
    * @return       The style value
    */
   private int getStyle(String value)
   {
      if (value.indexOf(ITextStylePreferences.STYLE_BOLD) >= 0)
      {
         return SWT.BOLD;
      }

      return SWT.NORMAL;
   }

   /**
    * Description of the Method
    *
    * @param property  Description of the Parameter
    * @return          Description of the Return Value
    */
   private int indexOf(String property)
   {
      if (property != null)
      {
         int length = properties.length;

         for (int i = 0; i < length; i++)
         {
            if (property.equals(properties[i]) || property.equals(foregroundPropertyNames[i])
                  || property.equals(backgroundPropertyNames[i]) || property.equals(stylePropertyNames[i]))
            {
               return i;
            }
         }
      }

      return -1;
   }
}
