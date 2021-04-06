package xmlreader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Change {

	public String getNew_value() {
		return new_value;
	}

	public String getOld_value() {
		return old_value;
	}

	public String getOld_size() {
		return old_size;
	}

	public String getTarget() {
		return target;
	}

	public String getParam_pos() {
		return param_pos;
	}

	@JacksonXmlProperty(localName = "new_value", isAttribute = true)
	private String new_value;
	@JacksonXmlProperty(localName = "old_value", isAttribute = true)
	private String old_value;
	@JacksonXmlProperty(localName = "old_size", isAttribute = true)
	private String old_size;
	@JacksonXmlProperty(localName = "target", isAttribute = true)
	private String target;
	@JacksonXmlProperty(localName = "param_pos", isAttribute = true)
	private String param_pos;
	@JacksonXmlText
	private String text;

	@Override
	public String toString() {
		return "Change [new_value=" + new_value + ", old_value=" + old_value + ", old_size=" + old_size + ", target="
				+ target + ", param_pos=" + param_pos + "]";
	}

}
