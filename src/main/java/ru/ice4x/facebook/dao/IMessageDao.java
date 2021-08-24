package ru.ice4x.facebook.dao;

import ru.ice4x.facebook.models.Message;

import java.util.List;

public interface IMessageDao
{
    List<Message> getOutgoingMessagesByUserEmail(String email);

    List<Message> getIncomingMessagesByUserEmail(String email);

    boolean addMessage(Message message);
}
