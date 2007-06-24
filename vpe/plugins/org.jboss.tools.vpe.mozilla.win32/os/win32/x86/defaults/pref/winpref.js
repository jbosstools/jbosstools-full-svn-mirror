/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 2 -*- */
/* ***** BEGIN LICENSE BLOCK *****
 * Version: NPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Netscape Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/NPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is mozilla.org code.
 *
 * The Initial Developer of the Original Code is 
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1998
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or 
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the NPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the NPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

pref("ui.key.menuAccessKeyFocuses", true);

pref("font.name.serif.ar", "Times New Roman");
pref("font.name.sans-serif.ar", "Arial");
pref("font.name.monospace.ar", "Courier New");
pref("font.name.cursive.ar", "Comic Sans MS");

pref("font.name.serif.el", "Times New Roman");
pref("font.name.sans-serif.el", "Arial");
pref("font.name.monospace.el", "Courier New");
pref("font.name.cursive.el", "Comic Sans MS");

pref("font.name.serif.he", "Narkisim");
pref("font.name.sans-serif.he", "Arial");
pref("font.name.monospace.he", "Fixed Miriam Transparent");
pref("font.name.cursive.he", "Gutmann Yad");
pref("font.name-list.serif.he", "Narkisim, David");
pref("font.name-list.monospace.he", "Fixed Miriam Transparent, Miriam Fixed, Rod, Courier New");
pref("font.name-list.cursive.he", "Gutmann Yad, Ktav, Arial");

// For CJK fonts, we list a font twice in name-list, once in the native script and once in English
// because the name of a CJK font returned by Win32 API is beyond our control and depends on
// whether or not Mozilla is run on CJK Win 9x/ME or Win 2k/XP with a CJK locale.
// (see bug 227815)

pref("font.name.serif.ja", "ＭＳ Ｐ明朝"); // "MS PMincho"
pref("font.name.sans-serif.ja", "ＭＳ Ｐゴシック"); // "MS PGothic"
pref("font.name.monospace.ja", "ＭＳ ゴシック"); // "MS Gothic"
pref("font.name-list.serif.ja", "MS PMincho, ＭＳ Ｐ明朝, MS Mincho, MS PGothic, MS Gothic");
pref("font.name-list.sans-serif.ja", "MS PGothic, ＭＳ Ｐゴシック, MS Gothic, MS PMincho, MS Mincho");
pref("font.name-list.monospace.ja", "MS Gothic, ＭＳ ゴシック, MS Mincho, ＭＳ 明朝, MS PGothic, MS PMincho");

pref("font.name.serif.ko", "바탕"); // "Batang" 
pref("font.name.sans-serif.ko", "굴림"); // "Gulim" 
pref("font.name.monospace.ko", "굴림체"); // "GulimChe" 
pref("font.name.cursive.ko", "궁서"); // "Gungseo"

pref("font.name-list.serif.ko", "Batang, 바탕, Gulim, 굴림"); 
pref("font.name-list.sans-serif.ko", "Gulim, 굴림"); 
pref("font.name-list.monospace.ko", "GulimChe, 굴림체"); 
pref("font.name-list.cursive.ko", "Gungseo, 궁서"); 

pref("font.name.serif.th", "Times New Roman");
pref("font.name.sans-serif.th", "Arial");
pref("font.name.monospace.th", "Courier New");
pref("font.name.cursive.th", "Comic Sans MS");

pref("font.name.serif.tr", "Times New Roman");
pref("font.name.sans-serif.tr", "Arial");
pref("font.name.monospace.tr", "Courier New");
pref("font.name.cursive.tr", "Comic Sans MS");

pref("font.name.serif.x-baltic", "Times New Roman");
pref("font.name.sans-serif.x-baltic", "Arial");
pref("font.name.monospace.x-baltic", "Courier New");
pref("font.name.cursive.x-baltic", "Comic Sans MS");

pref("font.name.serif.x-central-euro", "Times New Roman");
pref("font.name.sans-serif.x-central-euro", "Arial");
pref("font.name.monospace.x-central-euro", "Courier New");
pref("font.name.cursive.x-central-euro", "Comic Sans MS");

pref("font.name.serif.x-cyrillic", "Times New Roman");
pref("font.name.sans-serif.x-cyrillic", "Arial");
pref("font.name.monospace.x-cyrillic", "Courier New");
pref("font.name.cursive.x-cyrillic", "Comic Sans MS");

pref("font.name.serif.x-unicode", "Times New Roman");
pref("font.name.sans-serif.x-unicode", "Arial");
pref("font.name.monospace.x-unicode", "Courier New");
pref("font.name.cursive.x-unicode", "Comic Sans MS");

pref("font.name.serif.x-western", "Times New Roman");
pref("font.name.sans-serif.x-western", "Arial");
pref("font.name.monospace.x-western", "Courier New");
pref("font.name.cursive.x-western", "Comic Sans MS");

pref("font.name.serif.zh-CN", "宋体"); //MS Song
pref("font.name.sans-serif.zh-CN", "宋体"); //MS Song
pref("font.name.monospace.zh-CN", "宋体"); //MS Song
pref("font.name-list.serif.zh-CN", "MS Song, 宋体, SimSun");
pref("font.name-list.sans-serif.zh-CN", "MS Song, 宋体, SimSun");
pref("font.name-list.monospace.zh-CN", "MS Song, 宋体, SimSun");

pref("font.name.serif.zh-TW", "細明體"); // "MingLiU" 
pref("font.name.sans-serif.zh-TW", "細明體"); // "MingLiU" 
pref("font.name.monospace.zh-TW", "細明體"); // "MingLiU" 
pref("font.name-list.serif.zh-TW", "MingLiU, 細明體"); 
pref("font.name-list.sans-serif.zh-TW", "MingLiU, 細明體");
pref("font.name-list.monospace.zh-TW", "MingLiU, 細明體");

// hkscsm3u.ttf (HKSCS-2001) :  http://www.microsoft.com/hk/hkscs
pref("font.name.serif.zh-HK", "細明體_HKSCS"); 
pref("font.name.sans-serif.zh-HK", "細明體_HKSCS"); 
pref("font.name.monospace.zh-HK", "細明體_HKSCS"); 
pref("font.name-list.serif.zh-HK", "MingLiu_HKSCS, 細明體_HKSCS, Ming(for ISO10646), MingLiU, 細明體"); 
pref("font.name-list.sans-serif.zh-HK", "MingLiU_HKSCS, 細明體_HKSCS, Ming(for ISO10646), MingLiU, 細明體");  
pref("font.name-list.monospace.zh-HK", "MingLiU_HKSCS,  細明體_HKSCS, Ming(for ISO10646), MingLiU, 細明體");

pref("font.name.serif.x-devanagari", "Mangal");
pref("font.name.sans-serif.x-devanagari", "Raghindi");
pref("font.name.monospace.x-devanagari", "Mangal");
pref("font.name-list.serif.x-devanagari", "Mangal, Raghindi");
pref("font.name-list.monospace.x-devanagari", "Mangal, Raghindi");

pref("font.name.serif.x-tamil", "Latha");
pref("font.name.sans-serif.x-tamil", "Code2000");
pref("font.name.monospace.x-tamil", "Latha");
pref("font.name-list.serif.x-tamil", "Latha, Code2000");
pref("font.name-list.monospace.x-tamil", "Latha, Code2000");

pref("font.default", "serif");
pref("font.size.variable.ar", 16);
pref("font.size.fixed.ar", 13);

pref("font.size.variable.el", 16);
pref("font.size.fixed.el", 13);

pref("font.size.variable.he", 16);
pref("font.size.fixed.he", 13);

pref("font.size.variable.ja", 16);
pref("font.size.fixed.ja", 16);

pref("font.size.variable.ko", 16);
pref("font.size.fixed.ko", 16);

pref("font.size.variable.th", 16);
pref("font.size.fixed.th", 13);

pref("font.size.variable.tr", 16);
pref("font.size.fixed.tr", 13);

pref("font.size.variable.x-baltic", 16);
pref("font.size.fixed.x-baltic", 13);

pref("font.size.variable.x-central-euro", 16);
pref("font.size.fixed.x-central-euro", 13);

pref("font.size.variable.x-cyrillic", 16);
pref("font.size.fixed.x-cyrillic", 13);

pref("font.size.variable.x-devanagari", 16);
pref("font.size.fixed.x-devanagari", 13);

pref("font.size.variable.x-tamil", 16);
pref("font.size.fixed.x-tamil", 13);

pref("font.size.variable.x-unicode", 16);
pref("font.size.fixed.x-unicode", 13);

pref("font.size.variable.x-western", 16);
pref("font.size.fixed.x-western", 13);

pref("font.size.variable.zh-CN", 16);
pref("font.size.fixed.zh-CN", 16);

pref("font.size.variable.zh-TW", 16);
pref("font.size.fixed.zh-TW", 16);

pref("font.size.variable.zh-HK", 16);
pref("font.size.fixed.zh-HK", 16);

pref("font.size.nav4rounding", false);

pref("system.windows.lock_ui.default_mail_client", false);

pref("netinst.profile.show_profile_wizard", true); 

//The following pref is internal to Communicator. Please
//do *not* place it in the docs...
pref("netinst.profile.show_dir_overwrite_msg",  true); 

// Unread mail count timer. Value to be specified in seconds
// default is 5 minutes, i.e., 5 * 60 seconds = 300
pref("mail.windows_xp_integration.unread_count_interval", 300);

// override double-click word selection behavior.
pref("layout.word_select.eat_space_to_next_word", true);

// print_extra_margin enables platforms to specify an extra gap or margin
// around the content of the page for Print Preview only
pref("print.print_extra_margin", 90); // twips (90 twips is an eigth of an inch)

// This indicates whether it should use the native dialog or the XP Dialog
pref("print.use_native_print_dialog", true);

// Locate Java by scanning the Sun JRE installation directory with a minimum version
// Note: Does not scan if security.enable_java is not true
pref("plugin.scan.SunJRE", "1.3");

// Locate plugins by scanning the Adobe Acrobat installation directory with a minimum version
pref("plugin.scan.Acrobat", "5.0");

// Locate plugins by scanning the Quicktime installation directory with a minimum version
pref("plugin.scan.Quicktime", "5.0");

// Locate and scan the Window Media Player installation directory for plugins with a minimum version
pref("plugin.scan.WindowsMediaPlayer", "7.0");

// Locate plugins by the directories specified in the Windows registry for PLIDs
// Which is currently HKLM\Software\MozillaPlugins\xxxPLIDxxx\Path
pref("plugin.scan.plid.all", true);

// Controls the scanning of the Navigator 4.x directory for plugins
// When pref is missing, the default is to pickup popular plugins such as
// Flash, Shockwave, Acrobat, and Quicktime. If set to true, ALL plugins
// will be picked up and if set to false the scan will not happen at all
//pref("plugin.scan.4xPluginFolder", false);

// Help Windows NT, 2000, and XP dialup a RAS connection
// when a network address is unreachable.
pref("network.autodial-helper.enabled", true);

// Pref to control whether we set ddeexec subkeys for the http
// Internet shortcut protocol if we are handling it.  These
// subkeys will be set only while we are running (to avoid the
// problem of Windows showing an alert when it tries to use DDE
// and we're not already running).
pref("advanced.system.supportDDEExec", true);

// Use CP932 compatible map for JIS X 0208
pref("intl.jis0208.map", "CP932");

