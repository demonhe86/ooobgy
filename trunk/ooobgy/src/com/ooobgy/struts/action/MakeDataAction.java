/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.ooobgy.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ooobgy.struts.form.MakeDatabaseForm;

/** 
 * MyEclipse Struts
 * Creation date: 06-11-2010
 * 
 * XDoclet definition:
 * @struts.action path="/makeData" name="makeDataForm" input="/makeData.jsp" scope="request" validate="true"
 */
public class MakeDataAction extends Action {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		MakeDatabaseForm makeDataForm = (MakeDatabaseForm) form;// TODO Auto-generated method stub
		return null;
	}
}