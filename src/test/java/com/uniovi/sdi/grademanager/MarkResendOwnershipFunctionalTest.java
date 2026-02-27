package com.uniovi.sdi.grademanager;

import com.uniovi.sdi.grademanager.entities.Mark;
import com.uniovi.sdi.grademanager.repositories.MarksRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MarkResendOwnershipFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MarksRepository marksRepository;

    @Test
    void shouldAllowStudentToResendOwnMarkButNotOthers() throws Exception {
        MockHttpSession studentSession = login("99999990A", "123456");

        List<Mark> ownMarks = marksRepository.findAllByUserDni("99999990A");
        List<Mark> otherMarks = marksRepository.findAllByUserDni("99999992C");
        assertThat(ownMarks).isNotEmpty();
        assertThat(otherMarks).isNotEmpty();

        Long ownMarkId = ownMarks.get(0).getId();
        Long otherMarkId = otherMarks.get(0).getId();

        mockMvc.perform(get("/mark/{id}/resend", ownMarkId).session(studentSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/mark/list"));

        assertThat(marksRepository.findById(ownMarkId).orElseThrow().getResend()).isTrue();

        mockMvc.perform(get("/mark/{id}/noresend", ownMarkId).session(studentSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/mark/list"));

        assertThat(marksRepository.findById(ownMarkId).orElseThrow().getResend()).isFalse();

        mockMvc.perform(get("/mark/{id}/resend", otherMarkId).session(studentSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/mark/list"));

        assertThat(marksRepository.findById(otherMarkId).orElseThrow().getResend()).isFalse();
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
