package ac.nz.unitec.webcrawler.model.intf;

import java.util.List;

public interface IHttpParser {
	public List<String> parseLinks(String content);
}
