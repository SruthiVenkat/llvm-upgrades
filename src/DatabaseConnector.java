import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import xmlreader.domain.LibDiffsModel;

/**
 * 
 * DB - llvm_clang_libs_info
 * Tables - lib_versions, problem_ids, lib_diffs
 * 
 * drop database if exists llvm_clang_libs_info;
 * create database llvm_clang_libs_info;
 * 
 * create table lib_versions (id SERIAL PRIMARY KEY, old_version int, new_version int);
 * 
 * create table problem_ids (id SERIAL PRIMARY KEY, problem varchar(255));
 * 
 * create table lib_diffs (id SERIAL PRIMARY KEY, lib_versions_id int, problem_id int, header varchar(255), type varchar(255),
 * target varchar(255), old_value varchar(255), old_size varchar(255), new_value varchar(255), param_pos varchar(255),
 * FOREIGN KEY (lib_versions_id) REFERENCES lib_versions(id) ON DELETE CASCADE,
 * FOREIGN KEY (problem_id) REFERENCES problem_ids(id) ON DELETE CASCADE);
 * 
 */
public class DatabaseConnector {
	private static DatabaseConnector dc;
	private static Connection conn;
	private final String url = "jdbc:postgresql://localhost/llvm_clang_libs_info";
	private final String user = "postgres";
	private final String password = "password";

	private DatabaseConnector(boolean dropDB) {
		initDB(dropDB);
	}
	
	public static DatabaseConnector buildDatabaseConnector(boolean dropDB) {
		if (dc == null) {
			dc = new DatabaseConnector(dropDB);
		}
		return dc;
	}
	/**
	 * Connect to the PostgreSQL database
	 *
	 * @return a Connection object
	 */
	public void connect() {
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch(SQLException e) {
			System.out.println(e);		
		}
	}
	
	public void initDB(boolean dropDB) {
		createDB(dropDB);
		createTables();
	}
	
	public void createDB(boolean dropDB) {
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", user, password);
		} catch(SQLException e) {
			System.out.println(e);		
		}
		if (dropDB) {
			String dropSQL = "drop database if exists llvm_clang_libs_info;";
			try (PreparedStatement pstmt = conn.prepareStatement(dropSQL)) {
				pstmt.executeUpdate();
			} catch (SQLException ex) {
				System.out.println(ex);
			}
		}
		String createSQL = "CREATE DATABASE llvm_clang_libs_info;";
		try (PreparedStatement pstmt = conn.prepareStatement(createSQL)) {
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			if (ex.getSQLState().equals("42P04"))
				System.out.println("Using the llvm_clang_libs_info database");
			else
				System.out.println(ex);
		}
		connect();	
	}
	
	public void createTables() {
		String SQL1 = "create table if not exists lib_versions (id SERIAL PRIMARY KEY, old_version int, new_version int);";
		try (PreparedStatement pstmt = conn.prepareStatement(SQL1)) {
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		String SQL2 = "create table problem_ids (id SERIAL PRIMARY KEY, problem varchar(510));";
		try (PreparedStatement pstmt = conn.prepareStatement(SQL2)) {
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		String SQL3 = "create table lib_diffs (id SERIAL PRIMARY KEY, lib_versions_id int, problem_id int, header varchar(1000), type varchar(1500), target varchar(1000), old_value varchar(1000), old_size varchar(255), new_value varchar(1000), param_pos varchar(255), FOREIGN KEY (lib_versions_id) REFERENCES lib_versions(id) ON DELETE CASCADE, FOREIGN KEY (problem_id) REFERENCES problem_ids(id) ON DELETE CASCADE);";
		try (PreparedStatement pstmt = conn.prepareStatement(SQL3)) {
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
	}
	
	public void insertIntoLibVersions(int start_version, int end_version) {
		String SQL1 = "INSERT INTO lib_versions(old_version, new_version) " + "VALUES(?,?);";
		for (int i = start_version; i < end_version; i++) {
			try (PreparedStatement pstmt = conn.prepareStatement(SQL1, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setInt(1, i);
				pstmt.setInt(2, i+1);

				pstmt.executeUpdate();
			} catch (SQLIntegrityConstraintViolationException e) {
				e.printStackTrace();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public long getIdFromLibVersions(int old_version, int new_version) {
		String SQL = "SELECT id from lib_versions where old_version = ? and new_version = ?;";
		long id = 0;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setInt(1, old_version);
			pstmt.setInt(2, new_version);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				id = rs.getLong(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;
	}
	public long insertIntoProblemIds(String problem) {
		String SQL = "INSERT INTO problem_ids(problem) " + "VALUES(?);";
		long id = 0;

		try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, problem);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						id = rs.getLong(1);
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;
	}
	
	public List<LibDiffsModel> getLibDiffs(String problem, long lib_versions_id) {
		List<LibDiffsModel> libDiffsRowList = new ArrayList<LibDiffsModel>();
		String SQL = "SELECT header, type, target, old_value, old_size, new_value, param_pos from lib_diffs where lib_versions_id = ? and problem_id in (select id from problem_ids where problem ilike ?);";
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setLong(1, lib_versions_id);
			pstmt.setString(2, problem);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				LibDiffsModel libDiffsModel = new LibDiffsModel((String)rs.getObject(1), (String)rs.getObject(2), (String)rs.getObject(3), (String)rs.getObject(4), (String)rs.getObject(5),
						(String)rs.getObject(6), (String)rs.getObject(7));
				libDiffsRowList.add(libDiffsModel);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return libDiffsRowList;
	}
	
	public long getProblemFromProblemIdsTable(String problem) {
		String SQL = "SELECT id from problem_ids where problem = ?;";
		long id = 0;

		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, problem);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				id = rs.getLong(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;
	}
	
	public long insertIntoLibDiffs(String problem, int old_version, int new_version, String header, String type, String target, String old_value, String old_size, String new_value, String param_pos) {
		long probId = getProblemFromProblemIdsTable(problem);
		if (probId == 0) {
			probId = insertIntoProblemIds(problem);
		}
		long libDiffsId = getIdFromLibVersions(old_version, new_version);
		String SQL = "INSERT INTO lib_diffs(lib_versions_id, problem_id, header, type, target, old_value, old_size, new_value, param_pos) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
		long id = 0;

		try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, (int)libDiffsId);
			pstmt.setInt(2, (int)probId);
			pstmt.setString(3, header);
			pstmt.setString(4, type);
			pstmt.setString(5, target);
			pstmt.setString(6, old_value);
			pstmt.setString(7, old_size);
			pstmt.setString(8, new_value);
			pstmt.setString(9, param_pos);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						id = rs.getLong(1);
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;
	}
}
