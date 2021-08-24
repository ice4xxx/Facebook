package ru.ice4x.facebook.dao;

import org.springframework.stereotype.Component;
import ru.ice4x.facebook.HibernateSessionFactoryUtil;
import ru.ice4x.facebook.models.Message;

import java.util.List;

@Component("messageDAOBean")
public class MessageDAO implements IMessageDao
{

    @Override
    public List<Message> getOutgoingMessagesByUserEmail(String email)
    {
        return (List<Message>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("FROM Message M WHERE M.fromUser = :email ").setParameter("email", email).list();
    }

    @Override
    public List<Message> getIncomingMessagesByUserEmail(String email)
    {
        return (List<Message>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("FROM Message M WHERE M.toUser = :email ").setParameter("email", email).list();
    }

    @Override
    public boolean addMessage(Message message)
    {
        try{
            var session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            var transaction = session.getTransaction();
            transaction.begin();;

            session.save(message);
            transaction.commit();
            session.close();

            return true;
        }
        catch (Exception ex){
            return false;
        }
    }
}
