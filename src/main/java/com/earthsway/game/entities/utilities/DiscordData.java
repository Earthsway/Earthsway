package com.earthsway.game.entities.utilities;

public class DiscordData {
    protected String userId;
    protected String username;
    protected String discriminator;
    protected String avatar;

    public String userId() {
        return userId;
    }
    public String username() {
        return username;
    }
    public String discriminator() {
        return discriminator;
    }
    public String avatar() {
        return avatar;
    }

    public DiscordData(String userId, String username, String discriminator, String avatar){
        this.userId = userId;
        this.username = username;
        this.discriminator = discriminator;
        this.avatar = avatar;
    }
}
