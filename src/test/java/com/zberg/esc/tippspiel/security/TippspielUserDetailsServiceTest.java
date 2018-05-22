package com.zberg.esc.tippspiel.security;

import com.zberg.esc.tippspiel.security.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TippspielUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TippspielUserDetailsService testee;

    @Test
    public void loadUserByUsername_userExists_princinpalGenerated(){
        // arrange
        when(userRepository.findByUsername("test")).thenReturn(new User());
        // act
        final UserDetails result = testee.loadUserByUsername("test");
        // assert
        assertNotNull(result);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_noUser_userNotfoundException(){
        // arrange
        when(userRepository.findByUsername("test")).thenReturn(null);
        // act && assert
        testee.loadUserByUsername("test");
    }
}