package com.uniovi.sdi.grademanager.entities;

public class Department {

    private Long id;
    private String code;
    private String name;
    private String faculty;
    private int professors;

    // Constructor vacío (necesario)
    public Department() {
    }

    // Constructor completo
    public Department(Long id, String code, String name, String faculty, int professors) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.faculty = faculty;
        this.professors = professors;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public int getProfessors() {
        return professors;
    }

    public void setProfessors(int professors) {
        this.professors = professors;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", faculty='" + faculty + '\'' +
                ", professors=" + professors +
                '}';
    }
}
