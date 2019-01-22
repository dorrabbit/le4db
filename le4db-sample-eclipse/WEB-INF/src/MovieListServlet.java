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
public class MovieListServlet extends HttpServlet {

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
	    out.println("<title>動画一覧</title>");
	    out.println("</head>");
		out.println("<body class=\"uk-background-muted uk-padding\">");
		
		out.println("<h1 class=\"uk-text-center\">動画管理システム</h1>");
		HttpSession session = request.getSession();
		String uname = (String)session.getAttribute("user");
		out.println("<div class=\"login_head uk-text-small uk-text-right\">");
		out.println(uname + "：ログインしています");
		out.println("</br><a href=\"/auth/logout\">ログアウト</a>");
		out.println("</div>");
		session.setAttribute("target_add", "/mlist");
		out.println("<nav class=\"uk-navbar-container\" uk-navbar uk-sticky>");
		out.println("<div>");
		out.println("<ul class=\"uk-navbar-nav\">");
		out.println("<li><a href=\"/index.html\">ホーム</a></li>");
		out.println("<li class=\"uk-active\"><a href=\"/mlist\">動画</a></li>");
		out.println("<li><a href=\"/slist\">シリーズ</a></li>");
		out.println("<li><a href=\"/clist\">チャンネル</a></li>");
		out.println("<li><a href=\"/vdlist\">視聴済み</a></li>");
		out.println("<li><a href=\"/vilist\">未視聴</a></li>");
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

			ResultSet rs = stmt.executeQuery("SELECT * FROM movies ORDER BY update");
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

		out.println("<h3>追加</h3>");

