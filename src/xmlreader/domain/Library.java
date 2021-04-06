package xmlreader.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Library {

	public String getName() {
		return name;
	}

	public List<Symbol> getSymbols() {
		return symbols;
	}

	@JacksonXmlProperty(localName = "symbol", isAttribute = true)
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Symbol> symbols;
	
	@JacksonXmlProperty
	private String name;

	@Override
	public String toString() {
		return "Library [symbol=" + symbols + ", name=" + name + "]";
	}
}
