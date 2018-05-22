package com.zberg.esc.tippspiel.admin;

import com.zberg.esc.tippspiel.tournament.PlayerNotFoundException;
import com.zberg.esc.tippspiel.tournament.TournamentService;
import com.zberg.esc.tippspiel.tournament.db.TeamRepository;
import com.zberg.esc.tippspiel.tournament.dto.PlayerDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerControllerTest {
    @Mock
    private TournamentService tournamentService;
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private PlayerController testee;

    @Test
    public void playerForm_idGivenAndExists_userLoaded() {
        // arrange
        final Model model = mock(Model.class);
        final PlayerDto playerDto = new PlayerDto();
        when(tournamentService.loadPlayer(12L)).thenReturn(playerDto);
        // act
        final String view = testee.playerForm(model, "12");
        // assert
        verify(tournamentService, never()).createNewPlayer();
        assertEquals("/admin/player", view);
    }

    @Test
    public void playerForm_idGivenButDoesNotExist_newPlayerGenerated() {
        // arrange
        final Model model = mock(Model.class);
        when(tournamentService.loadPlayer(12L)).thenThrow(PlayerNotFoundException.class);
        // act
        final String view = testee.playerForm(model, "12");
        // assert
        verify(tournamentService).createNewPlayer();
        assertEquals("/admin/player", view);
    }

    @Test
    public void playerForm_idNotGiven_newPlayerGenerated() {
        // arrange
        final Model model = mock(Model.class);
        // act
        final String view = testee.playerForm(model, null);
        // assert
        verify(tournamentService).createNewPlayer();
        assertEquals("/admin/player", view);
    }

    @Test
    public void savePlayer_bindingErrors_returnToView(){
        // arrange
        final PlayerDto playerDto = new PlayerDto();
        final Model model = mock(Model.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        // act
        final String view = testee.savePlayer(playerDto, bindingResult, model);
        // assert
        verify(tournamentService, never()).saveOrUpdatePlayer(playerDto);
        assertEquals("/admin/player", view);
    }

    @Test
    public void savePlayer_noErrors_redirectToOverview(){
        // arrange
        final PlayerDto playerDto = new PlayerDto();
        final Model model = mock(Model.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        // act
        final String view = testee.savePlayer(playerDto, bindingResult, model);
        // assert
        verify(tournamentService).saveOrUpdatePlayer(playerDto);
        assertEquals("redirect:/admin/overview", view);
    }
}