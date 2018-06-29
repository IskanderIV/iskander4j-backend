package ru.cleverhause.app.filters;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
        System.out.println("Inside filter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        cfg = null;
        ctx = null;
    }
}
