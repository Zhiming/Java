package ac.nz.unitec.webcrawler.model.impl;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ac.nz.unitec.webcrawler.constant.PathConstants;
import ac.nz.unitec.webcrawler.constant.SearchConstants;
import ac.nz.unitec.webcrawler.model.intf.IIndexService;

public class IndexServiceImpl implements IIndexService{
	
	private static Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);
	
	@Override
	public String removeHTMLTags(String webContent){
		return Jsoup.parse(webContent).text();
	}
	
	@Override
	public boolean createIndex(String urlStr, String content) {
		IndexWriterConfig iwc;
		IndexWriter writer = null;
		Document doc = null;
		boolean flag = false;
		try {
			String path = PathConstants.root + PathConstants.LUCENE_DATA_PATH;
			Directory directory = FSDirectory.open(new File(path));
			iwc = new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
			writer = new IndexWriter(directory, iwc);
			doc = new Document();
			doc.add(new Field(SearchConstants.LUCENE_URL, urlStr, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field(SearchConstants.LUCENE_CONTENT, content, Field.Store.YES, Field.Index.ANALYZED));
			writer.addDocument(doc);
			flag = true;
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
}
