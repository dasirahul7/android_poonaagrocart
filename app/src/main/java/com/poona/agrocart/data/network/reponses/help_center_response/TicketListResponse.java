
package com.poona.agrocart.data.network.reponses.help_center_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.reponses.BaseResponse;

import java.util.List;

public class TicketListResponse extends BaseResponse {


    @SerializedName("data")
    @Expose
    private TicketList data;

    public TicketList getData() {
        return data;
    }

    public void setData(TicketList data) {
        this.data = data;
    }

    public class TicketList {

        @SerializedName("count_tickets")
        @Expose
        private String countTickets;
        @SerializedName("user_tickets")
        @Expose
        private List<UserTicket> userTickets = null;

        public String getCountTickets() {
            return countTickets;
        }

        public void setCountTickets(String countTickets) {
            this.countTickets = countTickets;
        }

        public List<UserTicket> getUserTickets() {
            return userTickets;
        }

        public void setUserTickets(List<UserTicket> userTickets) {
            this.userTickets = userTickets;
        }

        public  class UserTicket {
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

        }
    }


}
