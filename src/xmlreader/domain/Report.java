package xmlreader.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "report")
public class Report {

	public List<ProblemsWithSymbols> getProblemsWithSymbols() {
		return problemsWithSymbols;
	}

	public List<ProblemsWithTypes> getProblemsWithTypes() {
		return problemsWithTypes;
	}
	
	public TestInfo getTestInfo() {
		return testInfo;
	}

	@JacksonXmlProperty(localName = "problems_with_symbols", isAttribute = true)
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<ProblemsWithSymbols> problemsWithSymbols;

	@JacksonXmlProperty(localName = "problems_with_types", isAttribute = true)
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<ProblemsWithTypes> problemsWithTypes;
	
	@JacksonXmlProperty(localName = "test_info")
	private TestInfo testInfo;

	@Override
	public String toString() {
		return "Report [problemsWithSymbols=" + problemsWithSymbols + ", problemsWithTypes=" + problemsWithTypes
				+ ", testInfo=" + testInfo + "]";
	}

	public void setProblemsWithTypes(List<ProblemsWithTypes> value) {
		if (problemsWithTypes == null) {
			problemsWithTypes = new ArrayList<ProblemsWithTypes>(value.size());
		}
		problemsWithTypes.addAll(value.stream().filter(problemsWT -> problemsWT!=null && problemsWT.getHeaders()!=null).
				collect(Collectors.toList()));
	}
	
	public void setProblemsWithSymbols(List<ProblemsWithSymbols> value) {
		if (problemsWithSymbols == null) {
			problemsWithSymbols = new ArrayList<ProblemsWithSymbols>(value.size());
		}
		problemsWithSymbols.addAll(value.stream().filter(problemsWT -> problemsWT!=null && problemsWT.getHeaders()!=null).
				collect(Collectors.toList()));
	}
}
