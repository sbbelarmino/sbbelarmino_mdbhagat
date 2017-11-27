package sbbelarmino_mdbhagat;
import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;
/*
 * BigHero6 - a robot by Samuel B. & Mayank B.
 */
public class BigHero6 extends AdvancedRobot
{
		
	public final double PERCENT_BUFFER = 0.20; 
	//Number that indicates how close we want to be to the wall
	private int moveDirection = 1;
	ScannedRobotEvent enemy;
	
	public void run() {
		// Sets up the boundaries of the battle field
		double width = this.getBattleFieldWidth();
		double height = this.getBattleFieldHeight();
		
		// buffer is used to indicate how close we want our robot to be to the walls
		double buffer = PERCENT_BUFFER*Math.max(width,height);
		
		// Colors for the body,gun,radar
		setColors(Color.white,Color.red,Color.red); 
		
		//Makes sure that the gun doesnt turn as the robot is turning
		setAdjustGunForRobotTurn(true);
			
	while(true) {
		//Continuously rotate the radar 90 degrees each game-tick
		turnGunRight(90); 
		
		/* This section is a prelimenary test to see where our robot is traveling
		 * and will help us avoid the walls
		 */ 
		double xPos = this.getX(); 
		double yPos = this.getY();
		System.out.println("Y Pos: " + yPos + " " + buffer);
		System.out.println("X Pos" + xPos + " " + buffer);
		
		if(yPos<buffer){ 
			// If the robot gets near bottom, rotate up to avoid wall
			System.out.println("Bottom"); //Prints out if the robot is too close to bottom wall
			
			/* These numbers are determined by the Robocode Physics diagram;
			 *  Robocode doesnt work on a normal 0-360 circle which was 
			 *	confusing at first
			 */
			if((this.getHeading()<180) && (this.getHeading()>90))
			{
				this.setTurnLeft(90);
			}
			else if((this.getHeading()<270) && (this.getHeading()>180))
			{
				this.setTurnRight(90);
			}
		}
		else if (yPos>height-buffer){
			// If the robot gets near top, rotate down to avoid wall
			System.out.println("Top"); //Prints out if the robot is too close to top wall
			if((this.getHeading()<90) && (this.getHeading()>0))
			{
				this.setTurnRight(90);
			}
			else if((this.getHeading()<360) && (this.getHeading()>270))
			{
				this.setTurnLeft(90);
			}

		}
		else if(xPos<buffer){ 
			// If the robot gets near left, rotate rotate to avoid wall
			System.out.println("Left"); //Prints out if the robot is too close to left wall
			if((this.getHeading()<360) && (this.getHeading()>270))
			{
				this.setTurnLeft(90);
			}
			else if((this.getHeading()<270) && (this.getHeading()>180))
			{
				this.setTurnRight(-90);
			}
		}
		else if(xPos>width-buffer){ 
			// If the robot gets near right, rotate left to avoid wall
			System.out.println("Right"); //Prints out if the robot is too close to right wall
			if((this.getHeading()<90) && (this.getHeading()>0))
			{
				this.setTurnLeft(90);
			}
			else if((this.getHeading()<180) && (this.getHeading()>90))
			{
				this.setTurnRight(-90);
			}
		}
		else
		{
			//Does nothing if it's not near any walls
			this.turnRight(0);
			this.turnLeft(0);
		}
		ahead(100);} //How many spaces the robot will move each "game-tick"
	} // while(true) bracket
	
	public void onScannedRobot(ScannedRobotEvent e) {
		// Calculate exact location of the enemy robot
		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

		// If the robot is close enough, shoot at them
		if (Math.abs(bearingFromGun) <= 3) {
			turnGunRight(bearingFromGun);
			if (getGunHeat() == 0) { 
			//Gun will not fire if it is overheating so we need to wait until the gun heat is 0
				fire(3); //Amount of power we want our gun to output
				turnRight(90);
				ahead(100);
				// Shoots then moves to avoid being shot by the other robots
			}
		} 
		else {
			turnGunRight(bearingFromGun);
		}
		if (bearingFromGun == 0) {
			scan();
		}
		
		if(e.getDistance() < 150){
			//If the enemy is too close, our robot will move back 75 spaces
			back(75);
		}
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		/* If hit by a bullet, the robot will rotate 90 degrees 
		   in Robocode Physics and move back to avoid being hit again */
		turnRight(90);
		back(100);
	}
	
	public void onHitWall(HitWallEvent e){ 
	/*If the wall avoidance method fails and we're stuck at a wall,
		robot will rotate and continue with it's regular movement */
		if(getVelocity() == 0){
			turnRight(90);
			ahead(100);
		}
	}
	
	public void onWin(WinEvent e){
		/* Will spin, similar to spinBot when robot wins */
		for(int i=0; i<50; i++){
		setTurnRight(10000);
		setMaxVelocity(100);
		ahead(10000);
		}
	}
	
}
		



