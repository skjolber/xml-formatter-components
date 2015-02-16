package com.greenbird.xml.prettyprint.jaxrs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Assert;
import org.junit.Test;

import com.greenbird.xml.prettyprinter.PrettyPrinter;
import com.greenbird.xml.prettyprinter.plain.PlainPrettyPrinter;
import com.greenbird.xml.prettyprinter.plain.PlainPrettyPrinterForTextNodesWithXML;
import com.greenbird.xml.prettyprinter.plain.PlainPrettyPrinterWithMaxNodeLength;
import com.greenbird.xml.prettyprinter.plain.RobustPlainPrettyPrinter;
import com.greenbird.xml.prettyprinter.plain.filter.AbstractFilterPrettyPrinter;
import com.greenbird.xml.prettyprinter.plain.ws.PlainIndentedPrettyPrinter;

public class TestFactory {

	private static class Defaults implements InvocationHandler {
		  public static <A extends Annotation> A of(Class<A> annotation) {
		    return (A) Proxy.newProxyInstance(annotation.getClassLoader(),
		        new Class[] {annotation}, new Defaults());
		  }
		  public Object invoke(Object proxy, Method method, Object[] args)
		      throws Throwable {
		    return method.getDefaultValue();
		  }
		
		}

    @Test
    public void testNull() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();
    	
    	Assert.assertNotNull(factory.getPrettyPrinter(null));
     }
    
    @Test
    public void testDefault() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();
    	
    	PrettyPrinter prettyPrinter = factory.getPrettyPrinter(Defaults.of(PrettyPrint.class));
    	Assert.assertNotNull(prettyPrinter);
    	Assert.assertSame(prettyPrinter, factory.getPrettyPrinter(null));
    }

    @Test
    public void testParametersXmlDeclaration() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();

    	PrettyPrint mock = mock(PrettyPrint.class);
        when(mock.xmlDeclaration()).thenReturn(Boolean.TRUE);
        when(mock.maxTextNodeLength()).thenReturn(-1);
        when(mock.maxCDATANodeLength()).thenReturn(-1);
    	
    	PrettyPrinter result = factory.getPrettyPrinter(mock);
    	Assert.assertTrue(result.getClass().getName(), result instanceof PlainPrettyPrinter);
    }

    @Test
    public void testParametersIgnoreWhitespace() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();

    	PrettyPrint mock = mock(PrettyPrint.class);
        when(mock.ignoreWhitespace()).thenReturn(Boolean.TRUE);
        when(mock.maxTextNodeLength()).thenReturn(-1);
        when(mock.maxCDATANodeLength()).thenReturn(-1);
    	
    	PrettyPrinter result = factory.getPrettyPrinter(mock);
    	Assert.assertTrue(result.getClass().getName(), result instanceof PlainIndentedPrettyPrinter);
    }

    @Test
    public void testParametersRobustness1() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();

    	PrettyPrint mock = mock(PrettyPrint.class);
        when(mock.robustness()).thenReturn(Boolean.TRUE);
        when(mock.prettyPrintCData()).thenReturn(Boolean.TRUE);
        when(mock.maxTextNodeLength()).thenReturn(-1);
        when(mock.maxCDATANodeLength()).thenReturn(-1);
    	
    	PrettyPrinter result = factory.getPrettyPrinter(mock);
    	Assert.assertTrue(result.getClass().getName(), result instanceof RobustPlainPrettyPrinter);
    }
    

    @Test
    public void testParametersRobustness2() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();

    	PrettyPrint mock = mock(PrettyPrint.class);
        when(mock.robustness()).thenReturn(Boolean.TRUE);
        when(mock.prettyPrintComments()).thenReturn(Boolean.TRUE);
        when(mock.maxTextNodeLength()).thenReturn(-1);
        when(mock.maxCDATANodeLength()).thenReturn(-1);
    	
    	PrettyPrinter result = factory.getPrettyPrinter(mock);
    	Assert.assertTrue(result.getClass().getName(), result instanceof RobustPlainPrettyPrinter);
    }

    @Test
    public void testParametersRobustness3() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();

    	PrettyPrint mock = mock(PrettyPrint.class);
        when(mock.robustness()).thenReturn(Boolean.TRUE);
        when(mock.prettyPrintTextNodes()).thenReturn(Boolean.TRUE);
        when(mock.maxTextNodeLength()).thenReturn(-1);
        when(mock.maxCDATANodeLength()).thenReturn(-1);
    	
    	PrettyPrinter result = factory.getPrettyPrinter(mock);
    	Assert.assertTrue(result.getClass().getName(), result instanceof PlainPrettyPrinterForTextNodesWithXML);
    }
    
    @Test
    public void testParametersPrune() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();

    	PrettyPrint mock = mock(PrettyPrint.class);
        when(mock.pruneFilters()).thenReturn(new String[]{"/a"});
        when(mock.maxTextNodeLength()).thenReturn(-1);
        when(mock.maxCDATANodeLength()).thenReturn(-1);
    	
    	PrettyPrinter result = factory.getPrettyPrinter(mock);
    	Assert.assertTrue(result.getClass().getName(), result instanceof AbstractFilterPrettyPrinter);
    }
    
    @Test
    public void testParametersAnonymize() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();

    	PrettyPrint mock = mock(PrettyPrint.class);
        when(mock.anonymizeFilters()).thenReturn(new String[]{"/a"});
        when(mock.maxTextNodeLength()).thenReturn(-1);
        when(mock.maxCDATANodeLength()).thenReturn(-1);
    	
    	PrettyPrinter result = factory.getPrettyPrinter(mock);
    	Assert.assertTrue(result.getClass().getName(), result instanceof AbstractFilterPrettyPrinter);
    }

    @Test
    public void testParametersMaxCDATANodeLength() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();

    	PrettyPrint mock = mock(PrettyPrint.class);
        when(mock.maxTextNodeLength()).thenReturn(-1);
        when(mock.maxCDATANodeLength()).thenReturn(1024);
    	
    	PrettyPrinter result = factory.getPrettyPrinter(mock);
    	Assert.assertTrue(result.getClass().getName(), result instanceof PlainPrettyPrinterWithMaxNodeLength);
    }

    @Test
    public void testParametersMaxTextNodeLength() {
    	PrettyPrinterAnnotationFactory factory = new PrettyPrinterAnnotationFactory();

    	PrettyPrint mock = mock(PrettyPrint.class);
        when(mock.maxTextNodeLength()).thenReturn(1024);
        when(mock.maxCDATANodeLength()).thenReturn(-1);
    	
    	PrettyPrinter result = factory.getPrettyPrinter(mock);
    	Assert.assertTrue(result.getClass().getName(), result instanceof PlainPrettyPrinterWithMaxNodeLength);
    }

 
    
    
    
}
