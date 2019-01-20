import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class AddServlet extends HttpServlet {

	private String _hostname = null;
	private String _dbname = null;
	private String _username = null;
	private String _password = null;

	public void init() throws ServletException {
		// iniファイルから自分のデータベース情報を読み込む
		String iniFilePath = getServletConfig().getServletContext()
				.getRealPath("WEB-INF/le4db.ini");
		try {
			FileInputStream fis = new FileInputStream(iniFilePath);
			Properties prop = new Properties();
			prop.load(fis);
			_hostname = prop.getProperty("hostname");
			_dbname = prop.getProperty("dbname");
			_username = prop.getProperty("username");
			_password = prop.getProperty("password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		//テンプレ
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<link rel=\"stylesheet\" href=\"/uikit.min.css\">");
		out.println("<html>");
	    out.println("<head>");
	    out.println("<title>追加ページ</title>");
	    out.println("</head>");
		out.println("<body class=\"uk-background-muted uk-padding\">");
		
		out.println("<h1 class=\"uk-text-center\">動画管理システム</h1>");
		HttpSession session = request.getSession();
		String uname = (String)session.getAttribute("user");
		out.println("<div class=\"login_head uk-text-small uk-text-right\">");
		out.println(uname + "：ログインしています");
		out.println("</br><a href=\"/auth/logout\">ログアウト</a>");
		out.println("</div>");
		out.println("<nav class=\"uk-navbar-container\" uk-navbar uk-sticky>");
		out.println("<div>");
		out.println("<ul class=\"uk-navbar-nav\">");
		out.println("<li><a href=\"/index.html\">ホーム</a></li>");
		out.println("<li><a href=\"/mlist\">動画</a></li>");
		out.println("<li><a href=\"/slist\">シリーズ</a></li>");
		out.println("</ul>");
		out.println("</div>");
		out.println("</nav>");
		out.println("<h3>追加</h3>");
		//

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
					+ ":5432/" + _dbname, _username, _password);
			stmt = conn.createStatement();
			
			String target = (String)session.getAttribute("target_add");
//			out.println(target);
			if(target == null) {
				out.println("不正な遷移元です");
			}else {
				out.println(target);
				//loginページ以外から来たとき
				switch(target) {
				case "/auth/login":
					 //loginから来たとき
					String new_uname = request.getParameter("new_uname");
					String new_pass = request.getParameter("new_password");
					stmt.executeUpdate("INSERT INTO login VALUES('" + new_uname + "', '" + new_pass + "')");
					out.println("以下のユーザを追加しました。<br/><br/>");
					out.println("ユーザ名　: " + new_uname + "<br/>");
					out.println("パスワード: " + new_pass + "<br/>");
					out.println("<br/>ログインページに戻り、ログインし直してください");
				case "/mlist":
					String new_mtitle = request.getParameter("new_mtitle");
					String new_performer = request.getParameter("new_performer");
					String new_update_y = request.getParameter("new_update_y");
					String new_update_m = request.getParameter("new_update_m");
					String new_update_d = request.getParameter("new_update_d");
					String new_update = new_update_y + "-" + new_update_m + "-" + new_update_d;
					String new_viewcount = request.getParameter("new_viewcount");
					String new_chname = request.getParameter("new_chname");
					stmt.executeUpdate("INSERT INTO movies VALUES('" + new_mtitle + "', '" 
															 		+ new_performer + "', '" 
																	+ new_update + "', " 
																	+ new_viewcount + ", '" 
																	+ new_chname + "')");
					out.println("以下の動画を追加しました。<br/><br/>");
					out.println("動画タイトル　: " + new_mtitle + "<br/>");
					out.println("出演者　　　　: " + new_performer + "<br/>");
					out.println("投稿日　　　　: " + new_update_y + "年" + new_update_m + "月" + new_update_d + "日" + "<br/>");
					out.println("視聴数　　　　: " + new_viewcount + "<br/>");
					out.println("投稿チャンネル: " + new_chname + "<br/>");
					
					out.println("<br/>");
				case "/slist":
					String new_sname = request.getParameter("new_sname");
					String new_repperf = request.getParameter("new_repperf");
					String new_contents = request.getParameter("new_contents");
					stmt.executeUpdate("INSERT INTO series VALUES('" + new_sname + "', '" 
															  		 + new_contents + "', '" 
																	 + new_repperf + "')");
					out.println("以下のシリーズを追加しました。<br/><br/>");
					out.println("シリーズ名　　　： " + new_sname + "<br/>");
					out.println("レギュラー出演者: " + new_repperf + "<br/>");
					out.println("内容　　　　　　: " + new_contents + "<br/>");
					
					out.println("<br/>");
				}
				out.println("<a href=" + target + ">遷移元ページに戻る</a><br/>");	
			}
			out.println("</body>");
			out.println("</html>");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
