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
public class HomeServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//テンプレ
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<link rel=\"stylesheet\" href=\"/uikit.min.css\">");
		out.println("<html>");
	    out.println("<head>");
	    out.println("<title>ホーム</title>");
	    out.println("</head>");
		out.println("<body class=\"uk-background-muted uk-padding\">");
		
		out.println("<h1 class=\"uk-text-center\">動画管理システム</h1>");
		HttpSession session = request.getSession();
		String uname = (String)session.getAttribute("user");
		out.println("<div class=\"login_head uk-text-small uk-text-right\">");
		if(uname!=null) {
			out.println(uname + "：ログインしています");
			out.println("</br><a href=\"/auth/logout\">ログアウト</a>");
		}else {
			out.println("ログインしていません");
			out.println("</br><a href=\"/auth/login\">ログイン</a>");
		}
		out.println("</div>");
		out.println("<nav class=\"uk-navbar-container\" uk-navbar uk-sticky>");
		out.println("<div>");
		out.println("<ul class=\"uk-navbar-nav\">");
		out.println("<li class=\"uk-active\"><a href=\"/index.html\">ホーム</a></li>");
		out.println("<li><a href=\"/mlist\">動画</a></li>");
		out.println("<li><a href=\"/slist\">シリーズ</a></li>");
		out.println("<li><a href=\"/clist\">チャンネル</a></li>");
		out.println("<li><a href=\"/vdlist\">視聴済み</a></li>");
		out.println("<li><a href=\"/vilist\">未視聴</a></li>");
		out.println("</ul>");
		out.println("</div>");
		out.println("</nav>");
		//ここまでテンプレ（title変更・liのactiveの変更・ログインしていませんのif必要なし）
		
		out.println("<h3>理念</h3>");
		out.println("動画管理システムは、別チャンネルから投稿された同シリーズ動画を１シリーズに纏めます。<br/>"
				  + "また動画の視聴・未視聴を管理することも可能です。<br/>"
				  + "見たかどうか忘れて動画を再生した、赤いプログレスバーが少しだけ進んだ閲覧履歴から解放されよう。");
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
