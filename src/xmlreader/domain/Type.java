package xmlreader.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Type {

	@JacksonXmlProperty(localName = "problem", isAttribute = true)
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Problem> problems;

	@JacksonXmlProperty(localName = "name", isAttribute = true)
	private String name;
	
	public List<Problem> getProblems() {
		return problems;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Type [problems=" + problems + "]";
	}
}
