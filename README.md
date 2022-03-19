# Project summary
This project has been created as a part of my MSc thesis "Energy-efficient task allocation in cloud computing". The abstract of the thesis can be found at the bottom of this README file. The project consists of two essential elements:

* Implementation of the energy-efficient task allocation algorithm (_AMPL_ directory):
  * The analysed problem is an example of mixed integer nonlinearly constrained optimization problems.
  * Therefore, code was implemented in AMPL language and BARON solver was used to determine the solutions of this optimization problem.
  * Useful platform for solving the optimization problems: [NEOS Server](https://neos-server.org/neos/index.html)
* Source code of the CloudSim project (https://github.com/Cloudslab/cloudsim):
  * CloudSim's source code was enhanced in order to run thesis-specific simulations, which imitated the data centers applying the proposed energy-efficient task allocation algorithm.
  * Additionally, simple load balancing behaviour was implemented within [cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower/standardloadbalancing](https://github.com/MatNaw/Energy-efficient-task-allocation/tree/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower/standardloadbalancing) subpackage.
  * List of added or modified files/directories:
    * [cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower](https://github.com/MatNaw/Energy-efficient-task-allocation/tree/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/mypower) package (added)
    * [cloudsim/src/main/java/org/cloudbus/cloudsim/mypower](https://github.com/MatNaw/Energy-efficient-task-allocation/tree/main/cloudsim-cloudsim-4.0/modules/cloudsim/src/main/java/org/cloudbus/cloudsim/mypower) package (added)
    * [cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/power/Constants.java](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/power/Constants.java) (modified)
    * [cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/power/RunnerAbstract.java](https://github.com/MatNaw/Energy-efficient-task-allocation/blob/main/cloudsim-cloudsim-4.0/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/power/RunnerAbstract.java) (modified)
  * CloudSim version used in this project: **4.0**

# Abstract of the "Energy-efficient task allocation in cloud computing"
"As part of the thesis, research was conducted to develop and implement an energy-efficient computation management system. By optimizing the task allocation process in computing clusters, it is possible to reduce the power consumption of the system. Attention was focused on an energy-efficient, while ensuring compliance with the requirements for quality of service, algorithm for the allocation of virtual machines to real processors. The solutions proposed in the literature were adapted, the optimal allocation task was formulated and a computational method for its solution was proposed. The main objective was to overload a part of the physical machines of the computing system, taking into account the available resources and guaranteeing the security of computations, and to switch off the remaining, unloaded servers. This solution results in cost reduction and lowers the level of negative impact on the environment.

In order to verify the correctness of the realized system of optimal allocation of computations and to assess its effectiveness, cloud computing simulators were built in the CloudSim environment. Data centers with a small and large number of computing nodes were considered. The performed experiments confirmed the high efficiency of the proposed solution."
