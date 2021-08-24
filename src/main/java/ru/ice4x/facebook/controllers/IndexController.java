package ru.ice4x.facebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.ice4x.facebook.dao.IUserDAO;

@Controller
@RequestMapping("/")
public class IndexController
{
    private IUserDAO userDAO;

    @Autowired
    public IndexController(IUserDAO userDAO){
        this.userDAO = userDAO;
    }

    @GetMapping()
    public ModelAndView index(@CookieValue(value = "l",required = false) String email, @CookieValue(value = "p",required = false) String password){
        if(email == null && password == null){
            return new ModelAndView("redirect:/login");
        }
        else{
            try{
                userDAO.getByEmailAndPassword(email, password);
                return new ModelAndView("redirect:/message");
            }
            catch (IllegalArgumentException ex){
                return new ModelAndView("redirect:/login");
            }
        }
    }
}
