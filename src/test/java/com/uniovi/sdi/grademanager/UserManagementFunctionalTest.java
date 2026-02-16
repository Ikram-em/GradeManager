package com.uniovi.sdi.grademanager;

import com.uniovi.sdi.grademanager.entities.User;
import com.uniovi.sdi.grademanager.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UserManagementFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void shouldListUsers() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("usersList"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Usuarios")));
    }

    @Test
    void shouldAddAndDeleteUser() throws Exception {
        long before = usersRepository.count();
        String uniqueSuffix = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        String dni = "88" + uniqueSuffix + "A";

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dni", dni)
                        .param("name", "Usuario")
                        .param("lastName", "Prueba"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        assertThat(usersRepository.count()).isEqualTo(before + 1);

        User createdUser = StreamSupport.stream(usersRepository.findAll().spliterator(), false)
                .filter(user -> dni.equals(user.getDni()))
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/user/delete/{id}", createdUser.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        assertThat(usersRepository.count()).isEqualTo(before);
        assertThat(StreamSupport.stream(usersRepository.findAll().spliterator(), false)
                .noneMatch(user -> dni.equals(user.getDni()))).isTrue();
    }
}
