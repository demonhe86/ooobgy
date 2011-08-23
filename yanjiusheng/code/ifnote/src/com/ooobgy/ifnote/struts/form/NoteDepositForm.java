/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.ooobgy.ifnote.struts.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.ooobgy.ifnote.constants.SecretKey;
import com.ooobgy.ifnote.dbctrler.dao.Inote_DepositDao;
import com.ooobgy.ifnote.dbctrler.daoimpl.Inote_DepositDaoImpl;
import com.ooobgy.ifnote.entity.Inote_Deposit;
import com.ooobgy.ifnote.entity.User;

/** 
 * MyEclipse Struts
 * Creation date: 08-17-2011
 * 
 * XDoclet definition:
 * @struts.form name="noteDepositForm"
 */
public class NoteDepositForm extends ActionForm {
	/*
	 * Generated fields
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1098685983682590175L;

	/** bank_name property */
	private String bank_name;

	/** rate property */
	private String rate;

	/** sum property */
	private String sum;

	/** comment property */
	private String comment;

	/** type property */
	private String type;

	/** dep_time property */
	private String dep_time;

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
		
		try {
			if(Double.parseDouble(rate) < 0){
				throw new IllegalArgumentException();
			}
		} catch (Throwable e) {
			ActionMessage actionMessage = new ActionMessage("error.rate");
			errors.add("rate", actionMessage);
			return errors;
		}
		
		try {
			if(Double.parseDouble(sum) < 0){
				throw new IllegalArgumentException();
			}
		} catch (Throwable e) {
			ActionMessage actionMessage = new ActionMessage("error.account");
			errors.add("sum", actionMessage);
			return errors;
		}
		
		
		return errors;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String idStr = (String) request.getParameter("nid");
		System.out.println("#####" + idStr);
		if (idStr != null && idStr.length() > 0) {
			try {
				Integer id = Integer.parseInt(idStr);
				Inote_Deposit inote = initInote(id);
				HttpSession session = request.getSession();
				User user = (User)session.getAttribute(SecretKey.USER_KEY);
				if (user != null && inote != null) {
					if (user.getId().equals(inote.getUser_id())) {
						this.comment = inote.getComment();
						this.bank_name = inote.getBank_name();
						this.rate = inote.getRate().toString();
						this.sum = inote.getSum().toString();
						this.type = inote.getType();
						this.dep_time = inote.getDep_time();
						session.setAttribute("inote_deposit", inote);
						return;
					} else {
						initBlankInote();
					}
				}
				initBlankInote();
				
				return;
			} catch (Throwable e) {
				//Do nothing,转入初始化空白Bean的操作
			}
		}
		
		initBlankInote();
	}

	private void initBlankInote() {
		this.comment = "";
		this.bank_name = "";
		this.rate = "3.5";
		this.sum = "";
		this.type = "1";
		this.dep_time = "one year";
	}

	private Inote_Deposit initInote(Integer id) {
		Inote_DepositDao dao = new Inote_DepositDaoImpl();
		Inote_Deposit inote = dao.findWithId(id);
		
		if (inote==null || inote.getId() == null) {
			return null;
		} else {
			return inote;
		}
	}
	/** 
	 * Returns the bank_name.
	 * @return String
	 */
	public String getBank_name() {
		return bank_name;
	}

	/** 
	 * Set the bank_name.
	 * @param bank_name The bank_name to set
	 */
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
		
	}

	/** 
	 * Returns the rate.
	 * @return String
	 */
	public String getRate() {
		return rate;
	}

	/** 
	 * Set the rate.
	 * @param rate The rate to set
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}

	/** 
	 * Returns the sum.
	 * @return String
	 */
	public String getSum() {
		return sum;
	}

	/** 
	 * Set the sum.
	 * @param sum The sum to set
	 */
	public void setSum(String sum) {
		this.sum = sum;
	}

	/** 
	 * Returns the comment.
	 * @return String
	 */
	public String getComment() {
		return comment;
	}

	/** 
	 * Set the comment.
	 * @param comment The comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/** 
	 * Returns the type.
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/** 
	 * Set the type.
	 * @param type The type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/** 
	 * Returns the dep_time.
	 * @return String
	 */
	public String getDep_time() {
		return dep_time;
	}

	/** 
	 * Set the dep_time.
	 * @param dep_time The dep_time to set
	 */
	public void setDep_time(String dep_time) {
		this.dep_time = dep_time;
	}
}