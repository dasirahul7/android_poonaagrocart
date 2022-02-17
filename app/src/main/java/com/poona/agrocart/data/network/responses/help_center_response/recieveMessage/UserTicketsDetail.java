
package com.poona.agrocart.data.network.responses.help_center_response.recieveMessage;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserTicketsDetail {

    @SerializedName("ticket_id")
    @Expose
    private String ticketId;
    @SerializedName("ticket_no")
    @Expose
    private String ticketNo;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ticket_type")
    @Expose
    private String ticketType;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("created_by")
    @Expose
    private String createdBy;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
