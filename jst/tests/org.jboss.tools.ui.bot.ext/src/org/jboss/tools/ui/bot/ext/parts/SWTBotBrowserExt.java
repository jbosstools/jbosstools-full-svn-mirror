package org.jboss.tools.ui.bot.ext.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.hamcrest.SelfDescribing;

/**
 * browser component
 * @author lzoubek
 *
 */
public class SWTBotBrowserExt extends AbstractSWTBotControl<Browser> {

	/**
	 * internal ProgressListener, which listens progress of loading page in browser
	 * @author lzoubek
	 *
	 */
	class PListener implements ProgressListener {
		private final SWTBotBrowserExt browser;
		private boolean done=true;
		public PListener(SWTBotBrowserExt b) {
			browser=b;
		}
		public synchronized boolean isDone() {
			return done;
		}

		public synchronized void setDone(boolean done) {
			this.done = done;
		}

		public void changed(ProgressEvent event) {
		}

		public void completed(ProgressEvent event) {
			setDone(true);
			browser.widget.removeProgressListener(this);
		}

	}

	private final PListener pl;
	
	public SWTBotBrowserExt(Browser w, SelfDescribing description)
			throws WidgetNotFoundException {
		super(w, description);
		pl = new PListener(this);
	}

	public SWTBotBrowserExt(Browser w) {
		super(w);
		pl = new PListener(this);
	}
	/**
	 * gets 'href' attribute of link from currently loaded 
	 * @param title of link
	 * @return URL or null when no such link found
	 */
	public String getLink(String title) {
		Pattern regex = Pattern.compile("\\<a.*href=[\"|\'](\\S+)[\"|\'].*\\>(\\S+)\\<\\/a\\>",Pattern.CASE_INSENSITIVE);		
		for (String link : getLinks(getText()))  {
			Matcher m = regex.matcher(link);
			if (m.find()) {
				if (title.equalsIgnoreCase(m.group(2))) {
					return m.group(1);
				}
			}
		}
		return null;
	}
	private static List<String> getLinks(String htmlText) {
		List<String> list = new ArrayList<String>();
		String text = htmlText.toLowerCase();
		int index = 0;
		while (index<htmlText.length()) {
			int linkBegin=text.indexOf("<a", index);
			int linkEnd=text.indexOf("</a>",index);
			if (linkBegin>=0 && linkEnd>0) {
				list.add(htmlText.substring(linkBegin, linkEnd+4));
				index+=linkEnd-index+1;
			}
			else {
				break;
			}
			
		}
		return list;
	}
	/**
	 * navigates browser to URL found under link's href with given title
	 * @param title of link to follow
	 * @return true if such link was found and {@link #goURL(String)} was invoked
	 */
	public boolean followLink(String title) {		
		String link = getLink(title);
		if (link==null) return false;
		goURL(link);
		return true;
	}
	public void back() {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				pl.setDone(false);
				widget.addProgressListener(pl);
				if (widget.isBackEnabled()) {					
					if (!widget.back()) {
						pl.setDone(true);
						widget.removeProgressListener(pl);	
					}
				}

			}
		});
	}
	/**
	 * loads given URI into browser, page is loaded asynchronously (see {@link #isPageLoaded()})
	 * 
	 * @param uri
	 */
	public void goURL(final String uri) {		
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				pl.setDone(false);
				widget.addProgressListener(pl);
				if (!widget.setUrl(uri)) {
					pl.setDone(true);
					widget.removeProgressListener(pl);
				}

			}
		});

	}
	/**
	 * 
	 * @return true by default or when page was completely loaded by browser 
	 * = asynchronous page load invoked by {@link #goURL(String)} was finished 
	 */
	public boolean isPageLoaded() {
		return pl.isDone();
	}
	/**
	 * refreshes current page
	 */
	public void refresh() {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				widget.refresh();
			}
		});
	}

}
