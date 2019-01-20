import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class ItemSeriesServlet extends HttpServlet {

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
	    out.println("<title>アイテムページ</title>");
	    out.println("</head>");
		out.println("<body class=\"uk-background-muted uk-padding\">");
				
		out.println("<h1 class=\"uk-text-center\">動画管理システム</h1>");
		HttpSession session = request.getSession();
		String uname = (String)session.getAttribute("user");
		String target = (String)session.getAttribute("target");
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
		
		String sname = request.getParameter("sname");

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
					+ ":5432/" + _dbname, _username, _password);
			stmt = conn.createStatement();

			out.println("<h3>アイテム</h3>");
			
			out.println("<table border=\"1\">");
			out.println("<tr><th>シリーズ名</th><th>レギュラー出演者</th><th>内容</th><th>ジャンル</th></tr>");
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM series NATURAL RIGHT JOIN genre WHERE sname = '" + sname + "'");

			while (rs.next()) {
				String repperf = rs.getString("repperf");
				String contents = rs.getString("contents");
				String att = rs.getString("att");
				out.println("<tr>");
				out.println("<td>" + sname + "</td>");
				out.println("<td>" + repperf + "</td>");
				out.println("<td>" + contents + "</td>");
				out.println("<td>" + att + "</td>");
				out.println("</tr>");
				out.println("</table>");
				
//				out.println("<h3>更新</h3>");
//				out.println("<form action=\"update\" method=\"GET\">");
//				out.println("動画タイトル　：" + mtitle);
//				out.println("<input type=\"hidden\" name=\"update_mtitle\" value=\"" + mtitle + "\"/><br/>");
//				out.println("出演者　　　　：");
//				out.println("<input type=\"text\" name=\"update_performer\" value=\"" + performer + "\"/><br/>");
//				out.println("投稿日　　　　：");
//				out.println("<input type=\"text\" name=\"update_update\" value=\"" + update + "\"/><br/>");
//				out.println("視聴数　　　　：");
//				out.println("<input type=\"text\" name=\"update_viewcount\" value=\"" + viewcount + "\"/><br/>");
//				out.println("投稿チャンネル：");
//				out.println("<input type=\"text\" name=\"update_chname\" value=\"" + chname + "\"/><br/>");
			}
			rs.close();
//			out.println("<input type=\"submit\" value=\"更新\"/>");
//			out.println("</form>");
			out.println("<h3>" + sname + "内動画一覧</h3>");
			
			out.println("<table border=\"1\">");
			out.println("<tr><th>動画タイトル</th></tr>");
			
			ResultSet rs_c = stmt.executeQuery("SELECT * FROM classification WHERE sname = '" + sname + "'");

			while (rs_c.next()) {
				String mtitle = rs_c.getString("mtitle");
				out.println("<tr>");
				out.println("<td><a href=\"item?mtitle=" + mtitle + "\">" + mtitle + "</a></td>");
				out.println("</tr>");
			}
			rs_c.close();
			out.println("</table>");

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

//		out.println("<h3>削除</h3>");
//		out.println("<form action=\"delete\" method=\"GET\">");
//		out.println("<input type=\"hidden\" name=\"delete_mtitle\" value=\"" + mtitle + "\">");
//		out.println("<input type=\"submit\" value=\"削除\"/>");
//		out.println("</form>");

		out.println("<h3>追加</h3>");

		out.println("<form action=\"/sadd\" method=\"GET\">");
		out.println("シリーズ名　： " + sname);
		out.println("<input type=\"hidden\" name=\"new_sname\" value=\"" + sname + "\"/><br/>");
		out.println("動画タイトル： ");
		out.println("<input type=\"text\" name=\"new_mtitle\"/>");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"submit\" value=\"登録 \">");
		out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"reset\" value=\"リセット\">");
		out.println("</form>");
		
		
		out.println("</body>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
