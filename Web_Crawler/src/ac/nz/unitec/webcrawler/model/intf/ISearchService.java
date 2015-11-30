package ac.nz.unitec.webcrawler.model.intf;

import java.util.Map;

public interface ISearchService {
	public Map<String, String> search(String queryStr);
}
