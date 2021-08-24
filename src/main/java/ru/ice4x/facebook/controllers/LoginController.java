package ru.ice4x.facebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.ice4x.facebook.dao.IUserDAO;
import ru.ice4x.facebook.models.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController
{
    IUserDAO userDAO;

    @GetMapping()
    public String index(Model model, HttpServletResponse response)
    {
        deleteCookie(response);
        model.addAttribute("user", new User());
        return "Login/login";
    }

    @Autowired
    public LoginController(IUserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    @PostMapping()
    public ModelAndView login(@ModelAttribute("user") User user, @RequestParam(value = "rememberMe",defaultValue = "false") boolean rememberMeValue, HttpServletResponse response)
    {
        try
        {
            User existingUser = userDAO.getByEmailAndPassword(user.getEmail(),user.getPassword());

            if(rememberMeValue)
            {
                updateCookie(existingUser,response);
            }
            else
            {
                deleteCookie(response);
            }
        }
        catch (IllegalArgumentException ex){
            return new ModelAndView("Login/login");
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return new ModelAndView("redirect:/register");
        }
        return new ModelAndView("redirect:/message");
    }

    private void updateCookie(User existingUser, HttpServletResponse response){
        Cookie lCookie;
        Cookie pCookie;
        Cookie iCookie;
        iCookie = new Cookie("i", String.valueOf(existingUser.getId()));
        lCookie = new Cookie("l", existingUser.getEmail());
        pCookie = new Cookie("p", existingUser.getPassword());
        response.addCookie(iCookie);
        response.addCookie(lCookie);
        response.addCookie(pCookie);
    }

    private void deleteCookie( HttpServletResponse response){
        Cookie lCookie;
        Cookie pCookie;
        Cookie iCookie;
        iCookie = new Cookie("i",null);
        lCookie = new Cookie("l", null);
        pCookie = new Cookie("p", null);
        iCookie.setMaxAge(0);
        lCookie.setMaxAge(0);
        pCookie.setMaxAge(0);
        response.addCookie(iCookie);
        response.addCookie(lCookie);
        response.addCookie(pCookie);
    }
}
