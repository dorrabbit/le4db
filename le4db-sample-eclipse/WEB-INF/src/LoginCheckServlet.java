import java.io.*;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.Properties;
import javax.servlet.*;
import javax.servlet.http.*;
@SuppressWarnings("serial")

public class LoginCheckServlet extends HttpServlet {
	Connection conn = null;
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
			
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
					+ ":5432/" + _dbname, _username, _password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void destroy() {
	}
	
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException{

    response.setContentType("text/html; charset=Shift_JIS");
    PrintWriter out = response.getWriter();

    String user = request.getParameter("login_uname");
    String pass = request.getParameter("login_password");

    HttpSession session = request.getSession(true);

    boolean check = authUser(user, pass);
    if (check){
      session.setAttribute("login", "OK");
      session.setAttribute("user", user);

      String target = (String)session.getAttribute("target");
      if(target != null) {
    	  response.sendRedirect(target);
      }else { 
    	  response.sendRedirect("/index.html");
      }
    }else{
      /* 認証に失敗したら、ログイン画面に戻す */
      session.setAttribute("status", "Not Auth");
      response.sendRedirect("/auth/login");
    }
  }

  protected boolean authUser(String user, String pass){
    if (user == null || user.length() == 0 || pass == null || pass.length() == 0){
      return false;
    }
    
	Statement stmt = null;
	try {
		
		stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM login WHERE uname = '"
        		                         + user + "' AND password = '" + pass + "'");

        if (rs.next()){
          return true;
        }else{
          return false;
        }
      }catch (SQLException e){
        log("SQLException:" + e.getMessage());
        return false;
      }
    }
}