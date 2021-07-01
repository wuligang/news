package com.hisoft.news.javabean;

/**
 * @program: news
 * @description:
 * @author: wlg
 * @create: 2021-06-24 09:55:55
 **/
public class User {
    private String uname;
    private String[] hobbies;

    public User(String uname, String[] hobbies) {
        this.uname = uname;
        this.hobbies = hobbies;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }
}
