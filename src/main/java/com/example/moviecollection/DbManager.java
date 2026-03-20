package com.example.moviecollection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

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
