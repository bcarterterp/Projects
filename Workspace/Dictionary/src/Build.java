import java.io.File;

import de.tudarmstadt.ukp.jwktl.JWKTL;


public class Build {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	    File dumpFile = new File("/home/progterp/Downloads/enwiktionary-20131217-pages-articles.xml.bz2");
	    File outputDirectory = new File("/home/progterp/Desktop/Stuff");
	    boolean overwriteExisting = true;
	      
	    JWKTL.parseWiktionaryDump(dumpFile, outputDirectory, overwriteExisting);
	  }
}
