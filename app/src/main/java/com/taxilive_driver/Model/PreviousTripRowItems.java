package com.taxilive_driver.Model;

/**
 * Created by cqlsys on 25/5/17.
 */

public class PreviousTripRowItems
{
    private String title;
    private String request_id;
    private String emp_id;
    private String req_lat_from;
    private String req_lng_from;
    private String req_lat_to;
    private String req_lng_to;
    private String req_location_from;
    private String req_location_to;

    public String getStart_time_from() {
        return start_time_from;
    }

    public void setStart_time_from(String start_time_from) {
        this.start_time_from = start_time_from;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    private String start_time_from;
    private String start_date;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getReq_lat_from() {
        return req_lat_from;
    }

    public void setReq_lat_from(String req_lat_from) {
        this.req_lat_from = req_lat_from;
    }

    public String getReq_lng_from() {
        return req_lng_from;
    }

    public void setReq_lng_from(String req_lng_from) {
        this.req_lng_from = req_lng_from;
    }

    public String getReq_lat_to() {
        return req_lat_to;
    }

    public void setReq_lat_to(String req_lat_to) {
        this.req_lat_to = req_lat_to;
    }

    public String getReq_lng_to() {
        return req_lng_to;
    }

    public void setReq_lng_to(String req_lng_to) {
        this.req_lng_to = req_lng_to;
    }

    public String getReq_location_from() {
        return req_location_from;
    }

    public void setReq_location_from(String req_location_from) {
        this.req_location_from = req_location_from;
    }

    public String getReq_location_to() {
        return req_location_to;
    }

    public void setReq_location_to(String req_location_to) {
        this.req_location_to = req_location_to;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmp_image() {
        return emp_image;
    }

    public void setEmp_image(String emp_image) {
        this.emp_image = emp_image;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPay_by() {
        return pay_by;
    }

    public void setPay_by(String pay_by) {
        this.pay_by = pay_by;
    }

    private String username;
    private String emp_image;
    private String user_image;
    private String total_amt;
    private String type;
    private String pay_by;

    public PreviousTripRowItems()
    {}

    public PreviousTripRowItems(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}