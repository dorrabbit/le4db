import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import javax.servlet.http.*;
@SuppressWarnings("serial")
public class ViewingListServlet extends HttpServlet {

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

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<link rel=\"stylesheet\" href=\"/uikit.min.css\">");
		out.println("<html>");
	    out.println("<head>");
	    out.println("<title>未視聴一覧</title>");
	    out.println("</head>");
		out.println("<body class=\"uk-background-muted uk-padding\">");
		
		out.println("<h1 class=\"uk-text-center\">動画管理システム</h1>");
		HttpSession session = request.getSession();
		String uname = (String)session.getAttribute("user");
		out.println("<div class=\"login_head uk-text-small uk-text-right\">");
		out.println(uname + "：ログインしています");
		out.println("</br><a href=\"/auth/logout\">ログアウト</a>");
		out.println("</div>");
		session.setAttribute("target_add", "/clist");
		out.println("<nav class=\"uk-navbar-container\" uk-navbar uk-sticky>");
		out.println("<div>");
		out.println("<ul class=\"uk-navbar-nav\">");
		out.println("<li><a href=\"/index.html\">ホーム</a></li>");
		out.println("<li><a href=\"/mlist\">動画</a></li>");
		out.println("<li><a href=\"/slist\">シリーズ</a></li>");
		out.println("<li><a href=\"/clist\">チャンネル</a></li>");
		out.println("<li><a href=\"/vdlist\">視聴済み</a></li>");
		out.println("<li class=\"uk-active\"><a href=\"/vilist\">未視聴</a></li>");
		out.println("</ul>");
		out.println("</div>");
		out.println("</nav>");
		out.println("<h3>一覧</h3>");
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
					+ ":5432/" + _dbname, _username, _password);
			stmt = conn.createStatement();

			out.println("<table border=\"1\">");
			out.println("<tr><th>動画名</th><th>出演者</th><th>日付</th><th>視聴回数</th><th>チャンネル名</th></tr>");

			ResultSet rs = stmt.executeQuery("SELECT * FROM movies WHERE NOT mtitle IN (" +
										     "SELECT mtitle FROM viewing WHERE uname = '" + uname + "') ORDER BY update");
			while (rs.next()) {
				String mtitle = rs.getString("mtitle");
				String performer = rs.getString("performer");
				Date update = rs.getDate("update");
				int viewcount = rs.getInt("viewcount");
				String chname = rs.getString("chname");

				out.println("<tr>");
				out.println("<td><a href=\"item?mtitle=" + mtitle + "\">" + mtitle + "</td>");
				out.println("<td>" + performer + "</td>");
				out.println("<td>" + update + "</td>");
				out.println("<td>" + viewcount + "</td>");
				out.println("<td>" + chname + "</td>");
				out.println("</tr>");
			}
			rs.close();

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

//		out.println("<h3>追加</h3>");
//
//		out.println("<form action=\"/add\" method=\"GET\">");
//		out.println("チャンネル名　　　： ");
//		out.println("<input type=\"text\" name=\"new_chname\"/>");
//		out.println("<br/>");
//		out.println("チャンネル登録者数： ");
//		out.println("<input type=\"text\" name=\"new_viewernum\"/>");
//		out.println("<br/>");
//		out.println("<br/>");
//		out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"submit\" value=\"登録 \">");
//		out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"reset\" value=\"リセット\">");
//		out.println("</form>");
		
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
