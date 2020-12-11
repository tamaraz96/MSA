package rs.edu.raf.msa.game.entity;

import java.time.LocalTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

	@Id
	Long id;

	String code;
	
	String homeTeam;
	String visitorTeam;
	
	LocalTime startTime;
	LocalTime endTime;
	
	boolean finished;

	public Game(String code) {
		this.code = code;
		this.homeTeam = code.substring(8, 11);
		this.visitorTeam = code.substring(11, 14);
	}

}
