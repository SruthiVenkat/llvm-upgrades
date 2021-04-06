package xmlreader.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "problems_with_types")
public class ProblemsWithTypes {



	public List<Header> getHeaders() {
		return headers;
	}

	@JacksonXmlProperty(localName = "header", isAttribute = true)
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Header> headers;
	

	@JacksonXmlProperty(localName = "severity", isAttribute = true)
	private String severity;
	

	@Override
	public String toString() {
		return "ProblemsWithTypes [headers=" + headers + ", severity=" + severity + "]";
	}
	
}
