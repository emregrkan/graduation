package net.sni.graduation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteEnum {
    NEGATIVE("negative"), POSITIVE("positive");

    private final String vote;
}
