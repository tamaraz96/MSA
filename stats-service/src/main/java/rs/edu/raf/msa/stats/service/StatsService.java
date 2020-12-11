package rs.edu.raf.msa.stats.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rs.edu.raf.msa.stats.config.MessagingConfig;
import rs.edu.raf.msa.stats.message.GameScore;
import rs.edu.raf.msa.stats.message.PlayerScore;

@Service
@Slf4j
public class StatsService {

	/**
	 * <pre>
	 * { "gameId": 4, "homeTeam": "LAL", "visitorTeam": "DEN", "quarter": 4, "gameTime":"38:13", "homeScore": "87", "visitorScore": "98" }
	 * </pre>
	 */
	@RabbitListener(queues = MessagingConfig.GAME_SCORE_RUN)
	public void gameScoreRun(Message<GameScore> message) {
		log.info("gameScoreRun(): {}", message);
	}

	@RabbitListener(queues = MessagingConfig.GAME_SCORE_DROUGHT)
	public void gameScoreDrought(Message<GameScore> message) {
		log.info("gameScoreDrought(): {}", message);
	}

	@RabbitListener(queues = MessagingConfig.PLAYER_SCORE)
	public void playerScoreChanged(Message<PlayerScore> message) {
		log.info("playerScoreChanged(): {}", message);
	}

}
