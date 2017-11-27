package brianslho.gyrotest;

/**
Class for processing and storing data to be passed on to Arduino (via Bluetooth)
 */

public class OutputSignal {
    public float straight = 0;     // Positive is forwards, negative is backwards
    public float turn = 0;         // Positive is right, negative is left
    final static float ratio = 10; // Ratio for translating to rotor power <<<Adjust
    private float adj_straight = adjust_straight(straight);
    private float adj_turn = adjust_turn(turn);
    private float leftWheel = (straight + turn) * ratio;
    private float rightWheel = (straight - turn) * ratio;
    public int modeTrigger = 2;    // 0 when light trig., 1 when sound trig., 2 when not trig.

    //Adjust the range of straight values
    private float adjust_straight(float straight){
        float max_straight = (float)0.4;    // <<<Adjust
        float min_straight = (float)-0.4;   // <<<Adjust
        if(straight > max_straight){
            return max_straight;
        } else if (straight < min_straight){
            return min_straight;
        }
        return straight;
    }

    //Adjust the range of turn values
    private float adjust_turn(float turn){
        float max_turn = (float)0.32;   // <<<Adjust
        float min_turn = (float)-0.32;  // <<<Adjust
        if(turn > max_turn){
            return max_turn;
        } else if (turn < min_turn){
            return min_turn;
        }
        return turn;
    }
}
