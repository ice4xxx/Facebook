package ru.ice4x.facebook.models;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Entity
@Table(name = "Users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 3,message = "Please enter email address")
    private String email;

    @NotNull(message = "Password must be at least 8 characters long")
    @Size(min = 8)
    private String password;

    public User(){}

    public User(String login, String password){
        this.email = login;
        this.password = password;
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail (){
        return email;
    }

    public void setEmail(String login) { this.email = login; }

    public String getPassword(){ return password; }

    public void setPassword(String password) throws NoSuchAlgorithmException
    {
        if(password.length() < 8){
            return;
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashPassword = no.toString(16);
        while (hashPassword.length() < 32) {
            hashPassword = "0" + hashPassword;
        }
        this.password =  hashPassword;
    }
}
