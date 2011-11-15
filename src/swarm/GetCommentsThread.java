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
		//Thread.sleep(10000);
		do 
		{
			System.out.println(" Will connect to the database and get status.......");
			Connection conComments = PublicMethods.getConnection();  
			System.out.println(" Okay the connnection to mysql has been established..we are going to select id from status.....");
			java.sql.Statement stmt = conComments.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rset = stmt.executeQuery("select id, commentCounts from status where isDone = false limit 100");  
			int pageNum = 1;		
			int commentCounts = 0;
			long statusId = (long)0;  
			while(rset.next())
			{
				statusId = rset.getLong(1);	 
				commentCounts = rset.getInt(2);
				System.out.println("commentCounts: "+commentCounts);
				//Thread.sleep(2000);
				if(commentCounts == 0)
				{
					PublicMethods.UpdateStatusComments(conComments, statusId);
					//continue;
				}
				else
				{
					try 
					{   
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
								Thread.sleep(1000);
								break;
							}
							else
							{
								for (Comment comment : comments) 
								{
									PublicMethods.InsertCommentsSql(conComments, comment, statusId);
									commentCounts--;
								} 
								if(commentCounts <= 100)
								{
									break;
								}
							}
							Thread.sleep(3900);
						}
						while(true);		
						PublicMethods.UpdateStatusComments(conComments, statusId);			
					} 
					catch (WeiboException e)
					{
						e.printStackTrace();
					}
				}				
			} 
			conComments.close();
			System.out.println("No new status....so no new comments"); 
		} while (true);
	}
	
	public void run()
	{
		try 
		{
			while(true)
			{
				getComments();
			}
		} catch (ClassNotFoundException e) { 
			e.printStackTrace();
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
	}
}
