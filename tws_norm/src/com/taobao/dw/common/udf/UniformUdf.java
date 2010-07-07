package com.taobao.dw.common.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import com.taobao.dw.comm.tws.TwsNorm;

/**
 * ��һ��UDF
 * 
 * @author ����
 * @created 2010/07/02
 *
 */
public class UniformUdf extends UDF {
	private TwsNorm twsNorm;
	
	public String evaluate(String query){
		return getNormallizer().uniformQuery(query);		
	}
	
	private TwsNorm getNormallizer(){
		if (this.twsNorm == null){
			this.twsNorm = TwsNorm.getInstance();
			this.twsNorm.initOnHadoop();
		}
		
		return this.twsNorm;
	}
}
