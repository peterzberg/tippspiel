package com.zberg.esc.tippspiel.tournament.db.entities;

public enum ScoreRange {
    R_100_110("100 - 110"),//
    R_111_120("110 - 120"),//
    R_121_130("121 - 130"),//
    R_131_140("131 - 140"),//
    R_141_150("141 - 150"),//
    R_151_160("151 - 160"),//
    R_161_170("161 - 170"),//
    R_171_OPEN("171 - ...");

    private final String text;

    ScoreRange(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
