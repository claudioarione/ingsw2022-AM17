package it.polimi.ingsw.model.game_objects.dashboard_objects;

import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidStudentException;
import it.polimi.ingsw.model.Place;
import it.polimi.ingsw.model.game_objects.Student;

import java.util.ArrayList;
import java.util.List;

public class Entrance implements Place {
    private final List<Student> students;

    public Entrance() {
        students = new ArrayList<>();
    }

    public Entrance(List<Student> students) {
        this.students = students;
    }

    @Override
    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
    }

    @Override
    public void setStudents(ArrayList<Student> students) {
        this.students.clear();
        this.students.addAll(students);
    }

    @Override
    public void giveStudent(Place destination, Student student) throws InvalidStudentException, InvalidActionException {
        if (student == null || !students.contains(student)) {
            throw new InvalidStudentException("entrance_doesnt_contain_student");
        }
        students.remove(student);
        destination.receiveStudent(student);
    }

    @Override
    public void receiveStudent(Student student) {
        students.add(student);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entrance entrance = (Entrance) o;

        return students.equals(entrance.students);
    }
}
