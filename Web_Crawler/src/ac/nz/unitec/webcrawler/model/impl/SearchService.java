package ac.nz.unitec.webcrawler.model.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import ac.nz.unitec.webcrawler.constant.PathConstants;
import ac.nz.unitec.webcrawler.constant.SearchConstants;
import ac.nz.unitec.webcrawler.model.intf.ISearchService;

public class SearchService implements ISearchService{
	
	/**
	 * Search the keyword and return url with an abstract
	 */
	@Override
	public Map<String, String> search(String keyword) {
		
		Directory directory;
		IndexSearcher searcher;
		IndexReader reader;
		QueryParser parser;
		Query query;
		TopDocs tds;
		ScoreDoc[] sds;
		Document doc;
		Map<String,String> results = new HashMap<String, String>();

		try {
			directory = FSDirectory.open(new File(PathConstants.root + PathConstants.LUCENE_DATA_PATH));
			reader = IndexReader.open(directory);
			searcher = new IndexSearcher(reader);
			parser = new QueryParser(Version.LUCENE_36, SearchConstants.LUCENE_CONTENT, new StandardAnalyzer(
							Version.LUCENE_36));
			query = parser.parse(keyword);
			tds = searcher.search(query, 80);
			sds = tds.scoreDocs;

			for (ScoreDoc sd : sds) {
				doc = searcher.doc(sd.doc);
				String abstractStr = doc.get(SearchConstants.LUCENE_CONTENT);
				abstractStr = highlight(new StandardAnalyzer(Version.LUCENE_36), query,SearchConstants.LUCENE_CONTENT, abstractStr);
				results.put(doc.get(SearchConstants.LUCENE_URL),abstractStr);
			}

			if (null != searcher)
				searcher.close();
			if (null != reader)
				reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * Highlight the keyword in the abstract
	 * @param a
	 * @param query
	 * @param fieldName
	 * @param txt
	 * @return
	 */
	private String highlight(Analyzer a, Query query, String fieldName, String txt) {
		String str = null;
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter fmt = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
		Highlighter lighter = new Highlighter(fmt, scorer);
		lighter.setTextFragmenter(fragmenter);
		try {
			str = lighter.getBestFragment(a, fieldName, txt);
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
}
