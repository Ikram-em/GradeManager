package com.uniovi.sdi.grademanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MarkSearchByRoleFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void studentShouldSeeOnlyOwnMarksWhenSearching() throws Exception {
        MockHttpSession studentSession = login("99999990A", "123456");

        mockMvc.perform(get("/mark/list")
                        .session(studentSession)
                        .param("searchText", "Nota"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nota A1")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Nota B1"))))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Nota C1"))));
    }

    @Test
    void professorShouldSeeGlobalMarksWhenSearching() throws Exception {
        MockHttpSession professorSession = login("99999977E", "123456");

        mockMvc.perform(get("/mark/list")
                        .session(professorSession)
                        .param("searchText", "Nota"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nota A1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nota B1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nota C1")));
    }

    private MockHttpSession login(String dni, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", dni)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/home"))
                .andReturn();

        return (MockHttpSession) result.getRequest().getSession(false);
    }
}
