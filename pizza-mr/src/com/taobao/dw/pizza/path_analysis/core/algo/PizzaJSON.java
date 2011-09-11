package com.taobao.dw.pizza.path_analysis.core.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.taobao.dw.pizza.path_analysis.core.PizzaConst;
import com.taobao.dw.pizza.path_analysis.core.pojo.AtomPath;

/**
 * JSON��ز�����
 * 
 * @author ����
 *
 */
public class PizzaJSON {
	/**
	 *  JSON�ĸ�ʽ
	 *  
	 *  {
    "uid_or_mid": "107188702",
    "entitys": [
        {
            "url": "http://s.taobao.com/search?q=��ɹ�� ���� ͸��&keyword=&commend=all&ssid=s5-e&search_type=item&atype=&tracelog=&sourceId=tb.index",
            "nodeFeature": "1,2",
            "isUser": "1",
            "mid": "5618641004102355756",
            "isRefer": "1",
            "logTime": "20110619111834"
        },
        {
            "url": "http://item.taobao.com/item.htm?id=9519056136",
            "nodeFeature": "3,4,5",
            "isUser": "1",
            "mid": "5618641004102355756",
            "isRefer": "0",
            "logTime": "20110619112150"
        }
    ]
}
	 * @param jsonStr
	 */
	/**
	 * ����json�ַ���������֯��һ��map����node idΪkey
	 * 
	 * @param rawJSONStr
	 * @param nodeMap
	 * @throws JSONException 
	 */
	
	public static Map<String, JSONObject> parseUserRoute(String rawJSONStr) throws JSONException {
		Map<String, JSONObject> nodeAttrs = new LinkedHashMap<String, JSONObject>();
		JSONObject pathJO = new JSONObject(rawJSONStr);
		String uid = pathJO.getString("uid_or_mid");
		JSONArray entities = pathJO.getJSONArray("entitys"); 
		String nodeFeature = "";
		String preNodeFeature = "";		
		for (int i=0,j=entities.length(); i<j; i++){
			JSONObject entity = entities.getJSONObject(i);
			entity.put("uid", uid);			
			nodeFeature = entity.getString("nodeFeature");			
			//1.���˵�-1��û��ƥ���ϵĽڵ�
			if (nodeFeature.equalsIgnoreCase(PizzaConst.INVALID_NODE_ID)){
				continue;
			}
			//2.���˵�����һ���ظ��Ľڵ�
			if (nodeFeature.equalsIgnoreCase(preNodeFeature)){
				continue;
			}else{
				preNodeFeature = nodeFeature;
			}
			
			nodeAttrs.put(nodeFeature, entity);
		}
		return nodeAttrs;
	}

	/**
	 * �������·��Ϊԭ��·��
	 * 
	 * ���룺
	 * 			��8λ��[{"PATH_KEY":-662107825,"TABLE_MOD_ID":1,"PATH_EXP":"(86+88)(88-91)(91+13)(13+141)(141+16)"}]	
	 * ���:
	 * 			ԭ��·�������б�ԭ��·���Ķ���˵������#AtomPath
	 * 			
	 * @throws JSONException 
	 */
	public static List<AtomPath> parsePathExp(String pathValue) {
		pathValue = pathValue.replaceAll("\\[|\\]", "");			
		
		try {
			JSONObject pathJO = new JSONObject(pathValue);
			String pathId = pathJO.getString("PATH_KEY");
			String pathExp = pathJO.getString("PATH_EXP");
			String tableId = pathJO.getString("TABLE_MOD_ID");
				
			String[] paths = PathTraceUtils.splitPathToAtomPaths(pathExp);
			String headNodeId = paths[0].split(PizzaConst.NODE_SEP_PATTERN)[0];
			String tailNodeId = paths[paths.length-1].split(PizzaConst.NODE_SEP_PATTERN)[1];
			
			List<AtomPath> results = new ArrayList<AtomPath>();
			for (int i=0, j=paths.length; i<j; i++){
				AtomPath ap = new AtomPath();
				ap.path = paths[i];
				ap.pathId = pathId;
				ap.tableId = tableId;
				ap.headNodeId = headNodeId;
				ap.tailNodeId = tailNodeId;
				ap.nextNodeId = (i==j-1) ? ("-1"):(paths[i+1].split(PizzaConst.NODE_SEP_PATTERN)[1]) ;
				results.add(ap);
			}
			
			return results;
		} catch (Exception e) {
			System.err.println("ParsePathExp:"+e);
			return Collections.emptyList();
		}			
	}

}
