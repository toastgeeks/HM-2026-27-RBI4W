package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Bi4W_Manipulator;
import org.firstinspires.ftc.teamcode.mechanisms.Bi4W_servo;

@TeleOp()
public class Bi4W_TeleOp extends OpMode {
    MecanumDrive drive = new MecanumDrive(telemetry);
    Bi4W_Manipulator manipulator = new Bi4W_Manipulator();
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
        imu.resetYaw();
    }

    private void driveFieldRelative(double forward, double right, double rotate) {
        double robotAngle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Standard 2D vector rotation relative to field origin
        double rotatedRight = right * Math.cos(-robotAngle) - forward * Math.sin(-robotAngle);
        double rotatedForward = right * Math.sin(-robotAngle) + forward * Math.cos(-robotAngle);

        drive.drive(rotatedForward, rotatedRight, rotate);
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        driveFieldRelative(forward, right, rotate);

        manipulator.setIntakeSpeed(gamepad1.right_trigger - gamepad1.left_trigger);


        telemetry.addData("Intake speed", gamepad1.right_trigger);

        double armControl = gamepad2.right_trigger - gamepad2.left_trigger;
        manipulator.controlLift(armControl, telemetry);

        if (gamepad2.a) {
            servo.setServoPosition(0.35);
        }
        else{
            servo.setServoPosition(0.0);
        }

        if (manipulator.targetLiftTicks > 20) {
            drive.maxSpeed = 0.25;
        }
        else {
            drive.maxSpeed = 1;
        }

        if (gamepad1.dpad_up) {
            imu.resetYaw();
        }


    }

}
//wakeo was here and thatKerbal noticed