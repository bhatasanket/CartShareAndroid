/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.it354f701.CartShare;

import android.content.Context;

import java.util.HashMap;

/**
 *
 * @author it353s722
 */
public class LoginBean {
    
    private String userName;
    private String userPassword;

    /**
     * @return the userName
     */

    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @param userPassword the userPassword to set
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public static String authenticate(LoginBean user) {
        String response;
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("Billy111","aaa");
        userMap.put("Mary222","bbb");
        userMap.put("Joe333","ccc");
        userMap.put("abc444","ddd");
        if (userMap.containsKey(user.userName)) {
            if (userMap.get(user.userName).equals(user.userPassword)) {
                return user.userName+" is a VALID USER";
            }
            else{
                return user.userName+" is a INVALID USER or has a INVALID PASSWORD";
            }
        }
        
        return "User : "+user.userName+" not found";
    }
    public static String authenticateDB(Context context, LoginBean user) {
        String response;
//        HashMap<String, String> userMap = new HashMap<>();
//        userMap.put("Billy111","aaa");
//        userMap.put("Mary222","bbb");
//        userMap.put("Joe333","ccc");
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);

        AccountBean accountBean = sqLiteHelper.getUserDetails(user.userName);
        if (accountBean!=null) {
            if (accountBean.getPassword().equals(user.userPassword)) {
                return user.userName+" is a VALID USER";
            }
            else{
                return user.userName+" is a INVALID USER or has a INVALID PASSWORD";
            }
        }

        return "User : "+user.userName+" not found";
    }
}
