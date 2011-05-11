package edu.zju.cs.ooobgy.algo.dynamic_na.mapper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.collections15.Transformer;

import edu.zju.cs.ooobgy.algo.dynamic_na.pojo.ClusterSlice;
import edu.zju.cs.ooobgy.algo.dynamic_na.pojo.IdCluster;
import edu.zju.cs.ooobgy.algo.dynamic_na.qualify.ClusterSimilarity;
import edu.zju.cs.ooobgy.algo.math.BestMatrixSum;
import edu.zju.cs.ooobgy.algo.math.matrix.Matrix;
import edu.zju.cs.ooobgy.app.cache.DCD_Cache;

/**
 * ��2����Ƭ�����Ż�ӳ�䣬��ǰһʱ��Ƭ����ƬΪ��������ӳ����
 * ����map����transform�Դ������Ƭ����ӳ�䴦��.ע�⣬�˴���Ĭ���ƻ������slice;preSlice����
 * 
 * @author frogcherry ������
 * @created 2011-5-4
 * @Email frogcherry@gmail.com
 */
public class ClusterSliceMapper<V, E> implements
		Transformer<ClusterSlice<V, E>, ClusterSlice<V, E>> {
	private ClusterSlice<V, E> preSlice;

	public ClusterSliceMapper(ClusterSlice<V, E> preSlice) {
		super();
		this.preSlice = preSlice;
	}

	@Override
	public ClusterSlice<V, E> transform(ClusterSlice<V, E> nowSlice) {
		return map(nowSlice);
	}

	public ClusterSlice<V, E> map(ClusterSlice<V, E> nowSlice) {
		// 1.�������ƶȾ���
		int preC_cnt = preSlice.getClusters().size();
		//System.err.println(preSlice.getClusters());//debug
		int nowC_cnt = nowSlice.getClusters().size();
		List<IdCluster<V>> preClusters = new ArrayList<IdCluster<V>>(preSlice
				.getClusters().values());
		List<IdCluster<V>> nowClusters = new ArrayList<IdCluster<V>>(nowSlice
				.getClusters().values());
		Matrix mapMatrix = new Matrix(nowC_cnt, preC_cnt);
		for (int i = 0; i < preC_cnt; i++) {
			ClusterSimilarity<V> clusterSimilarity = new ClusterSimilarity<V>(
					preClusters.get(i).getVertexes());
			for (int j = 0; j < nowC_cnt; j++) {
				Double jaccard = clusterSimilarity.transform(
						nowClusters.get(j).getVertexes());
				mapMatrix.setElement(j, i, jaccard);
			}
		}
		//System.err.println(mapMatrix);//debug
		// 2.���ƶ�ӳ��
		BestMatrixSum kmMapper = new BestMatrixSum(mapMatrix);
		kmMapper.completeBestSumCombination(true);//Ĭ��jacֵԽ�������Խ��
		Map<Integer, Integer> bestMap = kmMapper.getCombination();//ӳ����
		//TODO 3.����д��ӳ������������Ҫд�ص����ݿ���
		nowSlice.clearClusters();//��վɵ���Ϣ
		for (Entry<Integer, Integer> now_pre : bestMap.entrySet()) {
			int now_i = now_pre.getKey();
			int pre_i = now_pre.getValue();
			if (now_i >= nowC_cnt) {//nowkeyԽ��˵����now_slice�����ڸ���
				continue;
			}
			
			Color color = Color.black;
			IdCluster<V> nowCluster = nowClusters.get(now_i);
			String id = nowCluster.getId();
			if (pre_i >= preC_cnt) {//ƥ��Խ��˵����pre_slice���治ƥ���now_slice��
				//TODO ��ɫ��ָ���㷨����Ҫ����
				color = DCD_Cache.similarColors[now_i % DCD_Cache.similarColors.length];
			} else {//��ƥ������
				id = preClusters.get(pre_i).getId();
				color = preClusters.get(pre_i).getColor();
			}
			nowCluster.setColor(color);
			nowCluster.setId(id);
			nowSlice.addCluster(id, nowCluster);
		}
		
		return nowSlice;
	}
}
