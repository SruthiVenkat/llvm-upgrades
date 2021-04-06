package xmlreader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Problem {

	public String getId() {
		return id;
	}

	public Change getChange() {
		return change;
	}


	@JacksonXmlProperty(localName = "id", isAttribute = true)
	private String id;
	
	@JacksonXmlProperty(localName = "change", isAttribute = true)
	private Change change;

	@Override
	public String toString() {
		return "Problem [id=" + id + ", change=" + change + "]";
	}
}
