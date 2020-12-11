package rs.edu.raf.msa.game.client.dto;

import java.util.List;

import lombok.Data;

@Data
public class PlayDto {

	int id;
	int p;
	String c;
	String d;
	String t;
	String atin;
	
	List<String> players;

	int x;
	int y;
	
	int hs;
	int vs;

	String et;

	//
	
	int q;
	String gt;
	
}
