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
public class SearchServlet extends HttpServlet {

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

		String srch_mtitle = request.getParameter("srch_mtitle");
		String srch_performer = request.getParameter("srch_performer");
		String srch_contents = request.getParameter("srch_contents");

		//テンプレ
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<link rel=\"stylesheet\" href=\"/uikit.min.css\">");
		out.println("<html>");
	    out.println("<head>");
	    out.println("<title>検索ページ</title>");
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
		out.println("<li><a href=\"/clist\">チャンネル</a></li>");
		out.println("<li><a href=\"/vdlist\">視聴済み</a></li>");
		out.println("<li><a href=\"/vilist\">未視聴</a></li>");
		out.println("</ul>");
		out.println("</div>");
		out.println("</nav>");
		//
		
//		out.println(srch_mtitle + "," + srch_performer + "," + srch_contents);
//		out.println(srch_performer.equals(""));
		String query_mtitle;
		String query_perf;
		String query_con;

		out.println("<h3>検索条件</h3>");
		out.println("検索に用いた語<br/>");
		if (srch_mtitle.equals("")) {
			query_mtitle = "true";
		}else {
			out.println("動画タイトル： " + srch_mtitle + "を含む<br/>");
			query_mtitle = "mtitle like '%" + srch_mtitle + "%'";
		}
		if (srch_performer.equals("")) {
			query_perf = "true";
		}else {
			out.println("出演者　　　： " + srch_performer + "を含む<br/>");
			query_perf = "performer like '%" + srch_performer + "%'";
		}
		if (srch_contents.equals("")) {
			query_con = "true";
		}else {
			out.println("内容　　　　： " + srch_contents + "を含む<br/>");
			query_con = "mtitle in ( select mtitle from classification where sname in ("
							 + "select sname from series where contents like '%"
							 + srch_contents + "%'))";
		}

		out.println("<h3>検索結果</h3>");

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
					+ ":5432/" + _dbname, _username, _password);
			stmt = conn.createStatement();

			out.println("<table border=\"1\">");
			out.println("<tr><th>動画名</th><th>出演者</th><th>日付</th><th>視聴回数</th><th>チャンネル名</th></tr>");

			ResultSet rs = stmt
					.executeQuery("SELECT * FROM movies WHERE "
								 + query_mtitle + " and " + query_perf + " and " + query_con
								 + " ORDER BY update");
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
			
			ResultSet rs_count = stmt
					.executeQuery("SELECT COUNT(*), SUM(viewcount) FROM movies WHERE "
								 + query_mtitle + " and " + query_perf + " and " + query_con);
			while (rs_count.next()) {
				int count = rs_count.getInt("count");
				int sum = rs_count.getInt("sum");
				out.println("検索結果　： " + count + "件<br/>");
				out.println("平均視聴数： " + sum/count);
			}
			rs_count.close();
			
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
