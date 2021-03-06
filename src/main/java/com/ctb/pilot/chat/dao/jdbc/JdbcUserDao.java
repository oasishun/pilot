package com.ctb.pilot.chat.dao.jdbc;

import static com.ctb.pilot.common.DbConstants.CONNECTION_URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ctb.pilot.chat.dao.UserDao;
import com.ctb.pilot.chat.model.User;

public class JdbcUserDao implements UserDao {

	private User getUserBySql(String sql) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("org.gjt.mm.mysql.Driver");

			con = DriverManager.getConnection(CONNECTION_URL);
			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				User user = new User();
				user.setSequence(rs.getInt("seq"));
				user.setUserId(rs.getString("user_id"));
				user.setPassword(rs.getString("password"));
				user.setNickname(rs.getString("nickname"));
				user.setJoinDate(rs.getTimestamp("join_date"));
				user.setDeleted(rs.getInt("del_yn") == 1);
				return user;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public User login(String userId, String password) {
		return getUserBySql("select * from tb_user where user_id='" + userId
				+ "' and password='" + password + "'");
	}

	@Override
	public User getUserBySequence(int sequence) {
		return getUserBySql("select * from tb_user where seq = " + sequence);
	}

	@Override
	public void join(String userId, String password, String nickname) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver");

			con = DriverManager.getConnection(CONNECTION_URL);
			stmt = con
					.prepareStatement("insert into tb_user (user_id, password, nickname, join_date) values (?, ?, ?, now())");
			stmt.setString(1, userId);
			stmt.setString(2, password);
			stmt.setString(3, nickname);
			stmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void update(User user) {
		int sequence = user.getSequence();
		String password = user.getPassword();
		String nickname = user.getNickname();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver");

			con = DriverManager.getConnection(CONNECTION_URL);
			stmt = con
					.prepareStatement("update tb_user set password=?, nickname=? where seq=?");
			stmt.setString(1, password);
			stmt.setString(2, nickname);
			stmt.setInt(3, sequence);
			stmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			// con = DriverManager.getConnection(CONNECTION_URL);

			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			con = DriverManager
					.getConnection("jdbc:apache:commons:dbcp:/ctb_db_pool");
			stmt = con.createStatement();

			rs = stmt.executeQuery("select * from tb_user");
			while (rs.next()) {
				User user = new User();
				user.setSequence(rs.getInt("seq"));
				user.setUserId(rs.getString("user_id"));
				user.setPassword(rs.getString("password"));
				user.setNickname(rs.getString("nickname"));
				user.setJoinDate(rs.getTimestamp("join_date"));
				user.setDeleted(rs.getInt("del_yn") == 1);
				users.add(user);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return users;
	}

}
