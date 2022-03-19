# Author: Mateusz Nawrot

################## Declarations ##################
set SERVERS ordered;
set SERVERS_EAS;
set LINKS ordered;
set LINKS_EAS;
set ROUTERS;
set DEMANDS ordered;
set VMS ordered;

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

param vm_required_efficiency {v in VMS};
param vm_demand_source {v in VMS, d in DEMANDS};
param vm_demand_target {v in VMS, d in DEMANDS};



################## Variables ##################
var server_in_eas {s in SERVERS, se in SERVERS_EAS} binary;
var link_in_eas {l in LINKS, le in LINKS_EAS} binary;
var router_used {r in ROUTERS} binary;
var demand_in_link {d in DEMANDS, l in LINKS} binary;
var vm_in_server {v in VMS, s in SERVERS} binary;



################## Subjects ##################
######### Constraints for binary variables #########
# EAS state for servers and links must be unique
subject to unique_server_state {s in SERVERS}: sum {se in SERVERS_EAS} server_in_eas[s,se] == 1;
subject to unique_link_state {l in LINKS}: sum {le in LINKS_EAS} link_in_eas[l,le] == 1;

# VM-to-server assignment must be unique
subject to unique_vm_in_server {v in VMS}: sum {s in SERVERS} vm_in_server[v,s] == 1;


######### Servers and VMs bindings #########
# Server efficiency must not be exceeded by running VMs
subject to server_efficiency_not_exceeded {s in SERVERS}:
	sum {v in VMS} (vm_required_efficiency[v] * vm_in_server[v,s])
		<= sum {se in SERVERS_EAS} (server_efficiency_in_eas[s,se] * server_in_eas[s,se]);

# Each VM must be fully covered by assigned servers
subject to vm_efficiency_fulfilled {v in VMS}:
	vm_required_efficiency[v] <= sum {s in SERVERS, se in SERVERS_EAS}
		(server_efficiency_in_eas[s,se] * server_in_eas[s,se] * vm_in_server[v,s]);


######### Links and demands bindings #########
# Link capacity must not be exceeded by processed demands for source and target VMs
subject to link_capacity_not_exceeded_source_link {l in LINKS, s in SERVERS, vs in VMS, vd in VMS}:
	# sum {d in DEMANDS} (demand_tasks[d] * vm_demand_source[vs,d] * vm_demand_target[vd,d] * demand_in_link[d,l])
	# 	<= sum {le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le]);
	sum {d in DEMANDS} (demand_tasks[d] * vm_demand_source[vs,d] * vm_demand_target[vd,d] * demand_in_link[d,l] * server_output[l,s])
		<= sum {le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le]);
subject to link_capacity_not_exceeded_target_link {l in LINKS, s in SERVERS, vs in VMS, vd in VMS}:
	sum {d in DEMANDS} (demand_tasks[d] * vm_demand_source[vs,d] * vm_demand_target[vd,d] * demand_in_link[d,l] * server_input[l,s])
		<= sum {le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le]);

# Each demand must be fully covered by assigned links for source and target VMs
subject to demand_fulfilled_source_vm {d in DEMANDS, s in SERVERS, vs in VMS, vd in VMS}:
	demand_tasks[d] * vm_demand_source[vs,d] * vm_demand_target[vd,d] * (vm_in_server[vs,s] - vm_in_server[vd,s])
		<= sum {l in LINKS, le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le] * demand_in_link[d,l] * server_output[l,s]);
subject to demand_fulfilled_target_vm {d in DEMANDS, s in SERVERS, vs in VMS, vd in VMS}:
	demand_tasks[d] * vm_demand_source[vs,d] * vm_demand_target[vd,d] * (vm_in_server[vd,s] - vm_in_server[vs,s])
		<= sum {l in LINKS, le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le] * demand_in_link[d,l] * server_input[l,s]);


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

subject to min_total_cost: total_cost >= 1;

minimize TotalCost: total_cost;
minimize ServerCosts: server_costs;