package com.taobao.dw.pizza.path_analysis.biz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogBean {
	
	public LogBean(){};
	
	public LogBean(String mid, String isUser,String nodeFeature,String url,String isRefer,String logTime,String urlWithParam){
		this.mid = mid;
		this.isUser = isUser;
		this.nodeFeature = nodeFeature; 
		this.url = url;
		this.isRefer = isRefer;
		this.logTime = logTime;
		this.urlWithParam = urlWithParam;
	}
	
	private String uid;
	
	private String mid;
	
	//1Ϊ��Ա,0Ϊ�ÿ�
	private String isUser;
	
	private String nodeFeature;
	
	private String url;
	
	private String logTime;
	
	//url�������httpheader�в����ϳɵ�url
	private String urlWithParam;
	
	public String getUrlWithParam() {
		return urlWithParam;
	}

	public void setUrlWithParam(String urlWithParam) {
		this.urlWithParam = urlWithParam;
	}

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	//1Ϊrefer,0Ϊurl
	private String isRefer;
	
	public String getIsRefer() {
		return isRefer;
	}

	public void setIsRefer(String isRefer) {
		this.isRefer = isRefer;
	}

	//�ӽڵ�(���ڶ��)
	private List<LogBean> nextPaths = new ArrayList<LogBean>();
	
	//���ڵ�(��һ����û�У����û�У���ýڵ�Ϊ��·������ʼ�ڵ�)
	private LogBean previous;

	public LogBean getPrevious() {
		return previous;
	}

	public void setPrevious(LogBean previous) {
		this.previous = previous;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getIsUser() {
		return isUser;
	}

	public void setIsUser(String isUser) {
		this.isUser = isUser;
	}

	public String getNodeFeature() {
		return nodeFeature;
	}

	public void setNodeFeature(String nodeFeature) {
		this.nodeFeature = nodeFeature;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	//��дhashCode,����Ϊ��˳��ƥ��ڵ����д����
	@Override
	public int hashCode(){
		return this.url.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false ;
		if(!(obj instanceof LogBean)) return false ;
		LogBean newlb =  (LogBean) obj;
		return (this.getUrl().equals(newlb.getUrl()));
	}
	
	private String getPreviousNodeFeture(){
		if(previous == null) return "-2";
		if(previous.getNodeFeature() == null) return "-2";
		return previous.getNodeFeature();
	}
	
	//��ȡҳ��ͣ��ʱ�䣬����ýڵ�û�к����ӽڵ���ͳ�ƣ�����-1
	//�����߼�Ϊ��
	private long getStayTime(){
		try {
			if(nextPaths == null || nextPaths.size() == 0) return -1;
			LogBean next = nextPaths.get(0);
			long staytime = Long.parseLong(next.getLogTime()) - Long.parseLong(this.getLogTime());
			return staytime > 180 ? 180 : staytime;
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	private String getNextNodeFeture(){
		String nextNodeFeture = "";
		Set<String> set = new HashSet<String>();
		//��set��ȥ��
		for (LogBean lb : nextPaths) {
			if(lb.getNodeFeature() == null) set.add("-2");
			else set.add(lb.getNodeFeature());
		}
		for(String o : set) nextNodeFeture += "," + o;
		if(nextNodeFeture.startsWith(",")) nextNodeFeture = nextNodeFeture.substring(1);
		if("".equals(nextNodeFeture)) nextNodeFeture = "-2";
		return nextNodeFeture;
	}
	
	//-1��ʾƥ�䲻�Ͻڵ�,-2��ʾ�ڵ�Ϊ��
	private String getNodeContext(){
		return getNodeFeature() + "_" + getPreviousNodeFeture() + "_" + getNextNodeFeture(); 
	}

	@Override
	public String toString() {
		return "" 
		+ "\"url\":\"" +  url + "\","
		+ "\"nodeFeature\":\"" + nodeFeature + "\","
		+ "\"isUser\":\"" + isUser + "\","
		+ "\"mid\":\"" + mid + "\","
		+ "\"isRefer\":\"" + isRefer + "\","
		+ "\"logTime\":\"" + logTime + "\","
		+ "\"nodeContext\":\"" + getNodeContext() + "\","
		+ "\"stayTime\":\"" + getStayTime() + "\","
		+ "\"urlWithParam\":\"" + getUrlWithParam() + "\""
		+ "]";
	}

	public List<LogBean> getNextPaths() {
		return nextPaths;
	}

	public void setNextPaths(List<LogBean> nextPaths) {
		this.nextPaths = nextPaths;
	}

}
