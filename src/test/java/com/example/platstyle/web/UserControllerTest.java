package com.example.platstyle.web;

import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.UserRepository;
import lombok.SneakyThrows;
import org.assertj.core.internal.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.View;

import java.awt.desktop.OpenFilesEvent;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class UserControllerTest {

//    User user;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    View mockView;
//
//    @InjectMocks
//    UserController userController;
//
//    @SneakyThrows
//    @BeforeEach
//    void setUp() {
//        user = new User();
//        user.setUid(Long.valueOf(1));
//        user.setFirstName("Tom");
//        user.setLastName("Thecat");
//        user.setEmail("user@email.com");
//        user.setPassword("1234");
//        String dateStr = "2022-08-03";
//        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
//        user.setRegisterDate(date);
//        user.setGender("M");
//        user.setRoles("ROLE_USER");
//        user.setPhone("6140001111");
//        MockitoAnnotations.openMocks(this);
//        mockMvc = standaloneSetup(userController).setSingleView(mockView).build();
//    }



//    @Test
//    public void findByEmail() throws Exception {
//        String email = "user@email.com";
//        Principal mockPrincipal = Mockito.mock(Principal.class);
//        Mockito.when(mockPrincipal.getName()).thenReturn(email);
//        Optional<User> userOptional = Optional.of(user);
//        when(userRepository.findByEmail(email)).thenReturn(userOptional);
//        mockMvc.perform(get("/user/account").principal(mockPrincipal)
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("user",user))
//                .andExpect(view().name("user"))
//                .andExpect(model().attribute("user", hasSize(1)));

//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/user/account")
//                .principal(mockPrincipal)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        int status = response.getStatus();
//        //Assert.assertEquals("response status is wrong", 200, status);

//        verify(userRepository, times(1)).findByEmail(anyString());
//        verifyNoMoreInteractions(userRepository);
//    }

}