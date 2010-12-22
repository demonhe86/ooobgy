package edu.zju.cs.ooobgy.algo.dynamic_na.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.zju.cs.ooobgy.graph.ClusterGraph;

/**
 * һ��(ʱ����Ƭ)��ͼ�Ż����
 * @author frogcherry ������
 * @created 2010-12-22
 * @Email frogcherry@gmail.com
 */
public class ClusterSlice<V, E> {
	private String sliceId;//��Ƭ��ʶid������201012
	private ClusterGraph<V, E> graph;//�����Ƭ��Ӧ��ͼ
	private Map<String, Set<V>> clusters;//�Ż������Key���Ż�id��value�ǵ�ǰ��Ƭ���Ż�vertex���
	private List<E> removedEdges;//��ȥ�ı�
	
	/**
	 * ȫ����
	 * @param sliceId
	 * @param graph
	 * @param clusters
	 * @param removedEdges
	 */
	public ClusterSlice(String sliceId, ClusterGraph<V, E> graph,
			Map<String, Set<V>> clusters, List<E> removedEdges) {
		super();
		this.sliceId = sliceId;
		this.graph = graph;
		this.clusters = clusters;
		this.removedEdges = removedEdges;
	}


	/**
	 * �½�һ���Ż�map�Ĺ��죬ʹ�øù�������Ҫʹ��putCluster�������Ż�ṹ
	 * @param sliceId
	 * @param graph
	 * @param removedEdges
	 */
	public ClusterSlice(String sliceId, ClusterGraph<V, E> graph,
			List<E> removedEdges) {
		super();
		this.sliceId = sliceId;
		this.graph = graph;
		this.removedEdges = removedEdges;
		this.clusters = new HashMap<String, Set<V>>();
	}
	
	/**
	 * ����(����)һ���Ż������Ϣ
	 */
	public void putCluster(String clusterId, Set<V> cluster){
		clusters.put(clusterId, cluster);
	}


	public Map<String, Set<V>> getClusters() {
		return clusters;
	}


	public void setClusters(Map<String, Set<V>> clusters) {
		this.clusters = clusters;
	}


	public String getSliceId() {
		return sliceId;
	}


	public ClusterGraph<V, E> getGraph() {
		return graph;
	}


	public List<E> getRemovedEdges() {
		return removedEdges;
	}
	
	
}