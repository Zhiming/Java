package ac.nz.unitec.webcrawler.constant;

import java.util.regex.Pattern;

public class RegularExpressionConstants {
	
	public static Pattern URLREGEXPPATTERN = Pattern.compile(
					"((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)",
					Pattern.CASE_INSENSITIVE);
	
//	public static final Pattern URLREGEXPPATTERN = Pattern.compile(
//			"(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
//					+ "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
//					+ "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
//			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	
//	public static Pattern URLREGEXPPATTERN = Pattern
//			.compile(
//					"((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)",
//					Pattern.CASE_INSENSITIVE);
}
