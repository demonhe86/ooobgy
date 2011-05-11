package edu.zju.cs.ooobgy.algo.dynamic_na.event;

/**
 * �Ż�ֽ��¼�Dissolve
 * @author frogcherry ������
 * @created 2011-5-11
 * @Email frogcherry@gmail.com
 */
public class ClusterDissolveEvent extends ClusterEvent {
	
	
	public ClusterDissolveEvent(String clusterId, String eventType) {
		super(clusterId, "dissolve");
	}
	
	@Override
	public String toString() {
		return toShortString();
	}
}
