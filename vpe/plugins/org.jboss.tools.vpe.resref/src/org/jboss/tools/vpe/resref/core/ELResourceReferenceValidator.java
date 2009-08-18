package org.jboss.tools.vpe.resref.core;

import java.util.Map;

import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.vpe.resref.Activator;

public class ELResourceReferenceValidator extends ResourceReferenceValidator {

	public static final String EL_NAME = "elName"; //$NON-NLS-1$
	private ResourceReference resref = null;
	private ResourceReference[] resrefList = null;
	
	public ELResourceReferenceValidator() {
		super();
	}

	public ELResourceReferenceValidator(Map<String, String> fields, ResourceReference resref,
			ResourceReference[] resrefList) {
		super(fields);
		this.resref = resref;
		this.resrefList = resrefList;
	}

	public void setResref(ResourceReference resref) {
		this.resref = resref;
	}

	public void setResrefList(ResourceReference[] resrefList) {
		this.resrefList = resrefList;
	}

	@Override
	protected boolean validate() {
		/*
		 * By default page is complete. 
		 * Remove any error message.
		 */
		errorMessage = null;
		pageComplete = true;
		
		if (null != fields) {
			String elName = fields.get(EL_NAME);
			/*
			 * If El name is specified
			 */
			if ((null != elName) && (elName.length() > 0)) {
				ELParser elParser = ELParserUtil.getDefaultFactory().createParser();
			 	ELModel model = elParser.parse("#{"+elName+"}"); //$NON-NLS-1$ //$NON-NLS-2$
			 	/*
			 	 * If could not parse el expression
			 	 */
			 	if(model == null || model.getSyntaxErrors().size()>0) {
			 		errorMessage = Messages.INVALID_EL_EXPRESSION;
			 		pageComplete = false;
			 	} else {
			 		/*
			 		 * El expression is parsed, 
			 		 * find already existed el expressions
			 		 * with the same name and scope. 
			 		 */
			 		String scope = fields.get(SCOPE);
			 		int selectedScope = -1;
			 		try {
			 			selectedScope = Integer.parseInt(scope);
					} catch (NumberFormatException e) {
						Activator.getDefault().logError(Messages.CANNOT_PARSE_SCOPE_VALUE, e);
					}
					/*
					 * Could parse scope integer.
					 */
					if (selectedScope != -1) {
						/*
						 * Compare el name and scope with the list items.
						 */
						for (ResourceReference listItemReference : resrefList) {
							if (resref != listItemReference
									&& listItemReference.getScope() == selectedScope
									&& elName.equals(listItemReference.getLocation())) {
								errorMessage = Messages.EL_EXPRESSION_ALREADY_EXISTS;
								pageComplete = false;
								break;
							}
						}
					}
			 	}
			} else {
				/*
				 * When El name is not specified
				 */
				errorMessage = "EL Name should be set.";
				pageComplete = false;
			}
		}
		return pageComplete;
	}

}
