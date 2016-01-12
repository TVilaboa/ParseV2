package models;

import models.javacandidatestruct.CandidateClass;

/**
 * @author Nicolas Burroni
 * @since 8/12/2014
 */
public class CandidateModel {

	private CandidateClass ccd;
	private int ranking;
	private String name, comment;
	private boolean deleted;

	public CandidateModel(CandidateClass ccd){
		this.ccd = ccd;
		this.ranking = 0;
		this.name = ccd.getName();
		this.comment = "";
		this.deleted = false;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
