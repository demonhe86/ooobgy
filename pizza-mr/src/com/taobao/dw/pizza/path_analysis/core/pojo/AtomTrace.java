package com.taobao.dw.pizza.path_analysis.core.pojo;

import java.util.HashMap;
import java.util.Map;

import com.taobao.dw.pizza.path_analysis.core.PizzaConst;

/**
 * 	ԭ�ӹ켣����ʽΪ��
 * 				ǿ�켣��nodeId1+nodeId2
 * 				���켣��nodeId1-nodeId2
 * 				
 * 				
 * @author ����
 * @modified ����  2011��8��12��16:45:21
 */
public class AtomTrace {

	final public String atomTrace;
	
	final public String startNode;
	final public String endNode;
	final public String seperator;
	final public int minDepth;
	final public int maxDepth;

	public Map<String, AtomPath> atomPaths = new HashMap<String, AtomPath>();
	
	public AtomTrace(String _startNode, String _seperator, String _endNode, int minDepth, int maxDepth) {
		super();
		this.startNode = _startNode.trim();
		this.endNode = _endNode.trim();
		this.seperator = _seperator.trim();
		this.atomTrace = this.startNode + this.seperator + this.endNode;
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}	
	
	public AtomTrace(String trace, int minDepth, int maxDepth) {
		super();
		this.startNode = trace.split(PizzaConst.NODE_SEP_PATTERN)[0];
		this.endNode =  trace.split(PizzaConst.NODE_SEP_PATTERN)[1];		
		this.seperator = (trace.indexOf("+") != -1) ? "+" : "-"; 
		this.atomTrace = trace;
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	private boolean isFirstPath = false;
	
	/**
	 * ��������ƥ���AtomPath���ж��Ƿ�Ϊ��һ·�������������·��Ϊ��һ·������ô��Trace�ʹ���Ϊ��һ·���Ŀ���
	 * 
	 * �÷�����һ��������ģ�������
	 * 
	 * @return
	 */
	public boolean getFirstPath(){
		for (AtomPath ap : atomPaths.values()){
			if (ap.isFirstPath) {
				isFirstPath = true;
				break;
			}
		}
		return isFirstPath;
	}

	
	public boolean startFrom(String firstNodeId) {
		return this.startNode.equalsIgnoreCase(firstNodeId);
	}
	
	public String toString(){
		return this.atomTrace + this.atomPaths;		
	}
	

}
