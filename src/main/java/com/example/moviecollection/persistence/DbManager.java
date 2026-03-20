package com.example.moviecollection.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbManager {
    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEmf() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("objectdb:db/moviecollection.odb");
        }
        return emf;
    }


    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
