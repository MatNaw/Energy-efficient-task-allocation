package org.cloudbus.cloudsim.examples.power;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.mypower.MyPowerVmAllocationPolicyMigration;
import org.cloudbus.cloudsim.mypower.MyPowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationInterQuartileRange;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationLocalRegression;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationLocalRegressionRobust;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationMedianAbsoluteDeviation;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationStaticThreshold;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMaximumCorrelation;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumMigrationTime;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumUtilization;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyRandomSelection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The Class RunnerAbstract.
 * <p>
 * If you are using any algorithms, policies or workload included in the power package, please cite
 * the following paper:
 * <p>
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 *
 * @author Anton Beloglazov
 */
public abstract class RunnerAbstract {

    /**
     * The enable output.
     */
    private static boolean enableOutput;

    /**
     * The broker.
     */
    protected static DatacenterBroker broker;

    /**
     * The cloudlet list.
     */
    protected static List<Cloudlet> cloudletList;

    /**
     * The vm list.
     */
    protected static List<Vm> vmList;

    /**
     * The host list.
     */
    protected static List<PowerHost> hostList;

    /**
     * Run.
     *
     * @param enableOutput       the enable output
     * @param outputToFile       the output to file
     * @param inputFolder        the input folder
     * @param outputFolder       the output folder
     * @param workload           the workload
     * @param vmAllocationPolicy the vm allocation policy
     * @param vmSelectionPolicy  the vm selection policy
     * @param parameter          the parameter
     */
    public RunnerAbstract(
            final boolean enableOutput,
            final boolean outputToFile,
            final String inputFolder,
            final String outputFolder,
            final String workload,
            final String vmAllocationPolicy,
            final String vmSelectionPolicy,
            final String parameter) {
        try {
            initLogOutput(
                    enableOutput,
                    outputToFile,
                    outputFolder,
                    workload,
                    vmAllocationPolicy,
                    vmSelectionPolicy,
                    parameter);
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        init(inputFolder + "/" + workload);
        start(
                getExperimentName(workload, vmAllocationPolicy, vmSelectionPolicy, parameter),
                outputFolder,
                getVmAllocationPolicy(vmAllocationPolicy, vmSelectionPolicy, parameter));
    }

    /**
     * Inits the log output.
     *
     * @param enableOutput       the enable output
     * @param outputToFile       the output to file
     * @param outputFolder       the output folder
     * @param workload           the workload
     * @param vmAllocationPolicy the vm allocation policy
     * @param vmSelectionPolicy  the vm selection policy
     * @param parameter          the parameter
     * @throws IOException           Signals that an I/O exception has occurred.
     * @throws FileNotFoundException the file not found exception
     */
    protected void initLogOutput(
            final boolean enableOutput,
            final boolean outputToFile,
            final String outputFolder,
            final String workload,
            final String vmAllocationPolicy,
            final String vmSelectionPolicy,
            final String parameter) throws IOException, FileNotFoundException {
        setEnableOutput(enableOutput);
        Log.setDisabled(!isEnableOutput());
        if (isEnableOutput() && outputToFile) {
            final File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }

            final File folder2 = new File(outputFolder + "/log");
            if (!folder2.exists()) {
                folder2.mkdir();
            }

            final File file = new File(outputFolder + "/log/"
                    + getExperimentName(workload, vmAllocationPolicy, vmSelectionPolicy, parameter) + ".txt");
            file.createNewFile();
            Log.setOutput(new FileOutputStream(file));
        }
    }

    /**
     * Inits the simulation.
     *
     * @param inputFolder the input folder
     */
    protected abstract void init(String inputFolder);

    /**
     * Starts the simulation.
     *
     * @param experimentName     the experiment name
     * @param outputFolder       the output folder
     * @param vmAllocationPolicy the vm allocation policy
     */
    protected void start(final String experimentName, final String outputFolder, final VmAllocationPolicy vmAllocationPolicy) {
        System.out.println("Starting " + experimentName);

        try {
            final PowerDatacenter datacenter = (PowerDatacenter) Helper.createDatacenter(
                    "Datacenter",
                    PowerDatacenter.class,
                    hostList,
                    vmAllocationPolicy);

            datacenter.setDisableMigrations(false);

            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);

            CloudSim.terminateSimulation(Constants.SIMULATION_LIMIT);
            final double lastClock = CloudSim.startSimulation();

            final List<Cloudlet> newList = broker.getCloudletReceivedList();
            Log.printLine("Received " + newList.size() + " cloudlets");

            CloudSim.stopSimulation();

            Helper.printResults(
                    datacenter,
                    vmList,
                    lastClock,
                    experimentName,
                    Constants.OUTPUT_CSV,
                    outputFolder);

        } catch (final Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
            System.exit(0);
        }

