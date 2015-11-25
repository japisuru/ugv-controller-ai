package terrain;

public class Position {
    private double x;
    private double y;
    private double z;
    
    public Position(double x, double y,double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    //calculate Euclidean Distance
    public double getDistance(Position otherPoint)
    {
        return Math.sqrt(Math.pow((this.x - otherPoint.x),2.0) + Math.pow((this.y - otherPoint.y),2.0) + Math.pow((this.z - otherPoint.z),2.0));
    }
    
    public boolean isSamePosition(Position position)
    {
        return x == position.getX() && y == position.getY() && z == position.getZ();
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
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
     * @param y the y to set
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
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }
}


