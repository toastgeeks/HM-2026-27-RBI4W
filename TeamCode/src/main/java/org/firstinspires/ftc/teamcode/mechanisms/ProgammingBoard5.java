package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ProgammingBoard5 {
    private DcMotor motor;
    private double ticksPerRotation;
    private Servo servo;

    public void init(HardwareMap hwMap) {
       motor = hwMap.get(DcMotor.class, "motor");
       motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       ticksPerRotation = motor.getMotorType().getTicksPerRev();
       servo = hwMap.get(Servo.class, "servo");
    }
    public void setMotorSpeed(double speed){
        motor.setPower(speed);
    }
    public double setMotorRotations(){
        return motor.getCurrentPosition() / ticksPerRotation;

    }
    public void setServoPosition(double position){
        servo.setPosition(position);
    }
}
