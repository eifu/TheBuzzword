package data;


import java.util.ArrayList;

public class MatrixData {
    ArrayList<Character> letters;
    int width;
    int height;
    int timeLimit;

    public MatrixData(ArrayList<Character> data){
        this.letters = data;
    }

    public static boolean isAdjacent(int x1, int y1, int x2, int y2){
        if ((x1 - x2) * ( x1 - x2) + (y1 - y2) * (y1 - y2) <= 2){
            return true;
        }

        return false;
    }

}
