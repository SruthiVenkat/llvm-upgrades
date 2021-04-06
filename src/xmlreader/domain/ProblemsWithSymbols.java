package xmlreader.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "problems_with_symbols")
public class ProblemsWithSymbols {

	@JacksonXmlProperty(localName = "header", isAttribute = true)
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Header> headers;

	@JacksonXmlProperty(localName = "severity", isAttribute = true)
	private String severity;
	
	public void setHeaders(List<Header> value) {
		if (headers == null) {
			headers = new ArrayList<>(value.size());
		}
		headers.addAll(value);
	}
	
	public List<Header> getHeaders() {
		return headers;
	}
	
	@Override
	public String toString() {
		return "ProblemsWithSymbols [headers=" + headers + ", severity=" + severity + "]";
	}
	
}
