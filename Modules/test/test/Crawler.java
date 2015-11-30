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
				// ��URL
				urlConnection = (HttpURLConnection) url.openConnection();
				// ��ȡ��������Ӧ����
				responsecode = urlConnection.getResponseCode();
				if (responsecode == 200) {
					// �õ������������������ҳ������
					reader = new BufferedReader(new InputStreamReader(
							urlConnection.getInputStream(), "GBK"));
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				} else {
					System.out.println("��ȡ������ҳ��Դ�룬��������Ӧ����Ϊ��" + responsecode);
				}
			} catch (MalformedURLException e) {
				System.out.println("URL����");
			} catch (IOException e) {
				System.out.println("URL�����쳣");
			}
		}
	}
}
