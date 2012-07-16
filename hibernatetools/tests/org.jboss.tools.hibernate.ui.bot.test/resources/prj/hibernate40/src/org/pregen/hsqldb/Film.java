package org.pregen.hsqldb;

// Generated Jul 16, 2012 4:51:41 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Film generated by hbm2java
 */
public class Film implements java.io.Serializable {

	private short filmId;
	private Language languageByOriginalLanguageId;
	private Language languageByLanguageId;
	private String title;
	private String description;
	private Date releaseYear;
	private byte rentalDuration;
	private BigDecimal rentalRate;
	private Short length;
	private BigDecimal replacementCost;
	private String rating;
	private String specialFeatures;
	private Date lastUpdate;
	private Set inventories = new HashSet(0);
	private Set filmActors = new HashSet(0);
	private Set filmCategories = new HashSet(0);

	public Film() {
	}

	public Film(short filmId, Language languageByLanguageId, String title,
			byte rentalDuration, BigDecimal rentalRate,
			BigDecimal replacementCost, Date lastUpdate) {
		this.filmId = filmId;
		this.languageByLanguageId = languageByLanguageId;
		this.title = title;
		this.rentalDuration = rentalDuration;
		this.rentalRate = rentalRate;
		this.replacementCost = replacementCost;
		this.lastUpdate = lastUpdate;
	}

	public Film(short filmId, Language languageByOriginalLanguageId,
			Language languageByLanguageId, String title, String description,
			Date releaseYear, byte rentalDuration, BigDecimal rentalRate,
			Short length, BigDecimal replacementCost, String rating,
			String specialFeatures, Date lastUpdate, Set inventories,
			Set filmActors, Set filmCategories) {
		this.filmId = filmId;
		this.languageByOriginalLanguageId = languageByOriginalLanguageId;
		this.languageByLanguageId = languageByLanguageId;
		this.title = title;
		this.description = description;
		this.releaseYear = releaseYear;
		this.rentalDuration = rentalDuration;
		this.rentalRate = rentalRate;
		this.length = length;
		this.replacementCost = replacementCost;
		this.rating = rating;
		this.specialFeatures = specialFeatures;
		this.lastUpdate = lastUpdate;
		this.inventories = inventories;
		this.filmActors = filmActors;
		this.filmCategories = filmCategories;
	}

	public short getFilmId() {
		return this.filmId;
	}

	public void setFilmId(short filmId) {
		this.filmId = filmId;
	}

	public Language getLanguageByOriginalLanguageId() {
		return this.languageByOriginalLanguageId;
	}

	public void setLanguageByOriginalLanguageId(
			Language languageByOriginalLanguageId) {
		this.languageByOriginalLanguageId = languageByOriginalLanguageId;
	}

	public Language getLanguageByLanguageId() {
		return this.languageByLanguageId;
	}

	public void setLanguageByLanguageId(Language languageByLanguageId) {
		this.languageByLanguageId = languageByLanguageId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getReleaseYear() {
		return this.releaseYear;
	}

	public void setReleaseYear(Date releaseYear) {
		this.releaseYear = releaseYear;
	}

	public byte getRentalDuration() {
		return this.rentalDuration;
	}

	public void setRentalDuration(byte rentalDuration) {
		this.rentalDuration = rentalDuration;
	}

	public BigDecimal getRentalRate() {
		return this.rentalRate;
	}

	public void setRentalRate(BigDecimal rentalRate) {
		this.rentalRate = rentalRate;
	}

	public Short getLength() {
		return this.length;
	}

	public void setLength(Short length) {
		this.length = length;
	}

	public BigDecimal getReplacementCost() {
		return this.replacementCost;
	}

	public void setReplacementCost(BigDecimal replacementCost) {
		this.replacementCost = replacementCost;
	}

	public String getRating() {
		return this.rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getSpecialFeatures() {
		return this.specialFeatures;
	}

	public void setSpecialFeatures(String specialFeatures) {
		this.specialFeatures = specialFeatures;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Set getInventories() {
		return this.inventories;
	}

	public void setInventories(Set inventories) {
		this.inventories = inventories;
	}

	public Set getFilmActors() {
		return this.filmActors;
	}

	public void setFilmActors(Set filmActors) {
		this.filmActors = filmActors;
	}

	public Set getFilmCategories() {
		return this.filmCategories;
	}

	public void setFilmCategories(Set filmCategories) {
		this.filmCategories = filmCategories;
	}

}