		out.println("<form action=\"/add\" method=\"GET\">");
		out.println("動画タイトル　： ");
		out.println("<input type=\"text\" name=\"new_mtitle\"/>");
		out.println("<br/>");
		out.println("出演者　　　　： ");
		out.println("<input type=\"text\" name=\"new_performer\"/>");
		out.println("<br/>");
		out.println("投稿日　　　　： ");
		out.println("<select name=\"new_update_y\">" +
					"<option value=\"2000\">2000</option>\r\n" + 
					"<option value=\"2001\">2001</option>\r\n" + 
					"<option value=\"2002\">2002</option>\r\n" + 
					"<option value=\"2003\">2003</option>\r\n" + 
					"<option value=\"2004\">2004</option>\r\n" + 
					"<option value=\"2005\">2005</option>\r\n" + 
					"<option value=\"2006\">2006</option>\r\n" + 
					"<option value=\"2007\">2007</option>\r\n" + 
					"<option value=\"2008\">2008</option>\r\n" + 
					"<option value=\"2009\">2009</option>\r\n" + 
					"<option value=\"2010\">2010</option>\r\n" + 
					"<option value=\"2011\">2011</option>\r\n" + 
					"<option value=\"2012\">2012</option>\r\n" + 
					"<option value=\"2013\">2013</option>\r\n" + 
					"<option value=\"2014\">2014</option>\r\n" + 
					"<option value=\"2015\">2015</option>\r\n" + 
					"<option value=\"2016\">2016</option>\r\n" + 
					"<option value=\"2017\">2017</option>\r\n" + 
					"<option value=\"2018\">2018</option>\r\n" + 
					"<option value=\"2019\">2019</option>\r\n" + 
					"<option value=\"2020\">2020</option>\r\n" + 
					"<option value=\"2021\">2021</option>\r\n" + 
					"<option value=\"2022\">2022</option>\r\n" + 
					"<option value=\"2023\">2023</option>\r\n" + 
					"<option value=\"2024\">2024</option>\r\n" + 
					"<option value=\"2025\">2025</option>\r\n" + 
					"<option value=\"2026\">2026</option>\r\n" + 
					"<option value=\"2027\">2027</option>\r\n" + 
					"<option value=\"2028\">2028</option>\r\n" + 
					"<option value=\"2029\">2029</option>\r\n" + 
					"<option value=\"2030\">2030</option>\r\n" + 
					"</select>年");
		out.println("<select name=\"new_update_m\">" +
					"<option value=\"01\">1</option>\r\n" + 
					"<option value=\"02\">2</option>\r\n" + 
					"<option value=\"03\">3</option>\r\n" + 
					"<option value=\"04\">4</option>\r\n" + 
					"<option value=\"05\">5</option>\r\n" + 
					"<option value=\"06\">6</option>\r\n" + 
					"<option value=\"07\">7</option>\r\n" + 
					"<option value=\"08\">8</option>\r\n" + 
					"<option value=\"09\">9</option>\r\n" + 
					"<option value=\"10\">10</option>\r\n" + 
					"<option value=\"11\">11</option>\r\n" + 
					"<option value=\"12\">12</option>\r\n" + 
					"</select>月");
		out.println("<select name=\"new_update_d\">" + 
					"<option value=\"01\">1</option>\r\n" + 
					"<option value=\"02\">2</option>\r\n" + 
					"<option value=\"03\">3</option>\r\n" + 
					"<option value=\"04\">4</option>\r\n" + 
					"<option value=\"05\">5</option>\r\n" + 
					"<option value=\"06\">6</option>\r\n" + 
					"<option value=\"07\">7</option>\r\n" + 
					"<option value=\"08\">8</option>\r\n" + 
					"<option value=\"09\">9</option>\r\n" + 
					"<option value=\"10\">10</option>\r\n" + 
					"<option value=\"11\">11</option>\r\n" + 
					"<option value=\"12\">12</option>\r\n" + 
					"<option value=\"13\">13</option>\r\n" + 
					"<option value=\"14\">14</option>\r\n" + 
					"<option value=\"15\">15</option>\r\n" + 
					"<option value=\"16\">16</option>\r\n" + 
					"<option value=\"17\">17</option>\r\n" + 
					"<option value=\"18\">18</option>\r\n" + 
					"<option value=\"19\">19</option>\r\n" + 
					"<option value=\"20\">20</option>\r\n" + 
					"<option value=\"21\">21</option>\r\n" + 
					"<option value=\"22\">22</option>\r\n" + 
					"<option value=\"23\">23</option>\r\n" + 
					"<option value=\"24\">24</option>\r\n" + 
					"<option value=\"25\">25</option>\r\n" + 
					"<option value=\"26\">26</option>\r\n" + 
					"<option value=\"27\">27</option>\r\n" + 
					"<option value=\"28\">28</option>\r\n" + 
					"<option value=\"29\">29</option>\r\n" + 
					"<option value=\"30\">30</option>\r\n" + 
					"<option value=\"31\">31</option>\r\n" + 
					"</select>日");
		out.println("<br/>");
		out.println("視聴数　　　　： ");
		out.println("<input type=\"text\" name=\"new_viewcount\"/>");
		out.println("<br/>");
		out.println("投稿チャンネル： ");
		out.println("<input type=\"text\" name=\"new_chname\"/>");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"submit\" value=\"登録 \">");
		out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"reset\" value=\"リセット\">");
		out.println("</form>");
		

		out.println("<h3>検索</h3>");

		out.println("<form action=\"/search\" method=\"GET\">");
		out.println("<div class=\"uk-margin uk-grid-small uk-child-width-auto uk-grid\">" + 
					"    <label><input class=\"uk-radio\" type=\"radio\" name=\"inradio\" value=\"inall\" checked>全動画</label>" + 
					"    <label><input class=\"uk-radio\" type=\"radio\" name=\"inradio\" value=\"invd\">視聴済み動画</label>" + 
					"    <label><input class=\"uk-radio\" type=\"radio\" name=\"inradio\" value=\"invi\">未視聴動画</label>" + 
					"</div>");
		out.println("動画タイトル： ");
		out.println("<input type=\"text\" name=\"srch_mtitle\"/>" + "を含む");
		out.println("<br/>");
		out.println("出演者　　　： ");
		out.println("<input type=\"text\" name=\"srch_performer\"/>" + "を含む");
		out.println("<br/>");
		out.println("内容　　　　： ");
		out.println("<input type=\"text\" name=\"srch_contents\"/>" + "を含む");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"submit\" value=\"検索 \">");
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
