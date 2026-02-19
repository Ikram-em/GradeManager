package com.uniovi.sdi.grademanager.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Mark {

    @Id
    @GeneratedValue
    private Long id;

    private String description;
    private Double score;
    private Boolean resend = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructor vacío (OBLIGATORIO para JPA)
    public Mark() {
    }

    // Constructor con parámetros
    public Mark(Long id, String description, Double score) {
        this.id = id;
        this.description = description;
        this.score = score;
    }

    public Mark(String description, Double score, User user) {
        this.description = description;
        this.score = score;
        this.user = user;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean getResend() {
        return resend;
    }

    public void setResend(Boolean resend) {
        this.resend = resend;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return Objects.equals(id, mark.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", score=" + score +
                '}';
    }
}
