package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class MecanumDrive {
    private final Telemetry telemetry;
    private DcMotor fleft;
    private DcMotor fright;
    private DcMotor bleft;
    private DcMotor bright;
    public double maxSpeed = 1.0;
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

       // fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

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

    public void drive(double forward, double right, double rotate) {
        double fleftPower = forward + right + rotate;
        double frightPower = forward - right - rotate;
        double bleftPower = forward - right + rotate;
        double brightPower = forward + right - rotate;

        setPowers(fleftPower, frightPower, bleftPower, brightPower);

        telemetry.addData("fleft", fleft.getCurrentPosition());
        telemetry.addData("fright", fright.getCurrentPosition());
        telemetry.addData("bleft", bleft.getCurrentPosition());
        telemetry.addData("bright", bright.getCurrentPosition());
    }
}
