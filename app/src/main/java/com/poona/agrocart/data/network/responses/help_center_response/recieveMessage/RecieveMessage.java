package com.poona.agrocart.data.network.responses.help_center_response.recieveMessage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RecieveMessage {

    @SerializedName("user_tickets_details")
    @Expose
    private List<com.poona.agrocart.data.network.responses.help_center_response.recieveMessage.UserTicketsDetail> userTicketsDetails = null;
    @SerializedName("all_chats")
    @Expose
    private List<AllChat> allChats = null;

    public List<com.poona.agrocart.data.network.responses.help_center_response.recieveMessage.UserTicketsDetail> getUserTicketsDetails() {
        return userTicketsDetails;
    }

    public void setUserTicketsDetails(List<com.poona.agrocart.data.network.responses.help_center_response.recieveMessage.UserTicketsDetail> userTicketsDetails) {
        this.userTicketsDetails = userTicketsDetails;
    }

    public List<AllChat> getAllChats() {
        return allChats;
    }

    public void setAllChats(List<AllChat> allChats) {
        this.allChats = allChats;
    }

}
