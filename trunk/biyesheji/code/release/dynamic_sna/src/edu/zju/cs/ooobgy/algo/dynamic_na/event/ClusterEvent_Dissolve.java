package edu.zju.cs.ooobgy.algo.dynamic_na.event;

public class ClusterEvent_Dissolve extends ClusterEvent {
	
	
	public ClusterEvent_Dissolve(String clusterId, String eventType) {
		super(clusterId, "dissolve");
	}

	/**
	 * �ó���cluster id
	 * @return
	 */
	public String toLongString() {
		StringBuilder sb = new StringBuilder();
		sb.append(clusterId);
		sb.append("\t: <");
		sb.append(eventType);
		sb.append(">    ");
		
		return sb.toString( );
	}
	
	/**
	 * �ö̵�cluster id
	 * @return
	 */
	public String toShortString() {
		StringBuilder sb = new StringBuilder();
		sb.append(clusterId.substring(28));
		sb.append("\t: <");
		sb.append(eventType);
		sb.append(">    ");
		
		return sb.toString( );
	}
	
	@Override
	public String toString() {
		return toShortString();
	}
}
