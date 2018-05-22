package com.zberg.esc.tippspiel.admin;

import com.zberg.esc.tippspiel.tournament.PlayerNotFoundException;
import com.zberg.esc.tippspiel.tournament.TournamentService;
import com.zberg.esc.tippspiel.tournament.db.TeamRepository;
import com.zberg.esc.tippspiel.tournament.db.entities.ScoreRange;
import com.zberg.esc.tippspiel.tournament.db.entities.Team;
import com.zberg.esc.tippspiel.tournament.dto.PlayerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class PlayerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class);

    private final TournamentService tournamentService;
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerController(final TournamentService tournamentService, TeamRepository teamRepository) {
        this.tournamentService = tournamentService;
        this.teamRepository = teamRepository;
    }


    @RequestMapping(value = "/admin/player", method = RequestMethod.GET)
    public String playerForm(final Model model, @RequestParam(name = "pid", required = false) final String playerId) {
        LOGGER.debug("loading player form");
        final PlayerDto result = initPlayer(playerId);
        model.addAttribute("player", result);
        appendGlobalData(model);
        return "admin/player";
    }

    private PlayerDto initPlayer(@RequestParam(name = "pid", required = false) String playerId) {
        PlayerDto result;
        if (!StringUtils.isEmpty(playerId) && isLong(playerId)) {
            LOGGER.debug("ID given. Loading player from database");
            try {
                result = tournamentService.loadPlayer(Long.valueOf(playerId));
            } catch (PlayerNotFoundException e) {
                LOGGER.warn("Given player does not exist. Creating a new one...");
                result = tournamentService.createNewPlayer();
            }
        } else {
            LOGGER.debug("Creating new player....");
            result = tournamentService.createNewPlayer();
        }
        return result;
    }

    @RequestMapping(value = "/admin/player", method = RequestMethod.POST)
    public String savePlayer(@Valid @ModelAttribute(name = "player") PlayerDto player, final BindingResult bindingResult, final Model model) {
        LOGGER.debug("Going to save new player {}", player);
        if (bindingResult.hasErrors()) {
            model.addAttribute("player", player);
            appendGlobalData(model);
            return "admin/player";
        }
        tournamentService.saveOrUpdatePlayer(player);
        return "redirect:/admin/overview";
    }

    private void appendGlobalData(Model model) {
        final List<Team> allTeams = StreamSupport.stream(teamRepository.findAll().spliterator(), false)//
                .sorted(Comparator.comparing(Team::getName)).collect(Collectors.toList());
        model.addAttribute("teams", allTeams);
        model.addAttribute("scoreRanges", ScoreRange.values());
    }


    private boolean isLong(String playerId) {
        try {
            Long.valueOf(playerId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
