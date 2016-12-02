package com.im.repository;

import com.im.model.Game;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class GameRepository {

    @PersistenceContext
    EntityManager em;


    public List<Game> getAllGames() {
        return em.createQuery("select g from game g order by g.id", Game.class).getResultList();
    }
}
