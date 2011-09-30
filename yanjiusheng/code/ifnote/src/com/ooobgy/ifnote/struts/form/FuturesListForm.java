/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.ooobgy.ifnote.struts.form;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.ooobgy.ifnote.constants.SecretKey;
import com.ooobgy.ifnote.dbctrler.dao.Inote_FuturesDao;
import com.ooobgy.ifnote.dbctrler.daoimpl.Inote_FuturesDaoImpl;
import com.ooobgy.ifnote.dispbeans.Disp_Futures;
import com.ooobgy.ifnote.entity.Inote_Futures;
import com.ooobgy.ifnote.entity.User;
import com.ooobgy.ifnote.struts.form.tpl.ListForm;

/** 
 * MyEclipse Struts
 * Creation date: 08-23-2011
 * 
 * XDoclet definition:
 * @struts.form name="futuresListForm"
 */
public class FuturesListForm extends ActionForm implements ListForm{
	/*
	 * Generated fields
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -853644166950005202L;

	/** startTime property */
	private String startTime;

	/** endTime property */
	private String endTime;
	
	private List<Inote_Futures> inote_Futures;
	private List<Disp_Futures> disp_Futures;

	/*
	 * Generated Methods
	 */

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		ActionMessage actionMessage = new ActionMessage("list");
		errors.add("list", actionMessage);		
		reset(mapping, request);
		
		return errors;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(SecretKey.USER_KEY);
		Inote_FuturesDao dao = new Inote_FuturesDaoImpl();
		if (startTime != null && endTime != null
				&& startTime.length() > 1 && endTime.length() > 1) {
			inote_Futures = dao.findAllWithUidTime(user.getId(), Timestamp
					.valueOf(startTime), Timestamp.valueOf(endTime));
		} else {
			inote_Futures = dao.findAllWithUid(user.getId());
			startTime = "";
			endTime = "";
		}
		
		makeDispBeans();
		
		super.reset(mapping, request);
	}

	/** 
	 * Returns the startTime.
	 * @return String
	 */
	public String getStartTime() {
		return startTime;
	}

	/** 
	 * Set the startTime.
	 * @param startTime The startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/** 
	 * Returns the endTime.
	 * @return String
	 */
	public String getEndTime() {
		return endTime;
	}

	/** 
	 * Set the endTime.
	 * @param endTime The endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the inote_Futures
	 */
	public List<Inote_Futures> getInote_Futures() {
		return inote_Futures;
	}

	/**
	 * @param inoteFutures the inote_Futures to set
	 */
	public void setInote_Futures(List<Inote_Futures> inoteFutures) {
		inote_Futures = inoteFutures;
	}

	/* (non-Javadoc)
	 * @see com.ooobgy.ifnote.struts.form.tpl.ListForm#makeDispBeans()
	 */
	public void makeDispBeans() {
		this.disp_Futures = new ArrayList<Disp_Futures>();
		
		for (Inote_Futures inote : inote_Futures) {
			Disp_Futures disp = new Disp_Futures(inote);
			disp.init();
			this.disp_Futures.add(disp);
		}
	}

	/**
	 * @return the disp_Futures
	 */
	public List<Disp_Futures> getDisp_Futures() {
		return disp_Futures;
	}

	/**
	 * @param dispFutures the disp_Futures to set
	 */
	public void setDisp_Futures(List<Disp_Futures> dispFutures) {
		disp_Futures = dispFutures;
	}
}