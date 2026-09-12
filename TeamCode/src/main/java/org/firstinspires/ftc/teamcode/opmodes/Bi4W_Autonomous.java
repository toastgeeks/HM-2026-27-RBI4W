package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Bi4W_Manipulator;
import org.firstinspires.ftc.teamcode.mechanisms.Bi4W_servo;

@Autonomous(name = "Bi4W Autonomous", group = "Autonomous")
public class Bi4W_Autonomous extends LinearOpMode {

    private MecanumDrive drive;

    @Override
    public void runOpMode() {
        drive = new MecanumDrive(telemetry);
        Bi4W_servo servo = new Bi4W_servo();
        drive.init(hardwareMap);
        servo.init(hardwareMap);

        Bi4W_Manipulator manipulator = new Bi4W_Manipulator();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // Step 1: Reset IMU heading zero-point
            drive.resetHeading();

            manipulator.init(hardwareMap);

            manipulator.setIntakeSpeed(1.0);

            // 333 ms = 24 inches
            drive.driveForward(2400);

            drive.driveBackward(100);

            drive.leftTurn(400);

            drive.driveForward(333);

            drive.leftTurn(380);

            drive.driveForward(1000);

            manipulator.setIntakeSpeed(0);

            drive.leftTurn(380);

            drive.driveForward(400);

            drive.rightTurn(380);

            drive.driveForward(800);

            drive.leftTurn(380);

            drive.driveBackward(400);

            drive.rightTurn(380);

            drive.driveBackward(1300);

            manipulator.setIntakeSpeed(1.0);

            drive.driveForward(100);

            drive.rightTurn(380);

            drive.driveForward(300);

            drive.leftTurn(380);

            drive.driveForward(900);

            manipulator.setIntakeSpeed(0.0);













//           driveForward(72, 0.5);

//           turnToHeading(0,0.3);

//           driveForward(24, 0);

//           turnToHeading(90, 0.3)e;

//           driveForward(48, 1.0)e;

//           turnToHeading(180, 0.3)e;

//           driveForward(48, 1.0)e;

//           turnToHeading(-90, 0.3)e;

//           driveForward(120, 1.0)e;

//           turnToHeading(0, 0.3)e;

//           driveForward(48, 1.0)e;

//           turnToHeading(90, 0.3)e;

//           driveForward(72, 1.0);


            // Stop robot at the end of the autonomous period
            drive.stop();
        }
    }

    private void driveForward(double inches, double power) {
        drive.driveEncoder(inches, power);

        while (opModeIsActive() && drive.isBusy()) {
            telemetry.addData("Status", "Driving Forward");
            telemetry.addData("Target Inches", inches);
            telemetry.update();
        }

        //drive.stop();
        //sleep(250); // Pause briefly between movements
    }

    private void turnToHeading(double targetAngle, double power) {
        targetAngle *= -1;
        double error = targetAngle - drive.getHeading();

        // Simple P-loop turn using IMU reading
        while (opModeIsActive() && Math.abs(error) > 3.0) {
            error = targetAngle - drive.getHeading();

            // Determine turn direction: positive turn power rotates counter-clockwise
            double turnPower = Math.signum(error) * power;
            drive.drive(0, 0, turnPower);

            telemetry.addData("Target Angle", targetAngle);
            telemetry.addData("Current Heading", drive.getHeading());
            telemetry.addData("Error", error);
            telemetry.update();
        }
    }
}
//wakeo was here!