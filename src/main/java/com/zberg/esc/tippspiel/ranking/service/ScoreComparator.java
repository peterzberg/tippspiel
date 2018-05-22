package com.zberg.esc.tippspiel.ranking.service;

import com.zberg.esc.tippspiel.tournament.db.entities.Player;

import java.util.Comparator;

public class ScoreComparator implements Comparator<Score> {
    @Override
    public int compare(Score o1, Score o2) {
        int scoreComparison = o2.getScore() - o1.getScore();
        if (scoreComparison != 0) {
            return scoreComparison;
        }
        final Player p1 = o1.getPlayer();
        final Player p2 = o2.getPlayer();

        final int lastnameComparison = p1.getLastname().compareTo(p2.getLastname());
        if (lastnameComparison != 0) {
            return lastnameComparison;
        }
        final int firstnameComparison = p1.getFirstname().compareTo(p2.getFirstname());
        if (firstnameComparison != 0) {
            return firstnameComparison;
        }
        final int streetComparison = p1.getStreet().compareTo(p2.getStreet());
        if (streetComparison != 0) {
            return streetComparison;
        }
        final int zipComparison = p1.getZip().compareTo(p2.getZip());
        if (zipComparison != 0) {
            return zipComparison;
        }
        final int cityComparison = p1.getCity().compareTo(p2.getCity());
        if (cityComparison != 0) {
            return cityComparison;
        }
        return 0;
    }
}
