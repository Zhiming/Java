package ac.nz.unitec.webcrawler.constant;

public class PathConstants {
	public static String root = (Thread.currentThread().getContextClassLoader()
			.getResource("").getPath().substring(1)).substring(0, Thread
			.currentThread().getContextClassLoader().getResource("").getPath()
			.substring(1).lastIndexOf("classes"));
	
	public static final String LUCENE_DATA_PATH = "lucene_dat";
}
