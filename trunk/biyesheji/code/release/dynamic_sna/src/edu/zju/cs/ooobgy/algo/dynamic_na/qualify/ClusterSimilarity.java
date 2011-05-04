package edu.zju.cs.ooobgy.algo.dynamic_na.qualify;

import java.util.Set;

import org.apache.commons.collections15.Transformer;

/**
 * Jaccard's coefficient and Adamic/Adar
 * ���������Ż�C1,C2�����ƶ� = |C1 �� C@| / |C1 �� C2| </br>
 * ����֪C1,Ȼ����C2���м���
 * @author frogcherry ������
 * @created 2010-12-18
 * @Email frogcherry@gmail.com
 * @see "David Liben-Nowell: The Link Prediction Problem for Social Networks"
 */
public class ClusterSimilarity<V> implements Transformer<Set<V>, Double>{
	private Set<V> c1;
	
	public ClusterSimilarity(Set<V> c1) {
		super();
		this.c1 = c1;
	}

	/**
	 * ���������Ż�C1,C2�����ƶ� = |C1 �� C@| / |C1 �� C2| </br>
	 * ����֪C1,Ȼ����C2���м���
	 */
	@Override
	public Double transform(Set<V> c2) {
		int intersectionSize = 0;//�����Ĵ�С
		int unionSize = 0;//�����Ĵ�С
		
		for (V vertex : c1) {
			if (c2.contains(vertex)) {
				intersectionSize ++;
			}
		}
		unionSize = c1.size() + c2.size() - intersectionSize;
		
		return new Double(intersectionSize) / new Double(unionSize);
	}

}
