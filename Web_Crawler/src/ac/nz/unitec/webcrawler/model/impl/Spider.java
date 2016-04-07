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
	//爬的深度
	//crawling depth
	private int parsedCount = 0;

	
	public void startCrawl(String strUrl, int depth) {
		System.out.println("crawling...");
		// Initial crawling
		// 开始首次爬
		if (alreadVisitedUrls.isEmpty()) {
			initCrawl(strUrl);
		}
		
		//不是第一次爬
		//not initial crawling
		while(!waitingContents.isEmpty() && parsedCount < depth){
			// 提取待解析网页中的url
			// extract urls in web page
			webContentProcess();
			// 下载网页
			// download web page
			downloadWebPage();
			parsedCount++;
		}
	}
	
	private void initCrawl(String strUrl) {
		// 访问url，下载网页内容
		// download web content
		if (strUrl != null && strUrl != "") {
			// if it's a valid url, then put it in the already visited set
			// 如果连接有效，则放在已经访问过的集合中
			if (client.get(strUrl, waitingContents)) {
				alreadVisitedUrls.add(strUrl);
			} else {
				// invalid url
				// 无效url
				invalidUrls.add(strUrl);
			}
		}
	}
	
	private void downloadWebPage() {
		for (String waitingUrl : waitingUrls) {
			// 没有处理过连接才访问并且下载
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
		// 待处理的连接集合，防止下次循环出现重复
		waitingUrls.clear();
	}
	
	/**
	 * 提取url，去除html标签后，建立lucene的索引
	 * extract url, remove html tags and create lucene indices
	 */
	private void webContentProcess() {
		
		for (Entry<String, String> webEntry : waitingContents.entrySet()) {
			String webContent = webEntry.getValue();
			String url = webEntry.getKey();
			// 提取url
			// extract url in the content
			List<String> urlList = this.parser.parseLinks(webContent);
			for (String extractedUrl : urlList) {
				// 如果HashSet中该元素已经存在，则不会再加进去，所以不用再单独检查
				// if the url exists, then it won't be added in
				waitingUrls.add(extractedUrl);
			}
			
			//将html标签去除
			//remove html tags
			webContent = indexService.removeHTMLTags(webContent);
			
			//建立Lucene的索引
			//create index in Lucene
			indexService.createIndex(url, webContent);
			
			// 把网页内容存起来，用作搜索引擎，应该是不需要了
			//though this might be not necessary
			alreadVisitedContents.add(webContent);
		}

		// 清空集合，不然下次循环时有重复的网页
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
