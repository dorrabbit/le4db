//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Properties;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@SuppressWarnings("serial")
//public class LoginServlet extends HttpServlet {
//
//	private String _hostname = null;
//	private String _dbname = null;
//	private String _username = null;
//	private String _password = null;
//
//	public void init() throws ServletException {
//		// iniファイルから自分のデータベース情報を読み込む
//		String iniFilePath = getServletConfig().getServletContext()
//				.getRealPath("WEB-INF/le4db.ini");
//		try {
//			FileInputStream fis = new FileInputStream(iniFilePath);
//			Properties prop = new Properties();
//			prop.load(fis);
//			_hostname = prop.getProperty("hostname");
//			_dbname = prop.getProperty("dbname");
//			_username = prop.getProperty("username");
//			_password = prop.getProperty("password");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	protected void doGet(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//
//		response.setContentType("text/html;charset=UTF-8");
//		PrintWriter out = response.getWriter();
//
//		out.println("<html>");
//		out.println("<body>");
//
//		out.println("<h3>ログイン</h3>");
//		out.println("<form action=\"search\" method=\"GET\">");
//		out.println("ユーザ名： ");
//		out.println("<input type=\"text\" name=\"login_uname\"/>");
//		out.println("<br/>");
//		out.println("パスワード： ");
//		out.println("<input type=\"text\" name=\"login_password\"/>");
//		out.println("<br/>");
//		out.println("<input type=\"submit\" value=\"ログイン\"/>");
//		out.println("</form>");
//		
//		Connection conn = null;
//		Statement stmt = null;
//		try {
//			Class.forName("org.postgresql.Driver");
//			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
//					+ ":5432/" + _dbname, _username, _password);
//			stmt = conn.createStatement();
//
//			out.println("<table border=\"1\">");
//			out.println("<tr><th>ユーザー名</th><th>パスワード</th></tr>");
//
//			ResultSet rs = stmt.executeQuery("SELECT * FROM login");
//			while (rs.next()) {
//				String uname = rs.getString("uname");
//				String password = rs.getString("password");
//
//				out.println("<tr>");
//				out.println("<td>" + uname + "</td>");
//				out.println("<td>" + password + "</td>");
//				out.println("</tr>");
//			}
//			rs.close();
//
//			out.println("</table>");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//
//		out.println("</body>");
//		out.println("</html>");
//	}
//
//	protected void doPost(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
//	}
//
//	public void destroy() {
//	}
//
//}
import java.io.*;
import java.util.Properties;

import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {
	
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
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException{

	response.setContentType("text/html; charset=Shift_JIS");
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head>");
    //
    out.println("<link rel=\"stylesheet\" href=\"/uikit.min.css\">");
    //
    out.println("<title>ログインページ</title>");
    out.println("</head>");
    out.println("<body class=\"uk-background-muted uk-padding\">");
    
    out.println("<h1 class=\"uk-text-center\">動画管理システム</h1>");
    out.println("<h3>ログイン</h3>");
    
	HttpSession session = request.getSession(true);
	/* 認証失敗から呼び出されたのかどうか */
    Object status = session.getAttribute("status");

    if (status != null){
      out.println("<p class=\"uk-text-danger\">認証に失敗しました</p>");
      out.println("<p class=\"uk-text-danger\">再度ユーザー名とパスワードを入力して下さい</p>");

      session.setAttribute("status", null);
    }
    

	out.println("<form action=\"/auth/logincheck\" method=\"POST\">");
	out.println("ユーザ名　： ");
	out.println("<input type=\"text\" name=\"login_uname\"/>");
	out.println("<br/>");
	out.println("パスワード： ");
	out.println("<input type=\"password\" name=\"login_password\"/>");
	out.println("<br/>");
	out.println("<br/>");
	out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"submit\" value=\"ログイン\">");
	out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"reset\" value=\"リセット\">");
	out.println("</form>");

	out.println("<h3>アカウント作成</h3>");

	out.println("<form action=\"/add\" method=\"GET\">");
	out.println("ユーザ名　： ");
	out.println("<input type=\"text\" name=\"new_uname\"/>");
	out.println("<br/>");
	out.println("パスワード： ");
	out.println("<input type=\"password\" name=\"new_password\"/>");
	out.println("<br/>");
	out.println("<br/>");
	out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"submit\" value=\"登録 \">");
	out.println("<input class=\"uk-button uk-button-default uk-card uk-card-default uk-card-hover\" type=\"reset\" value=\"リセット\">");
	out.println("</form>");
	
    out.println("</body>");
    out.println("</html>");
  }
}