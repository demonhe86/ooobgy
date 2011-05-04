package edu.zju.cs.ooobgy.algo.dynamic_na.pojo;

import java.awt.Color;
import java.util.Set;
import java.util.UUID;

/**
 * ���б�ǵ�cluster�������㼯��ǰ����ɫ��id
 * @author frogcherry ������
 * @created 2011-5-4
 * @Email frogcherry@gmail.com
 */
public class IdCluster<V> {
	private String id;
	private Set<V> vertexes;
	private Color color;
	
	
	
	public IdCluster(String id, Set<V> vertexes, Color color) {
		super();
		this.id = id;
		this.vertexes = vertexes;
		this.color = color;
	}

	public IdCluster(Set<V> vertexes, Color color) {
		super();
		this.vertexes = vertexes;
		this.color = color;
		this.id = UUID.randomUUID().toString();
	}
	
	public boolean containsVertex(V v){
		return vertexes.contains(v);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Set<V> getVertexes() {
		return vertexes;
	}
	public void setVertexes(Set<V> vertexes) {
		this.vertexes = vertexes;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("cluster:[");
		sb.append(id).append(", ");
		sb.append(vertexes).append("]");
				
		return sb.toString();
	}
}
