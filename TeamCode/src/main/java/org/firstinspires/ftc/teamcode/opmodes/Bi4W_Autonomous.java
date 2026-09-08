package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.mechanisms.newMecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Bi4W_Manipulator;

@Autonomous(name = "Bi4W Auto")
public class Bi4W_Autonomous extends LinearOpMode {

    @Override
    public void runOpMode() {

        newMecanumDrive drive =
                new newMecanumDrive(telemetry);

        Bi4W_Manipulator manipulator = new Bi4W_Manipulator();

        drive.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        manipulator.setIntakeSpeed(100);

        drive.driveDistance(
                24,
                0,
                0
        );


        drive.turnTo(90);

        drive.driveDistance(
                72,
                0,
                90
        );

        drive.turnTo(0);

        drive.driveDistance(
                24,
                0,
                0
        );

        drive.turnTo(90);

        drive.driveDistance(
                48,
                0,
                90
        );

        drive.turnTo(180);

        drive.driveDistance(
                48,
                0,
                180
        );

        drive.turnTo(270);

        drive.driveDistance(
                120,
                0,
                270
        );

        drive.turnTo(0);

        drive.driveDistance(
                48,
                0,
                0
        );

        drive.turnTo(90);

        drive.driveDistance(
                100,
                0,
                90
        );

        manipulator.setIntakeSpeed(0);


    }
}