package ru.ice4x.facebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.ice4x.facebook.dao.IUserDAO;
import ru.ice4x.facebook.models.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController
{
    private IUserDAO userDAO;

    @Autowired
    public RegisterController(IUserDAO userDAO){
        this.userDAO = userDAO;
    }

    @GetMapping()
    public String index(Model model)
    {
        model.addAttribute("user", new User());
        return "Register/register";
    }

    @PostMapping()
    public ModelAndView register(@ModelAttribute(name = "user") @Valid User user, BindingResult bindingResult, HttpServletResponse response)
    {
        if (bindingResult.hasErrors())
        {
            return new ModelAndView("Register/register");
        }

        var isEmailExist = userDAO.verifyEmailUser(user.getEmail());

        if(isEmailExist){
            bindingResult.addError(new FieldError("error","email","User with such email already exists"));
            return new ModelAndView("Register/register");
        }
        else
        {
            if(userDAO.registerUser(user)){
                Cookie iCookie;
                Cookie lCookie;
                Cookie pCookie;
                iCookie = new Cookie("i", null);
                lCookie = new Cookie("l", null);
                pCookie = new Cookie("p", null);
                iCookie.setMaxAge(0);
                lCookie.setMaxAge(0);
                pCookie.setMaxAge(0);
                response.addCookie(iCookie);
                response.addCookie(lCookie);
                response.addCookie(pCookie);
                return new ModelAndView("redirect:/login");
            }
            else{
                return new ModelAndView("Register/register");
            }
        }
    }
}
