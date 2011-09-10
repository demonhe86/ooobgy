package com.taobao.dw.pizza.path_analysis.core.pojo;

import java.util.HashMap;
import java.util.Map;


/**
 * ��Ϲ켣
 * 
 * @author ����
 *
 */
public class CompositeTrace {
	//��ʼ�켣
	public String trace;
	public Map<String, CompositePath>  cps = new HashMap<String, CompositePath>();
	
		
	/**
	 * ��ԭ�ӹ켣�еĹ켣�����뵽��Ϲ켣�С���ԭ�ӹ켣�е���ͬ·��id����һ��㣬���µ���Ϲ켣��
	 */
	public void extend(AtomTrace at) {
		for (String pathId : at.atomPaths.keySet()){
			if (this.cps.get(pathId) != null){
				this.cps.get(pathId).extend(at);
			}
		}
		
	}


	/**
	 * �ж�ԭ�ӹ켣�Ƿ����Ϊ��ǰ��Ϲ켣����һ��
	 * 
	 * @param atomTrace
	 * @return
	 */
	public boolean nextStep(AtomTrace atomTrace) {
		if ( atomTrace.atomPaths.size() == 0) return false;
		for (CompositePath cp : this.cps.values()){
			if(cp.nextStep(atomTrace))
				return true;
		}
		return false;
	}

	/**
	 * ��ʼ����һ���켣
	 * 
	 * @param at
	 */
	public void init(AtomTrace at) {
		this.trace = at.atomTrace;
		for (String key : at.atomPaths.keySet()){
			AtomPath ap = at.atomPaths.get(key);
			if (ap.isFirstPath){
				this.cps.put(key, new CompositePath(ap, at.maxDepth));
			}
		}
	}
	
	public String toString(){
		return this.trace + this.cps;
	}


}
