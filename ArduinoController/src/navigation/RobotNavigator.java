package navigation;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import myArduino.SerialTest;

public class RobotNavigator {

	SerialTest st;
	int sleepTime = 400;

	public RobotNavigator() {
		st = new SerialTest();
		st.initialize();
	}

	public void stopClose() {
		st.stop(2);
		st.close();
	}

	public void stop() {
		st.stop(2);
	}

	public void navigate(int id, int r_f_x, int r_f_y, int r_b_x, int r_b_y, int t_x, int t_y) {
		Vector2D r = new Vector2D(r_f_x - r_b_x, r_f_y - r_b_y);
		Vector2D t = new Vector2D(t_x - r_b_x, t_y - r_b_y);
		double angle = FastMath.toDegrees(FastMath.atan2(t.getY(), t.getX()) - FastMath.atan2(r.getY(), r.getX()));
		System.out.println(angle);
		try {
			if ((15 > angle && angle > 0) || -15 < angle && angle < 0) {
				st.forward(id);
				Thread.sleep(sleepTime);
				st.stop(id);

			} 
			else if((360 > angle && angle > 345) || -360 < angle && angle < -345)
			{
				st.forward(id);
				Thread.sleep(sleepTime);
				st.stop(id);
			}
			else if(180 > angle && angle > 15)
			{
				st.right(id);
				Thread.sleep((long) (sleepTime));
				st.stop(id);
			}
			else if(345 > angle && angle > 180)
			{
				st.left(id);
				Thread.sleep((long) (sleepTime));
				st.stop(id);
			}
			else if(-180 < angle && angle < -15)
			{
				st.left(id);
				Thread.sleep((long) (sleepTime));
				st.stop(id);
			}
			else if(-345 < angle && angle < -180)
			{
				st.right(id);
				Thread.sleep((long) (sleepTime));
				st.stop(id);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void mainx(String[] args) throws InterruptedException {
		RobotNavigator rn = new RobotNavigator();
		rn.navigate(2, 4, 4, 0, 0, 0, 7);
		Thread.sleep(500);
		rn.navigate(2, -4, 4, 0, 0, 0, 7);
		Thread.sleep(500);
		rn.navigate(2, 0, 4, 0, 0, 0, 7);
		Thread.sleep(2000);
		rn.navigate(2, 4, 4, 0, 0, 0, 7);
		Thread.sleep(500);
		rn.stopClose();
	}

}
