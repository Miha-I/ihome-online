package ua.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ua.model.LegalEntity;
import ua.service.LegalEntityService;
import ua.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static ua.config.AppConfig.DEFAULT_THEME_CONFIGURATION_NAME;

@Component
@PropertySource("classpath:application.properties")
public class ThemeLegalEntityInterceptor extends HandlerInterceptorAdapter {

    private String defaultTheme;
    private LegalEntityService legalEntityService;

    private static final Logger logger = LoggerFactory.getLogger(ThemeLegalEntityInterceptor.class);

    @Autowired
    public void setDefaultTheme(Environment environment){
        defaultTheme = environment.getRequiredProperty(DEFAULT_THEME_CONFIGURATION_NAME);
    }

    @Autowired
    public void serLegalEntityService(LegalEntityService legalEntityService){
        this.legalEntityService = legalEntityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("method preHandle request - " + request.getRequestURL());
        String subDomain = RequestUtil.getSubDomain(request);
        LegalEntity legalEntity = legalEntityService.getCurrent();
        if(legalEntity == null) {
            logger.warn("LegalEntity not exist with subdomain - " + subDomain);
            ModelAndView mav = new ModelAndView("error/pageNotFound");
            throw new ModelAndViewDefiningException(mav);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("method postHandle request - " + request.getRequestURL());
        if (modelAndView == null || !modelAndView.hasView()) {
            return;
        }
        String viewName = modelAndView.getViewName();
        if (viewName == null || viewName.startsWith("redirect:") || viewName.startsWith("forward:")) {
            return;
        }

        String theme = "";
        LegalEntity legalEntity = legalEntityService.getCurrent();
        if(Objects.nonNull(legalEntity)){
            modelAndView.addObject("legalEntity", legalEntity);
            theme = legalEntity.getTheme();
            if(theme == null || theme.isBlank())
                theme = defaultTheme;

            modelAndView.setViewName("/" + theme + "/" + viewName);
        }

        logger.debug("set theme - " + theme);
        modelAndView.addObject("theme", theme);
    }
}
