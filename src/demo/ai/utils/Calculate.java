package demo.ai.utils;


public class Calculate {
    public Calculate() {

    }


    public static float[][] sigmoid(float[][] A, float[][] g) throws Exception {
        //A : [m][1], g : [m][1]
        //g=1/1+e^-A
        int rows = A.length;
        for (int i = 0; i < rows; i++) {
            g[i][0] = 1 / (float) (1 + Math.exp(-A[i][0]));
        }
        return g;
    }
}
