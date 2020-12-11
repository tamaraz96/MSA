package rs.edu.raf.msa.pbp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import rs.edu.raf.msa.pbp.model.Play;
import rs.edu.raf.msa.pbp.model.PlayByPlay;

@SpringBootTest
@Slf4j
class GameControllerTest {

	@Autowired
	GameController gameController;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void gameLoaded() throws IOException {
		PlayByPlay pbp = gameController.game("20200924LALDEN");
		assertNotNull(pbp);

		String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pbp);
		log.debug(formattedJson);
	}

	@Test
	void gamesLoaded() throws IOException {
		List<String> games = gameController.games();
		assertNotNull(games);

		log.debug("{}", games);
		assertThat(games).contains("20200924LALDEN", "20200930MIALAL", "20201002MIALAL");
	}

	@Test
	void gamePlays() throws IOException {
		PlayByPlay pbp = gameController.game("20201002MIALAL");
		assertNotNull(pbp);

		List<Play> ps = pbp.play("23:45", "24:15");
		List<Integer> ids = ps.stream().map(Play::getId).collect(Collectors.toList());
		log.debug("{}", ids);
		assertThat(ids).containsExactly(326, 327, 328, 329, 330, 337, 338);
		assertThat(ps).extracting(p -> p.getGt()).containsExactly("23:49", "23:50", "24:00", "24:00", "24:00", "24:00", "24:12");
	}

}
