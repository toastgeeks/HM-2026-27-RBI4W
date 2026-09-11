package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class MecanumDrive {
    private final Telemetry telemetry;
    private DcMotor fleft;
    private DcMotor fright;
    private DcMotor bleft;
    private DcMotor bright;
    private IMU imu;

    public double maxSpeed = 1.0;

    // Adjust these constants based on your robot's physical specs:
    // (Ticks per revolution * Gear Ratio) / (Wheel Diameter * Math.PI)
    public static final double TICKS_PER_REV = 537.7; // Example: GoBILDA 312 RPM Yellowjacket
    public static final double WHEEL_DIAMETER_INCHES = 4.094488; // 104mm wheels
    public static final double TICKS_PER_INCH = TICKS_PER_REV / (WHEEL_DIAMETER_INCHES * Math.PI);

    public MecanumDrive(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void init(HardwareMap hardwareMap) {
        fleft = hardwareMap.dcMotor.get("fleft");
        fright = hardwareMap.dcMotor.get("fright");
        bleft = hardwareMap.dcMotor.get("bleft");
        bright = hardwareMap.dcMotor.get("bright");

        bleft.setDirection(DcMotor.Direction.REVERSE);
        fleft.setDirection(DcMotor.Direction.REVERSE);

        fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize IMU
        imu = hardwareMap.get(IMU.class, "imu");

        // Define hub orientation (Update these according to how your REV Hub is mounted)
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    public void resetEncoders() {
        fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double getHeading() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }

    public void resetHeading() {
        imu.resetYaw();
    }

    public void driveForward(double millis) {
        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ElapsedTime elapsedTime = new ElapsedTime();
        while (elapsedTime.milliseconds() < millis * 3.03) {
            double slowdown = 0.66;
            fleft.setPower(0.5 * slowdown);
            fright.setPower(0.46 * slowdown);
            bleft.setPower(0.5 * slowdown);
            bright.setPower(0.46 * slowdown);
        }

    }

    public void driveBackward(double millis) {
        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ElapsedTime elapsedTime = new ElapsedTime();
        while (elapsedTime.milliseconds() < millis * 3.03) {
            double slowdown = 0.66;
            fleft.setPower(-0.5 * slowdown);
            fright.setPower(-0.46 * slowdown);
            bleft.setPower(-0.5 * slowdown);
            bright.setPower(-0.46 * slowdown);
        }

    }

    public void rightTurn(double millis) {
        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ElapsedTime elapsedTime = new ElapsedTime();
        while (elapsedTime.milliseconds() < millis) {
            fleft.setPower(1);
            fright.setPower(-1);
            bleft.setPower(1);
            bright.setPower(-1);
        }

    }

    public void leftTurn(double millis) {
        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ElapsedTime elapsedTime = new ElapsedTime();
        while (elapsedTime.milliseconds() < millis) {
            fleft.setPower(-1);
            fright.setPower(1);
            bleft.setPower(-1);
            bright.setPower(1);
        }

    }

    // Drive forward/backward using encoders
    public void driveEncoder(double inches, double power) {
        resetEncoders();

        int targetTicks = (int) (inches * TICKS_PER_INCH);

        fleft.setTargetPosition(targetTicks);
        fright.setTargetPosition(targetTicks);
        bleft.setTargetPosition(targetTicks);
        bright.setTargetPosition(targetTicks);

        fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setPowers(power, power, power, power);
    }

    // Check if the drive motors are still busy moving to their target position
    public boolean isBusy() {
        return fleft.isBusy() && fright.isBusy() && bleft.isBusy() && bright.isBusy();
    }

    public void stop() {
        setPowers(0, 0, 0, 0);
    }

    private void setPowers(double fleftPower, double frightPower, double bleftPower, double brightPower) {
        fleft.setPower(fleftPower);
        fright.setPower(frightPower);
        bleft.setPower(bleftPower);
        bright.setPower(brightPower);
    }

    public void drive(double forward, double right, double rotate) {
        double fleftPower = (forward + right + rotate) * maxSpeed;
        double frightPower = (forward - right - rotate) * maxSpeed;
        double bleftPower = (forward - right + rotate) * maxSpeed;
        double brightPower = (forward + right - rotate) * maxSpeed;

        setPowers(fleftPower, frightPower, bleftPower, brightPower);

        telemetry.addData("Heading", getHeading());
        telemetry.addData("fleft", fleft.getCurrentPosition());
        telemetry.addData("fright", fright.getCurrentPosition());
        telemetry.addData("bleft", bleft.getCurrentPosition());
        telemetry.addData("bright", bright.getCurrentPosition());
    }
}