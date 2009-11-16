package org.jboss.tools.smooks.configuration.editors;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;

public final class GraphicsConstants {

	public static Color BORDER_CORLOR;

	public static Color TB_BG_CORLOR;
	
	public static Color FONT_COLOR ;

	static {
		FormColors toolkit = new FormColors(Display.getDefault());
		BORDER_CORLOR = toolkit.getBorderColor();
		TB_BG_CORLOR = toolkit.getColor(IFormColors.TB_BG);
		FONT_COLOR = toolkit.getColor(IFormColors.TITLE);
//		toolkit.dispose();
	}

	public static final String IMAGE_JAVA_AMPPING_TASK = "icons/full/obj32/java_mapping_task.jpg";
	
	public static final String IMAGE_DRAG_LINK = "icons/full/obj16/drag_link.gif";
	
	public static final String IMAGE_ADD_TASK_BUTTON = "icons/full/obj16/add_task_button.png";

	public static final String IMAGE_LINK16 = "icons/full/obj16/connection16.gif";

	public static final String IMAGE_LINK24 = "icons/full/obj16/connection24.gif";

	public static final String IMAGE_APPLY_XSL_TASK = "icons/full/obj32/xsl_task.png";

	public static final String IMAGE_APPLY_FREEMARKER_TASK = "icons/full/obj32/freemarker_task.jpg";

	public static final String IMAGE_INPUT_TASK = "icons/full/obj32/input_task.jpg";

	public static final String IMAGE_JAVA_ATTRIBUTE = "icons/full/obj16/att_obj.gif";

	public static final String IMAGE_VALIDATED_TAG = "icons/full/obj16/validated_tag.gif";

	public static final String IMAGE_PROPERTY_SHEET_PAGE = "icons/full/prop_ps.gif";

	public static final String IMAGE_JAVA_OBJECT = "icons/full/obj16/class_obj.gif";

	public static final String IMAGE_PACKAGE_OBJECT = "icons/full/obj16/package_obj.gif";

	public static final String IMAGE_XSL_CHOICE = "icons/full/obj16/xsl_choice.gif";

	public static final String IMAGE_XSL_TEMPLATE_APPLY = "icons/full/obj16/xsl_template_apply.gif";

	public static final String IMAGE_XSL_FOREACH = "icons/full/obj16/xsl_foreach.gif";

	public static final String IMAGE_XSL_SORT = "icons/full/obj16/xsl_sort.gif";

	public static final String IMAGE_XSL_IF = "icons/full/obj16/xsl_if.gif";

	public static final String IMAGE_XSL_STYLESHEET = "icons/full/obj16/xsl_stylesheet.gif";

	public static final String IMAGE_XSL_TEMPLATE = "icons/full/obj16/xsl_template.gif";

	public static final String IMAGE_OVR_ERROR = "icons/full/ovr16/error_co.gif";

	public static final String IMAGE_OVR_WARING = "icons/full/ovr16/warning_co.gif";

	public static final String IMAGE_JAVA_INTERFACE = "icons/full/obj16/int_obj.gif";

	public static final String IMAGE_JAVA_COLLECTION = "icons/full/obj16/javalistmodel.gif";

	public static final String IMAGE_JAVA_BEAN = "icons/full/obj16/java_bean.png";

	public static final String IMAGE_INPUT_DATA_HEADER = "icons/full/obj16/input_data_header.gif";

	public static final String IMAGE_JAVA_ARRAY = "icons/full/obj16/javaarraymodel.gif";

	public static final String IMAGE_CHECKBOX_CHECK = "icons/full/obj16/checkbox_checked.gif";

	public static final String IMAGE_CHECKBOX_UNCHECK = "icons/full/obj16/checkbox_uncheck.gif";

	public static final String JAVA_PROPERTY_ICON_PATH = IMAGE_JAVA_ATTRIBUTE;

	public static final String JAVA_PROPERTY_ICON = JAVA_PROPERTY_ICON_PATH;

	public static final String IMAGE_PATH_ERROR = "icons/full/obj16/error_obj.gif";

	public static final String IMAGE_PATH_WARNING = "icons/full/obj16/warning_obj.gif";

	public static final String IMAGE_PATH_BLANK = "icons/full/obj16/blank.gif";

	public static final String IMAGE_WARNING = IMAGE_PATH_WARNING;

	public static final String IMAGE_ERROR = IMAGE_PATH_ERROR;

	public static final String IMAGE_EMPTY = IMAGE_PATH_BLANK;

	public static final String IMAGE_XML_ELEMENT = "icons/full/obj16/element_obj.gif";

	public static final String IMAGE_XML_ATTRIBUTE = "icons/full/obj16/attribute_obj.gif";

	public static final String IMAGE_MAPPING_LINE = "icons/full/obj16/mapping_line.gif";

	public static final String IMAGE_BINDING_LINE = "icons/full/obj16/binding_line.gif";

	public static final String IMAGE_UNKNOWN_OBJ = "icons/full/obj16/unknown_obj.gif";

	public static final String IMAGE_JAVA_FILE = "icons/full/obj16/jcu_obj.gif";

	public static final String IMAGE_XML_FILE = "icons/full/obj16/XMLFile.gif";

	public static final String IMAGE_XSD_FILE = "icons/full/obj16/XSDFile.gif";
}
