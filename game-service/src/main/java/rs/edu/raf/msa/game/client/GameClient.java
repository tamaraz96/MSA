package rs.edu.raf.msa.game.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import rs.edu.raf.msa.game.client.dto.PlayDto;
import rs.edu.raf.msa.game.client.dto.PlayerDto;

@FeignClient(value = "pbp-service", url = "http://${pbp-service.url:localhost}:8080/")
public interface GameClient {

	@GetMapping("/games")
	public List<String> games();

	@GetMapping("/players/{gameId}")
	public List<PlayerDto> players(@PathVariable String gameId);

	@GetMapping("/plays/{gameId}/{fromMin}/{toMin}")
	public List<PlayDto> plays(@PathVariable("gameId") String gameId, @PathVariable("fromMin") String fromMin,
			@PathVariable("toMin") String toMin);

}
