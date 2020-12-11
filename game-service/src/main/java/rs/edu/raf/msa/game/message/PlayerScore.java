package rs.edu.raf.msa.game.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerScore {

	Long gameId;
	
	String player;

	int quarter;
	String gameTime;
	
	int points;
	boolean made;
	
}
