package com.school.mgnt.sys.student.management.entity;

import java.util.List;

import com.school.mgnt.sys.school.management.School;
import com.school.mgnt.sys.teacher.management.Teacher;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "classes" )
public class Classes {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classId;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    private String className;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "classes", cascade = CascadeType.ALL)
    private List<Grade> grades;


}