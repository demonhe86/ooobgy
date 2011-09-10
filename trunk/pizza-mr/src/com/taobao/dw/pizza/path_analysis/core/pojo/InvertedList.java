package com.taobao.dw.pizza.path_analysis.core.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.dw.pizza.path_analysis.core.PizzaConst;


/**
 * ·�����ű�
 * 
 * @author ����
 * @created 2011-07-28
 */
public class InvertedList {
	/*
	 * 		Key��ԭ��·��
	 * 		Value����Ӧ��ԭ��·����
	 */
	private Map<String, List<AtomPath>> invertedPath = new LinkedHashMap<String, List<AtomPath>>();
	/*
	 * 		Key:			�׽ڵ�
	 * 		Values��	ԭ��·������
	 */
	public Map<String, Set<String>> headPathIndex = new LinkedHashMap<String, Set<String>>();

	/**
	 * ��·���׽ڵ�Ķ��յ��ű����������ѯ
	 * �ռ�����ΪO(n)��n��·������
	 * 		Key:		�׽ڵ�id
	 * 		Values:		��Ӧ��·��id����
	 */
	private Map<String, Set<String>> invertedHeadNodeIndex = new HashMap<String, Set<String>>();
	
	/**
	 * ·����ϸ��Ϣ��ѡ�÷���{@link #putHeadNodePathIndex(String, String)}���¼��·���ĵڶ��ڵ㡢β�ڵ�
	 * ��ѡ�÷��� {@link #putHeadNodePathIndex(String)}��Ϊ�գ������Ĳ�ѯ�������Զ�����-1
	 * �ռ�����ΪO(2n)��n��·������
	 * Key:   ·��id
	 * Values:    �ڶ��ڵ�id��β�ڵ�id
	 */
	private Map<String, String[]> pathProfile = new HashMap<String, String[]>();
	
	/**
	 * �ڵ��ű��У�����·��id�ԣ�Ѱ�Ҷ��ڵ�·���ڵ�ԣ�����ָ����ʽ��䣬�����������
	 * @param at
	 */
	public void match(AtomTrace at) {
		List<AtomPath> aps = invertedPath.get(at.atomTrace);
		if ( aps != null){
			for (AtomPath ap : aps){
				at.atomPaths.put(ap.pathId, ap);
			}
		}
	}
	
	/**
	 * ����һ��nodeId�Ƿ�Ϊ�׽ڵ�
	 * @param nodeId
	 * @return
	 */
	public boolean isHeadNode(String nodeId){
		return (this.headPathIndex.get(nodeId) != null);
	}
	

	/**
	 *  ��·�����뵹�ű�
	 * 
	 * @param atomPath
	 * @param aps
	 */
	public void put(String atomPath, List<AtomPath> aps) {
		this.invertedPath.put(atomPath, aps);
	}
	
	/**
	 * �����׽ڵ�·������
	 * @param atomPath
	 */
	private void updateHeadPathIndex(String atomPath) {
		String startNode = atomPath.split(PizzaConst.NODE_SEP_PATTERN)[0];
		if (this.headPathIndex.get(startNode) == null){
			this.headPathIndex.put(startNode, new LinkedHashSet<String>());			
		}
		this.headPathIndex.get(startNode).add(atomPath);
	}
	
	/**
	 * �����׽ڵ��Ӧ�������׹켣����·��
	 * 
	 * @param headNodeId
	 * @return
	 */
	public List<AtomTrace> getHeadPath(String headNodeId){
		Set<String> headTraces = this.headPathIndex.get(headNodeId);		
		List<AtomTrace> results = new ArrayList<AtomTrace>();
		if (headTraces == null || headTraces.size() ==0) return results;
		for(String headTrace: headTraces){
				AtomTrace at = new AtomTrace(headTrace,0,1);//����1-2�������maxD��һ������1
				this.match(at);
				results.add(at);
		}
		return results;		
	}

	public String toString(){
		return this.invertedPath.toString() + "\nHead Paths:" + this.headPathIndex.toString() +
				"\nheadNode-pathId:" + this.invertedHeadNodeIndex;
	}

	public void clean() {
		this.invertedPath.clear();		
	}

	public void addAll(InvertedList _readInvertedList) {
		this.invertedPath.putAll(_readInvertedList.invertedPath);
	}

	/**
	 * ���޹�����·������
	 */
	public void rebuildHeadNodeIndex() {
		for (List<AtomPath> aps : this.invertedPath.values()){
			for (AtomPath ap:aps){
				this.updateHeadPathIndex(ap.path);
			}
		}			
	}
	
	public Set<String> findPathsWithHeadNode(String headNodeId) {
		return this.invertedHeadNodeIndex.get(headNodeId);
	}
	
	public void putHeadNodePath(String headNodeId, String pathId) {
		if (this.invertedHeadNodeIndex.get(headNodeId) == null) {
			this.invertedHeadNodeIndex.put(headNodeId, new HashSet<String>());
		}
		
		this.invertedHeadNodeIndex.get(headNodeId).add(pathId);
	}
	
	/**
	 * ���Ľϵ͵ķ���������¼·���ĵڶ��ڵ㡣
	 * ��������£������α�r_pizza_path_node_split�У�����ֻ�����·���׽ڵ���û��켣
	 * next_node_id������¼Ϊ-1��last_node_idҲ��¼Ϊ-1
	 * @param pathExp
	 * @see #putHeadNodePathIndex(String, String)
	 */
	public void putHeadNodePathIndex(String pathExp){
		String[] items = pathExp.split(PizzaConst.COMMA_SPLIT);
		this.putHeadNodePath(items[2], items[0]);
	}

	/**
	 * ��ȫ�ķ���������ÿ��·���ĵڶ��ڵ㼰β�ڵ���Ϣ
	 * ��������£������α�r_pizza_path_node_split�У�����ֻ�����·���׽ڵ���û��켣
	 * next_node_id,last_node_id����¼Ϊ��ʵ���
	 * @param atomPath
	 * @param pathExp
	 * @see #putHeadNodePathIndex(String)
	 */
	public void putHeadNodePathIndex(String atomPath, String pathExp) {
		String[] values = pathExp.split(PizzaConst.COMMA_SPLIT);
		String[] atomNodes = atomPath.split(PizzaConst.NODE_SEP_PATTERN);
		if (atomNodes[0].equals(values[2])) {
			String[] profile = new String[]{atomNodes[1], values[3]};
			this.pathProfile.put(values[0], profile);
		}
		this.putHeadNodePath(values[2], values[0]);
	}
	
	/**
	 * ·����ϸ
	 * @param pathId
	 * @return  [�ڶ��ڵ�id,β�ڵ�id]
	 */
	public String[] getPathFrofile(String pathId){
		return this.pathProfile.get(pathId);
	}
}
