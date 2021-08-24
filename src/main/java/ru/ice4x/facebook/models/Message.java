package ru.ice4x.facebook.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Messages")
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Please enter at least 1 character")
    @Size(min = 1,message = "Please enter at least 1 character")
    private String text;

    private String fromUser;

    @NotNull(message = "Please enter email")
    @Size(min = 1,message = "Please enter email")
    private String toUser;

    public void setToUser(String toUser)
    {
        this.toUser = toUser;
    }

    public String getToUser()
    {
        return toUser;
    }

    public String getFromUser()
    {
        return fromUser;
    }

    public void setFromUser(String fromUser)
    {
        this.fromUser = fromUser;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public int getId()
    {
        return id;
    }
}
