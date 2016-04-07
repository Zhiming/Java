package ac.nz.unitec.webcrawler.model.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ac.nz.unitec.webcrawler.model.intf.IHttpClient;

public class HttpClientImpl implements IHttpClient {

	private static Logger logger = LoggerFactory.getLogger(HttpClientImpl.class);
	
	@Override
	public boolean get(String strUrl, Map<String, String> waitingContents) {
		boolean flag = false;
		BufferedReader reader = null;
		HttpURLConnection urlConn = null;
		URL url = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e1) {
			logger.debug("invalid url, the sent string is: " + strUrl);
		}
		try {
			urlConn = (HttpURLConnection) url.openConnection();
			// 获取服务器响应代码
			// get response code from server
			int responsecode = urlConn.getResponseCode();
			if (responsecode == 200) {
				// 得到输入流，即获得了网页的内容
				// input stream aka page content
				reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),  "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				String webContent = sb.toString();
				waitingContents.put(strUrl, webContent);
				flag = true;
			} else {
				logger.debug("Fail to download web source code, url: "
						+ urlConn.getURL() + " server response code is: " + responsecode);
			}
		} catch (IOException e) {
			logger.debug("Fail to access the url: " + url.toString());
		} catch (Exception e) {
			logger.debug(e.toString());
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(urlConn != null){
				urlConn.disconnect();
			}
		}
		return flag;
	}
}
