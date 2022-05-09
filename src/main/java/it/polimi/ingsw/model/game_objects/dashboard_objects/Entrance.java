package it.polimi.ingsw.model.game_objects.dashboard_objects;

import it.polimi.ingsw.exceptions.InvalidStudentException;
import it.polimi.ingsw.model.Place;
import it.polimi.ingsw.model.game_objects.Student;

import java.util.ArrayList;

public class Entrance implements Place {
    private final ArrayList<Student> students;

    public Entrance() {
        students = new ArrayList<>();
    }

    @Override
    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
    }

    @Override
    public void giveStudent(Place destination, Student student) throws InvalidStudentException {
        if (student == null || !students.contains(student)) {
            throw new InvalidStudentException("The entrance doesn't contain this student");
        }
        students.remove(student);
        destination.receiveStudent(student);
    }

    @Override
    public void receiveStudent(Student student) {
        students.add(student);
    }
}
