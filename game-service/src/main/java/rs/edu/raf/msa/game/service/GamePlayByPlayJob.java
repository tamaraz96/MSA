package rs.edu.raf.msa.game.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.edu.raf.msa.game.client.GameClient;
import rs.edu.raf.msa.game.client.dto.PlayDto;
import rs.edu.raf.msa.game.client.dto.PlayerDto;
import rs.edu.raf.msa.game.entity.Game;
import rs.edu.raf.msa.game.entity.Play;
import rs.edu.raf.msa.game.entity.PlayPlayer;
import rs.edu.raf.msa.game.entity.Player;
import rs.edu.raf.msa.game.repository.GameRepository;
import rs.edu.raf.msa.game.repository.PlayRepository;
import rs.edu.raf.msa.game.repository.PlayerRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class GamePlayByPlayJob {

	final GameClient gameClient;

	final GameRepository gameRepository;
	final PlayerRepository playerRepository;
	final PlayRepository playRepository;

	static final DateTimeFormatter MMSS = DateTimeFormatter.ofPattern("mm:ss");
	
	@Scheduled(fixedDelay = 10_000)
	@Transactional
	public void scanGames() {
		List<String> allGames = gameClient.games();
		log.info("Loaded games from pbp-service: {}", allGames);

		for (String gameCode : allGames) {
			Game game = gameRepository.findByCode(gameCode);

			if (game == null) {
				game = new Game(gameCode);
				game = gameRepository.save(game);
			} else if (game.isFinished()) {
				continue;
			}

			if (game.getEndTime() == null) {
				log.info("Starting parsing game: {}", game);

				List<PlayerDto> playerDtos = gameClient.players(gameCode);
				saveAllPlayers(game, playerDtos);

				game.setEndTime(LocalTime.parse("00:00:00"));
			}

			game.setStartTime(game.getEndTime());
			game.setEndTime(game.getStartTime().plusMinutes(10));

			log.info("Loading plays for game: {}", game);
			List<PlayDto> playDtos = gameClient.plays(game.getCode(), 
					game.getStartTime().format(MMSS),
					game.getEndTime().format(MMSS));
			saveAllPlays(game, playDtos);
			
			log.debug("Saved {} play(s) for game id={}", playDtos.size(), game.getId());

			if (game.getEndTime().getMinute() >= 48) {
				game.setFinished(true);
			}

			gameRepository.save(game);
		}

	}

	private void saveAllPlayers(Game game, List<PlayerDto> playerDtos) {
		for (PlayerDto playerDto : playerDtos) {
			if (playerRepository.findByExternalId(playerDto.getId()) == null) {
				Player player = new Player(playerDto);
				log.info("For game: {} - saving player: {}", game.getId(), player);
				playerRepository.save(player);
			}
		}
	}

	private void saveAllPlays(Game game, List<PlayDto> playDtos) {
		for (PlayDto playDto : playDtos) {
			if (playRepository.findByGameIdAndExternalId(game.getId(), (long)playDto.getId()) != null) {
				continue;
			}

			Play play = new Play(playDto);
			play.setGameId(game.getId());

			List<String> ppn = playDto.getPlayers();
			if (ppn != null && !ppn.isEmpty()) {
				List<PlayPlayer> pps = ppn.stream().map(pc -> Long.parseLong(pc))
						.map(pl -> playerRepository.findByExternalId(pl)).filter(p -> p != null)
						.map(p -> p.getId())
						.distinct()
						.map(id -> new PlayPlayer(id)).collect(toList());

				play.setPlayers(pps);
			}
			
			playRepository.save(play);
		}
	}

}
