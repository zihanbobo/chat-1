package com.brother.myanmar.chat.bean;

public class PacketState {
    private String packetId;
    private Integer reciever;//0:normal 1:luck
    private String recieverName;
    private String recieverAccount;
    private String recieverAvatar;
    private Double amount;
    private Long time;

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public Integer getReciever() {
        return reciever;
    }

    public void setReciever(Integer reciever) {
        this.reciever = reciever;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public String getRecieverAccount() {
        return recieverAccount;
    }

    public void setRecieverAccount(String recieverAccount) {
        this.recieverAccount = recieverAccount;
    }

    public String getRecieverAvatar() {
        return recieverAvatar;
    }

    public void setRecieverAvatar(String recieverAvatar) {
        this.recieverAvatar = recieverAvatar;
    }
}
