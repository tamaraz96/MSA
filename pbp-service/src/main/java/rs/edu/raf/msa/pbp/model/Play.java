package rs.edu.raf.msa.pbp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Play {

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
