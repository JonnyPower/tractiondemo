package com.jonnypower.tractiondemo.domain.twitter;

public interface TwitterMessageVisitor {

    // =========================
    // PACKAGE PRIVATE METHODS
    // =========================

    void visit(TwitterStatus twitterStatus);

}
