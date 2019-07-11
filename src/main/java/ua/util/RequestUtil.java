package ua.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Component
@SuppressWarnings("WeakerAccess")
public class RequestUtil {

    private final static Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    public static boolean isAjax(@NotNull HttpServletRequest request){
        logger.debug(request.getHeader("X-Requested-With"));
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public static String getSubDomain(@NotNull HttpServletRequest request) {
        return request.getServerName().replaceAll("[\\w.-]*?([\\w\\-]*)\\.?localhost", "$1");
    }

    public static String getSubDomain() {
        HttpServletRequest request = getHttpServletRequest();
        if(request == null){
            logger.debug("method getSubDomain() returned ''");
            return "";
        }
        String subDomain = getSubDomain(request);
        logger.debug("method getSubDomain() returned " + subDomain);
        return subDomain;
    }


    public static HttpServletRequest getHttpServletRequest() {
        //return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        if (attribs instanceof NativeWebRequest) {
            return (HttpServletRequest) ((NativeWebRequest) attribs).getNativeRequest();
        }
        if (attribs instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) attribs).getRequest();
        }
        return null;
    }

    public static String getDomain() {
        HttpServletRequest request = getHttpServletRequest();
        if(request == null){
            logger.debug("method getDomain() returned ''");
            return "";
        }
        StringBuffer url = request.getRequestURL();
        String domain = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        logger.debug("method getDomain() returned " + domain);
        return domain;
    }

    public static String getOrigin() {
        HttpServletRequest request = getHttpServletRequest();
        if(request == null){
            logger.debug("method getOrigin() returned ''");
            return "";
        }
        String origin = request.getHeader("Origin");
        logger.debug("method getOrigin() returned " + origin);
        return origin;
    }
}
