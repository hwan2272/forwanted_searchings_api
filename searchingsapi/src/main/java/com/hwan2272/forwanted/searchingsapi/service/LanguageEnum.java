package com.hwan2272.forwanted.searchingsapi.service;

public enum LanguageEnum {
    ko("ko"),
    en("en"),
    ja("ja"),
    jp("jp"),
    tagKo("태그"),
    tagEn("tag"),
    tagJa("タグ");

    final private String name;
    LanguageEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
