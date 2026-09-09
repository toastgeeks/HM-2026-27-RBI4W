package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class newMecanumDrive {

    private final Telemetry telemetry;
    private final LinearOpMode opMode;

    private DcMotor fleft;
    private DcMotor fright;
    private DcMotor bleft;
    private DcMotor bright;

    private IMU imu;
    public double maxSpeed = 1.0;


    // ENCODER CONSTANTS


    // goBILDA 5203 19.2:1
    private static final double TICKS_PER_REV = 537.7;

    // 104mm wheels
    private static final double WHEEL_DIAMETER_IN = 104.0 / 25.4;

    private static final double WHEEL_CIRCUMFERENCE =
            Math.PI * WHEEL_DIAMETER_IN;

    private static final double TICKS_PER_INCH =
            TICKS_PER_REV / WHEEL_CIRCUMFERENCE;

    // Start here. We will calibrate this later.
    private static final double STRAFE_MULTIPLIER = 1.0;

    // =========================
    // PID CONSTANTS
    // =========================

    // Heading correction while driving
    private static final double HEADING_KP = 0.015;

    // Turning
    private static final double TURN_KP = 0.015;

    // Minimum power used when moving
    private static final double MIN_DRIVE_POWER = 0.15;
    private static final double MIN_TURN_POWER = 0.15;

    public newMecanumDrive(LinearOpMode opMode) {
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
    }
    // INITIALIZATION


    public void init(HardwareMap hardwareMap) {

        fleft = hardwareMap.dcMotor.get("fleft");
        fright = hardwareMap.dcMotor.get("fright");
        bleft = hardwareMap.dcMotor.get("bleft");
        bright = hardwareMap.dcMotor.get("bright");

        // Your existing motor directions
        fleft.setDirection(DcMotor.Direction.REVERSE);
        bleft.setDirection(DcMotor.Direction.REVERSE);

        // Brake when power is zero
        fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Encoder mode
        fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // =========================
        // IMU
        // =========================

        imu = hardwareMap.get(IMU.class, "imu");

        /*
         * CHANGE THESE IF YOUR CONTROL HUB IS MOUNTED DIFFERENTLY.
         *
         * This example assumes:
         *   Logo = UP
         *   USB = FORWARD
         */
        RevHubOrientationOnRobot orientation =
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                );

        imu.initialize(
                new IMU.Parameters(orientation)
        );

        imu.resetYaw();
    }


    // ============================================================
    // TELEOP DRIVE
    // ============================================================

    public void drive(double forward, double right, double rotate) {

        double fleftPower =
                forward + right + rotate;

        double frightPower =
                forward - right - rotate;

        double bleftPower =
                forward - right + rotate;

        double brightPower =
                forward + right - rotate;

        setPowers(
                fleftPower,
                frightPower,
                bleftPower,
                brightPower
        );

        telemetry.addData("FL Encoder", fleft.getCurrentPosition());
        telemetry.addData("FR Encoder", fright.getCurrentPosition());
        telemetry.addData("BL Encoder", bleft.getCurrentPosition());
        telemetry.addData("BR Encoder", bright.getCurrentPosition());
        telemetry.addData("Heading", getHeading());
    }


    // ============================================================
    // BASIC MOTOR CONTROL
    // ============================================================

    private void setPowers(double fleftPower, double frightPower, double bleftPower, double brightPower) {
        // Find the max power requested across all wheels
        double max = Math.max(Math.abs(fleftPower), Math.abs(frightPower));
        max = Math.max(max, Math.abs(bleftPower));
        max = Math.max(max, Math.abs(brightPower));

        // Normalize powers if any motor exceeds 1.0
        if (max > 1.0) {
            fleftPower /= max;
            frightPower /= max;
            bleftPower /= max;
            brightPower /= max;
        }

        // Apply global speed scaling (e.g., when lift is raised)
        fleft.setPower(fleftPower * maxSpeed);
        fright.setPower(frightPower * maxSpeed);
        bleft.setPower(bleftPower * maxSpeed);
        bright.setPower(brightPower * maxSpeed);
    }


    private void stop() {
        setPowers(0, 0, 0, 0);
    }

    // HEADING

    public double getHeading() {
        return imu.getRobotYawPitchRollAngles()
                .getYaw(AngleUnit.DEGREES);
    }


    /*
     * Returns the smallest difference between two angles.
     *
     * Example:
     * target = 5°
     * current = 355°
     *
     * Error = +10°, NOT -350°.
     */
    private double angleError(double target, double current) {

        double error = target - current;

        while (error > 180) {
            error -= 360;
        }

        while (error < -180) {
            error += 360;
        }

        return error;
    }


    // DRIVE DISTANCE

    /*
     * Drive a certain distance while maintaining a heading.
     *
     * forwardInches:
     *   + = forward
     *   - = backward
     *
     * rightInches:
     *   + = right
     *   - = left
     *
     * heading:
     *   desired robot heading in degrees
     *
     * Example:
     *
     * driveDistance(24, 0, 0);
     *
     * = drive forward 24 inches while facing 0°
     */
    public void driveDistance(
            double forwardInches,
            double rightInches,
            double heading) {

        int forwardTicks =
                (int) (forwardInches * TICKS_PER_INCH);

        int rightTicks =
                (int) (rightInches *
                        TICKS_PER_INCH *
                        STRAFE_MULTIPLIER);

        /*
         * Mecanum encoder targets.
         */

        int flTarget =
                fleft.getCurrentPosition()
                        + forwardTicks
                        + rightTicks;

        int frTarget =
                fright.getCurrentPosition()
                        + forwardTicks
                        - rightTicks;

        int blTarget =
                bleft.getCurrentPosition()
                        + forwardTicks
                        - rightTicks;

        int brTarget =
                bright.getCurrentPosition()
                        + forwardTicks
                        + rightTicks;

        fleft.setTargetPosition(flTarget);
        fright.setTargetPosition(frTarget);
        bleft.setTargetPosition(blTarget);
        bright.setTargetPosition(brTarget);

        fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        /*
         * Drive until all motors reach their targets.
         */

        while (
                fleft.isBusy() ||
                        fright.isBusy() ||
                        bleft.isBusy() ||
                        bright.isBusy()
        ) {

            // -------------------------
            // Heading correction
            // -------------------------

            double error =
                    angleError(heading, getHeading());

            double correction =
                    error * HEADING_KP;

            /*
             * Determine basic movement direction.
             */

            double forwardPower =
                    Math.signum(forwardInches) *
                            Math.min(
                                    Math.abs(forwardInches) / 10.0,
                                    1.0
                            );

            double rightPower =
                    Math.signum(rightInches) *
                            Math.min(
                                    Math.abs(rightInches) / 10.0,
                                    1.0
                            );

            /*
             * Make sure the robot doesn't start with
             * almost-zero power.
             */

            if (Math.abs(forwardPower) > 0) {
                forwardPower = Math.copySign(
                        Math.max(
                                Math.abs(forwardPower),
                                MIN_DRIVE_POWER
                        ),
                        forwardPower
                );
            }

            if (Math.abs(rightPower) > 0) {
                rightPower = Math.copySign(
                        Math.max(
                                Math.abs(rightPower),
                                MIN_DRIVE_POWER
                        ),
                        rightPower
                );
            }

            // -------------------------
            // Mecanum power calculation
            // -------------------------

            double fl =
                    forwardPower +
                            rightPower +
                            correction;

            double fr =
                    forwardPower -
                            rightPower -
                            correction;

            double bl =
                    forwardPower -
                            rightPower +
                            correction;

            double br =
                    forwardPower +
                            rightPower -
                            correction;

            setPowers(fl, fr, bl, br);

            telemetry.addData("Target Heading", heading);
            telemetry.addData("Heading", getHeading());
            telemetry.addData("Heading Error", error);
            telemetry.addData("FL", fleft.getCurrentPosition());
            telemetry.addData("FR", fright.getCurrentPosition());
            telemetry.addData("BL", bleft.getCurrentPosition());
            telemetry.addData("BR", bright.getCurrentPosition());
            telemetry.update();
        }

        stop();

        /*
         * Return to normal encoder mode.
         */

        fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    // TURN TO ANGLE

    public void turnTo(double targetHeading) {

        while (opMode.opModeIsActive()) {

            double error = angleError(targetHeading, getHeading());

            if (Math.abs(error) < 1.0) {
                break;
            }

            double power = error * TURN_KP;

            // Limit maximum turning power
            power = Math.max(-0.5, Math.min(0.5, power));

            // Make sure the robot can actually overcome friction
            if (Math.abs(power) < MIN_TURN_POWER) {
                power = Math.copySign(MIN_TURN_POWER, power);
            }

            setPowers(
                    power,
                    -power,
                    power,
                    -power
            );

            telemetry.addData("Target Heading", targetHeading);
            telemetry.addData("Heading", getHeading());
            telemetry.addData("Error", error);
            telemetry.addData("Turn Power", power);
            telemetry.update();
        }

        stop();
    }

    // RESET HEADING

    public void resetHeading() {
        imu.resetYaw();
    }

    // CONSTANTS FOR DEBUGGING

    public double getTicksPerInch() {
        return TICKS_PER_INCH;
    }
}