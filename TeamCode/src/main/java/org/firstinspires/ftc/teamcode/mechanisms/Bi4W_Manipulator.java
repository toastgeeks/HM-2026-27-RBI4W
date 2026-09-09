package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Bi4W_Manipulator {
    public DcMotor intake;
    private int MIN_TICKS = 0;
    private static int MAX_TICKS = 4000;
    private double NUDGE_POWER = 0.5;
    private double RUN_TO_POSITION_POWER = 1;
    private DcMotorEx liftMotor;
    public int targetLiftTicks = 0;

    public void init(HardwareMap hwMap) {
        intake = hwMap.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);


        liftMotor = hwMap.get(DcMotorEx.class, "lift");
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setTargetPosition(0);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        targetLiftTicks = 0;


    }

   public void setTargetLiftTicks(int ticks) {
       // actually command motor to move
       targetLiftTicks = Range.clip(ticks, MIN_TICKS, MAX_TICKS);

       liftMotor.setTargetPosition(targetLiftTicks);

       liftMotor.setPower(RUN_TO_POSITION_POWER);
       if(liftMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
           liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       }

       liftMotor.setPower(RUN_TO_POSITION_POWER);
   }

    public void setIntakeSpeed(double intakeSpeed){
        intake.setPower(intakeSpeed);
    }

    public void setTargetPercentage(int percent){
        int ticks = (int) Range.scale(percent, 0, 100, MIN_TICKS, MAX_TICKS);
        setTargetLiftTicks(ticks);
    }

    public boolean isBusy() {return liftMotor.isBusy();}

    public void setNUDGE_POWER(double requestedPower) {
        if(liftMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        double clippedPower = Range.clip(requestedPower, -NUDGE_POWER, NUDGE_POWER);

        int curPos = liftMotor.getCurrentPosition();

        if (curPos <= MIN_TICKS && clippedPower <0) {
            clippedPower = 0;
        }else if (curPos >= MAX_TICKS && clippedPower > 0) {
            clippedPower = 0;
        }

        liftMotor.setPower(clippedPower);
    }



    public void controlLift(double control, Telemetry telemetry) {
        if ((control < 0.05) && (control > -0.05)) {  //no active input
            // No trigger: HOLD position
            //liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftMotor.setPower(0.6);
            liftMotor.setTargetPosition(liftMotor.getCurrentPosition());
        } else {
            if (control < -0.05) {
                // Lowering: allow gravity to help
                liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
            int currentPosition = liftMotor.getCurrentPosition();
            int target = currentPosition + (int)(control * 25);

            target = Range.clip(target, MIN_TICKS, MAX_TICKS);

            liftMotor.setTargetPosition(target);
        }
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(RUN_TO_POSITION_POWER);
        telemetry.addData("Lift Ticks", liftMotor.getCurrentPosition());
    }
}
