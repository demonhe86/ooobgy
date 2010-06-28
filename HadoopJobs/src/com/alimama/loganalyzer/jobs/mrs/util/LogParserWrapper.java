package com.alimama.loganalyzer.jobs.mrs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.taobao.loganalyzer.common.util.tbacookie.AcookieUtil;
import com.taobao.loganalyzer.input.acookie.parser.AcookieLog;
import com.taobao.loganalyzer.input.acookie.parser.AcookieLogParser;

/**
 * �Ա���ACookie��־������װ�࣬��AcookieLogParser.jar�ķ�װ���ѣ��ÿ�Ϊ���ÿ⡣
 * 
 * @author ����
 *
 */
public class LogParserWrapper {
	private AcookieLog acookieLog;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
	private static final SimpleDateFormat sdfMinSecond = new SimpleDateFormat("mm:ss");
	
	
	@SuppressWarnings("unused")
	private LogParserWrapper(){
		
	}
	
	/**
	 * Ĭ�Ϲ����������봫��һ��Log���н������������acookieLog��
	 * @param log
	 */
	public LogParserWrapper(String log){
		super();
		acookieLog = AcookieLogParser.parse(log);		
	}
	
	/**
	 * ����Log������Cookie��Ϣ
	 * @return
	 */
	public String getCookie(){
		if (acookieLog == null){
			return null;
		}
		return acookieLog.getCookie();

	}
	
	/**
	 * ����Log������ʱ�䣬�ַ�����ʽ
	 * @return
	 */
	public String getCookieTimeStr(){
		if (acookieLog == null){
			return null;
		}

		String time = acookieLog.getTime();
		int splitPos = time.indexOf(KQConst.TAB_SPLIT);
		if (splitPos >= 0) {
			time = time.substring(splitPos + 1);
		}
		return time;
	}
	
	
	/**
	 * ����Log����ʱ�䣬���ڸ�ʽ����Ϊ���ظ�ʽ�ɶ��Բ�����⿪�ţ���Ҫ��ͨ��getTimeInDay��getTimeInHour��getTimeInMMSS����
	 * 
	 * @return yyyyMMdd ���� 20100306
	 * @throws ParseException 
	 */
	private Date getCookieTime() throws ParseException{
		try {
			return sdf.parse(getCookieTimeStr());
		} catch (ParseException e) {
			throw e;
		}
	}
	
	/**
	 * cookie��־��ʱ�䣬��ȷ����
	 * 
	 * @return 
	 * @throws ParseException 
	 */
	public String getCookieTimeInDate() throws ParseException{
		return sdfDate.format(getCookieTime());
	}
	
	
	/**
	 * cookie��־��ʱ�䣬��ȷ��Сʱ	
	 * @return hh ��13
	 * @throws ParseException 
	 */
	public String getCookieTimeInHour() throws ParseException{
		return sdfHour.format(getCookieTime());
	}
	
	/**
	 * cookie��־��ʱ�䣬��ȷ����
	 * 
	 * @return mm:ss ��59:03
	 * @throws ParseException 
	 */
	public String getCookieTimeInMMSS() throws ParseException{
		return sdfMinSecond.format(getCookieTime());		
	}
	
	/**
	 * ����Log�е�Url��Ϣ
	 * @return
	 */
	public String getUrl(){
		return acookieLog.getUrl();
	}
	
	/**
	 * ����Log�е�Refer Url
	 * 
	 * @return
	 */
	
	public String getReferUrl(){
		if (acookieLog == null){
			return null;
		}

		String urlInfo = acookieLog.getUrlInfo();
		Map<String, String> urlInfo_map = AcookieUtil.parseURLInfo(urlInfo);
		if (urlInfo_map == null){
			return "";
		}
		String pre = urlInfo_map.get("pre");
		if (pre == null) {
			return "";
		}
		String refer = AcookieUtil.urlinfoReferDecode(pre);
		return refer;
	}

	/**
	 * ����URL Info����URL��ͬ�����п�����ȡCategoryId
	 * 
	 * @return
	 */
	public String getUrlInfo() {
		return acookieLog.getUrlInfo();
	}

	/**
	 * ����UserId
	 * @return
	 */
	public int getUserId() {
		String strUserId = acookieLog.getCookieUid();
		int userId = 0;
		if (StringUtils.isValid(strUserId)) {
			try {
				userId = Integer.parseInt(strUserId);
			} catch (Throwable e) {
				return KQConst.UNKNOWN_INT_ID;
			}
			return userId;
		}else{
			return KQConst.UNKNOWN_INT_ID;
		}
		
	}
	



}
