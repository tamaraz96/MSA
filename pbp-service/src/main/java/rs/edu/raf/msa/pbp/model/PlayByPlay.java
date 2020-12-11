package rs.edu.raf.msa.pbp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class PlayByPlay {

	Map<String, Player> players = new LinkedHashMap<>();
	
	List<Quarter> quarters = new ArrayList<>(4);
	
	//
	
	public List<Play> play(String fromMin, String toMin) {
		int fromSec = secondsOfGame(fromMin);
		int toSec = secondsOfGame(toMin);
		
		List<Play> plays = new ArrayList<>();
		
		for (Quarter quarter : quarters) {
			int q = quarter.getQ();
			
			for (Play p : quarter.getPlays()) {
				int ps = secondsOfQuarter(q, p.getC());
				if (ps >= fromSec && ps <= toSec) {
					p.setQ(q);
					p.setGt(gameTime(ps));
					plays.add(p);
				}
			}
		}
		
		return plays;
	}
	
	private int secondsOfGame(String mmss) {
		int[] t = time(mmss);
		return t[1] + t[0] * 60;
	}
	
	private int secondsOfQuarter(int q, String mmss) {
		int[] t = time(mmss);
		return ((q - 1) * 12) * 60 + ((11 - t[0]) * 60) + (60 - t[1]);
	}
	
	private static int[] time(String mmss) {
		String[] s = mmss.split(":");
		return new int[] { (int)Double.parseDouble(s[0]), (int)Double.parseDouble(s[1]) }; 
	}
	
	private static String gameTime(int sec) {
		return String.format("%02d:%02d", (sec / 60), (sec % 60)); 
	}
	
}
