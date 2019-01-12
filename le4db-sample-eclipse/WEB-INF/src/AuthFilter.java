import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;

public class AuthFilter implements Filter{
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
    try{
      String target = ((HttpServletRequest)request).getRequestURI();

      HttpSession session = ((HttpServletRequest)request).getSession();

      if (session == null){
        /* まだ認証されていない */
        session = ((HttpServletRequest)request).getSession(true);
        session.setAttribute("target", target);

        ((HttpServletResponse)response).sendRedirect("/auth/login");
      }else{
        Object loginCheck = session.getAttribute("login");
        if (loginCheck == null){
          /* まだ認証されていない */
          session.setAttribute("target", target);
          ((HttpServletResponse)response).sendRedirect("/auth/login");
        }
      }

      chain.doFilter(request, response);
    }catch (ServletException se){
    }catch (IOException e){
    }
  }

  public void init(FilterConfig filterConfig) throws ServletException{
  }

  public void destroy(){
  }
}