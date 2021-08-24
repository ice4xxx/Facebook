package ru.ice4x.facebook.dao;

import org.springframework.stereotype.Component;
import ru.ice4x.facebook.models.User;

public interface IUserDAO
{
    public User getByEmailAndPassword(String login, String password) throws IllegalArgumentException;

    public User getById(int id);

    public boolean verifyEmailUser(String email);

    public boolean registerUser(User user);

    public int getIdByEmail(String  email);
}
