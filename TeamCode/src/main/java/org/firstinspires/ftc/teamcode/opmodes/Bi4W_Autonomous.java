package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.mechanisms.newMecanumDrive;

@Autonomous(name = "Bi4W Auto")
public class Bi4W_Autonomous extends LinearOpMode {

    @Override
    public void runOpMode() {

        newMecanumDrive drive =
                new newMecanumDrive(telemetry);

        drive.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        drive.driveDistance(
                120,
                0,
                0
        );


        drive.turnTo(-90);

        drive.driveDistance(
                20,
                0,
                -90
        );
    }
}