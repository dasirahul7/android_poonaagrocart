
package com.poona.agrocart.data.network.reponses.help_center_response.recieveMessage;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RecieveMessage {

    @SerializedName("user_tickets_details")
    @Expose
    private List<UserTicketsDetail> userTicketsDetails = null;
    @SerializedName("all_chats")
    @Expose
    private List<AllChat> allChats = null;

    public List<UserTicketsDetail> getUserTicketsDetails() {
        return userTicketsDetails;
    }

    public void setUserTicketsDetails(List<UserTicketsDetail> userTicketsDetails) {
        this.userTicketsDetails = userTicketsDetails;
    }

    public List<AllChat> getAllChats() {
        return allChats;
    }

    public void setAllChats(List<AllChat> allChats) {
        this.allChats = allChats;
    }

}
