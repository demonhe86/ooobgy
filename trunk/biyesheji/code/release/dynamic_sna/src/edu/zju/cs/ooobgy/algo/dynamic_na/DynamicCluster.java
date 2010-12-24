package edu.zju.cs.ooobgy.algo.dynamic_na;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import edu.zju.cs.ooobgy.algo.dynamic_na.pojo.ClusterSlice;
import edu.zju.cs.ooobgy.algo.math.matrix.Matrix;

/**
 * ��������Ƭ���ж�̬����
 * 
 * @author frogcherry ������
 * @created 2010-12-22
 * @Email frogcherry@gmail.com
 */
public class DynamicCluster<V, E> implements
		Transformer<ClusterSlice<V, E>, ClusterSlice<V, E>> {
	private ClusterSlice<V, E> preSlice;// ��֪����һ��Ƭ
	private Set<Set<V>> newSliceClusters;// ��֪�Ĵ�������Ƭ���Ż���Ϣ

	/**
	 * ʹ����֪����һ��Ƭ����֪�Ĵ�������Ƭ���Ż���Ϣ���й���; ��֪�����������޷����з���
	 * 
	 * @param preSlice
	 * @param newSliceClusters
	 */
	public DynamicCluster(ClusterSlice<V, E> preSlice,
			Set<Set<V>> newSliceClusters) {
		super();
		this.preSlice = preSlice;
		this.newSliceClusters = newSliceClusters;
	}

	/**
	 * �����µ���Ƭ������ͼ��Ϣ���������Ż��Ӧ��Ϣ��,������֪����һ��Ƭ���ж�Ӧ��̬����
	 */
	@Override
	public ClusterSlice<V, E> transform(ClusterSlice<V, E> newSlice) {
		ClusterSlice<V, E> slice = newSlice;
		List<Entry<String, Set<V>>> preCluster = new ArrayList<Entry<String, Set<V>>>(
				preSlice.getClusters().entrySet());//��֪�Ż��id��Ӧ�����תΪlist�����������
		List<Set<V>> newClusters = new ArrayList<Set<V>>(newSliceClusters);//�µ��Ż���Ϣ��������list
		Matrix similarityMatrix = new Matrix(preCluster.size(), newClusters.size());//���ƶȾ���
		
		//1.������ƶȾ���
		for (int i = 0; i < preCluster.size(); i++) {
			
		}
		
		// TODO Auto-generated method stub
		return slice;
	}

}
