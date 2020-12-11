package rs.edu.raf.msa.game.entity;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.msa.game.client.dto.PlayDto;

@Data
@NoArgsConstructor
public class Play {

    @Id
    Long id;

    Long externalId;

    Long gameId;

    int quarter;

    String quarterTime;
    String gameTime;

    String description;

    int homeScore;
    int visitorScore;

    // @MappedCollection(idColumn = "PLAY_ID", keyColumn = "POSITION")
    @MappedCollection(idColumn = "play_id", keyColumn = "position")
    List<PlayPlayer> players;

    public Play addPlayers(long... ids) {
	if (players == null) {
	    players = new ArrayList<>();
	}
	for (long playerId : ids) {
	    players.add(new PlayPlayer(playerId));
	}
	return this;
    }

    @Builder
    public Play(Long id, Long externalId, Long gameId, int quarter, String quarterTime, String gameTime,
	    String description, int homeScore, int visitorScore, long... playerIds) {
	super();
	this.id = id;
	this.externalId = externalId;
	this.gameId = gameId;
	this.quarter = quarter;
	this.quarterTime = quarterTime.substring(0, min(5, quarterTime.length()));
	this.gameTime = gameTime;
	this.description = description;
	this.homeScore = homeScore;
	this.visitorScore = visitorScore;
	addPlayers(playerIds);
    }

    public Play(PlayDto playDto) {
	this(null, (long) playDto.getId(), null, playDto.getQ(), playDto.getC(), playDto.getGt(), playDto.getD(),
		playDto.getHs(), playDto.getVs());
    }
}
