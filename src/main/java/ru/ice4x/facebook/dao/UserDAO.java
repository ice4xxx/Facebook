package ru.ice4x.facebook.dao;

import org.springframework.stereotype.Component;
import ru.ice4x.facebook.HibernateSessionFactoryUtil;
import ru.ice4x.facebook.models.User;

import java.util.List;

@Component("userDAOBean")
public class UserDAO implements IUserDAO
{
    @Override
    public User getByEmailAndPassword(String email, String password) throws IllegalArgumentException
    {
        var userList = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("FROM User U where U.email = :email and U.password = :password" )
                .setParameter("email",email).setParameter("password",password).list();

        if(userList.size() == 1){
            return (User)userList.get(0);
        }
        else{
            throw new IllegalArgumentException("Incorrect email or password");
        }
    }

    @Override
    public User getById(int id)
    {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(User.class,id);
    }

    @Override
    public boolean verifyEmailUser(String email)
    {
        var result = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("SELECT 1 FROM User U WHERE U.email = :email ").setParameter("email",email).uniqueResult();
        return result != null;
    }

    @Override
    public boolean registerUser(User user)
    {
        try{
            var session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            var transaction = session.getTransaction();
            transaction.begin();;

            session.save(user);
            transaction.commit();
            session.close();

            return true;
        }
        catch (Exception ex){
            return false;
        }
    }

    @Override
    public int getIdByEmail(String email)
    {
        var id = (int)HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .createQuery("SELECT U.id FROM User U WHERE U.email = :email").setParameter("email",email).uniqueResult();

        return id;
    }
}
