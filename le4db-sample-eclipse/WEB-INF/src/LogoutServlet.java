import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LogoutServlet extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException{

    response.setContentType("text/html; charset=Shift_JIS");
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession(true);
    session.invalidate();

    response.sendRedirect("/auth/login");
  }
}