package ru.cleverhause.filters;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(filterName = "UserFilter", urlPatterns = {"/*"},
initParams = {@WebInitParam(name = "paramName", value = "someString")})
public class UserFilter implements Filter {
    private FilterConfig cfg;
    private ServletContext ctx;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.cfg = filterConfig;
        this.ctx = cfg.getServletContext();
        ctx.log(cfg.getInitParameter("paramName"));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(ctx.getResource("classpath:application.properties"));
    }

    @Override
    public void destroy() {

    }
}
