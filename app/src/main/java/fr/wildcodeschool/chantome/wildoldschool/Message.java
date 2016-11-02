package fr.wildcodeschool.chantome.wildoldschool;

import java.io.Serializable;

/**
 * Created by chantome on 21/09/2016.
 */
public class Message implements Serializable {
    private String uid;
    private String message;
    private String created_on;

    public Message(){
    }

    public Message(String uid, String message, String created_on){
        setUid(uid);
        setMessage(message);
        setCreated_on(created_on);
    }

    //setters
    public void setUid(String uid){ this.uid = uid;}
    public void setMessage(String message){
        this.message = message;
    }
    public void setCreated_on(String created_on){this.created_on=created_on;}

    //getters
    public String getUid(){
        return this.uid;
    }
    public String getMessage(){
        return this.message;
    }
    public String getCreated_on(){ return this.created_on;}
}
