package com.roomex.assessment.integration;

public enum Language {
    EN ("EN"),
    ES ("ES"),
    FR ("FR");

    private final String id;

    Language(String id) {
        this.id = id;
    }

    public static Language getLanguageId(String value) {
        for(Language lang: Language.values()) {
            if(lang.id.equalsIgnoreCase(value)) {
                return lang;
            }
        }

        throw new IllegalArgumentException("No language exists with id: " + value);
    }

    @Override
    public String toString() {
        return this.id;
    }
}
