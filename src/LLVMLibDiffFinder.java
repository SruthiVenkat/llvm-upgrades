import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import xmlreader.domain.Header;
import xmlreader.domain.Problem;
import xmlreader.domain.ProblemsWithSymbols;
import xmlreader.domain.ProblemsWithTypes;
import xmlreader.domain.Report;
import xmlreader.domain.Symbol;
import xmlreader.domain.Type;

public class LLVMLibDiffFinder {

	public static void main(String[] args) {
		Scanner myObj = new Scanner(System.in);
		System.out.println("Enter XML file path");
		String filePath = myObj.nextLine();
		System.out.println("Enter old version");
		int old_version = myObj.nextInt();
		System.out.println("Enter new version");
		int new_version = myObj.nextInt();
		// System.out.println("Drop and create new DB?");
		// boolean dropDB = myObj.nextBoolean();
		myObj.close();

		XmlMapper mapper = new XmlMapper();
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		try {
			Report report = mapper.readValue(new File(filePath), Report.class);
			System.out.println(report);
			System.out.println(report.getProblemsWithSymbols());
			if (report.getTestInfo() == null) {
				throw new RuntimeException("Error while parsing the XML, no test info element");
			}
			DatabaseConnector connector = DatabaseConnector.buildDatabaseConnector(true);
			connector.insertIntoLibVersions(old_version, new_version);
			// updated versions from the xml file
			old_version = report.getTestInfo().getVersion1().getNumber();
			new_version = report.getTestInfo().getVersion2().getNumber();
			for (ProblemsWithTypes problemsWithTypes : report.getProblemsWithTypes()) {
				List<Header> headersFromTypes = problemsWithTypes.getHeaders();
				for (Header header : headersFromTypes) {
					if (header != null) {
						for (Type typeFromHeader : header.getTypes()) {
							String type = getTypeFromHeader(header, typeFromHeader, null);
							List<Problem> problems = getProblemFromHeader(header, typeFromHeader, null);
							if (problems != null) {
								for (Problem problem : problems) {
									if (problem != null && problem.getId() != null) {
										connector.insertIntoLibDiffs(problem.getId(), old_version, new_version,
												header.getName(), type,
												demangleType(problem.getChange().getTarget()),
												demangleType(problem.getChange().getOld_value()), problem.getChange().getOld_size(),
														demangleType(problem.getChange().getNew_value()), problem.getChange().getParam_pos());
									}
								}
							}

						}
					}
				}
			}

			for (ProblemsWithSymbols problemsWithSymbols : report.getProblemsWithSymbols()) {
				List<Header> headers = problemsWithSymbols.getHeaders();
				for (Header header : headers) {
					if (header != null) {
						for (Symbol symbol : header.getLibrary().getSymbols()) {
							String type = getTypeFromHeader(header, null, symbol);
							List<Problem> problems = getProblemFromHeader(header, null, symbol);
							if (problems != null) {
								for (Problem problem : problems) {
									if (problem != null && problem.getId() != null) {
										String libName = "";
										if (header.getLibrary() != null) {
											libName = header.getLibrary().getName();
										}
										connector.insertIntoLibDiffs(problem.getId(), old_version, new_version,
												header.getName(), type,
												demangleType(problem.getChange().getTarget()),
												demangleType(problem.getChange().getOld_value()), problem.getChange().getOld_size(),
														demangleType(problem.getChange().getNew_value()), problem.getChange().getParam_pos());
									}
								}
							}
						}
					}

				}
			}
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getTypeFromHeader(Header header, Type type, Symbol symbol) {
		if (header.getTypes() == null) {
			if (header.getLibrary() != null) {
				return demangleType(symbol.getName());
			}
			return null;
		} else {
			return type.getName();
		}
	}

	public static String demangleType(String type) {
		try {
			String cmd = "c++filt -n " + type;
			Runtime run = Runtime.getRuntime();
			Process pr = run.exec(cmd);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			return buf.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Problem> getProblemFromHeader(Header header, Type type, Symbol symbol) {
		if (header.getTypes() == null) {
			if (header.getLibrary() != null) {
				return symbol.getProblems();
			}
			return null;
		} else {
			return type.getProblems();
		}
	}
}