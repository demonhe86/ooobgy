package edu.zju.cs.ooobgy.algo.dynamic_na.analayzer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections15.Transformer;

import edu.zju.cs.ooobgy.algo.dynamic_na.event.ClusterEvent;
import edu.zju.cs.ooobgy.algo.dynamic_na.eventjudger.ClusterContinueJudger;
import edu.zju.cs.ooobgy.algo.dynamic_na.pojo.ClusterSlice;
import edu.zju.cs.ooobgy.algo.dynamic_na.pojo.IdCluster;

/**
 * �ھ��Ż��¼�
 * @author frogcherry ������
 * @created 2011-5-4
 * @Email frogcherry@gmail.com
 */
public class ClusterEventAnalyzer<V, E> implements
				Transformer<ClusterSlice<V, E>, List<ClusterEvent>> {
	private ClusterSlice<V, E> preSlice;
	private Collection<IdCluster<V>> preClusters;
	private ClusterSlice<V, E> nowSlice;

	public ClusterEventAnalyzer(ClusterSlice<V, E> preSlice) {
		super();
		this.preSlice = preSlice;
		preClusters = preSlice.getClusters().values();
	}
	
	/**
	 * ���������Ż��¼�
	 * @param nowSlice
	 * @return
	 */
	public List<ClusterEvent> analyze(ClusterSlice<V, E> nowSlice) {
		this.nowSlice = nowSlice;
		List<ClusterEvent> result = new LinkedList<ClusterEvent>();
		
		result.addAll(judgeContinueEvents(nowSlice));
		result.addAll(judgeKMergeEvents(nowSlice));
		result.addAll(judgeKSplitEvents(nowSlice));
		result.addAll(judgeFormEvents(nowSlice));
		result.addAll(judgeDissolveEvents(nowSlice));
			
		return result;
	}

	/**
	 * ��������¼�
	 * @param nowSlice
	 * @return
	 */
	public List<ClusterEvent> judgeContinueEvents(ClusterSlice<V, E> nowSlice) {
		this.nowSlice = nowSlice;
		List<ClusterEvent> result;
		ClusterContinueJudger<V> judger = new ClusterContinueJudger<V>();
		result = judger.judge(preSlice.getClusters(), nowSlice.getClusters());

		return result;
	}
	
	/**
	 * ���ϲ��¼�
	 * @param nowSlice
	 * @return
	 */
	public List<ClusterEvent> judgeKMergeEvents(ClusterSlice<V, E> nowSlice) {
		this.nowSlice = nowSlice;
		List<ClusterEvent> result = new LinkedList<ClusterEvent>();
		// TODO Auto-generated method stub
		return result;
	}
	
	/**
	 * �������¼�
	 * @param nowSlice
	 * @return
	 */
	public List<ClusterEvent> judgeKSplitEvents(ClusterSlice<V, E> nowSlice) {
		this.nowSlice = nowSlice;
		List<ClusterEvent> result = new LinkedList<ClusterEvent>();
		// TODO Auto-generated method stub
		return result;
	}
	
	/**
	 * ����γ��¼�
	 * @param nowSlice
	 * @return
	 */
	public List<ClusterEvent> judgeFormEvents(ClusterSlice<V, E> nowSlice) {
		this.nowSlice = nowSlice;
		List<ClusterEvent> result = new LinkedList<ClusterEvent>();
		// TODO Auto-generated method stub
		return result;
	}

	/**
	 * ���ֽ��¼�
	 * @param preClusterId
	 * @param nowClusterId
	 * @return
	 */
	public List<ClusterEvent> judgeDissolveEvents(ClusterSlice<V, E> nowSlice) {
		this.nowSlice = nowSlice;
		List<ClusterEvent> result = new LinkedList<ClusterEvent>();
		// TODO Auto-generated method stub
		return result;
	}

	/**
	 * Ĭ�ϵ�transform���������¼�����
	 */
	@Override
	public List<ClusterEvent> transform(ClusterSlice<V, E> nowSlice) {		
		return analyze(nowSlice);
	}
}
