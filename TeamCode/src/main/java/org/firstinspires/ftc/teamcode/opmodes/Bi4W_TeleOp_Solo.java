package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.Bi4W_Manipulator_solo;
import org.firstinspires.ftc.teamcode.mechanisms.Bi4W_servo;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@TeleOp()
public class Bi4W_TeleOp_Solo extends OpMode {
    MecanumDrive drive = new MecanumDrive(telemetry);
    Bi4W_Manipulator_solo manipulator = new Bi4W_Manipulator_solo();
    Bi4W_servo servo = new Bi4W_servo();
    IMU imu;

    @Override
    public void init() {
        drive.init(hardwareMap);

        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot revHubOrientationOnRobot =
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.UP);

        imu.initialize(new IMU.Parameters(revHubOrientationOnRobot));
        servo.init(hardwareMap);

        manipulator.init(hardwareMap);
    }

    private void driveFieldRelative(double forward, double right, double rotate) {
        double robotAngle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        //convert to polar
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(forward, right);
        //rotate angle
        theta = AngleUnit.normalizeRadians(theta - robotAngle);

        //convert back to cartesian
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        drive.drive(newForward, newRight, rotate);
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        driveFieldRelative(forward, right, rotate);

        manipulator.setIntakeSpeed(gamepad1.right_bumper);

        telemetry.addData("Intake speed", gamepad1.right_bumper);

        double armControl = gamepad1.right_trigger - gamepad1.left_trigger;
        manipulator.controlLift(armControl, telemetry);

        if (gamepad1.a) {
            servo.setServoPosition(0.5);
        }
        else{
            servo.setServoPosition(0.0);
        }


    }

}
