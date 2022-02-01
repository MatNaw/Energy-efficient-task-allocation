package org.cloudbus.cloudsim.examples.mypower.standardloadbalancing;

import lombok.Data;

@Data
public class ServerType {
    private final String typeName;
    private final double[] easCost;
    private final int[] operationsPerSecond;

    public static ServerType HpG3() {
        final double[] easCost = {0, 105, 112, 118, 125, 131, 137, 147, 153, 157, 164, 169};
        final int[] operationsPerSecond = {0, 0, 5350, 10574, 16060, 20925, 26287, 32041, 36786, 42111, 46820, 52303};

        return new ServerType("HpG3", easCost, operationsPerSecond);
    }

    public static ServerType HpG4() {
        final double[] easCost = {0, 86, 89.4, 92.6, 96, 99.5, 102, 106, 108, 112, 114, 117};
        final int[] operationsPerSecond = {0, 0, 5712, 10776, 16312, 22139, 26801, 33168, 37963, 44054, 49096, 54479};

        return new ServerType("HpG4", easCost, operationsPerSecond);
    }

    public static ServerType HpG5() {
        final double[] easCost = {0, 93.7, 97, 101, 105, 110, 116, 121, 125, 129, 133, 135};
        final int[] operationsPerSecond = {0, 0, 9995, 19617, 29715, 38981, 49267, 59625, 69212, 79692, 90167, 98472};

        return new ServerType("HpG5", easCost, operationsPerSecond);
    }

    public static ServerType IbmX3470() {
        final double[] easCost = {0, 41.6, 46.7, 52.3, 57.9, 65.4, 73, 80.7, 89.5, 99.6, 105, 113};
        final int[] operationsPerSecond = {0, 0, 31024, 62958, 93818, 126143, 156517, 190454, 220822, 252911, 283126, 313804};

        return new ServerType("IbmX3470", easCost, operationsPerSecond);
    }

    public static ServerType IbmX3480() {
        final double[] easCost = {0, 42.3, 46.7, 49.7, 55.4, 61.8, 69.3, 76.1, 87, 96.1, 106, 113};
        final int[] operationsPerSecond = {0, 0, 32518, 63715, 96931, 128583, 163897, 192424, 229041, 259744, 291998, 320730};

        return new ServerType("IbmX3480", easCost, operationsPerSecond);
    }

    public static ServerType IbmX5670() {
        final double[] easCost = {0, 66, 107, 120, 131, 143, 156, 173, 191, 211, 229, 247};
        final int[] operationsPerSecond = {0, 0, 92523, 183593, 274137, 366302, 458004, 553056, 641449, 735396, 825710, 912178};

        return new ServerType("IbmX5670", easCost, operationsPerSecond);
    }

    public static ServerType IbmX5675() {
        final double[] easCost = {0, 58.4, 98, 109, 118, 128, 140, 153, 170, 189, 205, 222};
        final int[] operationsPerSecond = {0, 0, 89918, 179398, 268151, 357958, 446975, 535435, 626127, 717822, 806317, 890651};

        return new ServerType("IbmX5675", easCost, operationsPerSecond);
    }
}
