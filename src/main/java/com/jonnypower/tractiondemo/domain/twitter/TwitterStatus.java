package com.jonnypower.tractiondemo.domain.twitter;

public class TwitterStatus extends TwitterMessage {

    // =========================
    // ATTRIBUTES
    // =========================

    private TwitterUser user;
    private String text;

    // =========================
    // IMPLEMENTATION OF TwitterMessage
    // =========================

    public void accept(TwitterMessageVisitor visitor) {
        visitor.visit(this);
    }

    // =========================
    // PUBLIC METHODS
    // =========================

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(TwitterUser user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public TwitterUser getUser() {
        return user;
    }

}
