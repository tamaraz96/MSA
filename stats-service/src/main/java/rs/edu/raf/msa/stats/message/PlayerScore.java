package rs.edu.raf.msa.stats.message;

import lombok.Data;

@Data
public class PlayerScore {

	Long gameId;
	
	String player;

	int quarter;
	String gameTime;
	
	int points;
	boolean made;
	
}
