package com.uniovi.sdi.grademanager;

import com.uniovi.sdi.grademanager.entities.User;
import com.uniovi.sdi.grademanager.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UserRolesFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void shouldCreateUserWithSelectedRoleAndAllowLogin() throws Exception {
        MockHttpSession adminSession = login("99999988F", "123456");

        mockMvc.perform(get("/user/add").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("rolesList"));

        String uniqueSuffix = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        String dni = "77" + uniqueSuffix + "Z";
        String rawPassword = "654321";
        String selectedRole = "ROLE_PROFESSOR";

        mockMvc.perform(post("/user/add")
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dni", dni)
                        .param("name", "Nuevo")
                        .param("lastName", "Profesor")
                        .param("password", rawPassword)
                        .param("role", selectedRole))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        User createdUser = usersRepository.findByDni(dni);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getRole()).isEqualTo(selectedRole);
        assertThat(createdUser.getPassword()).isNotEqualTo(rawPassword);

        login(dni, rawPassword);
    }

    private MockHttpSession login(String dni, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", dni)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andReturn();

        return (MockHttpSession) result.getRequest().getSession(false);
    }
}
