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
public class LoginAddServlet extends HttpServlet {

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

		response.setContentType("text/html; charset=Shift_JIS");
	    PrintWriter out = response.getWriter();
	    
	    out.println("<html>");
	    out.println("<head>");
	    //
	    out.println("<link rel=\"stylesheet\" href=\"/uikit.min.css\">");
	    //
	    out.println("<title>ユーザ追加</title>");
	    out.println("</head>");
	    out.println("<body class=\"uk-background-muted uk-padding\">");
	    
	    out.println("<h1 class=\"uk-text-center\">動画管理システム</h1>");
	    out.println("<h3>ユーザ追加</h3>");
		//

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
					+ ":5432/" + _dbname, _username, _password);
			stmt = conn.createStatement();
			
			String new_uname = request.getParameter("new_uname");
			String new_pass = request.getParameter("new_password");
			stmt.executeUpdate("INSERT INTO login VALUES('" + new_uname + "', '" + new_pass + "')");
			out.println("以下のユーザを追加しました。<br/><br/>");
			out.println("ユーザ名　: " + new_uname + "<br/>");
			out.println("パスワード: " + new_pass + "<br/>");
			out.println("<br/>ログインページに戻り、ログインし直してください<br/>");
			out.println("<a href=\"/auth/login\">ログインページへ</a>");
			
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
