package net.sni.graduation.constant;

import lombok.Getter;

@Getter
public enum VoteEnum {
    NEGATIVE("negative"), POSITIVE("positive");

    private final String vote;

    VoteEnum(String vote) {
        this.vote = vote;
    }
}
