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
package org.jboss.ide.eclipse.jdt.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * An overlaying preference store.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class OverlayPreferenceStore implements IPreferenceStore
{
   /** Description of the Field */
   protected IPreferenceStore parent;

   /** Description of the Field */
   protected IPreferenceStore store;

   private PropertyListener fPropertyListener;

   private PreferenceDescriptor[] keys;

   /**
    *Constructor for the OverlayPreferenceStore object
    *
    * @param parent       Description of the Parameter
    * @param overlayKeys  Description of the Parameter
    */
   public OverlayPreferenceStore(IPreferenceStore parent, PreferenceDescriptor[] overlayKeys)
   {
      this.parent = parent;
      this.keys = overlayKeys;

      store = new PreferenceStore();
   }

   /**
    * Adds a feature to the PropertyChangeListener attribute of the OverlayPreferenceStore object
    *
    * @param listener  The feature to be added to the PropertyChangeListener attribute
    */
   public void addPropertyChangeListener(IPropertyChangeListener listener)
   {
      store.addPropertyChangeListener(listener);
   }

   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @return      Description of the Return Value
    */
   public boolean contains(String name)
   {
      return store.contains(name);
   }

   /**
    * Description of the Method
    *
    * @param name      Description of the Parameter
    * @param oldValue  Description of the Parameter
    * @param newValue  Description of the Parameter
    */
   public void firePropertyChangeEvent(String name, Object oldValue, Object newValue)
   {
      store.firePropertyChangeEvent(name, oldValue, newValue);
   }

   /**
    * Gets the boolean attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The boolean value
    */
   public boolean getBoolean(String name)
   {
      return store.getBoolean(name);
   }

   /**
    * Gets the defaultBoolean attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The defaultBoolean value
    */
   public boolean getDefaultBoolean(String name)
   {
      return store.getDefaultBoolean(name);
   }

   /**
    * Gets the defaultDouble attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The defaultDouble value
    */
   public double getDefaultDouble(String name)
   {
      return store.getDefaultDouble(name);
   }

   /**
    * Gets the defaultFloat attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The defaultFloat value
    */
   public float getDefaultFloat(String name)
   {
      return store.getDefaultFloat(name);
   }

   /**
    * Gets the defaultInt attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The defaultInt value
    */
   public int getDefaultInt(String name)
   {
      return store.getDefaultInt(name);
   }

   /**
    * Gets the defaultLong attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The defaultLong value
    */
   public long getDefaultLong(String name)
   {
      return store.getDefaultLong(name);
   }

   /**
    * Gets the defaultString attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The defaultString value
    */
   public String getDefaultString(String name)
   {
      return store.getDefaultString(name);
   }

   /**
    * Gets the double attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The double value
    */
   public double getDouble(String name)
   {
      return store.getDouble(name);
   }

   /**
    * Gets the float attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The float value
    */
   public float getFloat(String name)
   {
      return store.getFloat(name);
   }

   /**
    * Gets the int attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The int value
    */
   public int getInt(String name)
   {
      return store.getInt(name);
   }

   /**
    * Gets the long attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The long value
    */
   public long getLong(String name)
   {
      return store.getLong(name);
   }

   /**
    * Gets the string attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The string value
    */
   public String getString(String name)
   {
      return store.getString(name);
   }

   /**
    * Gets the default attribute of the OverlayPreferenceStore object
    *
    * @param name  Description of the Parameter
    * @return      The default value
    */
   public boolean isDefault(String name)
   {
      return store.isDefault(name);
   }

   /** Description of the Method */
   public void load()
   {
      for (int i = 0; i < keys.length; i++)
      {
         loadProperty(parent, keys[i], store, true);
      }
   }

   /** Description of the Method */
   public void loadDefaults()
   {
      for (int i = 0; i < keys.length; i++)
      {
         setToDefault(keys[i].key);
      }
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean needsSaving()
   {
      return store.needsSaving();
   }

   /** Description of the Method */
   public void propagate()
   {
      for (int i = 0; i < keys.length; i++)
      {
         propagateProperty(store, keys[i], parent);
      }
   }

   /**
    * Description of the Method
    *
    * @param name   Description of the Parameter
    * @param value  Description of the Parameter
    */
   public void putValue(String name, String value)
   {
      if (covers(name))
      {
         store.putValue(name, value);
      }
   }

   /**
    * Description of the Method
    *
    * @param listener  Description of the Parameter
    */
   public void removePropertyChangeListener(IPropertyChangeListener listener)
   {
      store.removePropertyChangeListener(listener);
   }

   /**
    * Sets the default attribute of the OverlayPreferenceStore object
    *
    * @param name   The new default value
    * @param value  The new default value
    */
   public void setDefault(String name, double value)
   {
      if (covers(name))
      {
         store.setDefault(name, value);
      }
   }

   /**
    * Sets the default attribute of the OverlayPreferenceStore object
    *
    * @param name   The new default value
    * @param value  The new default value
    */
   public void setDefault(String name, float value)
   {
      if (covers(name))
      {
         store.setDefault(name, value);
      }
   }

   /**
    * Sets the default attribute of the OverlayPreferenceStore object
    *
    * @param name   The new default value
    * @param value  The new default value
    */
   public void setDefault(String name, int value)
   {
      if (covers(name))
      {
         store.setDefault(name, value);
      }
   }

   /**
    * Sets the default attribute of the OverlayPreferenceStore object
    *
    * @param name   The new default value
    * @param value  The new default value
    */
   public void setDefault(String name, long value)
   {
      if (covers(name))
      {
         store.setDefault(name, value);
      }
   }

   /**
    * Sets the default attribute of the OverlayPreferenceStore object
    *
    * @param name   The new default value
    * @param value  The new default value
    */
   public void setDefault(String name, String value)
   {
      if (covers(name))
      {
         store.setDefault(name, value);
      }
   }

   /**
    * Sets the default attribute of the OverlayPreferenceStore object
    *
    * @param name   The new default value
    * @param value  The new default value
    */
   public void setDefault(String name, boolean value)
   {
      if (covers(name))
      {
         store.setDefault(name, value);
      }
   }

   /**
    * Sets the toDefault attribute of the OverlayPreferenceStore object
    *
    * @param name  The new toDefault value
    */
   public void setToDefault(String name)
   {
      store.setToDefault(name);
   }

   /**
    * Sets the value attribute of the OverlayPreferenceStore object
    *
    * @param name   The new value value
    * @param value  The new value value
    */
   public void setValue(String name, double value)
   {
      if (covers(name))
      {
         store.setValue(name, value);
      }
   }

   /**
    * Sets the value attribute of the OverlayPreferenceStore object
    *
    * @param name   The new value value
    * @param value  The new value value
    */
   public void setValue(String name, float value)
   {
      if (covers(name))
      {
         store.setValue(name, value);
      }
   }

   /**
    * Sets the value attribute of the OverlayPreferenceStore object
    *
    * @param name   The new value value
    * @param value  The new value value
    */
   public void setValue(String name, int value)
   {
      if (covers(name))
      {
         store.setValue(name, value);
      }
   }

   /**
    * Sets the value attribute of the OverlayPreferenceStore object
    *
    * @param name   The new value value
    * @param value  The new value value
    */
   public void setValue(String name, long value)
   {
      if (covers(name))
      {
         store.setValue(name, value);
      }
   }

   /**
    * Sets the value attribute of the OverlayPreferenceStore object
    *
    * @param name   The new value value
    * @param value  The new value value
    */
   public void setValue(String name, String value)
   {
      if (covers(name))
      {
         store.setValue(name, value);
      }
   }

   /**
    * Sets the value attribute of the OverlayPreferenceStore object
    *
    * @param name   The new value value
    * @param value  The new value value
    */
   public void setValue(String name, boolean value)
   {
      if (covers(name))
      {
         store.setValue(name, value);
      }
   }

   /** Description of the Method */
   public void start()
   {
      if (fPropertyListener == null)
      {
         fPropertyListener = new PropertyListener();
         parent.addPropertyChangeListener(fPropertyListener);
      }
   }

   /** Description of the Method */
   public void stop()
   {
      if (fPropertyListener != null)
      {
         parent.removePropertyChangeListener(fPropertyListener);
         fPropertyListener = null;
      }
   }

   /**
    * Description of the Method
    *
    * @param key  Description of the Parameter
    * @return     Description of the Return Value
    */
   PreferenceDescriptor findOverlayKey(String key)
   {
      for (int i = 0; i < keys.length; i++)
      {
         if (keys[i].key.equals(key))
         {
            return keys[i];
         }
      }

      return null;
   }

   /**
    * Description of the Method
    *
    * @param orgin   Description of the Parameter
    * @param key     Description of the Parameter
    * @param target  Description of the Parameter
    */
   void propagateProperty(IPreferenceStore orgin, PreferenceDescriptor key, IPreferenceStore target)
   {
      if (orgin.isDefault(key.key))
      {
         if (!target.isDefault(key.key))
         {
            target.setToDefault(key.key);
         }

         return;
      }

      PreferenceDescriptor.Type d = key.type;
      if (PreferenceDescriptor.BOOLEAN == d)
      {
         boolean originValue = orgin.getBoolean(key.key);
         boolean targetValue = target.getBoolean(key.key);
         if (targetValue != originValue)
         {
            target.setValue(key.key, originValue);
         }
      }
      else if (PreferenceDescriptor.DOUBLE == d)
      {
         double originValue = orgin.getDouble(key.key);
         double targetValue = target.getDouble(key.key);
         if (targetValue != originValue)
         {
            target.setValue(key.key, originValue);
         }
      }
      else if (PreferenceDescriptor.FLOAT == d)
      {
         float originValue = orgin.getFloat(key.key);
         float targetValue = target.getFloat(key.key);
         if (targetValue != originValue)
         {
            target.setValue(key.key, originValue);
         }
      }
      else if (PreferenceDescriptor.INT == d)
      {
         int originValue = orgin.getInt(key.key);
         int targetValue = target.getInt(key.key);
         if (targetValue != originValue)
         {
            target.setValue(key.key, originValue);
         }
      }
      else if (PreferenceDescriptor.LONG == d)
      {
         long originValue = orgin.getLong(key.key);
         long targetValue = target.getLong(key.key);
         if (targetValue != originValue)
         {
            target.setValue(key.key, originValue);
         }
      }
      else if (PreferenceDescriptor.STRING == d)
      {
         String originValue = orgin.getString(key.key);
         String targetValue = target.getString(key.key);
         if (targetValue != null && originValue != null && !targetValue.equals(originValue))
         {
            target.setValue(key.key, originValue);
         }
      }
   }

   /**
    * Description of the Method
    *
    * @param key  Description of the Parameter
    * @return     Description of the Return Value
    */
   private boolean covers(String key)
   {
      return (findOverlayKey(key) != null);
   }

   /**
    * Description of the Method
    *
    * @param orgin                Description of the Parameter
    * @param key                  Description of the Parameter
    * @param target               Description of the Parameter
    * @param forceInitialization  Description of the Parameter
    */
   private void loadProperty(IPreferenceStore orgin, PreferenceDescriptor key, IPreferenceStore target,
         boolean forceInitialization)
   {
      PreferenceDescriptor.Type d = key.type;
      if (PreferenceDescriptor.BOOLEAN == d)
      {
         if (forceInitialization)
         {
            target.setValue(key.key, true);
         }
         target.setValue(key.key, orgin.getBoolean(key.key));
         target.setDefault(key.key, orgin.getDefaultBoolean(key.key));
      }
      else if (PreferenceDescriptor.DOUBLE == d)
      {
         if (forceInitialization)
         {
            target.setValue(key.key, 1.0D);
         }
         target.setValue(key.key, orgin.getDouble(key.key));
         target.setDefault(key.key, orgin.getDefaultDouble(key.key));
      }
      else if (PreferenceDescriptor.FLOAT == d)
      {
         if (forceInitialization)
         {
            target.setValue(key.key, 1.0F);
         }
         target.setValue(key.key, orgin.getFloat(key.key));
         target.setDefault(key.key, orgin.getDefaultFloat(key.key));
      }
      else if (PreferenceDescriptor.INT == d)
      {
         if (forceInitialization)
         {
            target.setValue(key.key, 1);
         }
         target.setValue(key.key, orgin.getInt(key.key));
         target.setDefault(key.key, orgin.getDefaultInt(key.key));
      }
      else if (PreferenceDescriptor.LONG == d)
      {
         if (forceInitialization)
         {
            target.setValue(key.key, 1L);
         }
         target.setValue(key.key, orgin.getLong(key.key));
         target.setDefault(key.key, orgin.getDefaultLong(key.key));
      }
      else if (PreferenceDescriptor.STRING == d)
      {
         if (forceInitialization)
         {
            target.setValue(key.key, "1");//$NON-NLS-1$
         }
         target.setValue(key.key, orgin.getString(key.key));
         target.setDefault(key.key, orgin.getDefaultString(key.key));
      }
   }

   /**
    * Description of the Class
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   private class PropertyListener implements IPropertyChangeListener
   {
      /**
       * Description of the Method
       *
       * @param event  Description of the Parameter
       */
      public void propertyChange(PropertyChangeEvent event)
      {
         PreferenceDescriptor key = findOverlayKey(event.getProperty());
         if (key != null)
         {
            propagateProperty(parent, key, store);
         }
      }
   }
}
