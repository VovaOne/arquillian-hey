package com.im.repository;

import com.im.TestApp;
import com.im.model.Game;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;


@RunWith(Arquillian.class)
public class GameRepositoryTest {

    @Deployment
    public static WebArchive createDeployment() throws URISyntaxException {

        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(TestApp.class, Game.class, GameRepository.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("db-migration.xml")
                .addAsLibraries(files)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    GameRepository gameRepository;

    @PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Before
    public void prepare_data() throws Exception {
        utx.begin();
        em.createQuery("delete from game").executeUpdate();
        em.persist(new Game("Tennis"));
        utx.commit();
    }

    @Test
    public void should_get_one_record() {
        List<Game> games = gameRepository.getAllGames();
        assertEquals(1, games.size());
    }


}
