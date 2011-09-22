package swarm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import weibo4j.Status;
import weibo4j.User;

public class PublicMethods
{
	public static Connection getConnection() throws SQLException,java.lang.ClassNotFoundException 
	{
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://localhost:3306/weibo";
		String username = "root";
		String password = "root";

		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public static String dateToMySQLDateTimeString(Date date)
	{
		final String[] MONTH = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };

		StringBuffer ret = new StringBuffer();
		String dateToString = date.toString(); // like
												// "Sat Dec 17 15:55:16 CST 2005"
		ret.append(dateToString.substring(24, 24 + 4));// append yyyy
		String sMonth = dateToString.substring(4, 4 + 3);
		for (int i = 0; i < 12; i++) 
		{ // append mm
			if (sMonth.equalsIgnoreCase(MONTH[i])) 
			{
				if ((i + 1) < 10)
					ret.append("-0");
				else
					ret.append("-");
				ret.append((i + 1));
				break;
			}
		}

		ret.append("-");
		ret.append(dateToString.substring(8, 8 + 2));
		ret.append(" ");
		ret.append(dateToString.substring(11, 11 + 8));

		return ret.toString();
	}
	
	public static boolean InsertStatusSql(Status status) 
	{
		try 
		{
			String insql = "insert ignore into status(id,userName,userId,createdAt,text,source,isTruncated,inReplyToStatusId,inReplyToUserId,isFavorited,inReplyToScreenName,latitude,longitude,thumbnail_pic,bmiddle_pic,original_pic,mid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = PublicMethods.getConnection();
			PreparedStatement ps = con.prepareStatement(insql);
			ps.setLong(1, status.getId());
			ps.setString(2, status.getUser().getName());
			ps.setLong(3, status.getUser().getId());
			ps.setString(4, PublicMethods.dateToMySQLDateTimeString(status
					.getCreatedAt()));
			ps.setString(5, status.getText());
			ps.setString(6, status.getSource());
			ps.setBoolean(7, status.isTruncated());
			ps.setLong(8, status.getInReplyToStatusId());
			ps.setLong(9, status.getInReplyToUserId());
			ps.setBoolean(10, status.isFavorited());
			ps.setString(11, status.getInReplyToScreenName());
			ps.setDouble(12, status.getLatitude());
			ps.setDouble(13, status.getLongitude());
			ps.setString(14, status.getThumbnail_pic());
			ps.setString(15, status.getBmiddle_pic());
			ps.setString(16, status.getOriginal_pic());
			ps.setString(17, status.getMid());

			int result = ps.executeUpdate();
			con.close();
			if (result > 0)
				return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean InsertStatusSql(Status status,User user) 
	{
		try 
		{
			String insql = "insert ignore into status(id,userName,userId,createdAt,text,source,isTruncated,inReplyToStatusId,inReplyToUserId,isFavorited,inReplyToScreenName,latitude,longitude,thumbnail_pic,bmiddle_pic,original_pic,mid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = PublicMethods.getConnection();
			PreparedStatement ps = con.prepareStatement(insql);
			ps.setLong(1, status.getId());
			ps.setString(2, user.getScreenName());
			ps.setLong(3, user.getId());
			ps.setString(4, PublicMethods.dateToMySQLDateTimeString(status
					.getCreatedAt()));
			ps.setString(5, status.getText());
			ps.setString(6, status.getSource());
			ps.setBoolean(7, status.isTruncated());
			ps.setLong(8, status.getInReplyToStatusId());
			ps.setLong(9, status.getInReplyToUserId());
			ps.setBoolean(10, status.isFavorited());
			ps.setString(11, status.getInReplyToScreenName());
			ps.setDouble(12, status.getLatitude());
			ps.setDouble(13, status.getLongitude());
			ps.setString(14, status.getThumbnail_pic());
			ps.setString(15, status.getBmiddle_pic());
			ps.setString(16, status.getOriginal_pic());
			ps.setString(17, status.getMid());

			int result = ps.executeUpdate();
			con.close();
			if (result > 0)
				return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
}