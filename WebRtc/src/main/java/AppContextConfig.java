import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.resource.Resource;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;

public class AppContextConfig {

    public HandlerList getHandlersConfig() {
        HandlerList handlers = new HandlerList();
        handlers.addHandler(getWebResourceHandler());
        handlers.addHandler(getServletHandler());
        return handlers;
    }

    private Handler getServletHandler() {
        ServletContextHandler servletsHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletsHandler.setContextPath("/");
        FilterHolder holder = new FilterHolder(new CrossOriginFilter());
        holder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,OPTIONS");
        servletsHandler.addFilter(holder, "/", EnumSet.of(DispatcherType.REQUEST));
        return servletsHandler;
    }


    private Handler getWebResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request,
                               HttpServletResponse response) throws IOException, ServletException {
                if(target.equals("")) {
                    Boolean flag = true;
                    for (Cookie cookie : request.getCookies()) {
                        if ("token".equals(cookie.getName())) {
                            flag = false;
                            if (cookie.getValue() == null || cookie.getValue().equals("")) {
                                flag = true;
                            }
                            break;
                        }
                    }
                    if (flag) {
                        response.sendRedirect("/index.html");
                    }
                }
                super.handle(target, baseRequest, request, response);
            }
        };
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setBaseResource(Resource.newClassPathResource("/webapp"));
        return resourceHandler;
    }
}