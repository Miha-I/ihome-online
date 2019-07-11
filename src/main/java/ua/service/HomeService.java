package ua.service;

import ua.model.Home;

import java.util.List;

public interface HomeService {

    List<Home> getAll();

    Home add(Home home);

    Home get(int id);

    void delete(int id);

    Home update(int id, Home home);
}
