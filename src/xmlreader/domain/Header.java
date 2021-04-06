package xmlreader.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "header")
public class Header {

	public List<Type> getTypes() {
		return types;
	}

	public Library getLibrary() {
		return library;
	}

	public String getName() {
		return name;
	}

	@JacksonXmlProperty(localName = "type", isAttribute = true)
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Type> types;

	@JacksonXmlProperty(localName = "library", isAttribute = true)
	private Library library;

	@JacksonXmlProperty(localName = "name", isAttribute = true)
	private String name;

	@Override
	public String toString() {
		return "Header [type=" + types + ", library=" + library + ", name=" + name + "]";
	}
}
