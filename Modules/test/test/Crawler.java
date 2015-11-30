package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Crawler {
	public static void main(String args[]) {
		URL url;
		int responsecode;
		HttpURLConnection urlConnection;
		BufferedReader reader;
		String line;
		String[] urls = { "http://www.sin11a.com.cn", "http://www.sina.com.cn" };
		for (int i = 0; i < urls.length; i++) {
			try {
				url = new URL(urls[i]);
				// 打开URL
				urlConnection = (HttpURLConnection) url.openConnection();
				// 获取服务器响应代码
				responsecode = urlConnection.getResponseCode();
				if (responsecode == 200) {
					// 得到输入流，即获得了网页的内容
					reader = new BufferedReader(new InputStreamReader(
							urlConnection.getInputStream(), "GBK"));
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				} else {
					System.out.println("获取不到网页的源码，服务器响应代码为：" + responsecode);
				}
			} catch (MalformedURLException e) {
				System.out.println("URL错误");
			} catch (IOException e) {
				System.out.println("URL连接异常");
			}
		}
	}
}
