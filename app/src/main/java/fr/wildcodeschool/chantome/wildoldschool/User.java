package fr.wildcodeschool.chantome.wildoldschool;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chantome on 22/09/2016.
 */
public class User{
    private String email;
    private String firstname;//*
    private String lastname;//*
    private String pseudo;
    private boolean genre;//*
    private String birthday;
    private String desc;
    private int photo;
    private String favories="0";
    private Date created_on;
    private boolean online;
    private boolean writing;
    private int profil_complete;
    private Map<String,Chat> created_chats;
    private Map<String,String> list_added_chats;
    private String adresse;
    private String school;
    private String formation;

    public User(){
    }

    public User(String pseudo){
        setPseudo(pseudo);
    }

    public User(String pseudo, boolean online){
        setPseudo(pseudo);
        setOnline(online);
    }

    public User(String pseudo,
                String firstname,
                String lastname,
                String descriptif,
                String birthday,
                String school,
                String formation,
                String favories,
                boolean genre,
                boolean online
    ){
        setPseudo(pseudo);
        setFirstname(firstname);
        setLastname(lastname);
        setDesc(descriptif);
        setBirthday(birthday);
        setSchool(school);
        setFormation(formation);
        setFavories(favories);
        setGenre(genre);
        setOnline(online);
    }

    //setter
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setPseudo(String pseudo){ this.pseudo = pseudo; }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setGenre(boolean genre) {
        this.genre = genre;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setFavories(String favories) {
        this.favories = favories;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setWriting(boolean writing) {
        this.writing = writing;
    }

    public void setProfil_complete(int profil_complete) {
        this.profil_complete = profil_complete;
    }

    public void setCreated_chats(Map<String, Chat> created_chats) {
        this.created_chats = created_chats;
    }

    public void setList_added_chats(Map<String, String> list_added_chats) {
        this.list_added_chats = list_added_chats;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    //getter

    public String getEmail() {
        return this.email;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getPseudo(){ return this.pseudo; }

    public String getLastname() {
        return this.lastname;
    }

    public boolean isGenre() {
        return this.genre;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getPhoto() {
        return this.photo;
    }

    public String getFavories() {
        return this.favories;
    }

    public Date getCreated_on() {
        return this.created_on;
    }

    public boolean isOnline() {
        return this.online;
    }

    public boolean isWriting() {
        return this.writing;
    }

    public int getProfil_complete() {
        return this.profil_complete;
    }

    public Map<String, Chat> getCreated_chats() {
        return this.created_chats;
    }

    public Map<String, String> getList_added_chats() {
        return this.list_added_chats;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public String getSchool() {
        return this.school;
    }

    public String getFormation() {
        return this.formation;
    }
}
