package com.example.platstyle.web;

import com.example.platstyle.entities.Stylist;
import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.StylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Incubating;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class StylistControllerTest {

    Stylist stylist;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    StylistRepository stylistRepository;

    @Mock
    View mockView;

    @InjectMocks
    UserController userController;

    @InjectMocks
    StylistController stylistController;

    @BeforeEach
    void setUp() {
        stylist = new Stylist();
        stylist.setSid(Long.valueOf(1));
        stylist.setEmail("stylist@email.com");
        stylist.setName("Tom TheCat");
        stylist.setVerify(true);
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(userController).setSingleView(mockView).build();
    }

    @Test
    public void findAllVerifiedStylist() throws Exception {
        List<Stylist> list = new ArrayList<Stylist>();
        list.add(stylist);
        list.add(stylist);
        when(stylistRepository.findAllVerifiedStylist()).thenReturn(list);
        mockMvc.perform(get("/user/shop"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("stylistList", list))
                // .andExpect(view().name(""))
                .andExpect(model().attribute("stylistList", hasSize(2)));
        verify(stylistRepository, times(1)).findAllVerifiedStylist();
        verifyNoMoreInteractions(stylistRepository);
    }

}