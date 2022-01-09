package org.cloudbus.cloudsim.examples.mypower;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.Helper;
import org.cloudbus.cloudsim.examples.power.RunnerAbstract;

import java.util.Calendar;

/**
 * The energy-efficient task runner simulating the workload in the datacenter.
 * <p>
 * It provides functionality similar to {@link org.cloudbus.cloudsim.examples.power.random.RandomRunner},
 * but makes a use of {@link MyPowerRunnerHelper}, which customizes the creation of cloudlets, VMs and hosts
 * (which originally is provided by {@link org.cloudbus.cloudsim.examples.power.random.RandomHelper}).
 * <p>
 * Citation of the relevant paper (as the code is based on the {@link org.cloudbus.cloudsim.examples.power.random.RandomRunner}
 * from '<b>power</b>' package):
 *
 * <ul>
 * <li><a href="http://dx.doi.org/10.1002/cpe.1867">Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012</a>
 * </ul>
 *
 * @author Mateusz Nawrot
 */
public class MyPowerRunner extends RunnerAbstract {
    /**
     * Instantiates a new MyPowerRunner.
     *
     * @param enableOutput                  enables/disables the output for logging the simulation steps
     * @param outputToFile                  enables/disables the redirection of the console output to the file
     * @param outputFolder                  output folder name
     * @param readableHostSpecificationName readable host specification name
     * @param vmAllocationPolicy            name of the allocation policy
     * @param vmSelectionPolicy             name of the selection policy
     */
    public MyPowerRunner(final boolean enableOutput,
                         final boolean outputToFile,
                         final String outputFolder,
                         final String readableHostSpecificationName,
                         final String vmAllocationPolicy,
                         final String vmSelectionPolicy) {
        super(enableOutput,
              outputToFile,
              "",
              outputFolder,
              readableHostSpecificationName,
              vmAllocationPolicy,
              vmSelectionPolicy,
              "");
    }

    @Override
    protected void init(final String inputFolder) {
        try {
            CloudSim.init(1, Calendar.getInstance(), false);

            broker = Helper.createBroker();
            final int brokerId = broker.getId();

            cloudletList = MyPowerRunnerHelper.createCloudletList(
                    brokerId,
                    MyPowerRunnerHelper.NUMBER_OF_VMS);
            vmList = Helper.createVmList(brokerId, cloudletList.size());
            hostList = Helper.createHostList(MyPowerRunnerHelper.NUMBER_OF_HOSTS);
        } catch (final Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
            System.exit(0);
        }
    }
}
