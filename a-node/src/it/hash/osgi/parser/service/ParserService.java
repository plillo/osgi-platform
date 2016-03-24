package it.hash.osgi.parser.service;


public interface ParserService {
	String getAppCode();
	boolean createCollectionBy(String url, String nomefile);
}