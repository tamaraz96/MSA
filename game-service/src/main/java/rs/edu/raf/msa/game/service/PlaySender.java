package rs.edu.raf.msa.game.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rs.edu.raf.msa.game.entity.Game;
import rs.edu.raf.msa.game.entity.Play;
import rs.edu.raf.msa.game.message.GameScore;
import rs.edu.raf.msa.game.message.PlayerScore;
import rs.edu.raf.msa.game.repository.GameRepository;

@RequiredArgsConstructor
@Component
public class PlaySender {

    final RabbitTemplate rabbitTemplate;
    final GameRepository gameRepository;

    public void sendGameScore(Play p) {
        Game game = gameRepository.findById(p.getGameId())
                .orElseThrow(() -> new IllegalStateException("Game of play not found, gameId=" + p.getGameId()));

        GameScore gs = GameScore.builder()
                .gameId(p.getGameId())
                .quarter(p.getQuarter())
                .gameTime(p.getGameTime())
                .homeScore(p.getHomeScore())
                .visitorScore(p.getVisitorScore())
                .homeTeam(game.getHomeTeam())
                .visitorTeam(game.getVisitorTeam())
                .build();

        rabbitTemplate.convertAndSend("game-score", null, gs);
    }

    public void sendPlayerScore(Play p) {
        PlayerScore ps = PlayerScore.builder()
                .gameId(p.getGameId())
                .quarter(p.getQuarter())
                .gameTime(p.getGameTime())
                .player(p.getPlayers().get(0) + "")
                .points(2)
                .made(false)
                .build();
        rabbitTemplate.convertAndSend("player-score", null, ps);
    }

}
