import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class UpdateServlet extends HttpServlet {

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
	    out.println("<title>更新ページ</title>");
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
		out.println("</ul>");
		out.println("</div>");
		out.println("</nav>");
		out.println("<h3>更新</h3>");
		//

		String update_mtitle = request.getParameter("update_mtitle");
		String update_performer = request.getParameter("update_performer");
		String update_update = request.getParameter("update_update");
		String update_viewcount = request.getParameter("update_viewcount");
		String update_chname = request.getParameter("update_chname");

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
					+ ":5432/" + _dbname, _username, _password);
			stmt = conn.createStatement();

			stmt.executeUpdate("UPDATE movies SET performer = '" + update_performer
								+ "', update = '" + update_update
								+ "', viewcount = " + update_viewcount
								+ ", chname = '" + update_chname
								+ "' WHERE mtitle = '" + update_mtitle + "'");

			out.println("以下の動画を更新しました。<br/><br/>");
			out.println("動画タイトル　：" + update_mtitle + "<br/>");
			out.println("出演者　　　　：" + update_performer + "<br/>");
			out.println("投稿日　　　　：" + update_update + "<br/>");
			out.println("視聴数　　　　：" + update_viewcount + "<br/>");
			out.println("投稿チャンネル：" + update_chname + "<br/>");

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

		out.println("<br/>");
		out.println("<a href=" + target + ">遷移元ページに戻る</a><br/>");
		out.println("<a href=\"/auth/login\">ログインページに戻る</a><br/>");
		out.println("<a href=\"home\">トップページに戻る</a>");

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
