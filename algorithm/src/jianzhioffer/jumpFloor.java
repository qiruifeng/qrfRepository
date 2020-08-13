package jianzhioffer;

public class jumpFloor {
    public int JumpFloor(int target) {
        if (target==1){
            return 1;
        }

        if (target==2){
            return 2;
        }

        return 2*JumpFloor(target-2)+JumpFloor(target-1);
    }
}
