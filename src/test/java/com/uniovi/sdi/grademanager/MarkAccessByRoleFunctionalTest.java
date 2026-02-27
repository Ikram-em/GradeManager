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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class MarkAccessByRoleFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRedirectAnonymousUserToLoginWhenAccessingMarkAdd() throws Exception {
        mockMvc.perform(get("/mark/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/login")));
    }

    @Test
    void shouldDenyStudentAccessToMarkAdd() throws Exception {
        MockHttpSession studentSession = login("99999990A", "123456"); // ROLE_STUDENT

        mockMvc.perform(get("/mark/add").session(studentSession))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowProfessorAccessToMarkAdd() throws Exception {
        MockHttpSession professorSession = login("99999993D", "123456"); // ROLE_PROFESSOR

        mockMvc.perform(get("/mark/add").session(professorSession))
                .andExpect(status().isOk())
                .andExpect(view().name("mark/add"));
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
