package ru.ice4x.facebook.controllers;

import org.hibernate.boot.model.source.internal.hbm.ModelBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.ice4x.facebook.dao.IMessageDao;
import ru.ice4x.facebook.dao.IUserDAO;
import ru.ice4x.facebook.models.Message;
import ru.ice4x.facebook.models.User;

import javax.validation.Valid;

@Controller
@RequestMapping("/message")
public class MessageController
{
    private final IMessageDao messageDAO;
    private final IUserDAO userDAO;
    private User currentUser;

    @Autowired
    public MessageController(IMessageDao messageDAO, IUserDAO userDAO){
        this.messageDAO = messageDAO;
        this.userDAO = userDAO;
    }

    @GetMapping()
    public ModelAndView index(@CookieValue(name = "i", required = true) String id, Model model){
        if(id == null){
            return new ModelAndView("redirect:/login");
        }
        currentUser = userDAO.getById(Integer.parseInt(id));
        model.addAttribute("outgoingMessages",messageDAO.getOutgoingMessagesByUserEmail(currentUser.getEmail()));
        model.addAttribute("incomingMessages",messageDAO.getIncomingMessagesByUserEmail(currentUser.getEmail()));
        model.addAttribute("message",new Message());
        return new ModelAndView("Message/message");
    }

    @PostMapping("/send")
    public ModelAndView send(@ModelAttribute(name = "message")@Valid Message message,Model model, BindingResult bindingResult){

        if(currentUser == null){
            return new ModelAndView("redirect:/login");
        }
        model.addAttribute("messages",messageDAO.getOutgoingMessagesByUserEmail(currentUser.getEmail()));

        if(bindingResult.hasErrors()){
            return new ModelAndView("Message/message");
        }

        if(userDAO.verifyEmailUser(message.getToUser()))
        {
            int id = userDAO.getIdByEmail(message.getToUser());
            message.setFromUser(currentUser.getEmail());
            messageDAO.addMessage(message);
            return new ModelAndView("redirect:/message");
        }
        else{
            bindingResult.addError(new FieldError("error","toUser","User with such email do not exist"));
            return new ModelAndView("Message/message");
        }
    }
}
