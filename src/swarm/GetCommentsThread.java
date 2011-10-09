package swarm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import weibo4j.Comment;
import weibo4j.Paging;
import weibo4j.WeiboException;

public class GetCommentsThread implements Runnable
{
	
	public static void getComments() throws ClassNotFoundException, SQLException, InterruptedException
	{
		do 
		{
			System.out.println(" Will connect to the database and get status.......");
			Connection conComments = PublicMethods.getConnection();  
			System.out.println(" Okay the connnection to mysql has been established..we are going to select id from status.....");
			java.sql.Statement stmt = conComments.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rset = stmt.executeQuery("select id from status"); 
			System.out.println(" Okay has all statuses, now we are going to shuffle them.......");
			List<Long> statusIdList = new ArrayList<Long>();
			int pageNum = 1;		
			long statusId = (long)0; 
			do
			{
				rset.next();
				statusId = rset.getLong(1);	
				//System.out.println("statusId: "+statusId);				
				Long userIdLongValue = new Long(statusId);
				statusIdList.add(userIdLongValue);
			}
			while(!rset.isLast());
			Collections.shuffle(statusIdList);
			for(Long statusIdLong:statusIdList) 
			{
				try 
				{  
					statusId = statusIdLong.longValue();
					Paging pag = new Paging(); 
					pag.setCount(100); 
					pageNum = 1;
					do
					{
						pag.setPage(pageNum);
						List<Comment> comments = PublicMethods.weibo.getComments(statusId+"",pag);
						//List<Comment> comments = PublicMethods.weibo.getComments("3343531616094195",pag);
						
						pageNum++;
						if(comments.isEmpty())
						{
							Thread.sleep(2500);
							break;
						}
						else
						{
							for (Comment comment : comments) 
							{
								PublicMethods.InsertCommentsSql(conComments, comment, statusId);
							} 
						}
						Thread.sleep(2500);
					}
					while(true);
				} catch (WeiboException e) {
					e.printStackTrace();
				}
			}  
			conComments.close();  
		} while (true);
	}
	
	public void run()
	{
		try 
		{
			getComments();
		} catch (ClassNotFoundException e) { 
			e.printStackTrace();
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
	}
}