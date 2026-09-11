package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.ProgammingBoard5;


@TeleOp()
@Disabled
public class ServoGamepadOpMode extends OpMode {
    ProgammingBoard5 board = new ProgammingBoard5();
    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            board.setServoPosition(1.0);
        }
        else if (gamepad1.b) {
            board.setServoPosition(0.0);
        }
        else{
            board.setServoPosition(0.5);
        }
    }
}
