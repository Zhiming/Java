package ac.nz.unitec.webcrawler.model.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import ac.nz.unitec.webcrawler.constant.RegularExpressionConstants;
import ac.nz.unitec.webcrawler.model.intf.IHttpParser;

public class HttpParserImpl implements IHttpParser {
	
	/**
	 * extract url from web page
	 */
	@Override
	public List<String> parseLinks(String content) {
		List<String> urlList = new LinkedList<String>();
		Matcher m = RegularExpressionConstants.URLREGEXPPATTERN.matcher(content);
		while(m.find()){
			urlList.add(content.substring(m.start(0),m.end(0)));
		}
		return urlList;
	}
}
