# Project summary
This project was created as a part of my MSc thesis "Energy-efficient task allocation in cloud computing". The abstract of the thesis can be found at the bottom of this README file.

The project consists of two essential elements:

* Implementation of the energy-efficient task allocation algorithm (_**AMPL**_ directory):
  * The analysed problem is an example of mixed integer nonlinearly constrained optimization problems.
  * Therefore, the code was implemented in AMPL language and BARON solver was used to determine the solutions of this optimization problem in multiple test runs.
  * Popular platform which was used for solving the optimization problem: [NEOS Server](https://neos-server.org/neos/index.html)
* Source code of the CloudSim project (https://github.com/Cloudslab/cloudsim):
  * CloudSim version used in this project: **4.0** (Java 8)
  * CloudSim's source code was enhanced in order to run thesis-specific simulations, which imitated the data centers applying the proposed energy-efficient task allocation algorithm.
  * List of added or modified files/directories:
    * [cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower](https://github.com/MatNaw/Energy-efficient-task-allocation/tree/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower) package (added)
    * [cloudsim/src/main/java/org/cloudbus/cloudsim/mypower](https://github.com/MatNaw/Energy-efficient-task-allocation/tree/main/cloudsim-cloudsim-4.0/modules/cloudsim/src/main/java/org/cloudbus/cloudsim/mypower) package (added)
    * [cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/power/Constants.java](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/power/Constants.java) (modified)
    * [cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/power/RunnerAbstract.java](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/power/RunnerAbstract.java) (modified)
   * Additionally, simple load balancing behaviour was implemented within [cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower/standardloadbalancing](https://github.com/MatNaw/Energy-efficient-task-allocation/tree/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower/standardloadbalancing) subpackage.

# Usage
### Solving the energy-efficient task allocation problem
As mentioned before, the energy-efficient task allocation problem is an example of mixed integer nonlinearly constrained optimization problems. The code was implemented in AMPL, hence the BARON solver for models in AMPL format was used as well. You may find the proper solver setup in [NEOS Server](https://neos-server.org/neos/index.html) platform: https://neos-server.org/neos/solvers/minco:BARON/AMPL.html. Alternatively (if you have the paid or academic license), you may run the source code directly within the [AMPL IDE](https://ampl.com/products/ide/).

Project's AMPL source code is divided following the common AMPL pattern:
* data files
  * "small data center" ([power.dat](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/AMPL/power.dat))
  * "big data center" ([powerBigDC.dat](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/AMPL/powerBigDC.dat))
* model file ([power.mod](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/AMPL/power.mod))
* commands file ([power.run](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/AMPL/power.run))

Before you start solving the optimization problem, it's suggested to have a look at the data files and analyze the basic configuration of how the data centers are modelled. Example network model:
![](https://user-images.githubusercontent.com/26259511/159135765-54f7d051-0d1e-4809-9998-06fdfe9b1aaf.png)

### Running the simulations
Two main classes were implemented within this project:
* [MyPowerSimulation.java](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower/MyPowerSimulation.java)
* [StandardLoadBalancerRunner.java](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower/standardloadbalancing/StandardLoadBalancerRunner.java)

_**MyPowerSimulation**_ is meant to run the CloudSim simulations applying the energy-efficient task allocation behaviour within the simulated data center. Details about the simulation parameters are described in the Javadoc of the _**main**_ method.

The purpose of _**StandardLoadBalancerRunner**_ is to run the simple algorithm imitating the typical load balancing policy. This runner may be used for comparisons between the energy-efficient and load balancing algorithms. 

# Abstract of the MSc thesis "Energy-efficient task allocation in cloud computing"
"As part of the thesis, research was conducted to develop and implement an energy-efficient computation management system. By optimizing the task allocation process in computing clusters, it is possible to reduce the power consumption of the system. Attention was focused on an energy-efficient, while ensuring compliance with the requirements for quality of service, algorithm for the allocation of virtual machines to real processors. The solutions proposed in the literature were adapted, the optimal allocation task was formulated and a computational method for its solution was proposed. The main objective was to overload a part of the physical machines of the computing system, taking into account the available resources and guaranteeing the security of computations, and to switch off the remaining, unloaded servers. This solution results in cost reduction and lowers the level of negative impact on the environment.

In order to verify the correctness of the realized system of optimal allocation of computations and to assess its effectiveness, cloud computing simulators were built in the CloudSim environment. Data centers with a small and large number of computing nodes were considered. The performed experiments confirmed the high efficiency of the proposed solution."
