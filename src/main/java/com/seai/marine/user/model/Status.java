package com.seai.marine.user.model;

public enum Status implements AsyncApiEnum {

    ONBOARD, HOME;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getLabel() {
        return name();
    }
}
