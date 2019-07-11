package ua.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.exception.ModelNotFoundException;
import ua.exception.RemoveModelException;
import ua.model.Home;
import ua.service.HomeService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/home")
@PreAuthorize("hasRole(T(ua.model.User).ROLE_ADMIN_COMPANY)")
public class HomeController {

    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    private HomeService homeService;

    public HomeController(HomeService homeService){
        this.homeService = homeService;
    }

    @PreAuthorize("hasAuthority('ADD_HOME')")
    @GetMapping("/add-home")
    public String addHomePage(Model model){
        logger.debug("method get add-home return addHome page");
        model.addAttribute("home", new Home());
        return "/home/addHome";
    }

    @PreAuthorize("hasAuthority('ADD_HOME')")
    @PostMapping("/add-home")
    public String addHomeAction(@ModelAttribute @Validated Home home, BindingResult bindingResult, Model model){
        if(!bindingResult.hasErrors()){
            Home savedHome = homeService.add(home);
            if(savedHome != null && savedHome.getId() != 0){
                logger.debug("method post add-home redirect editHome page with id - " + savedHome.getId());
                return "redirect:/home/edit-home/" + savedHome.getId();
            }
            model.addAttribute("addHomeError", "Failed to add house");
        }

        logger.debug("method post add-home return addHome page");
        Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(
                HashMap::new,
                (m, v) -> m.put(v.getField(), v.getDefaultMessage()),
                HashMap::putAll);
        model.addAttribute("errors", errors);
        model.addAttribute("home", home);
        return "/home/addHome";
    }

    @PreAuthorize("hasRole(T(ua.model.User).ROLE_ADMIN_HOMEOWNERS) || hasAuthority('EDIT_HOME')")
    @GetMapping("/edit-home/{id}")
    public String editHomePage(@PathVariable("id") int id, Model model){
        Home home = homeService.get(id);

        if(home == null) {
            logger.warn("method get edit-home forward to 404 page");
            return "forward:/error/pageNotFound";
        }

        logger.debug("method get edit-home return editHome page");
        model.addAttribute("home" , home);
        return "/home/editHome";
    }

    @PreAuthorize("hasRole(T(ua.model.User).ROLE_ADMIN_HOMEOWNERS) || hasAuthority('EDIT_HOME')")
    @PostMapping("/edit-home/{id}")
    public String editHomeAction(@PathVariable("id") int id, @ModelAttribute @Validated Home home,
                                 BindingResult bindingResult, Model model){
        if(!bindingResult.hasErrors()){
            Home savedHome = homeService.update(id, home);
            if(savedHome != null){
                logger.debug("method post edit-home redirect editHome page with id - " + savedHome.getId());
                return "redirect:/home/edit-home/" + savedHome.getId();
            }
            logger.warn("method edit-home wrong Home parameter with id - " + id);
            model.addAttribute("addHomeError", "Failed to edit house");
        }

        logger.debug("method post edit-home return editHome page");
        Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(
                HashMap::new,
                (m, v) -> m.put(v.getField(), v.getDefaultMessage()),
                HashMap::putAll);
        model.addAttribute("errors", errors);
        model.addAttribute("home" , home);
        return "/home/editHome/";
    }

    @PreAuthorize("hasAuthority('DELETE_HOME')")
    @RequestMapping("/delete-home/{id}")
    public String deleteHomeAction(@PathVariable("id") int id, Model model, HttpServletResponse response){

        try{
            homeService.delete(id);
            logger.debug("method delete-home deleted Home with id - " + id);
            model.addAttribute("success", true);
        } catch (ModelNotFoundException ex){
            logger.warn("method delete-home wrong Home parameter with id - " + id);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (RemoveModelException ex){
            logger.debug("method delete-home failed delete Home with id - " + id);
            model.addAttribute("success", false);
        }

        return "redirect:/";
    }
}
