package com.jimi.smt.eps.serversocket.entity;

public class Login {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column login.id
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column login.line
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    private String line;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column login.mac
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    private String mac;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column login.ip
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    private String ip;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column login.id
     *
     * @return the value of login.id
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column login.id
     *
     * @param id the value for login.id
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column login.line
     *
     * @return the value of login.line
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    public String getLine() {
        return line;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column login.line
     *
     * @param line the value for login.line
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    public void setLine(String line) {
        this.line = line == null ? null : line.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column login.mac
     *
     * @return the value of login.mac
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    public String getMac() {
        return mac;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column login.mac
     *
     * @param mac the value for login.mac
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column login.ip
     *
     * @return the value of login.ip
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    public String getIp() {
        return ip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column login.ip
     *
     * @param ip the value for login.ip
     *
     * @mbggenerated Thu Mar 01 14:19:24 CST 2018
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }
}