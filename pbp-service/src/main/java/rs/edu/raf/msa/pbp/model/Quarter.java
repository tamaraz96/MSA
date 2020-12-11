package rs.edu.raf.msa.pbp.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Quarter {
	
	int q;
	
	List<Play> plays = new ArrayList<>(100);

}