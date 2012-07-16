package org.diagram;

// Generated Jul 16, 2012 4:06:14 PM by Hibernate Tools 4.0.0

/**
 * FilmCategoryId generated by hbm2java
 */
public class FilmCategoryId implements java.io.Serializable {

	private byte categoryId;
	private short filmId;

	public FilmCategoryId() {
	}

	public FilmCategoryId(byte categoryId, short filmId) {
		this.categoryId = categoryId;
		this.filmId = filmId;
	}

	public byte getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(byte categoryId) {
		this.categoryId = categoryId;
	}

	public short getFilmId() {
		return this.filmId;
	}

	public void setFilmId(short filmId) {
		this.filmId = filmId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FilmCategoryId))
			return false;
		FilmCategoryId castOther = (FilmCategoryId) other;

		return (this.getCategoryId() == castOther.getCategoryId())
				&& (this.getFilmId() == castOther.getFilmId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCategoryId();
		result = 37 * result + this.getFilmId();
		return result;
	}

}
