package ac.nz.unitec.webcrawler.model.intf;

public interface IIndexService {

	public boolean createIndex(String urlStr, String content);
	public String removeHTMLTags(String webContent);
}
