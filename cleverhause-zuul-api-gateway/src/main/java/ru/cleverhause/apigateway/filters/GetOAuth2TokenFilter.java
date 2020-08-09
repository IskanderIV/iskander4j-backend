package ru.cleverhause.apigateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.util.HTTPRequestUtils;
import org.springframework.cloud.netflix.zuul.util.RequestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@Component
public class GetOAuth2TokenFilter extends ZuulFilter {

    private static final String FILTER_PATH = "/oauth/token";

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Object requestUri = ctx.getRequest().getRequestURI();
        String requestMethod = ctx.getRequest().getMethod();
        return requestUri.equals(FILTER_PATH) && requestMethod.equals(HttpMethod.GET.name());
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequestWrapper wrapper = new PostHttpServletRequest(ctx.getRequest());
        ctx.setRequest(wrapper);
        return null;
    }

    private static class PostHttpServletRequest extends HttpServletRequestWrapper {
        public PostHttpServletRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getMethod() {
            return HttpMethod.POST.name();
        }
    }
}
