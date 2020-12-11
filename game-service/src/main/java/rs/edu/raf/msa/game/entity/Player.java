package rs.edu.raf.msa.game.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.msa.game.client.dto.PlayerDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

	@Id
	Long id;

	Long externalId;
	
	String shortName;
	String firstName;
	String lastName;

	
	public Player(PlayerDto playerDto) {
		this.externalId = playerDto.getId();
		this.firstName = playerDto.getF();
		this.lastName = playerDto.getL();
		this.shortName = playerDto.getC();
	}

}
