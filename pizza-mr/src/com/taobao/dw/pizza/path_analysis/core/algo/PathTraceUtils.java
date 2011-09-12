package com.taobao.dw.pizza.path_analysis.core.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.taobao.dw.pizza.path_analysis.core.PizzaConst;
import com.taobao.dw.pizza.path_analysis.core.pojo.AtomTrace;
import com.taobao.dw.pizza.path_analysis.core.pojo.CompositeTrace;
import com.taobao.dw.pizza.path_analysis.core.pojo.InvertedList;

/**
 * �켣·����ز�������
 * 
 * @author ����
 * @modified: ������ 2011��9��11��11:05:06
 */
public class PathTraceUtils {
	private static final String[] DEFAULT_PATH_PRO = {"-1","-1"};

	/**
	 * �û��켣���
	 * 
	 * @path �û��켣���ö��ŷָ�
	 * 
	 * 					[a,b,e,f,g]
	 * 				
	 * @return ԭ�ӹ켣�б�
	 * 
	 * 					a+b,
	 * 					a-b,
	 * 					b+e,
	 * 					b-e,
	 * 					e+f,
	 * 					e-f,
	 * 					f+g,
	 * 					f-g,
	 * 					a-e,
	 * 					a-f
	 * 					a-g,
	 * 					b-f,
	 * 					b-g,
	 * 					e-g
	 */
	public static List<AtomTrace> splitTrace(String path){
		path = path.replaceAll("\\[|\\]", "");
		String[] nodeIds = path.split(PizzaConst.COMMA_SPLIT);
		Set<AtomTrace> atomPaths = new LinkedHashSet<AtomTrace>(nodeIds.length * 2);
		for (int i=0, j=nodeIds.length-1; i<j; i++){
			for (int x=i+1, y=nodeIds.length; x<y; x++){
				if (x==i+1){
					//���Ե����ڵ���ͬ�ڵ�
					if (nodeIds[x].equals(nodeIds[i])) continue;
					atomPaths.add(new AtomTrace(nodeIds[i], "+", nodeIds[x], i, x));					
					atomPaths.add(new AtomTrace(nodeIds[i], "-", nodeIds[x], i, x));					
				}else{
					atomPaths.add(new AtomTrace(nodeIds[i], "-", nodeIds[x], i, x));					
				}
			}
		}
		List<AtomTrace> result = new ArrayList<AtomTrace>();
		result.addAll(atomPaths);
		return result;
	}
	
	/**
	 * �켣�ϲ�����ԭ��·���������������ղ������·��
	 * 
	 * @param ����·��
	 * 
	 * 	 					pathNodePairs:
			 * 						a+b:		<p1,c>,	<p3,e>, <p5,h>...
			 * 						b+c:		<p1,c>,	<p3,e>, <p5,h>...
			 * 						c+d:		<p1,c>,	<p3,e>, <p5,h>...
			 * 						a-c:		<p2,c>,	<p3,e>, <p5,h>...
			 * 						a-e:		<p1,c>,	<p3,e>, <p5,h>...
			 * 						a-f:		<p1,c>,	<p3,e>, <p5,h>...
	 * 
	 * @return Դ·��
	 * 
	 * 					a+b+c+d:		<p1,e>
	 *					a-c-h:			<p2,o>
	 *					a-f+g:			<p3,i>
	 * 
	 */
	public static List<CompositeTrace> mergeTrace(List<AtomTrace> atomTraces){
		List<CompositeTrace> results = new ArrayList<CompositeTrace>(atomTraces.size());
		for (int i = 0, j = atomTraces.size(); i < j; i++) {
			AtomTrace at = atomTraces.get(i);

			if (!at.getFirstPath())
				continue;
			
			CompositeTrace ct = new CompositeTrace();
			ct.init(at);

			for (int x = i+1, y = atomTraces.size(); x < y; x++) {
				AtomTrace nextAt = atomTraces.get(x); 
				if (ct.nextStep(nextAt)) {
					ct.extend(nextAt);
				}
			}

			results.add(ct);	
			
		}
		return results;
		
	}
	
