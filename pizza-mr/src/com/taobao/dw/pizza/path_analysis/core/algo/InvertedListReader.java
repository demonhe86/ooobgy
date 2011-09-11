package com.taobao.dw.pizza.path_analysis.core.algo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.taobao.dw.pizza.path_analysis.core.PizzaConst;
import com.taobao.dw.pizza.path_analysis.core.pojo.AtomPath;
import com.taobao.dw.pizza.path_analysis.core.pojo.InvertedList;

/**
 * ��ȡ���ű����ݣ����쵹�ű�·�����ݽṹ
 * 
 * �����ʽΪ��
 * 			NodeId1-NodeId2^AAtomPath1^BAtomPath2^BAtomPath3..
 * �����ʽΪMap��List����ϣ�
 * 			{
 * 				"nodeId1+nodeId2": [AP1, AP2, AP3...],
 * 				"nodeId1-nodeId2":  [AP1, AP2, AP3...],
 * 				...........
 * 			}
 * 
 * @author ����
 * @modified: ������ 2011��9��10��08:05:11
 *
 */
public class InvertedListReader {
	InvertedList ips = new InvertedList();	//���ű�

	public InvertedList readInvertedList(String dataPath){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataPath), PizzaConst.UTF_8_ENCODING));
			return readInvertedList(reader);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException("Data Loader loading failed!", e);
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private void handleData(String line, InvertedList ips) {
		String[] values = line.split(PizzaConst.SPLIT);
		
		if (values.length < 2){
			System.err.println("Invalid line: " + line);
			return;
		}
		String atomPath = values[0];
		String[] pathExps =values[1].split(PizzaConst.SECOND_SPLIT); 

		List<AtomPath> aps = new ArrayList<AtomPath>();
		for (String pathExp:pathExps) {
			aps.add(new AtomPath(atomPath, pathExp));
			
//			ips.putHeadNodePathIndex(pathExp);
			ips.putHeadNodePathIndex(atomPath, pathExp);
		}
		ips.put(atomPath, aps);
	}

	public InvertedList readInvertedList(BufferedReader reader) throws IOException {
		this.ips.clean();
		String line = "";
		while ((line = reader.readLine()) != null) {
			try{
				handleData(line, ips);
			} catch (Throwable e) {
				System.err.println("Invalid Data��" + line);
				e.printStackTrace();
			}					
		}
		return ips;
	}
}
