package org.cloudbus.cloudsim.examples.mypower;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.examples.power.Constants;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The main class running the simulation of the energy-efficient task allocation in cloud computing.
 * Please check out the JavaDoc of the '<b>main</b>' to see the description of simulation's parameters.
 *
 * @author Mateusz Nawrot
 */
public class MyPowerSimulation {
    /**
     * The '<b>main</b>' method running the simulation. It specifies the simulation parameters, logs them in the console
     * output and starts the simulation runner.
     * <p>
     * <br/><br/>
     * Simulations parameters defined inside the '<b>main</b>' method:
     * <ul>
     *     <li><b>enableOutput</b> - enables/disables the output for logging the simulation steps</li>
     *     <li><b>outputToFile</b> - enables/disables the redirection of the console output to the file. File name is
     *                               undergoing the following pattern name:
     *                                  '<b><i>readableHostSpecificationName_vmAllocationPolicy_vmSelectionPolicy</i></b>'
     *                               <br/>Example file name:
     *                                  '<b><i>IbmX3550XeonX5675_MyPowerAllocation_MyPowerSelection.txt</i></b>'
     *                               <br/>Check the parameter values in the method block to determine the final file name.
     *                               This file will be put under path:
     *                                  '<b><i>project_root/outputFolder/log/</i></b>'</li>
     *     <li><b>outputFolder</b> - output folder name, which will contain the log files</li>
     *     <li><b>vmAllocationPolicy</b> - name of the allocation policy, which will be used during the simulation</li>
     *     <li><b>vmSelectionPolicy</b> - name of the selection policy, which will be used during the simulation</li>
     * </ul>
     * <p>
     * Simulation parameters defined outside the '<b>main</b>' method, but crucial for the simulation process:
     * <ul>
     *     <li><b>NUMBER_OF_HOSTS</b> - number of hosts to be created in the datacenter during the simulation
     *                                  (variable defined in {@link MyPowerRunnerHelper#NUMBER_OF_HOSTS})</li>
     *     <li><b>NUMBER_OF_VMS</b> - number of VMs to be created in the datacenter during the simulation
     *                                (variable defined in {@link MyPowerRunnerHelper#NUMBER_OF_VMS})</li>
     *     <li><b>TEST_HOST_SPECIFICATION</b> - host specification class, representing the type of the servers used
     *                                          during the simulation
     *                                          (variable defined in {@link Constants#TEST_HOST_SPECIFICATION})</li>
     *     <li><b>TEST_VM_SPECIFICATION</b> - VM specification class, representing the type of the VMs used during
     *                                        the simulation
     *                                        (variable defined in {@link Constants#TEST_VM_SPECIFICATION})</li>
     * </ul>
     *
     * @param args program arguments
     * @author Mateusz Nawrot
     */
    public static void main(final String[] args) {
        final boolean enableOutput = true;
        final boolean outputToFile = true;
        final String outputFolder = "MyPowerResults";
        final String vmAllocationPolicy = "MyPowerAllocation";
        final String vmSelectionPolicy = "MyPowerSelection";

        final String hostSpecificationName = Constants.TEST_HOST_SPECIFICATION.getPowerModel()
                .getClass()
                .getSimpleName();
        final String readableHostSpecificationName = Arrays.stream(hostSpecificationName.split("(?=\\p{Upper})"))
                .filter(word -> !(Arrays.asList("Power", "Model", "Spec", "Power").contains(word)))
                .collect(Collectors.joining(""));

        final String simulationInfo = String.format(
                "Running %s for host specification type: %s",
                MyPowerSimulation.class.getSimpleName(),
                readableHostSpecificationName);
        final String simulationParameters = String.format(
                "Simulation parameters: [networkType = %s, numberOfHosts = %s, numberOfVMs = %s, hostSpecificationName = %s, vmMips = %s]",
                Constants.TEST_VM_SPECIFICATION.getAmplNetworkType(),
                MyPowerRunnerHelper.NUMBER_OF_HOSTS,
                MyPowerRunnerHelper.NUMBER_OF_VMS,
                readableHostSpecificationName,
                Constants.TEST_VM_SPECIFICATION.getMips());
        Log.printLine(simulationInfo);
        Log.printLine(simulationParameters);

        new MyPowerRunner(enableOutput,
                          outputToFile,
                          outputFolder,
                          readableHostSpecificationName,
                          vmAllocationPolicy,
                          vmSelectionPolicy);
    }
}
