# Author: Mateusz Nawrot

################## Declarations ##################
set SERVERS ordered;
set SERVERS_EAS;
set VMS;

param server_efficiency_in_eas {s in SERVERS, se in SERVERS_EAS};
param server_energetic_cost {s in SERVERS, se in SERVERS_EAS};

param vm_required_efficiency {v in VMS};

################## Variables ##################
var server_in_eas {s in SERVERS, se in SERVERS_EAS} binary;
var vm_in_server {v in VMS, s in SERVERS} binary;

################## Subjects ##################
######### Constraints for binary variables #########
# EAS state for servers and links must be unique
subject to unique_server_state {s in SERVERS}: sum {se in SERVERS_EAS} server_in_eas[s,se] == 1;

# VM-to-server assignment must be unique
subject to unique_vm_in_server {v in VMS}: sum {s in SERVERS} vm_in_server[v,s] == 1;

######### Servers and VMs bindings #########
# Server efficiency must not be exceeded by running VMs
subject to server_efficiency_not_exceeded {s in SERVERS}:
	sum {v in VMS} (vm_required_efficiency[v] * vm_in_server[v,s]) <= sum {se in SERVERS_EAS} (server_efficiency_in_eas[s,se] * server_in_eas[s,se]);

# Each VM must be fully covered by assigned servers
subject to vm_efficiency_fulfilled {v in VMS}:
	vm_required_efficiency[v] <= sum {s in SERVERS, se in SERVERS_EAS} (server_efficiency_in_eas[s,se] * server_in_eas[s,se] * vm_in_server[v,s]);

################## Objective function ##################
var server_costs = sum {s in SERVERS, se in SERVERS_EAS} (server_energetic_cost[s,se] * server_in_eas[s,se]);

minimize ServerCosts: server_costs;