package com.welflex.xml.sax;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class FilteringSaxParserFactory extends SAXParserFactory {
  // Delegate to this parser
  private SAXParserFactory delegate;
  
  // Delegate class
  private static final String DELEGATE_CLASS = "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl";
  
  // Allowed Entity Paths
  private Set<String> allowedEntityPaths;
  
  public FilteringSaxParserFactory() {
    delegate = SAXParserFactory.newInstance(DELEGATE_CLASS, Thread.currentThread().getContextClassLoader());
    delegate.setNamespaceAware(true);
    delegate.setValidating(true);
    allowedEntityPaths = new HashSet<String>();
    allowedEntityPaths.add("/usr/local/entity/somefile");
  }
  
  @Override
  public boolean getFeature(String name) throws ParserConfigurationException,
    SAXNotRecognizedException,
    SAXNotSupportedException {
    return delegate.getFeature(name);
  }

  @Override
  public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
    SAXParser parser = delegate.newSAXParser();
    XMLReader xmlReader = parser.getXMLReader();
    xmlReader.setEntityResolver(new EntityResolver() {
      
      @Override
      public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
        IOException {
        if (allowedEntityPaths.contains(systemId)) {
          return new InputSource(systemId);
        }
        
        // Return blank path to prevent untrusted entities
        return new InputSource();
      }
    });
    
    return parser;
  }

  @Override
  public void setFeature(String name, boolean value) throws ParserConfigurationException,
    SAXNotRecognizedException,
    SAXNotSupportedException {
    delegate.setFeature(name, value);
  }
}
