package ac.nz.unitec.webcrawler.model.intf;

import java.util.Map;

public interface IHttpClient {
	/**
	 * 访问url，下载其内容
	 * download the url content
	 * @param strUrl
	 * @param waitingContents
	 * @return
	 */
	public boolean get(String strUrl, Map<String,String> waitingContents);
}
