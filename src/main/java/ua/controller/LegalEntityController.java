package ua.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.model.Home;
import ua.service.HomeService;
import ua.service.LegalEntityService;
import ua.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/entity")
@PreAuthorize("hasRole(T(ua.model.User).ROLE_ADMIN_COMPANY)")
public class LegalEntityController {

    private final static Logger logger = LoggerFactory.getLogger(LegalEntityController.class);

    private HomeService homeService;

    private LegalEntityService legalEntityService;

    public LegalEntityController(HomeService homeService, LegalEntityService legalEntityService){
        this.homeService = homeService;
        this.legalEntityService = legalEntityService;
    }

    @PreAuthorize("hasAuthority('SET_DEFAULT_HOME')")
    @ResponseBody
    @PostMapping("/set_default_home/{id}")
    public ResponseEntity<?> setDefaultHomeActionAjax(@PathVariable("id") int id, HttpServletRequest request){
        if(RequestUtil.isAjax(request)) {
            Home home = homeService.get(id);

            if (home != null) {
                String json;
                Home defaultHome = legalEntityService.setDefaultHome(home);
                if(defaultHome != null){
                    logger.debug("method set_default_home set home with id - " + id);
                    json = "{\"success\":true}";
                } else {
                    logger.error("method set_default_home failed set home with id - " + id);
                    json = "{'success':false}";
                }
                return ResponseEntity.ok(json);
            }

            logger.warn("method ajax set_default_home failed home with id - " + id);
            return ResponseEntity.ok("{'success':'false'}");
        }
        return ResponseEntity.badRequest().build();
    }
}