        Log.printLine("Finished " + experimentName);
    }

    /**
     * Gets the experiment name.
     *
     * @param args the args
     * @return the experiment name
     */
    protected String getExperimentName(final String... args) {
        final StringBuilder experimentName = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (args[i].isEmpty()) {
                continue;
            }
            if (i != 0) {
                experimentName.append("_");
            }
            experimentName.append(args[i]);
        }
        return experimentName.toString();
    }

    /**
     * Gets the vm allocation policy.
     *
     * @param vmAllocationPolicyName the vm allocation policy name
     * @param vmSelectionPolicyName  the vm selection policy name
     * @param parameterName          the parameter name
     * @return the vm allocation policy
     */
    protected VmAllocationPolicy getVmAllocationPolicy(
            final String vmAllocationPolicyName,
            final String vmSelectionPolicyName,
            final String parameterName) {
        VmAllocationPolicy vmAllocationPolicy = null;
        PowerVmSelectionPolicy vmSelectionPolicy = null;
        if (!vmSelectionPolicyName.isEmpty()) {
            vmSelectionPolicy = getVmSelectionPolicy(vmSelectionPolicyName);
        }
        double parameter = 0;
        if (!parameterName.isEmpty()) {
            parameter = Double.parseDouble(parameterName);
        }
        switch (vmAllocationPolicyName) {
            case "MyPowerAllocation":
                vmAllocationPolicy = new MyPowerVmAllocationPolicyMigration(hostList, vmSelectionPolicy);
                break;
            case "iqr": {
                final PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                        hostList,
                        vmSelectionPolicy,
                        0.7);
                vmAllocationPolicy = new PowerVmAllocationPolicyMigrationInterQuartileRange(
                        hostList,
                        vmSelectionPolicy,
                        parameter,
                        fallbackVmSelectionPolicy);
                break;
            }
            case "mad": {
                final PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                        hostList,
                        vmSelectionPolicy,
                        0.7);
                vmAllocationPolicy = new PowerVmAllocationPolicyMigrationMedianAbsoluteDeviation(
                        hostList,
                        vmSelectionPolicy,
                        parameter,
                        fallbackVmSelectionPolicy);
                break;
            }
            case "lr": {
                final PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                        hostList,
                        vmSelectionPolicy,
                        0.7);
                vmAllocationPolicy = new PowerVmAllocationPolicyMigrationLocalRegression(
                        hostList,
                        vmSelectionPolicy,
                        parameter,
                        Constants.SCHEDULING_INTERVAL,
                        fallbackVmSelectionPolicy);
                break;
            }
            case "lrr": {
                final PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                        hostList,
                        vmSelectionPolicy,
                        0.7);
                vmAllocationPolicy = new PowerVmAllocationPolicyMigrationLocalRegressionRobust(
                        hostList,
                        vmSelectionPolicy,
                        parameter,
                        Constants.SCHEDULING_INTERVAL,
                        fallbackVmSelectionPolicy);
                break;
            }
            case "thr":
                vmAllocationPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                        hostList,
                        vmSelectionPolicy,
                        parameter);
                break;
            case "dvfs":
                vmAllocationPolicy = new PowerVmAllocationPolicySimple(hostList);
                break;
            default:
                System.out.println("Unknown VM allocation policy: " + vmAllocationPolicyName);
                System.exit(0);
        }
        return vmAllocationPolicy;
    }

    /**
     * Gets the vm selection policy.
     *
     * @param vmSelectionPolicyName the vm selection policy name
     * @return the vm selection policy
     */
    protected PowerVmSelectionPolicy getVmSelectionPolicy(final String vmSelectionPolicyName) {
        PowerVmSelectionPolicy vmSelectionPolicy = null;
        switch (vmSelectionPolicyName) {
            case "MyPowerSelection":
                vmSelectionPolicy = new MyPowerVmSelectionPolicy();
                break;
            case "mc":
                vmSelectionPolicy = new PowerVmSelectionPolicyMaximumCorrelation(
                        new PowerVmSelectionPolicyMinimumMigrationTime());
                break;
            case "mmt":
                vmSelectionPolicy = new PowerVmSelectionPolicyMinimumMigrationTime();
                break;
            case "mu":
                vmSelectionPolicy = new PowerVmSelectionPolicyMinimumUtilization();
                break;
            case "rs":
                vmSelectionPolicy = new PowerVmSelectionPolicyRandomSelection();
                break;
            default:
                System.out.println("Unknown VM selection policy: " + vmSelectionPolicyName);
                System.exit(0);
        }
        return vmSelectionPolicy;
    }

    /**
     * Sets the enable output.
     *
     * @param enableOutput the new enable output
     */
    public void setEnableOutput(final boolean enableOutput) {
        RunnerAbstract.enableOutput = enableOutput;
    }

    /**
     * Checks if is enable output.
     *
     * @return true, if is enable output
     */
    public boolean isEnableOutput() {
        return enableOutput;
    }

}
