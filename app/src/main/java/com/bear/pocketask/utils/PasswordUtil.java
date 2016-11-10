package com.bear.pocketask.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具类
 * Created by bear on 16/11/10.
 */

public class PasswordUtil {
    /**
     * 获取经过简单md5加密的密码，只进行一次加密
     *
     * @param password 需要加密的密码
     * @return string 经过加密的密码
     * @throws NoSuchAlgorithmException
     */
    public static String getSampleEncodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new BigInteger(1, md.digest(password.getBytes())).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * \
     * 获取经过两次md5加密的密码
     *
     * @param password
     * @return
     */
    public static String getDoubleEncodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String once = new BigInteger(1, md.digest(password.getBytes())).toString(16);
            return new BigInteger(1, md.digest(once.getBytes())).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取经过用户名和密码双重加密的密码
     * @param username
     * @param password
     * @return
     */
    public static String getEncodeUsernamePassword(String username, String password) {
        String pwd = username + password;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new BigInteger(1, md.digest(pwd.getBytes())).toString(16);
    }
}
