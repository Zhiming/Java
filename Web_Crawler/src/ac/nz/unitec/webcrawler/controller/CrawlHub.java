package ac.nz.unitec.webcrawler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ac.nz.unitec.webcrawler.model.impl.Spider;

@Controller
public class CrawlHub {
	
	private Spider spider;

	@RequestMapping(value = "/crawl", method = RequestMethod.POST)
	public String startCrawl(ModelMap model, String url, String strDepth) {
		int depth = -1;
		String msg = "";
		try{
			depth = Integer.valueOf(strDepth);
		}catch(NumberFormatException nfe){
			msg = "Invalid depth";
			model.addAttribute("message", msg);
			return "index";
		}
		spider.startCrawl(url, depth);
		model.addAttribute("message", "crawl completed");
		return "search";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(ModelMap model, String keyword){
		model.addAttribute("results", spider.search(keyword));
		return "search";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchForGet(ModelMap model, String keyword){
		model.addAttribute("results", spider.search(keyword));
		return "search";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String test(ModelMap model) {
		return "index";
	}

	public Spider getSpider() {
		return spider;
	}

	public void setSpider(Spider spider) {
		this.spider = spider;
	}
}
