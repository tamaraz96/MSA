package rs.edu.raf.msa.pbp.controller;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import rs.edu.raf.msa.pbp.model.Play;
import rs.edu.raf.msa.pbp.model.PlayByPlay;
import rs.edu.raf.msa.pbp.model.Player;

@RestController
@Slf4j
public class GameController {

	@Autowired
	ObjectMapper objectMapper;

	@org.springframework.beans.factory.annotation.Value("${logstash.host:NONE}")
	String logstashHost;
	
	@GetMapping("/test/log")
	public String testLog() {
	    log.info("From pbp-service GameController as INFO: logstash.host={}", logstashHost);
	    return logstashHost;
	}
	
	@GetMapping("/game/{gameId}")
	public PlayByPlay game(@PathVariable String gameId) {
		PlayByPlay result = loadPlayByPlay(gameId);
		log.info("Returing details for gameId={}", gameId);
		return result;
	}

	private PlayByPlay loadPlayByPlay(String gameId) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("games/" + gameId + ".json");
		try {
			return objectMapper.readValue(input, PlayByPlay.class);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error opening game id=" + gameId);
		}
	}

	@GetMapping("/games")
	public List<String> games() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);

		Resource[] games;
		try {
			games = resolver.getResources("classpath:games/*.json");
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error opening games!");
		}

		List<String> result = new ArrayList<>(games.length);
		for (Resource game : games) {
			result.add(StringUtils.replace(game.getFilename(), ".json", ""));
		}

		log.info("Returning all games: {}", result);
		return result;
	}

	@GetMapping("/plays/{gameId}/{fromMin}/{toMin}")
	public List<Play> plays(@PathVariable("gameId") String gameId, @PathVariable("fromMin") String fromMin,
			@PathVariable("toMin") String toMin) {
		PlayByPlay pbp = game(gameId);
		List<Play> plays = pbp.play(fromMin, toMin);
		log.info("For gameId={}, fromMin={}, toMin={} -> returning {} play(s)", gameId, fromMin, toMin, plays.size());
		return plays;
	}

	@GetMapping("/players/{gameId}")
	public List<Player> players(@PathVariable String gameId) {
		Map<String, Player> map = loadPlayByPlay(gameId).getPlayers();
		return map.entrySet().stream().map(e -> {
			e.getValue().setId(e.getKey());
			return e.getValue();
		}).collect(toList());
	}

}