	/**
	 *  ���룺
	 *  			(86+87)(87-91)(88-91)(90-91)(91+13)(91+141)(13+141)(141+16)
		�����
					86+87
				   	87-91
				   	91+13
				   	13+141
				   	141+16
				   	
	 * @return
	 */
	private static Pattern COMPLEX_PATH_PATTERN = Pattern.compile("\\((\\d+[\\+|\\-]\\d+)\\)");

	public static String[] splitPathToAtomPaths(String path) {
		List<String> nodes = new ArrayList<String>();
		Matcher m = COMPLEX_PATH_PATTERN.matcher(path);
		while (m.find()) {
			for (int i = 1, j = m.groupCount(); i < j + 1; i++)
				nodes.add(m.group(i));
		}
		return nodes.toArray(new String[nodes.size()]);
	}

	/**
	 *  ���룺
	 *  			(86+90)(90-91)(91+13)(13+141)(141+16)
		�����
					[86, 90, 91, 13, 141, 16]
		
	    @path	���·��
	    
	 * @return �ڵ�����
	 */
	private static Pattern COMPLEX_PATH_PATTERN2 = Pattern.compile("\\((\\d+)[\\+|\\-](\\d+)\\)");

	public static String[] splitPathToNodes(String path) {
		Set<String> nodes = new LinkedHashSet<String>();
		Matcher m = COMPLEX_PATH_PATTERN2.matcher(path);
		while (m.find()) {
				nodes.add(m.group(1));
				nodes.add(m.group(2));
		}
		return nodes.toArray(new String[nodes.size()]);
	}
	
	/**
	 * �ַ������󽻲溯��
	 * 
	 * ��[{1,2},
	 * 	  {3,4,5},
	 *    {7,8}]
	 *    
	 *  ���ַ������󣬱��
	 *  
	 *  	{1,3,7,   1,3,8,   1,4,7,   1,4,8,  1,5,7,   1,5,8,  2,3,7,   2,3,8,  2,4,7,  2,4,8, 2,5,7,  2,5,8}
	 *  
	 *  A (2,3,9)
	 *  
	 *  һ���ַ�������
	 *  
	 *  ʹ���˵ݹ��㷨����Ҫ�����������
	 * 
	 * @param originalPaths
	 * @param depth
	 * 
	 * @return
	 */
	public static String[] lateralView(List<String[]> originalPaths ,int depth){
		if (depth > 100){
			throw new IllegalArgumentException("Loop too deeply, something is wrong. Go back. Go back.");
		}
		
		if (originalPaths.size() == 0){
			return new String[0];
		}else if (originalPaths.size() == 1){
			return originalPaths.get(0);
		}else if (originalPaths.size() == 2){
			String[] x = originalPaths.get(0);
			String[] y = originalPaths.get(1);
			return crossMulti(x, y);
		}else if (originalPaths.size() > 2){
			String[] x = originalPaths.get(0);
			String[] y = lateralView(originalPaths.subList(1, originalPaths.size()), depth++);
			return crossMulti(x, y);
		}else{
			return new String[0];
		}
	}

	/**
	 * ��������
	 * 
	 * @param x
	 * @param y
	 * 
	 * @return z
	 */
	private static String[] crossMulti(String[] x, String[] y) {
		String[] z = new String[x.length*y.length];
		int k = 0;
		for (String xx:x){
			for (String yy:y){
				z[k++] = xx.trim() + "," + yy.trim();
			}
		}
		return z;
	}

	
	/**
	 * ���켣������ϣ��õ������ܶ�Ĺ켣�����ǲ��ظ�
	 * 
	 * @param nodeAttrs
	 * @return
	 * @throws JSONException 
	 */
	public static String[] extractTraces(Set<String> nodeAttrs) {
		List<String[]> nodeFeatures = new ArrayList<String[]>();
		for (String nodeFeature: nodeAttrs){
			String[] nodeIds =nodeFeature.split(PizzaConst.COMMA_SPLIT);
			for (int i=0,j=nodeIds.length; i<j ; i++){
				nodeIds[i] = nodeIds[i].trim();
			}
			nodeFeatures.add(nodeIds);
		}
		
		Set<String> results = new TreeSet<String>(Arrays.asList(lateralView(nodeFeatures, 0)));
		return results.toArray(new String[results.size()]);
	}

