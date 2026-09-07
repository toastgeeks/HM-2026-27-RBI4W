package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Bi4W_servo {
    private Servo servo;

    public void init (HardwareMap hwMap) {
        servo = hwMap.get(Servo.class, "servo");
    }
    public void setServoPosition(double position){
        servo.setPosition(position);
    }
}
