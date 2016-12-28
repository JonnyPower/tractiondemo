package com.jonnypower.tractiondemo.domain.twitter;

public abstract class TwitterMessage extends TwitterIndexed {

    // =========================
    // ABSTRACT METHODS
    // =========================

    public abstract void accept(TwitterMessageVisitor visitor);

}
