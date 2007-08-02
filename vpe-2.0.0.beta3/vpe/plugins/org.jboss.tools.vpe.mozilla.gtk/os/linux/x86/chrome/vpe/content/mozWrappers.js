/* ***** BEGIN LICENSE BLOCK *****
 * Licensed under Version: MPL 1.1/GPL 2.0/LGPL 2.1
 * Full Terms at http://mozile.mozdev.org/license.html
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Playsophy code (www.playsophy.com).
 *
 * The Initial Developer of the Original Code is Playsophy
 * Portions created by the Initial Developer are Copyright (C) 2002-2003
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * - Christian Stocker (Bitflux)
 *
 * ***** END LICENSE BLOCK ***** */

/*
 * mozWrapper.js v0.52
 * 
 * Global Mozilla singleton that provides access native objects needed in Mozile:
 * - clipboard
 * - caret controller
 * - file dialogs and operations
 * - ... 
 *
 * Access using "mozilla.<methodName>" ex/ "mozilla.getClipboard()"
 *
 * - need to run locally or user needs to explicitly grant a document permission
 * (?: can a web site be set as a secure source?)
 *
 * POST05:
 * - save to file: see mozedit and jslib projects - http://www.bokil.com/downloads/
 * - work on security issue: way to prompt user to change his security settings etc.
 * - preferences service: see if can force a user to be prompted to change a preference ...
 * - review: http://cvs2.oeone.com/index.cgi/penzilla3/apps/navigator/content/navigator/navigator.js?annotate=1.14&sortby=date
 * [lot's of native access code - not OO]
 * - the "select" dialog used for image selection
 * - address printing as well as saving
 * - address ftp and post [ie/ maybe abstraction of "save" for a file]
 */

/*******************************************************************************************************
 * Global object - ideally set up at browser initialization but is only a JS object here.
 *******************************************************************************************************/

var mozilla = new Moz();

function Moz()
{
	this.__clipboard = null;
	try {
		// try enable xpconnect
//alert("mozWrappers:1001");
		netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
//alert("mozWrappers:1002");
	}
	catch(e) // POST05: should really check exception type and not just assume!
	{
//alert("mozWrappers:1003");
		this.__allowedNativeCalls = false;
		return;
	}
	this.__allowedNativeCalls = true;
}

/**
 * May get the Mozilla clipboard if available - otherwise get IFrame clipboard
 */
Moz.prototype.getClipboard = function() 
{
	if(!this.__clipboard) 
	{
		if(this.__allowedNativeCalls)
			this.__clipboard = new MozClipboard();
		else
			this.__clipboard = new NonNativeClipboard();
	}

	return this.__clipboard;
}

/*******************************************************************************************************
 * Non native clipboard: used if native support is not available - only handles
 *
 * POST05: 
 * - Christian's IFrame clipboard: enhance nonNativeClipboard to use an id'ed IFrame to paste too and from
 * a Mozile page from other applications.
 *******************************************************************************************************/

function NonNativeClipboard()
{
	this.__copiedData = null;
}

NonNativeClipboard.prototype.getData = function(dataFlavor)
{
	// return copy of internal contents if any
	if(this.__copiedData)
		return this.__copiedData;
}

NonNativeClipboard.prototype.setData = function(data, dataFlavor)
{
	this.__copiedData = data;
}

/*******************************************************************************************************
 * Moz clipboard
 *
 * Access with call "mozilla.getClipboard()" [ala window.getSelection()]
 * 
 * Two references:
 * - http://www.mozilla.org/xpfe/xptoolkit/introClipDD.html
 * - http://www.xulplanet.com/tutorials/xultu/clipboard.html
 * - http://www.xulplanet.com/references/xpcomref/nsITransferable.html
 * - http://lxr.mozilla.org/seamonkey/source/widget/public/nsIClipboard.idl
 * - http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dnwue/html/ch06f.asp
 *
 * Note: normally would be a method of "window" but window may be created before the Mozile scripts are loaded. 
 *
 * POST05: 
 * - check out gClipboardHelper
 * - may need to return a tuple of {data, dataFlavor} - or take the "downgrade to text" approach.
 * - overlap with domlevel3 in transforming data types etc ... may layer standard implementations over these
 * - copy html - try to get back as text - it fails. Change my impl to downgrade to text ie/ get all as text!
 * - how to get XML as XML (doesn't seem to work!); other formats? Word? [after all Moz's clipboard is a nice
 * portable i/f for clipboard]
 * - http://lxr.mozilla.org/seamonkey/source/xpfe/global/resources/content/nsTransferable.js#301 (seem
 * to be moz specific data flavors - add to list? [found this file late - consider stuff in it for
 * inclusion here]
 * - marry this with support for drag and drop of elements into and out of editable area
 * - ultimately consts for i/fs and services (const nsISupports = Components.interfaces.nsISupports; with
 * proper object wrappers for all the things I need (don't try domlevel3 directly over native)
 * 
 ******************************************************************************************************/

function MozClipboard()
{
	// enable xpconnect
	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');

	// create an object for accessing the native clipboard
	this.clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
}

// POST05: redo as consts (scale's better)
MozClipboard.TEXT_FLAVOR = "text/unicode";
MozClipboard.HTML_FLAVOR = "text/html";

/*
 * Returns content of clipboard
 * 
 * - return requested flavor if available; otherwise return text if available; other return null
 *
 * POST05:
 * - do data downgrading too ie/ if text is not available but text is requested then return another format AS text!
 * (required as if I copy html and then try to get it as text, I get nothing now!)
 */
