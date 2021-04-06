package xmlreader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Version1 {

	@JacksonXmlProperty
	private int number;

	public int getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return "Version2 [number=" + number + "]";
	}
	
}
