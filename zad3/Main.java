package org.example;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

// Модель пользователя
class User {
    private String username;
    private String password;
    private String role;
    private String fullName;
    private String birthDate;
    private String snils;

    public User(String username, String password, String role, String fullName, String birthDate, String snils) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.snils = snils;
    }

    // Геттеры и сеттеры
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getBirthDate() { return birthDate; }
    public String getSnils() { return snils; }
}

// Модель кандидата
class Candidate {
    private String id;
    private String name;
    private String party;
    private String bio;

    public Candidate(String id, String name, String party, String bio) {
        this.id = id;
        this.name = name;
        this.party = party;
        this.bio = bio;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getName() { return name; }
    public String getParty() { return party; }
    public String getBio() { return bio; }
}

// Модель голосования
class Voting {
    private String id;
    private String title;
    private LocalDate endDate;
    private List<String> candidateIds;
    private Map<String, Integer> votes;

    public Voting(String id, String title, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.endDate = endDate;
        this.candidateIds = new ArrayList<>();
        this.votes = new HashMap<>();
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getTitle() { return title; }
    public LocalDate getEndDate() { return endDate; }
    public List<String> getCandidateIds() { return candidateIds; }
    public Map<String, Integer> getVotes() { return votes; }

    public void addCandidate(String candidateId) {
        candidateIds.add(candidateId);
        votes.put(candidateId, 0);
    }

    public void voteForCandidate(String candidateId) {
        votes.put(candidateId, votes.getOrDefault(candidateId, 0) + 1);
    }
}

// Модель системы
class VotingModel {
    private List<User> users;
    private List<Candidate> candidates;
    private List<Voting> votings;
    private User currentUser;

    public VotingModel() {
        this.users = new ArrayList<>();
        this.candidates = new ArrayList<>();
        this.votings = new ArrayList<>();
        // Добавляем тестового администратора
        users.add(new User("admin", "admin123", "admin", "Admin", "01.01.1970", "000-000-000 00"));
    }

    // Методы аутентификации
    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Методы для администратора
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
    }

    public void createCEC(String username, String password) {
        users.add(new User(username, password, "cec", "CEC User", "", ""));
    }

    // Методы для ЦИК
    public void createVoting(String title, LocalDate endDate) {
        String id = "vote_" + System.currentTimeMillis();
        votings.add(new Voting(id, title, endDate));
    }


}


class VotingView {
    private Scanner scanner;

    public VotingView() {
        this.scanner = new Scanner(System.in);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    public void showAdminMenu() {
        System.out.println("\nМеню администратора:");
        System.out.println("1. Просмотр списка пользователей");
        System.out.println("2. Удаление пользователя");
        System.out.println("3. Создание ЦИК");
        System.out.println("4. Просмотр кандидатов");
        System.out.println("5. Удаление кандидата");
        System.out.println("0. Выход");
    }


}

// Контроллер
class VotingController {
    private VotingModel model;
    private VotingView view;

    public VotingController(VotingModel model, VotingView view) {
        this.model = model;
        this.view = view;
    }

    public void start() {
        while (true) {
            view.showMessage("\nСистема электронного голосования");
            String username = view.getInput("Введите логин");
            String password = view.getInput("Введите пароль");

            if (model.login(username, password)) {
                User currentUser = model.getCurrentUser();
                view.showMessage("Добро пожаловать, " + currentUser.getFullName() + "!");

                switch (currentUser.getRole()) {
                    case "admin":
                        adminMenu();
                        break;
                    case "cec":
                        cecMenu();
                        break;
                    // Другие роли...
                    default:
                        userMenu();
                }
            } else {
                view.showMessage("Неверный логин или пароль!");
            }
        }
    }

    private void adminMenu() {
        while (true) {
            view.showAdminMenu();
            String choice = view.getInput("Выберите действие");

            switch (choice) {
                case "1":
                    // Просмотр пользователей
                    break;
                case "2":
                    // Удаление пользователя
                    break;
                case "3":
                    // Создание ЦИК
                    break;
                case "0":
                    model.logout();
                    return;
                default:
                    view.showMessage("Неверный выбор!");
            }
        }
    }


}


public class Main {
    public static void main(String[] args) {
        VotingModel model = new VotingModel();
        VotingView view = new VotingView();
        VotingController controller = new VotingController(model, view);
        controller.start();
    }
}