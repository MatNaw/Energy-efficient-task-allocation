package org.cloudbus.cloudsim.examples.power;

import org.cloudbus.cloudsim.examples.mypower.AmplNetworkType;
import org.cloudbus.cloudsim.examples.mypower.HostSpecification;
import org.cloudbus.cloudsim.examples.mypower.VmSpecification;
import org.cloudbus.cloudsim.power.models.PowerModel;

/**
 * If you are using any algorithms, policies or workload included in the power package, please cite
 * the following paper:
 * <p>
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 *
 * @author Anton Beloglazov
 * @since Jan 6, 2012
 */
public class Constants {
    public final static boolean ENABLE_OUTPUT = true;
    public final static boolean OUTPUT_CSV = false;

    public final static double SCHEDULING_INTERVAL = 300;
    public final static double SIMULATION_LIMIT = 60 * 60;

    public final static int CLOUDLET_LENGTH = 25000 * (int) SIMULATION_LIMIT;
    public final static int CLOUDLET_PES = 1;

    // Change the value of 'TEST_HOST_SPECIFICATION' below to choose desired server type for the simulation
    public final static HostSpecification TEST_HOST_SPECIFICATION = HostSpecification.IbmX3550XeonX5675();
    public final static VmSpecification TEST_VM_SPECIFICATION = new VmSpecification(TEST_HOST_SPECIFICATION,
                                                                                    AmplNetworkType.BIG);

    /*
     * VM instance types:
     *   High-Memory Extra Large Instance: 3.25 EC2 Compute Units, 8.55 GB // too much MIPS
     *   High-CPU Medium Instance: 2.5 EC2 Compute Units, 0.85 GB
     *   Extra Large Instance: 2 EC2 Compute Units, 3.75 GB
     *   Small Instance: 1 EC2 Compute Unit, 1.7 GB
     *   Micro Instance: 0.5 EC2 Compute Unit, 0.633 GB
     *   We decrease the memory size two times to enable oversubscription
     */
    public final static int VM_TYPES = 1;
    public final static int[] VM_PES = {1};
    public final static int[] VM_RAM = {1};
    public final static int VM_BW = 1; // 100 Mbit/s
    public final static int VM_SIZE = 1; // 2.5 GB

    /**
     * To approximate the CloudSim experiments to my AMPL implementation, the `VM_MIPS` value must be set accordingly
     * to the simulation setup from AMPL test runs (which varies between the tests of small and big computer network).
     * To achieve comparable results, {@link VmSpecification} class has been created to calculate the proper MIPS value
     * for the VMs. For these very simulations, only `VM_MIPS` is relevant - <b>other VM parameters (PES, RAM, BW
     * and VM size) are not under the research!</b>
     */
    public final static int[] VM_MIPS = {TEST_VM_SPECIFICATION.getMips()};

    public final static int HOST_TYPES = 1;
    public final static int HOST_BW = 1000000; // 1 Gbit/s
    public final static int HOST_STORAGE = 1000000; // 1 GB
    public final static int[] HOST_PES = {TEST_HOST_SPECIFICATION.getPes()};
    public final static int[] HOST_RAM = {TEST_HOST_SPECIFICATION.getRam()};
    public final static PowerModel[] HOST_POWER = {TEST_HOST_SPECIFICATION.getPowerModel()};

    /**
     * To approximate the CloudSim experiments to my AMPL implementation, the host specification values
     * must be set accordingly to the test simulation setup from AMPL test runs (which varies between the tests of small
     * and big computer network). To achieve comparable results, {@link HostSpecification} class has been created to
     * easily switch between subsequent server types. For my personal test runs, only `HOST_MIPS` is relevant - <b>other
     * host parameters (PES, RAM and BW) are not under the research!</b> Please note that these parameters
     * (PES, RAM and BW) are part of {@link HostSpecification} class to just keep all parameters (related to real host
     * specifications) together.
     *
     * <p>
     * List of server/host types:
     * <li>HP ProLiant ML110 G3 (1 x [Pentium D930 3000 MHz, 2 cores], 4GB)</li>
     * <li>HP ProLiant ML110 G4 (1 x [Xeon 3040 1860 MHz, 2 cores], 4GB)</li>
     * <li>HP ProLiant ML110 G5 (1 x [Xeon 3075 2660 MHz, 2 cores], 4GB)</li>
     * <li>IBM server x3250 (1 x [Xeon X3470 2933 MHz, 4 cores], 8GB)</li>
     * <li>IBM server x3250 (1 x [Xeon X3480 3067 MHz, 4 cores], 8GB)</li>
     * <li>IBM server x3550 (2 x [Xeon X5670 2933 MHz, 6 cores], 12GB)</li>
     * <li>IBM server x3550 (2 x [Xeon X5675 3067 MHz, 6 cores], 16GB)</li>
     */
    public final static int[] HOST_MIPS = {TEST_HOST_SPECIFICATION.getMips()};
}
