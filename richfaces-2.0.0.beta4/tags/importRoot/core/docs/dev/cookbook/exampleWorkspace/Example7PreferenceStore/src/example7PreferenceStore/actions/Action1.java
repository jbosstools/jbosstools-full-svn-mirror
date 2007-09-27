package example7PreferenceStore.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import example7PreferenceStore.Example7PreferenceStorePlugin;
import example7PreferenceStore.preferences.PreferencePage1;

public class Action1 implements IObjectActionDelegate, IWorkbenchWindowActionDelegate {

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

	public void run(IAction action) {
		// Here we'll just get our preference store and see if it
		// contains a preference by that name. 
		IPreferenceStore store = Example7PreferenceStorePlugin
			.getDefault().getPreferenceStore();
		boolean contains = store.contains(PreferencePage1.TARGET_PREFIX + "0");
		System.out.println("Does store contain one target? " + contains );
		if( contains ) {
			String s = store.getString(PreferencePage1.TARGET_PREFIX + "0");
			PreferencePage1.NameUriPair pair = new PreferencePage1.NameUriPair(s);
			System.out.println("name: " + pair.getName() + ", uri: " + pair.getUri());
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		
	}

}
