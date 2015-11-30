package ac.nz.unitec.webcrawler.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ac.nz.unitec.webcrawler.model.intf.IHttpClient;
import ac.nz.unitec.webcrawler.model.intf.IHttpParser;
import ac.nz.unitec.webcrawler.model.intf.IIndexService;
import ac.nz.unitec.webcrawler.model.intf.ISearchService;

public class Spider {

	private IHttpClient client;
	private IHttpParser parser;
	private IIndexService indexService;
	private ISearchService searchService;
	private HashSet<String> alreadVisitedUrls;
	private HashSet<String> alreadVisitedContents;
	private HashSet<String> waitingUrls;
	private HashMap<String,String> waitingContents;
	private HashSet<String> invalidUrls;
	//�������
	//crawling depth
	private int parsedCount = 0;

	
	public void startCrawl(String strUrl, int depth) {
		System.out.println("crawling...");
		// Initial crawling
		// ��ʼ�״���
		if (alreadVisitedUrls.isEmpty()) {
			initCrawl(strUrl);
		}
		
		//���ǵ�һ����
		//not initial crawling
		while(!waitingContents.isEmpty() && parsedCount < depth){
			// ��ȡ��������ҳ�е�url
			// extract urls in web page
			webContentProcess();
			// ������ҳ
			// download web page
			downloadWebPage();
			parsedCount++;
		}
	}
	
	private void initCrawl(String strUrl) {
		// ����url��������ҳ����
		// download web content
		if (strUrl != null && strUrl != "") {
			// if it's a valid url, then put it in the already visited set
			// ���������Ч��������Ѿ����ʹ��ļ�����
			if (client.get(strUrl, waitingContents)) {
				alreadVisitedUrls.add(strUrl);
			} else {
				// invalid url
				// ��Чurl
				invalidUrls.add(strUrl);
			}
		}
	}
	
	private void downloadWebPage() {
		for (String waitingUrl : waitingUrls) {
			// û�д�������Ӳŷ��ʲ�������
			// only download urls that are not visited before
			if ((!alreadVisitedUrls.contains(waitingUrl))
					&& (!invalidUrls.contains(waitingUrl))) {
				if (client.get(waitingUrl, waitingContents)) {
					alreadVisitedUrls.add(waitingUrl);
				} else {
					invalidUrls.add(waitingUrl);
				}
			}
		}
		// ����������Ӽ��ϣ���ֹ�´�ѭ�������ظ�
		waitingUrls.clear();
	}
	
	/**
	 * ��ȡurl��ȥ��html��ǩ�󣬽���lucene������
	 * extract url, remove html tags and create lucene indices
	 */
	private void webContentProcess() {
		
		for (Entry<String, String> webEntry : waitingContents.entrySet()) {
			String webContent = webEntry.getValue();
			String url = webEntry.getKey();
			// ��ȡurl
			// extract url in the content
			List<String> urlList = this.parser.parseLinks(webContent);
			for (String extractedUrl : urlList) {
				// ���HashSet�и�Ԫ���Ѿ����ڣ��򲻻��ټӽ�ȥ�����Բ����ٵ������
				// if the url exists, then it won't be added in
				waitingUrls.add(extractedUrl);
			}
			
			//��html��ǩȥ��
			//remove html tags
			webContent = indexService.removeHTMLTags(webContent);
			
			//����Lucene������
			//create index in Lucene
			indexService.createIndex(url, webContent);
			
			// ����ҳ���ݴ������������������棬Ӧ���ǲ���Ҫ��
			//though this might be not necessary
			alreadVisitedContents.add(webContent);
		}

		// ��ռ��ϣ���Ȼ�´�ѭ��ʱ���ظ�����ҳ
		// clear the set, otherwise, duplicate contents will be processed in
		// next while loop
		waitingContents.clear();
	}
	
	public Map<String, String> search(String keyword){
		return searchService.search(keyword);
	}
	
	public IHttpClient getClient() {
		return client;
	}

	public void setClient(IHttpClient client) {
		this.client = client;
	}

	public IHttpParser getParser() {
		return parser;
	}

	public void setParser(IHttpParser parser) {
		this.parser = parser;
	}

	public HashSet<String> getAlreadVisitedUrls() {
		return alreadVisitedUrls;
	}

	public void setAlreadVisitedUrls(HashSet<String> alreadVisitedUrls) {
		this.alreadVisitedUrls = alreadVisitedUrls;
	}

	public void setInvalidUrls(HashSet<String> invalidUrls) {
		this.invalidUrls = invalidUrls;
	}
	
	public HashSet<String> getInvalidUrls() {
		return invalidUrls;
	}
	
	public void setWaitingUrls(HashSet<String> waitingUrls) {
		this.waitingUrls = waitingUrls;
	}
	
	public HashSet<String> getWaitingUrls() {
		return waitingUrls;
	}
	
	public HashMap<String, String> getWaitingContents() {
		return waitingContents;
	}
	
	public void setWaitingContents(HashMap<String, String> waitingContents) {
		this.waitingContents = waitingContents;
	}
	
	public HashSet<String> getAlreadVisitedContents() {
		return alreadVisitedContents;
	}
	
	public void setAlreadVisitedContents(HashSet<String> alreadVisitedContents) {
		this.alreadVisitedContents = alreadVisitedContents;
	}
	public IIndexService getIndexService() {
		return indexService;
	}
	public void setIndexService(IIndexService indexService) {
		this.indexService = indexService;
	}
	
	public ISearchService getSearchService() {
		return searchService;
	}
	
	public void setSearchService(ISearchService searchService) {
		this.searchService = searchService;
	}
}
