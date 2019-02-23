/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class Robot extends IterativeRobot {
	private DifferentialDrive m_myRobot;
	Joystick driveStick = new Joystick(0);
	//Joystick driveStick1 = new Joystick(1);
	Joystick operatorStick = new Joystick(1);
	
	
	   /*********************************************
	    * variables
	    ********************************************/
		double leftDrive;
		double rightDrive;
		double sLeft;
		double sRight;
		double shooterRampCount;
	   /*********************************************
	    * Module chooser
	    ********************************************/
		final String Shooter = "Default";
		final String Arm = "My Auto";
		String moduleMode;
		SendableChooser<String> chooser = new SendableChooser<>();
	   /*********************************************
	    * Right Side
	    ********************************************/
	    WPI_TalonSRX m_frontleft = new WPI_TalonSRX(1);
	    WPI_TalonSRX m_rearleft = new WPI_TalonSRX(3);
	  
	   /*********************************************
	    * Left Side
	    ********************************************/
	    WPI_TalonSRX m_frontright = new WPI_TalonSRX(2);
	    WPI_TalonSRX m_rearright = new WPI_TalonSRX(4);
	   /*********************************************
	    *Shooter
	    ********************************************/
	    WPI_TalonSRX m_sRight = new WPI_TalonSRX(6);
	    WPI_TalonSRX m_sLeft = new WPI_TalonSRX(7);
	    WPI_TalonSRX m_sFeed = new WPI_TalonSRX(8);
	    WPI_TalonSRX m_sAim = new WPI_TalonSRX(5);
	

	   /*********************************************
	    * speed controller groups
	    ********************************************/
	    SpeedControllerGroup m_right = new SpeedControllerGroup(m_frontright, m_rearright);
	    SpeedControllerGroup m_left = new SpeedControllerGroup(m_frontleft, m_rearleft);
	    SpeedControllerGroup m_shooter = new SpeedControllerGroup(m_sRight, m_sLeft);

	@Override
	public void robotInit() {
		m_myRobot = new DifferentialDrive(m_right, m_left);
		chooser.addDefault("Shooter", Shooter);
		chooser.addObject("Arm", Arm);
		SmartDashboard.putData("Module choices", chooser);
	}

	@Override
	public void teleopPeriodic() {
		
			m_frontleft.configOpenloopRamp(2, 20);
		if(driveStick.getRawAxis(1) <=0.25 && driveStick.getRawAxis(5) <= 0.25 && driveStick.getRawAxis(1) >= 0.15 && driveStick.getRawAxis(5)>= 0.15 || driveStick.getRawAxis(1) <=-0.25 && driveStick.getRawAxis(5) <=- 0.25 && driveStick.getRawAxis(1) >= -0.15 && driveStick.getRawAxis(5)>= -0.15) {
			m_frontright.configOpenloopRamp(2, 20);
			m_rearright.configOpenloopRamp(2, 20);
			m_rearleft.configOpenloopRamp(2, 20);
		}
		else {
			m_frontleft.configOpenloopRamp(2, 0);
			m_frontright.configOpenloopRamp(2, 0);
			m_rearright.configOpenloopRamp(2, 0);
			m_rearleft.configOpenloopRamp(2, 0);
		}
		
	    m_sRight.setInverted(true); //sets the left shooter motor to run in reverse
		m_myRobot.tankDrive(leftDrive, rightDrive);
		
		/******************************************
		 * Deadzones
		 ******************************************/
		if (driveStick.getRawAxis(1) >= 0.2 || driveStick.getRawAxis(1) <= -0.2) {
			leftDrive = driveStick.getRawAxis(1);
		}
		else {
			leftDrive = 0;
		}
		
		if (driveStick.getRawAxis(5) >= 0.2 || driveStick.getRawAxis(5) <= -0.2) {
			rightDrive = driveStick.getRawAxis(5);
		}
		else {
			rightDrive = 0;
		}
		/******************************************
		 * Module_chooser
		 * WIP, allows the module code to be changed
		 * based on a selection on the smartdashboard
		 * and allows all modules to be used without
		 * the change of code
		 ******************************************/
		moduleMode = chooser.getSelected();
		/*
		 * switch (moduleMode) {
		 * 	case Shooter:
		 * 		
		 * 		place shooter code here
		 * 		break;
		 * 	case Arm:
		 * 		place arm code her
		 * 		break;
		 * }
		 * 
		 */
		/******************************************
		 * shooter
		 ******************************************/
		if(driveStick.getRawAxis(3) >=0.5) {
			m_shooter.set(driveStick.getRawAxis(0));
			shooterRampCount++;
			if(shooterRampCount < 1400)
			{
			m_sFeed.set(0.64);
			}
		}
		else {
			shooterRampCount = 0;
			m_sFeed.set(0);
			m_shooter.set(0);
		}
		if (driveStick.getRawButton(4)) {
			m_sAim.set(0.6);
		
		}
		else if (driveStick.getRawButton(1)) {
			m_sAim.set(-0.6);
		}
		else {
			m_sAim.set(0.05);
		}
		
		
		
	}
}
