package edu.zju.cs.ooobgy.algo.dynamic_na.pojo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
	private Map<String, IdCluster<V>> clusters;//�Ż������Key���Ż�id��value�ǵ�ǰ��Ƭ���Ż�vertex���
	private List<E> removedEdges;//��ȥ�ı�

	public Set<Set<V>> getClusterSet(){
		Set<Set<V>> cls = new HashSet<Set<V>>();
		for (IdCluster<V> idC : clusters.values()) {
			cls.add(idC.getVertexes());
		}
		
		return cls;
	}
	
	/**
	 * ������е��Ż�
	 */
	public void clearClusters(){
		clusters.clear();
	}
	
	public String getVertexClusterId(V v){
		for (IdCluster<V> cluster : clusters.values()) {
			if (cluster.containsVertex(v)) {
				return cluster.getId();
			}
		}
		
		return "NULL";
	}
	
	public void setSliceId(String sliceId) {
		this.sliceId = sliceId;
	}


	public void setGraph(ClusterGraph<V, E> graph) {
		this.graph = graph;
	}


	public void setRemovedEdges(List<E> removedEdges) {
		this.removedEdges = removedEdges;
	}


	/**
	 * ȫ����
	 * @param sliceId
	 * @param graph
	 * @param clusters
	 * @param removedEdges
	 */
	public ClusterSlice(String sliceId, ClusterGraph<V, E> graph,
			Map<String, IdCluster<V>> clusters, List<E> removedEdges) {
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
	public ClusterSlice(String sliceId, ClusterGraph<V, E> graph) {
		super();
		this.sliceId = sliceId;
		this.graph = graph;
		this.removedEdges = new LinkedList<E>();
		this.clusters = new HashMap<String, IdCluster<V>>();
	}
	
	/**
	 * ����(����)һ���Ż������Ϣ
	 */
	public void addCluster(String clusterId, IdCluster<V> cluster){
		clusters.put(clusterId, cluster);
	}


	public Map<String, IdCluster<V>> getClusters() {
		return clusters;
	}


	public void setClusters(Map<String, IdCluster<V>> clusters) {
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