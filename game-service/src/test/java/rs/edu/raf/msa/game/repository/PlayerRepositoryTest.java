package rs.edu.raf.msa.game.repository;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort.TypedSort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import rs.edu.raf.msa.game.entity.Game;
import rs.edu.raf.msa.game.entity.Play;
import rs.edu.raf.msa.game.entity.Player;

@SpringBootTest
@RabbitListenerTest
@ActiveProfiles("test")
@Slf4j
@Transactional
public class PlayerRepositoryTest {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayRepository playRepository;

    @Autowired
    RabbitListenerTestHarness harness;

    @Test
    void testAll() throws InterruptedException {
        Game g = Game.builder().code("20201010DENLAL").homeTeam("DEN").visitorTeam("LAL").build();
        g = gameRepository.save(g);

        g.setStartTime(LocalTime.parse("00:01"));
        g.setEndTime(LocalTime.parse("05:17"));
        g.setFinished(true);
        g = gameRepository.save(g);

        Player p1 = Player.builder().externalId(203999L).shortName("nikola_jokic").firstName("Nikola").lastName("Jokic")
                .build();
        Player p2 = Player.builder().externalId(2544L).shortName("lebron_james").firstName("Lebron").lastName("James")
                .build();
        playerRepository.saveAll(asList(p1, p2));

        Play pp1 = Play.builder().gameId(g.getId()).externalId(1L).quarter(1).quarterTime("11:59").gameTime("00:01")
                .homeScore(0).visitorScore(0).description("Jump ball").playerIds(new long[] { p1.getId(), p2.getId() })
                .build();
        playRepository.save(pp1);

        Play pp2 = Play.builder().gameId(g.getId()).externalId(2L).quarter(1).quarterTime("11:52").gameTime("00:08")
                .homeScore(2).visitorScore(0).description("Layup by Jokic").playerIds(new long[] { p1.getId() })
                .build();
        playRepository.save(pp2);

        Play pp1_ = playRepository.findById(pp1.getId()).orElse(null);
        assertThat(pp1_.getPlayers()).extracting(pp -> pp.getPlayerId()).containsExactly(p1.getId(), p2.getId());

        List<Play> pps = playRepository.findByGameId(g.getId(), TypedSort.sort(Play.class).by(Play::getGameTime));
        assertThat(pps).extracting(pp -> pp.getExternalId()).containsExactly(1L, 2L);
        assertThat(pps).flatExtracting(pp -> pp.getPlayers()).extracting(pp -> pp.getPlayerId())
                .containsExactly(p1.getId(), p2.getId(), p1.getId());
        
        //InvocationData id = harness.getNextInvocationDataFor("game-score", 5, TimeUnit.SECONDS);
        // log.info("{}", id.getResult());
    }

    @Test
    void savePlayer() {
        Player p1 = Player.builder().externalId(203999L).shortName("nikola_jokic").firstName("Nikola").lastName("Jokic")
                .build();
        Player p2 = Player.builder().externalId(2544L).shortName("lebron_james").firstName("Lebron").lastName("James")
                .build();

        playerRepository.saveAll(asList(p1, p2));

        Iterable<Player> found = playerRepository.findAll();
        log.debug("{}", found);
        assertThat(found).contains(p1, p2);
    }

    @Test
    void findByExternalId() {
        Player p1 = Player.builder().externalId(203999L).shortName("nikola_jokic").firstName("Nikola").lastName("Jokic")
                .build();
        Player p2 = Player.builder().externalId(2544L).shortName("lebron_james").firstName("Lebron").lastName("James")
                .build();

        playerRepository.saveAll(asList(p1, p2));

        assertThat(playerRepository.findByExternalId(2544)).extracting(p -> p.getShortName()).isEqualTo("lebron_james");
    }
}
