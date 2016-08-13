package sa42.uno.rest;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns="/api/*",dispatcherTypes={DispatcherType.REQUEST})
public class CORSFilter implements Filter{

    @Override
    public void init(FilterConfig config){           
        }

    @Override
    public void doFilter(ServletRequest request, 
            ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Access-Control-Allow-Origin","*");
        resp.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST");
        chain.doFilter(request, response);       
    }

    @Override
    public void destroy() {
    }
    
}
