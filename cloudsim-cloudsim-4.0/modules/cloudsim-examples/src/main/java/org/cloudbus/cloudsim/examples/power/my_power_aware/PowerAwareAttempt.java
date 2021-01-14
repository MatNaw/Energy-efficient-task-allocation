package org.cloudbus.cloudsim.examples.power.my_power_aware;

import org.cloudbus.cloudsim.examples.power.random.MadMmt;
import org.cloudbus.cloudsim.examples.power.random.RandomRunner;

import java.io.IOException;
import java.net.URL;

public class PowerAwareAttempt {
    /**
     * The main method.
     * <p>
     * Based on:
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     * @see MadMmt
     */
    public static void main(final String[] args) throws IOException {
        final boolean enableOutput = true;
        final boolean outputToFile = false;

        final URL url = MadMmt.class.getClassLoader().getResource("workload/my_power_aware");
        String inputFolder = "";
        if (url != null) {
            inputFolder = url.getPath();
        }

        final String outputFolder = "";
        final String workload = "20110303"; // workload copied from planetlab
        final String vmAllocationPolicy = "mad"; // Median Absolute Deviation (MAD) VM allocation policy
        final String vmSelectionPolicy = "mmt"; // Minimum Migration Time (MMT) VM selection policy
        final String parameter = "2.5"; // the safety parameter of the MAD policy

        new RandomRunner(
                enableOutput,
                outputToFile,
                inputFolder,
                outputFolder,
                workload,
                vmAllocationPolicy,
                vmSelectionPolicy,
                parameter);
    }
}
