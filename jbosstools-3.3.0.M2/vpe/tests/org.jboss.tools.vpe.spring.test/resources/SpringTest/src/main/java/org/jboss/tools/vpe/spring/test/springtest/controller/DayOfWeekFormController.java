package org.jboss.tools.vpe.spring.test.springtest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jboss.tools.vpe.spring.test.springtest.bean.DayOfWeekForm;
import org.jboss.tools.vpe.spring.test.springtest.editor.DayOfWeekPropertyEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class DayOfWeekFormController extends SimpleFormController {
	
	private Map refData;

	public DayOfWeekFormController() {
		
		setCommandClass(DayOfWeekForm.class);

		refData = new HashMap();

		List list = new ArrayList();
		for (int i = 0; i < 7; i++) {
			list.add(String.valueOf(i));
		}

		refData.put("dayOfWeekNumbers", list);
	}

	protected Map referenceData(HttpServletRequest request) throws Exception {
		return refData;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(String.class, new DayOfWeekPropertyEditor());
	}
}