MozClipboard.prototype.getData = function(dataFlavor)
{
	// enable xpconnect: must do again in this function even though enabled for constructor (seems daft)
	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');

	var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
	if(dataFlavor != MozClipboard.TEXT_FLAVOR)
		trans.addDataFlavor(dataFlavor); // can't share trans: need to do required flavor first or never shows up!
	trans.addDataFlavor(MozClipboard.TEXT_FLAVOR);

	this.clip.getData(trans, this.clip.kGlobalClipboard); // must use global - selection clipboard doesn't work and I don't know why!

	var str = new Object();
    	var len = new Object();

	try 
	{
		trans.getTransferData(dataFlavor, str, len);
	}
	catch(e)
	{
		try 
		{
			trans.getTransferData(MozClipboard.TEXT_FLAVOR, str, len);
		}
		catch(e)
		{
			return null; // nothing we can copy
		}
	}

	str= str.value.QueryInterface(Components.interfaces.nsISupportsString);
	if(str)
		str = (str.data.substring(0,len.value / 2));
	return str;
}

/**
 * Add data to the clipboard: data in the form of a string
 */
MozClipboard.prototype.setData = function(data, dataFlavor)
{
	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');

	// xpcom wrapper for data - note: wstring doesn't seem to work so choose string - seems arbitrary but it works!
	var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
	str.data = data;

	var trans = Components.classes["@mozilla.org/widget/transferable;1"].createInstance(Components.interfaces.nsITransferable);
	trans.addDataFlavor(dataFlavor);
	trans.setTransferData(dataFlavor, str, data.length*2); 

	this.clip.setData(trans, null, this.clip.kGlobalClipboard); // note: I don't know the difference between global and selection but selection works!

	this.clip.forceDataToClipboard(this.clip.kGlobalClipboard);
}

/************************************************************************************
 * use these in the full installation to enable or disable caret browsing
 * these are global across all tabs in the
 * browser
 * 
 * POST05: 
 * - one of many user preferences - could just generalize as a user preference
 * setter. Maybe set security setting here?
 * ex/ mozilla.getService(Moz.PREFERENCE_SERVICE)
 * - fit into notion of user designating "trusted sites": is there a way
 * to designate this in a constant that the user could set?
 ************************************************************************************/

Moz.prototype.enableCaretBrowsing = function()
{
	if(!this.__allowedNativeCalls)
		return;
	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
	var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefService).getBranch(null);
	if(!prefs.getBoolPref('accessibility.browsewithcaret'))
		prefs.setBoolPref('accessibility.browsewithcaret', true);
}

Moz.prototype.disableCaretBrowsing = function()
{
	if(!this.__allowedNativeCalls)
		return;
	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
	var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefService).getBranch(null);
	if(prefs.getBoolPref('accessibility.browsewithcaret'))
		prefs.setBoolPref('accessibility.browsewithcaret', false);
}

/***********************************************************************************
 * MozFilePicker - why make up your own file picker when mozilla makes one
 * available?
 * 
 * POST05:
 * - http://www.xulplanet.com/tutorials/xultu/filedialog.html
 * - how to turn off extra prompt that says "do you want to save over a file ..."
 * - only supports save dialog now - expand to support choosing images etc
 * - add "dialog" to the name (look at other Mozilla GUIs that can be reused)
 ***********************************************************************************/

Moz.prototype.createFilePicker = function(mode, prompt) 
{
	if(!this.__allowedNativeCalls)
		return null;
	return new MozFilePicker(mode, prompt);
}

/**
 * POST05:
 * - add support for more modes
 */
function MozFilePicker(mode, prompt)
{
	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
	this.nsIFilePicker = Components.interfaces.nsIFilePicker;
	this.fp = Components.classes["@mozilla.org/filepicker;1"].createInstance(this.nsIFilePicker);
	if(mode == MozFilePicker.MODE_SAVE)
		this.fp.init(window, prompt, this.nsIFilePicker.modeSave);	
	this.__file = null;
}

MozFilePicker.MODE_SAVE = 0;
MozFilePicker.FILTER_HTML = 1;

/**
 * POST05:
 * - add more filters ex/ for image types
 */
MozFilePicker.prototype.addFilter = function(filterType)
{
	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');

	if(filterType == MozFilePicker.FILTER_HTML)
		this.fp.appendFilters(this.nsIFilePicker.filterHTML);
}

MozFilePicker.prototype.promptUser = function()
{
	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');

	var result = this.fp.show();
	if(result == this.nsIFilePicker.returnCancel) // replace or ok are other values
		return false;

	this.__file = new MozLocalFile(this.fp.file);
	return true;
}

MozFilePicker.prototype.__defineGetter__(
	"file",
	function() { return this.__file; }
);

/***********************************************************************************************************
 * MozFile
 *
 * POST05:
 * - expand this very basic file wrapper or consider using jslib but that doesn't seem
 * to match either Mozilla's nuance or DOM's format/style
 * - how to preserve relative "links" in a file ie/ style refs etc. 
 * - add things like: this.__native.isWritable() and check for this before writing data
 ***********************************************************************************************************/

function MozLocalFile(nativeFile)
{
	this.__native = nativeFile;
}

MozLocalFile.prototype.write = function(data)
{
	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');

	var fos = Components.classes["@mozilla.org/network/file-output-stream;1"].createInstance(Components.interfaces.nsIFileOutputStream);
	fos.init(this.__native, -1, -1, 0);
	var result = fos.write(data, data.length); // result = bytes written
	fos.flush();
	fos.close();
}