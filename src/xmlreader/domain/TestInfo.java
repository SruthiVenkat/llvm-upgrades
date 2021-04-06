package xmlreader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestInfo {

	public Version1 getVersion1() {
		return version1;
	}

	public Version2 getVersion2() {
		return version2;
	}

	@Override
	public String toString() {
		return "TestInfo [version1=" + version1 + ", version2=" + version2 + "]";
	}

	@JacksonXmlProperty(localName = "version1")
	private Version1 version1;

	@JacksonXmlProperty(localName = "version2")
	private Version2 version2;
}
