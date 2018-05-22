package com.zberg.esc.tippspiel.admin;

import com.zberg.esc.tippspiel.tournament.TournamentService;
import com.zberg.esc.tippspiel.tournament.dto.PlayerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class OverviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OverviewController.class);
    private final TournamentService tournamentService;

    @Autowired
    public OverviewController(final TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @RequestMapping(value = "/admin/overview", method = RequestMethod.GET)
    public String allPlayers(final Model model) {
        LOGGER.debug("loading player form");
        final List<PlayerDto> players = tournamentService.getAllPlayers();
        model.addAttribute("players", players);
        return "admin/overview";
    }
}
