package com.zberg.esc.tippspiel.admin;

import com.zberg.esc.tippspiel.tournament.TournamentService;
import com.zberg.esc.tippspiel.tournament.db.TeamRepository;
import com.zberg.esc.tippspiel.tournament.db.entities.ScoreRange;
import com.zberg.esc.tippspiel.tournament.db.entities.Team;
import com.zberg.esc.tippspiel.tournament.dto.TournamentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class TournamentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentController.class);
    private final TournamentService tournamentService;
    private final TeamRepository teamRepository;

    @Autowired
    public TournamentController(final TournamentService tournamentService, TeamRepository teamRepository) {
        this.tournamentService = tournamentService;
        this.teamRepository = teamRepository;
    }

    @RequestMapping(value = "/admin/tournament", method = RequestMethod.GET)
    public String tournament(final Model model) {
        LOGGER.debug("loading tournament form");
        final TournamentDto tournament = tournamentService.getActiveTournamnet();
        model.addAttribute("tournament", tournament);
        appendGlobalData(model);
        return "admin/tournament";
    }

    @RequestMapping(value = "/admin/tournament", method = RequestMethod.POST)
    public String updateTournament(final Model model, @Valid @ModelAttribute(name = "tournament") final TournamentDto tournamentDto, final BindingResult bindingResult) {
        LOGGER.debug("updating tournament");
        if (bindingResult.hasErrors()) {
            appendGlobalData(model);
            return "admin/tournament";
        }
        tournamentService.updateTournament(tournamentDto);
        return "redirect:/admin/overview";
    }

    private void appendGlobalData(Model model) {
        final List<Team> allTeams = StreamSupport.stream(teamRepository.findAll().spliterator(), false)//
                .sorted(Comparator.comparing(Team::getName)).collect(Collectors.toList());
        model.addAttribute("teams", allTeams);
        model.addAttribute("scoreRanges", ScoreRange.values());
    }

}
