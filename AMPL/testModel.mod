# Author: Mateusz Nawrot

################## Declarations ##################
set SERVERS ordered;
set SERVERS_EAS;
set LINKS;
set LINKS_EAS;
set ROUTERS;
set DEMANDS;
set VMS;

param server_input {l in LINKS, s in SERVERS};
param server_output {l in LINKS, s in SERVERS};
param server_efficiency_in_eas {s in SERVERS, se in SERVERS_EAS};
param server_energetic_cost {s in SERVERS, se in SERVERS_EAS};

param link_capacity {l in LINKS, le in LINKS_EAS};
param link_energetic_cost {l in LINKS, le in LINKS_EAS};

param router_input {l in LINKS, r in ROUTERS};
param router_output {l in LINKS, r in ROUTERS};
param router_usage_cost {r in ROUTERS};

param demand_tasks {d in DEMANDS};
param demand_tasks_source_nodes {d in DEMANDS};
param demand_tasks_destination_nodes {d in DEMANDS};

param vm_required_efficiency {v in VMS};

################## Variables ##################
var server_in_eas {s in SERVERS, se in SERVERS_EAS} binary;
var link_in_eas {l in LINKS, le in LINKS_EAS} binary;
var router_used {r in ROUTERS} binary;
var demand_in_link {d in DEMANDS, l in LINKS} binary;
var vm_in_server {v in VMS, s in SERVERS} binary;



################## Subjects ##################
######### Unique values of binary variables #########
# EAS state for servers and links must be unique
subject to unique_server_state {s in SERVERS}: sum {se in SERVERS_EAS} server_in_eas[s,se] = 1; # >= 1
subject to unique_link_state {l in LINKS}: sum {le in LINKS_EAS} link_in_eas[l,le] = 1;			# >= 1

# Demand-to-link assignment must be unique
subject to unique_demand_in_link {d in DEMANDS}: sum {l in LINKS} demand_in_link[d,l] = 1;		# >= 1

# VM-to-server assignment must be unique
subject to unique_vm_in_server {v in VMS}: sum {s in SERVERS} vm_in_server[v,s] = 1;			# >= 1


######### Servers and VMs bindings #########
# Server efficiency must not be exceeded by running VMs
subject to server_efficiency_not_exceeded {s in SERVERS}:
	sum {v in VMS} (vm_required_efficiency[v] * vm_in_server[v,s]) <= sum {se in SERVERS_EAS} (server_efficiency_in_eas[s,se] * server_in_eas[s,se]);

# Each VM must be fully covered by assigned servers
subject to vm_efficiency_fulfilled {v in VMS}: vm_required_efficiency[v] <= sum {s in SERVERS, se in SERVERS_EAS} (server_efficiency_in_eas[s,se] * vm_in_server[v,s]);


######### Links and demands bindings #########
# Link capacity must not be exceeded by processed demands
subject to link_capacity_not_exceeded {l in LINKS}:
	sum {d in DEMANDS} (demand_tasks[d] * demand_in_link[d,l]) <= sum {le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le]);

# Each demand must be fully covered by assigned links
subject to demand_fulfilled {d in DEMANDS}: demand_tasks[d] <= sum {l in LINKS, le in LINKS_EAS} (link_capacity[l,le] * demand_in_link[d,l]);
#### This idea:
#subject to demand_fulfilled {d in DEMANDS, le in LINKS_EAS}: demand_tasks[d] <= sum {l in LINKS} (link_capacity[l,le] * demand_in_link[d,l]);
#### is rather NOT working


######### Flow rules for servers and routers #########
#subject to servers_flow_rule {d in DEMANDS, v in VMS, s in SERVERS}:
#	sum {l in LINKS} (server_output[l,s] * demand_in_link[d,l] - server_input[l,s] * demand_in_link[d,l]) = 
#				vm_in_server[v,member(demand_tasks_source_nodes[d], SERVERS)] - vm_in_server[v,member(demand_tasks_destination_nodes[d], SERVERS)];
subject to routers_flow_rule {d in DEMANDS, r in ROUTERS}:
	sum {l in LINKS} (router_output[l,r] * demand_in_link[d,l]) - sum {l in LINKS} (router_input[l,r] * demand_in_link[d,l]) = 0;

######### Constraints for routers #########
subject to check_if_switch_input_used {d in DEMANDS, r in ROUTERS}: 
	sum {l in LINKS} (router_input[l,r] * demand_in_link[d,l]) <= router_used[r];
subject to check_if_switch_output_used {d in DEMANDS, r in ROUTERS}:
	sum {l in LINKS} (router_output[l,r] * demand_in_link[d,l]) <= router_used[r];



################## Objective function ##################
var server_costs = sum {s in SERVERS, se in SERVERS_EAS} (server_energetic_cost[s,se] * server_in_eas[s,se]);
var link_costs = sum {l in LINKS, le in LINKS_EAS} (link_energetic_cost[l,le] * link_in_eas[l,le]);
var router_costs = sum {r in ROUTERS} (router_usage_cost[r] * router_used[r]);
var total_cost = server_costs + link_costs + router_costs;

minimize TotalCost: total_cost;