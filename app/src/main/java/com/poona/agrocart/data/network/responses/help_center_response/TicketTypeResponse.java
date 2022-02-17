
package com.poona.agrocart.data.network.responses.help_center_response;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.BaseResponse;

public class TicketTypeResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<TicketType> data = null;

    public List<TicketType> getData() {
        return data;
    }

    public void setData(List<TicketType> data) {
        this.data = data;
    }

    public static class TicketType {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("ticket_type")
        @Expose
        private String ticketType;
        @SerializedName("status")
        @Expose
        private String status;

        public TicketType(String ticketId, String ticketName) {
            this.id = ticketId;
            this.ticketType = ticketName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTicketType() {
            return ticketType;
        }

        public void setTicketType(String ticketType) {
            this.ticketType = ticketType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}
