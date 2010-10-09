package edu.zju.cs.ooobgy.mr.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @Author: ������
 * @created: 2010-7-21
 **/

public class HadoopLauncher {
	private Map<String, String> configMap = new HashMap<String, String>();

	public static void main(String[] args) {
		HadoopLauncher launcher = new HadoopLauncher();
		String configFilePath = null;
		if (args.length < 1) {
			System.err
					.println("Usage : HadoopLauncher [HadoopJobClass] {[ConfigFile]}");
			System.exit(1);
		} else if (args.length == 1) {
			String[] detail = args[0].split("\\.");
			String configFile = detail[detail.length - 1];
			configFilePath = configFile + ".xml";// Ĭ��������JobClassͬ��ͬ·���������ļ�
		} else if (args.length == 2) {
			configFilePath = args[1];
		}
		HadoopJob hadoopJob = null;
		try {
			hadoopJob = (HadoopJob) Class.forName(args[0]).newInstance();
		} catch (Exception e) {
			exitWithError(e, "Invalid HadoopJob Class");
		}
		JobConf conf = new JobConf(hadoopJob.getClass());
		conf.setMapperClass(hadoopJob.getMapper());
		conf.setReducerClass(hadoopJob.getReducer());
		launcher.configFromFile(configFilePath, conf);// �ȴ��ļ�����
		hadoopJob.configJob(conf);// �ٴӳ�������
		try {
			JobClient.runJob(conf);
		} catch (IOException e) {
			exitWithError(e, "Error : Loading Job error!");
		}
	}

	/**
	 * ���������ļ���HadoopJob��������
	 * 
	 * @see HadoopLauncher#configFromFile(String)
	 * @param configFilePath
	 * @param jobConf
	 * @return jobConf
	 */
	public JobConf configFromFile(String configFilePath, JobConf jobConf) {
		loadConfigFile(configFilePath);
		Set<String> configs = configMap.keySet();
		for (String config : configs) {
			jobConf.set(config, configMap.get(config));
		}
		return jobConf;
	}

	/**
	 * ʹ�ø÷������ļ�����Job��newһ��JobConf�����ú󷵻ظ�JobConf
	 * 
	 * @see HadoopLauncher#configFromFile(String, JobConf)
	 * @param configFilePath
	 * @return jobConf
	 */
	public JobConf configFromFile(String configFilePath) {
		return configFromFile(configFilePath, new JobConf());
	}

	/**
	 * �������ļ�(xml�ļ�)����������Ϣ
	 * 
	 * @param configFilePath
	 */
	private void loadConfigFile(String configFilePath) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
			Document doc = documentBuilder.parse(new File(configFilePath));
			Element root = doc.getDocumentElement();
			NodeList properties = root.getElementsByTagName("property");
			Element property;
			String name;
			String value;
			for (int i = 0; i < properties.getLength(); i++) {
				property = (Element) properties.item(i);
				name = property.getElementsByTagName("name").item(0)
						.getFirstChild().getNodeValue();
				value = property.getElementsByTagName("value").item(0)
						.getFirstChild().getNodeValue();
				this.configMap.put(name, value);
			}
		} catch (ParserConfigurationException e) {
			exitWithError(e, "Error : Can't load config parser!");
		} catch (SAXException e) {
			exitWithError(e,
					"Error : Parse config error!(Maybe xml file is not in the correct format)");
		} catch (IOException e) {
			exitWithError(e, "Error : Invalid config file.(file not exist)");
		}
	}

	private static void exitWithError(Exception e, String errorInfo) {
		System.err.println(errorInfo);
		e.printStackTrace();
		System.exit(1);
	}
}