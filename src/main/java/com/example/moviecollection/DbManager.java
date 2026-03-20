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

    public static void addCopia(Copia copia) {
        EntityManager em = getEmf().createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Copia> query = em.createQuery("SELECT c FROM Copia c WHERE c.pelicula = :pelicula AND c.usuario = :usuario AND c.formato = :formato AND c.soporte = :soporte", Copia.class);
            query.setParameter("pelicula", copia.getPelicula());
            query.setParameter("usuario", copia.getUsuario());
            query.setParameter("formato", copia.getFormato());
            query.setParameter("soporte", copia.getSoporte());
            List<Copia> existingCopias = query.getResultList();
            if (!existingCopias.isEmpty()) {
                Copia existingCopia = existingCopias.get(0);
                existingCopia.aumentarCantidad();
                em.merge(existingCopia);
            } else {
                em.persist(copia);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
