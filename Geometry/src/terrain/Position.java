package terrain;

public class Position {
	private double x;
	private double y;
	private double z;

	public Position(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Position(String str) {
		String temp[] = str.split(",");
		try {
			this.x = Double.parseDouble(temp[0]);
			this.y = Double.parseDouble(temp[1]);
			this.z = Double.parseDouble(temp[2]);
		} catch (Exception ex) {
			System.out.println("Number Format Exception: " + str);
		}
	}

	// calculate Euclidean Distance
	public double getDistance(Position otherPoint) {
		return Math.sqrt(Math.pow((this.x - otherPoint.x), 2.0) + Math.pow((this.y - otherPoint.y), 2.0)
				+ Math.pow((this.z - otherPoint.z), 2.0));
	}

	public boolean isSamePosition(Position position) {
		return x == position.getX() && y == position.getY() && z == position.getZ();
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Position))
			return false;
		Position otherMyClass = (Position) other;
		if (this.getX() == otherMyClass.getX() && this.getY() == otherMyClass.getY()
				&& this.getZ() == otherMyClass.getZ())
			return true;
		else
			return false;
	}

	public String toString() {
		return this.x + "," + this.y + "," + this.z;
	}
}
