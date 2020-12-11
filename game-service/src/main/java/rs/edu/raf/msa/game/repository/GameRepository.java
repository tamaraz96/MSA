package rs.edu.raf.msa.game.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import rs.edu.raf.msa.game.entity.Game;

public interface GameRepository extends PagingAndSortingRepository<Game, Long> {

	public Game findByCode(String code);
	
}
