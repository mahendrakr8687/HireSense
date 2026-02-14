package in.hiresense.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.hiresense.dbutils.DBConnection;
import in.hiresense.models.ApplicationPojo;

public class ApplicationDAO {

	public static boolean apply(ApplicationPojo app) throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;

		try {

			conn = DBConnection.getConnection();
			String sql = "INSERT INTO applications(user_id, job_id, resume_path, score) VALUES (?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, app.getUserId());
			ps.setInt(2, app.getJobId());
			ps.setString(3, app.getResumePath());
			ps.setDouble(4, app.getScore());
			int ans = ps.executeUpdate();
			return ans > 0;
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

//	public static boolean hasAlreadyApplied(int userId, int jobId) throws Exception {
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//
//		try {
//			// Step 1: Get a connection from the DB
//			conn = DBConnection.getConnection();
//
//			// Step 2: Prepare SQL to check for existing application
//			String sql = "SELECT COUNT(*) FROM applications WHERE user_id = ? AND job_id = ?";
//			ps = conn.prepareStatement(sql);
//
//			// Step 3: Bind parameters
//			ps.setInt(1, userId); // ID of the user
//			ps.setInt(2, jobId); // ID of the job
//
//			// Step 4: Execute the query
//			rs = ps.executeQuery();
//
//			// Step 5: Check if at least one record exists
//			boolean exists = false;
//			if (rs.next()) {
//				exists = rs.getInt(1) > 0; // COUNT(*) > 0 means already applied
//			}
//
//			return exists;
//
//		} finally {
//			// Step 6: Close resources
//			if (ps != null)
//				ps.close();
//			if (rs != null)
//				rs.close();
//		}
//	}

	public static List<ApplicationPojo> getApplicationsByUser(int userId) throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			List<ApplicationPojo> list = new ArrayList<>();

			conn = DBConnection.getConnection();
			String sql = "SELECT * FROM applications WHERE user_id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new ApplicationPojo(
						rs.getInt("id"),
						rs.getInt("user_id"),
						rs.getInt("job_id"),
						rs.getString("resume_path"),
						rs.getFloat("score"),
						rs.getString("status"),
						rs.getString("applied_at")
						));
			}
			return list;
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}


	public static List<ApplicationPojo> getApplicationsByJobAndStatus(int jobId, String status) throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			List<ApplicationPojo> list = new ArrayList<>();

			conn = DBConnection.getConnection();
			String sql = "SELECT * FROM applications WHERE job_id = ? AND status = ? ORDER BY score DESC";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, jobId);
			ps.setString(2, status);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new ApplicationPojo(
						rs.getInt("id"),
						rs.getInt("user_id"),
						rs.getInt("job_id"),
						rs.getString("resume_path"),
						rs.getFloat("score"),
						rs.getString("status"),
						rs.getString("applied_at")
						));
			}
			return list;
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}

		}

	}

	public static boolean updateApplicationStatus(int appId, String status) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE applications SET status = ? WHERE id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ps.setInt(2, appId);
			int rows = ps.executeUpdate();
			return rows > 0;
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}
}