	/**
	 * �����׽ڵ㣬������е�ԭ��·��
	 * 
	 * @param headNodeId
	 * @return
	 */
	public static List<AtomTrace> findAtsWithHeadNode(String headNodeId, InvertedList il) {	
		return il.getHeadPath(headNodeId);
	}

	/**
	 * ��keyFeature��ɢ����ɶ�����nodeIdΪkey�����keyָ��һ��nodeAttr
	 * 
	 * @param nodeAttrs
	 * @return
	 */
	public static Map<String, JSONObject> rebuildNodeAttrs(Map<String, JSONObject> nodeAttrs) {
		Map<String, JSONObject> newNodeAttrs = new LinkedHashMap<String, JSONObject>();
		for(String nodeFeature: nodeAttrs.keySet()){
			String[] nodeIds = nodeFeature.split(PizzaConst.COMMA_SPLIT);			
			for (String nodeId : nodeIds) newNodeAttrs.put(nodeId.trim(), nodeAttrs.get(nodeFeature));
		}
		return newNodeAttrs;
	}
	
	/**
	 * ɸѡ�µ��׽ڵ㵥�������
	 * �µ��׽ڵ㣺�û��켣ƥ�䵽��·���ϣ��û����������׽ڵ㡣���綨��·��p1:1+2+3���û��켣1,4,5���û��ڵ�1���ǹµ��׽ڵ�
	 * @param iList ���ű����ã����ڲ����׽ڵ���Ϣ
	 * @param nodeAttrs  �û������Ľڵ㼯�ϣ���Ҫɸѡ
	 * @param cts  �Ѿ�merge�õ����·������
	 * @return lonelyHeadNodeProfile "�µ��׽ڵ�"��Ϣ  {[nodeId, pathId, nextNodeId, lastNodeId�� next_outer_node_id],...}
	 */
	public static Set<String[]> matchLonelyHeadNode(InvertedList iList,
			String userTrace, List<CompositeTrace> cts) {
		String[] nodeIds = userTrace.split(PizzaConst.COMMA_SPLIT);
		for (int i = 0, j = nodeIds.length; i < j; i++) {
			nodeIds[i] = nodeIds[i].trim();
		}

		Set<String> unLonelyPaths = new LinkedHashSet<String>();// �ǹµ�·��
		for (CompositeTrace ct : cts) {
			unLonelyPaths.addAll(ct.cps.keySet());
		}

		Set<String[]> lonelyProfiles = new LinkedHashSet<String[]>();

		for (int i = 0; i < nodeIds.length; i++) {
			Set<String> lonelyPaths = iList.findPathsWithHeadNode(nodeIds[i]);
			if (lonelyPaths == null) {
				continue;
			}
			for (String pathId : lonelyPaths) {
				if (!unLonelyPaths.contains(pathId)) {
					String outer;
					if (i == nodeIds.length - 1) {// �Ѿ������һ��
						outer = PizzaConst.INVALID_NODE_ID;
					} else {
						outer = nodeIds[i + 1];
					}
					String[] pathProfile = iList.getPathFrofile(pathId);
					if (pathProfile == null) {
						pathProfile = DEFAULT_PATH_PRO;
					}
					String[] lonelyProfile = { nodeIds[i], pathId,
							pathProfile[0], pathProfile[1], outer };
					lonelyProfiles.add(lonelyProfile);

				}
			}
		}

		return lonelyProfiles;
	}
}
