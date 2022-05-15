package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.constants.ConsoleColors;
import it.polimi.ingsw.constants.Messages;
import it.polimi.ingsw.model.game_objects.Color;
import it.polimi.ingsw.model.game_objects.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class containing only static methods to handle collections of students efficiently
 */
public class Students {
    private Students() {
    }

    /**
     * Counts the number of students of the given {@code Color} inside the provided {@code ArrayList} of students
     *
     * @param students an {@code ArrayList} of students to be counted
     * @param color    a {@code Color} to count the students of
     * @return the number of students of the given {@code Color}
     */
    public static int countColor(ArrayList<Student> students, Color color) {
        int res = 0;
        for (Student student : students) {
            if (student.getColor() == color) {
                res++;
            }
        }
        return res;
    }

    /**
     * Returns a shuffled {@code ArrayList} of students containing num students of each color.
     * If a negative number is provided the method returns an empty collection
     *
     * @param num the number of students of each color to be contained in the result
     * @return a shuffled {@code ArrayList} of students containing num students of each color
     */
    public static ArrayList<Student> getSomeStudents(int num) {
        ArrayList<Student> res = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            for (Color color : Color.values()) {
                res.add(new Student(color));
            }
        }

        Collections.shuffle(res);

        return res;
    }

    /**
     * Finds the first student of the selected {@code Color} in the given {@code ArrayList}
     *
     * @param students an {@code ArrayList} of students to search in
     * @param color    a {@code Color} to find the first {@code Student} of
     * @return the first student of the selected {@code Color} in the given {@code ArrayList} or null if not present
     */
    public static Student findFirstStudentOfColor(ArrayList<Student> students, Color color) {
        for (Student student : students) {
            if (student.getColor() == color)
                return student;
        }
        return null;
    }

    /**
     * Returns a String from a list of students
     *
     * @param students a {@code List} of students
     * @return a string
     */
    public static String getStringFromList(List<Student> students) {
        if (students == null || students.isEmpty()) return "";

        StringBuilder stringBuilder = new StringBuilder();
        for (Student student : students) {
            switch (student.getColor()) {
                case GREEN -> stringBuilder.append(ConsoleColors.GREEN + Messages.STUDENT_CHAR + ConsoleColors.RESET);
                case PINK -> stringBuilder.append(ConsoleColors.PURPLE + Messages.STUDENT_CHAR + ConsoleColors.RESET);
                case RED -> stringBuilder.append(ConsoleColors.RED + Messages.STUDENT_CHAR + ConsoleColors.RESET);
                case BLUE -> stringBuilder.append(ConsoleColors.BLUE + Messages.STUDENT_CHAR + ConsoleColors.RESET);
                case YELLOW -> stringBuilder.append(ConsoleColors.YELLOW + Messages.STUDENT_CHAR + ConsoleColors.RESET);
            }
        }

        return stringBuilder.toString();
    }
}
