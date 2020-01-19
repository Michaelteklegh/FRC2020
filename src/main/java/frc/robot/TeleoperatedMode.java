package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class TeleoperatedMode implements IRobotMode {

    private XboxController xboxController;
    private IDrive drive;
    private IRoller roller;

    private static final double LEFT_STICK_EXPONENT = 3.0;
    private static final double ROTATION_SPEED_THRESHOLD = 0.3;

    public TeleoperatedMode(IDrive drive) {

        xboxController = new XboxController(PortMap.USB.XBOXCONTROLLER);

        this.drive = drive;
    }

    @Override
    public void init() {
        drive.init();
    }

    @Override
    public void periodic() {

        // Process Linear Motion Controls
        double leftX = xboxController.getX(Hand.kLeft);
        double leftY = -xboxController.getY(Hand.kLeft);

        leftX = Math.pow(leftX, LEFT_STICK_EXPONENT);
        leftY = Math.pow(leftY, LEFT_STICK_EXPONENT);

        drive.driveManual(leftX, leftY);

        // Process Rotation control
        double rightX = xboxController.getX(Hand.kRight);
        double rightY = -xboxController.getY(Hand.kRight);

        double rotationSpeed = Math.sqrt(Math.pow(rightX, 2.0) + Math.pow(rightY, 2.0));

        double angle = rightX >= 0 ? Math.atan2(rightY, rightX) : -Math.atan2(rightY, rightX);

        if (rotationSpeed > ROTATION_SPEED_THRESHOLD) {
            drive.lookAt(angle, rotationSpeed);
        }
        
        if (xboxController.getBumper(Hand.kRight)){
            roller.start();
        } else {
            roller.stop();
        }
    }
}
