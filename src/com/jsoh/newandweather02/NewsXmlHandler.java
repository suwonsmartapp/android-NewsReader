package com.jsoh.newandweather02;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class NewsXmlHandler extends DefaultHandler {
	private ArrayList<News> messages;
	private News currentMessage;
	private StringBuffer builder;
	
	public ArrayList<News> getMessages() {
		return this.messages;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		builder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (this.currentMessage != null) {
			if (localName.equalsIgnoreCase("title")) {
				currentMessage.setTitle(builder.toString());
				Log.i("Xml", builder.toString());
			} else if (localName.equalsIgnoreCase("link")) {
				currentMessage.setLink(builder.toString());
				Log.i("Xml", builder.toString());
			} else if (localName.equalsIgnoreCase("category")) {
				currentMessage.setCategory(builder.toString());
				Log.i("Xml", builder.toString());
			} else if (localName.equalsIgnoreCase("pubDate")) {
				currentMessage.setPubDate(builder.toString());
				Log.i("Xml", builder.toString());
			} else if (localName.equalsIgnoreCase("description")) {
				currentMessage.setDescription(builder.toString());
				Log.i("Xml", builder.toString());
			} else if (localName.equalsIgnoreCase("item")) {
				messages.add(currentMessage);
			}
		}
		Log.i("Xml", "endElement : " + qName);
	}

	@Override
	public void startDocument() throws SAXException {
		messages = new ArrayList<News>();
		builder = new StringBuffer();
		Log.i("Xml", "startDoc");
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equalsIgnoreCase("item")) {
			this.currentMessage = new News();
		}
		builder.setLength(0);
		Log.i("Xml", "startElement : " + localName);
	}
	
	
}
