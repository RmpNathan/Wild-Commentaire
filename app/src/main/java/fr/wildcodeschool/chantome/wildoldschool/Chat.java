package fr.wildcodeschool.chantome.wildoldschool;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by chantome on 26/09/2016.
 */
public class Chat implements Serializable{
    //attributs
    private String name;
    private String desc;
    private String author;
    private Map<String,String> groupUser;
    private Map<String,Message> messages;
    private int categorieChat;
    private boolean status;
    private boolean access;
    private Date lastOpen;
    private int nbrs_messages;
    private String created_on;

    public Chat(){

    }

    public Chat(String name, String author,String desc, boolean status, boolean access, Map<String,String> groupUser,String created_on){
        setName(name);
        setAuthor(author);
        setDesc(desc);
        setStatus(status);
        setAccess(access);
        setGroupUser(groupUser);
        setCreated_on(created_on);
    }

    //setters
    public void setName(String name){this.name=name;}
    public void setAuthor(String author){this.author=author;}
    public void setDesc(String desc){this.desc=desc;}
    public void setGroupUser(Map<String,String> groupUser){this.groupUser=groupUser;}
    public void setMessages(Map<String,Message> messages){this.messages=messages;}
    public void setCategorieChat(int categorieChat){this.categorieChat=categorieChat;}
    public void setStatus(boolean status){this.status=status;}
    public void setAccess(boolean access){this.access=access;}
    public void setLastOpen(Date lastOpen){this.lastOpen=lastOpen;}
    public void setNbrs_messages(int nbrs_messages){this.nbrs_messages=nbrs_messages;}
    public void setCreated_on(String created_on){this.created_on=created_on;}

    //getters
    public String getName(){return this.name;}
    public String getAuthor(){return this.author;}
    public String getDesc(){return this.desc;}
    public Map<String,String> getGroupUser(){return this.groupUser;}
    public Map<String,Message> getMessages(){return this.messages;}
    public int getCategorieChat(){return this.categorieChat;}
    public boolean isStatus(){return this.status;}
    public boolean isAccess(){return this.access;}
    public Date getLastOpen(){return this.lastOpen;}
    public int getNbrs_messages(){return this.nbrs_messages;}
    public String getCreated_on(){return this.created_on;}

}