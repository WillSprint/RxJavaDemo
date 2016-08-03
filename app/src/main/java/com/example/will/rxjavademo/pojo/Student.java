package com.example.will.rxjavademo.pojo;

import java.util.List;

/**
 * Created by will on 16/8/2.
 */

public class Student {
    private String name;
    private Course[] courses;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course[] getCourses() {
        return this.courses;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }
}
