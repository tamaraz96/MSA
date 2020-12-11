package rs.edu.raf.msa.pbp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {

	String c;
	String f;
	String l;

	String id;

}
