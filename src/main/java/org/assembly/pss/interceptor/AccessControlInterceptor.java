package org.assembly.pss.interceptor;

import java.lang.annotation.Annotation;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.assembly.pss.annotation.RequireAdmin;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AccessControlInterceptor extends HandlerInterceptorAdapter {

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
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.startsWith(authHeader, "Basic ")) {
            String basicAuth = new String(Base64.getDecoder().decode(authHeader.substring(6)));
            String[] values = basicAuth.split(":", 2);
            if (values.length == 2) {
                return values[0].equals("admin") && values[1].equals("nimda"); // TODO
            }
        }
        return false;
    }
}
