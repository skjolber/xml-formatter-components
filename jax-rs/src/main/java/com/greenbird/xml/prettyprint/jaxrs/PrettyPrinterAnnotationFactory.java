package com.greenbird.xml.prettyprint.jaxrs;

import com.greenbird.xml.prettyprinter.PrettyPrinter;
import com.greenbird.xml.prettyprinter.PrettyPrinterFactory;

public class PrettyPrinterAnnotationFactory {

	private static final PrettyPrinter defaultPrettyPrinter;
	static {
		PrettyPrinterFactory factory = new PrettyPrinterFactory();
		
		defaultPrettyPrinter = factory.newPrettyPrinter();
	}
	
	public PrettyPrinter getPrettyPrinter(PrettyPrint annotation) {
		
		if(annotation == null) {
			return defaultPrettyPrinter;
		}
		
		boolean robustness = annotation.robustness();
		boolean ignoreWhitespace = annotation.ignoreWhitespace();
		boolean prettyPrintCData = annotation.prettyPrintCData();
		boolean prettyPrintComments = annotation.prettyPrintComments();
		boolean prettyPrintTextNodes = annotation.prettyPrintTextNodes(); // i.e. XML only
		boolean xmlDeclaration = annotation.xmlDeclaration();

		int maxTextNodeLength = annotation.maxTextNodeLength();
		int maxCDATANodeLength = annotation.maxCDATANodeLength();
		
		String[] anonymizeFilters = annotation.anonymizeFilters();
		String[] pruneFilters = annotation.pruneFilters();
		
		PrettyPrinter prettyPrinter;
		if(
				ignoreWhitespace || 
				prettyPrintCData || 
				prettyPrintComments || 
				prettyPrintTextNodes ||
				robustness || 
				xmlDeclaration ||
				maxTextNodeLength != -1 ||
				maxCDATANodeLength != -1 ||
				(anonymizeFilters != null && anonymizeFilters.length > 0) ||
				(pruneFilters != null && pruneFilters.length > 0)
				) {
			PrettyPrinterFactory factory = new PrettyPrinterFactory();
			factory.setRobustness(robustness);
			factory.setIgnoreWhitespace(ignoreWhitespace);
			factory.setPrettyPrintCData(prettyPrintCData);
			factory.setPrettyPrintComments(prettyPrintComments);
			factory.setPrettyPrintTextNodes(prettyPrintTextNodes);
			factory.setXmlDeclaration(xmlDeclaration);
			
			factory.setMaxTextNodeLength(maxTextNodeLength);
			factory.setMaxCDATANodeLength(maxCDATANodeLength);
			
			factory.setAnonymizeFilters(anonymizeFilters);
			factory.setPruneFilters(pruneFilters);

			prettyPrinter = factory.newPrettyPrinter();
		} else {
			prettyPrinter = defaultPrettyPrinter;
		}
		return prettyPrinter;
	}

}
