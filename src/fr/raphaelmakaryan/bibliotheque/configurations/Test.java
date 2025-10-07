package fr.raphaelmakaryan.bibliotheque.configurations;

public enum Test {
    JvJ,JvB, BvJ;

    public boolean isFirstPlayerHuman(){
        return switch(this){
            case JvB -> true;
            case JvJ -> true;
            case BvJ -> false;
        };

    }

}
