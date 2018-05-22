package com.zberg.esc.tippspiel.ranking.controller;

import com.zberg.esc.tippspiel.ranking.dto.Ranking;
import com.zberg.esc.tippspiel.ranking.service.RankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RankingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RankingController.class);
    private final RankingService rankingService;

    @Autowired
    public RankingController(final RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/ranking")
    public String getRanking(final Model model) {
        LOGGER.info("querying rankings...");
        final List<Ranking> ranking = rankingService.getRankingForTournament();
        model.addAttribute("ranking", ranking);
        return "ranking";

    }
}
