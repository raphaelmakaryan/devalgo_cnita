package fr.raphaelmakaryan.bibliotheque.view;

public interface MenuObservable {

    void addMenuObserver(MenuObserver observer);

    void removeMenuObserver(MenuObserver observer);

    void notifyLeaveGame(String message);
}
