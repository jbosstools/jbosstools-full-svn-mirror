//$Id: DataPoint.java 7231 2005-06-19 22:04:00Z oneovthafew $
package mapping.readonly;

import java.math.BigDecimal;

/**
 * @author Gavin King
 */
public class DataPoint {
	private long id;
	private BigDecimal x;
	private BigDecimal y;
	private String description;
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return Returns the x.
	 */
	public BigDecimal getX() {
		return x;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(BigDecimal x) {
		this.x = x;
	}
	/**
	 * @return Returns the y.
	 */
	public BigDecimal getY() {
		return y;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(BigDecimal y) {
		this.y = y;
	}
}
