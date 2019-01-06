package org.assembly.pss.interceptor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assembly.pss.annotation.RequireAdmin;
import org.assembly.pss.config.PropertyConfig;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AccessControlInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LogManager.getLogger();

    private final String adminUser;
    private final String adminPassword;
    private boolean enableAdmin = false;

    public AccessControlInterceptor() {
        // TODO proper user authentication/management, basic auth is just a temporary thing... i hope
        adminUser = PropertyConfig.get("admin.user");
        adminPassword = PropertyConfig.get("admin.password");
        if (StringUtils.isBlank(adminUser)) {
            LOG.warn("Admin username not set, disabling admin functions");
        } else if (StringUtils.isBlank(adminPassword)) {
            LOG.warn("Admin password not set, disabling admin functions");
        } else {
            enableAdmin = true;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod && isAnnotated((HandlerMethod) handler, RequireAdmin.class)) {
            return checkAdmin(request, response);
        }
        return true;
    }

    private boolean isAnnotated(HandlerMethod handlerMethod, Class<? extends Annotation> clazz) {
        Annotation annotation = handlerMethod.getMethod().getAnnotation(clazz);
        if (annotation != null) {
            return true;
        }
        annotation = handlerMethod.getMethod().getDeclaringClass().getAnnotation(clazz);
        return annotation != null;
    }

    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response) {
        if (!enableAdmin) {
            try {
                response.sendError(404);
            } catch (IOException ex) {
                LOG.error("Failed to send 404 error", ex);
            }
        } else {
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.startsWith(authHeader, "Basic ")) {
                String basicAuth = new String(Base64.getDecoder().decode(authHeader.substring(6)));
                String[] values = basicAuth.split(":", 2);
                if (values.length == 2 && values[0].equals(adminUser) && values[1].equals(adminPassword)) { // TODO
                    return true;
                }
            }
            try {
                response.sendError(403);
            } catch (IOException ex) {
                LOG.error("Failed to send 403 error", ex);
            }
        }
        return false;
    }
}
