package com.alimama.loganalyzer.jobs.mrs.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.loganalyzer.common.util.tbacookie.AcookieUtil;
import com.taobao.loganalyzer.common.util.tbacookie.SearchListContent;
import com.taobao.loganalyzer.common.util.url.URLParser;

/**
 * URL�����࣬��װ�˶Թ��ÿ�LogUtil.jar��ĵ���
 * 
 * @author ����
 * 
 */

public class UrlParserWrapper {
	private static final String URL_ENCODING = "gbk";
	
	private static final String TAOBAO_SEARCH_HOME_URL = "search.taobao.com";
	
	private static final String TAOBAO_SEARCH_SHOP_URL = "shopsearch.taobao.com";
    private static final String TAOBAO_LIST_HITAO_URL = "list.hitao.taobao.com";
    private static final String TAOBAO_LIST_3C_URL = "list.3c.taobao.com";
    private static final String TAOBAO_LIST_MALL_URL = "list.mall.taobao.com";
    
	private static final String SSID_KEY = "ssid";
	
	/// SSIDģʽ������ĸs��ͷ�����������
	private static final Pattern SSID_PATTERN = Pattern.compile("(s\\d+)");
	private static final int MAX_QUERY_LENGTH = 512;

	/**
	 * �ж�URL�Ƿ�Ϊ������URL����ʽΪ
	 * 1)search.taobao.com
	 * 2)shopsearch.taobao.com
	 * 3)search1.taobao.com
	 * 4)search8.taobao.com
	 * 
	 * @param url
	 *            ���ж�URL
	 * @return �� ����true���񷵻�false
	 */
	public static boolean isSearchUrl(String url) {
		if (!StringUtils.isValid(url)) { 
		    return false;
		}
		if (url.indexOf(TAOBAO_SEARCH_HOME_URL) > 0
		    || url.indexOf(TAOBAO_SEARCH_SHOP_URL) > 0
		    || AcookieUtil.isSearch1(url)
		    || AcookieUtil.isSearch8(url) ){
			return true;
		}else{
			return false;
		}
		
	}

	
    /**
     * �ж�URL�Ƿ�Ϊ�б���URL����ʽΪ
     * 1)list.hitao.taobao.com
     * 2)list.3c.taobao.com
     * 3)list.mall.taobao.com
     * 
     * @param url
     *            ���ж�URL
     * @return �� ����true���񷵻�false
     */
    public static boolean isListUrl(String url) {
        if (!StringUtils.isValid(url)) {
            return false;
        }
        if (url.indexOf(TAOBAO_LIST_HITAO_URL) > 0 
            || url.indexOf(TAOBAO_LIST_3C_URL) > 0
            || url.indexOf(TAOBAO_LIST_MALL_URL) > 0 ){
            return true;
        }else{
            return false;
        }
        
    }
    
    
	/**
	 * �Ƿ�Ϊ��������ҳURl
	 * 
	 * @param url
	 *            ���ж�URL
	 * @return �Ƿ�Ϊ��������URL
	 */
	public static boolean isItemUrl(String url) {
		return AcookieUtil.isItem(url);
	}

	/**
	 * ��ȡURL�еĲ�ѯ��
	 * 
	 * @param url
	 *            ����ȡ��URL
	 * 
	 * @return��ѯ��
	 */

	public static String parseQuery(String url) {
	    String query = "";
	    Map<String, String> params = null;
	    
	    if (isSearchUrl(url) || isListUrl(url)) {
	        params = AcookieUtil.getAllURLPara(url);
	        if (params.containsKey("q")) {
	            query = params.get("q");
	        }
	    }
	      
	    if (StringUtils.isValid(query)) {  
	        try {
	            query = URLDecoder.decode(query, URL_ENCODING);
	        } catch (Throwable t) {
	            t.printStackTrace();
	            System.err.println("Query�ʽ���ʧ��");
	        }
	    }
	    
        query = query.trim();
	    if (isValueQuery(query)){
		    return query;
	    }else{
	    	return null;
	    }

      
	}

	/**
	 * ��ȡURL�е�SSID
	 * 
	 * @param referUrl ����URL
	 * @return SSID
	 */
	public static String parseSsid(String referUrl) {
		if (!isSearchUrl(referUrl)) {
			return KQConst.UNKNOWN_STR_ID;
		}
		Hashtable<String, String> ht = URLParser.parsePara(referUrl);
		String ssid = ht.get(SSID_KEY);

		if (ssid == null) {
			return KQConst.UNKNOWN_STR_ID;
		}

		Matcher matcher = SSID_PATTERN.matcher(ssid);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return KQConst.UNKNOWN_STR_ID;
		}
	}

	/**
	 * ��ȡURL�е���ĿID
	 * 
	 * @param url ����ȡURL
	 * @return ��ĿID
	 * 
	 */
	public static int parseCategory(String url) {
		Map<String, String> urlInfoMap = AcookieUtil.parseURLInfo(url);
		return parseCategory(urlInfoMap);

	}

	/**
	 * ��ȡ��������ҳ����Ŀ
	 * 
	 * @param urlInfoMap
	 * @return ��ĿID
	 */
	private static int parseCategory(Map<String, String> urlInfoMap) {
	    
		String category = (String) urlInfoMap.get("category");
		if (StringUtils.isValid(category)) {
			int sep = category.lastIndexOf("_");
			if (sep >= 0) {
				category = category.substring(sep + 1);
			}
			try {
				return Integer.parseInt(category);
			} catch (NumberFormatException e) {
				return KQConst.UNKNOWN_INT_ID;
			}
		} else {
			return KQConst.UNKNOWN_INT_ID;
		}
	}

	/**
	 * ��ȡ����id
	 * 
	 * @param url
	 * @return
	 */
	public static String parseStoreId(String url, String urlInfo) {
	    String storeId = "";
		Map<String, String> params = AcookieUtil.getAllURLPara(url, urlInfo);
		if ( params.containsKey("shopid") ) { 
		    storeId = params.get("shopid");
		}else {
		    return KQConst.UNKNOWN_STR_ID;
		}
		return storeId;
	}


	/**
	 * �Ƿ�����Ч��ѯ��
	 * 
	 * @param query
	 * @return
	 */
	public static boolean isValueQuery(String query) {
		return StringUtils.isValid(query)
				&& query.length() < MAX_QUERY_LENGTH
				&& query.indexOf(KQConst.DEFAULT_SPLIT) < 0
				&& query.indexOf(KQConst.SECOND_SPLIT) < 0
				&& query.indexOf(KQConst.TAB_SPLIT) < 0;
		
	}

	/**
	 * ��ȡĿ����Դ������
	 * 
	 * @param url
	 * @return
	 */
	public static byte parseUrlType(String url) {
		return KQConst.UNKNOWN_INT_ID;
	}

	   /**
     * ��ȡ��������ID
     * 
     * @param url
     * @return
     */
    public static String parseItemId(String url) {
        return parseItemId(url, "");
    }

	/**
	 * ��ȡ��������ID
	 * 
	 * @param url, urlInfo
	 * @return
	 */
	public static String parseItemId(String url, String urlInfo) {
        String itemId = "";
        Map<String, String> params = AcookieUtil.getAllURLPara(url, urlInfo);
        if ( params.containsKey("itemid") ) { 
            itemId = params.get("itemid");
        }else {
            return KQConst.UNKNOWN_STR_ID;
        }
        return itemId;
    }

}
