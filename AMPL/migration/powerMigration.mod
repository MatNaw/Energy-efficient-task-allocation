# Author: Mateusz Nawrot

################## Declarations ##################
set SERVERS ordered;
set SERVERS_EAS;
set LINKS;
set LINKS_EAS;
set ROUTERS;
# set DEMANDS;
set VMS;

param old_server_in_eas {s in SERVERS, se in SERVERS_EAS};
param server_input {l in LINKS, s in SERVERS};
param server_output {l in LINKS, s in SERVERS};
param server_efficiency_in_eas {s in SERVERS, se in SERVERS_EAS};
param server_energetic_cost {s in SERVERS, se in SERVERS_EAS};
param old_server_costs = sum {s in SERVERS, se in SERVERS_EAS} (server_energetic_cost[s,se] * old_server_in_eas[s,se]);

param link_capacity {l in LINKS, le in LINKS_EAS};
param link_energetic_cost {l in LINKS, le in LINKS_EAS};

param router_input {l in LINKS, r in ROUTERS};
param router_output {l in LINKS, r in ROUTERS};
param router_usage_cost {r in ROUTERS};

# param demand_tasks_used;
# param demand_tasks {d in DEMANDS};
# param demand_tasks_source_nodes {d in DEMANDS};
# param demand_tasks_destination_nodes {d in DEMANDS};
# param demand_tasks_route_length {d in DEMANDS};

param vm_required_efficiency {v in VMS};
param vm_migration_required_link_capacity {v in VMS};
param vm_server_assignment {v in VMS, s in SERVERS};

################## Variables ##################
var server_in_eas {s in SERVERS, se in SERVERS_EAS} binary;
var link_in_eas {l in LINKS, le in LINKS_EAS} binary;
var router_used {r in ROUTERS} binary;
# var demand_in_link {d in DEMANDS, l in LINKS} binary;
# var demand_in_link_sum {d in DEMANDS} = sum {l in LINKS} (demand_in_link[d,l]);
# var vm_in_link {v in VMS, l in LINKS} binary;
param vm_in_link {v in VMS, l in LINKS};
var vm_in_server {v in VMS, s in SERVERS} binary;



################## Subjects ##################
######### Constraints for binary variables #########
# EAS state for servers and links must be unique
subject to unique_server_state {s in SERVERS}: sum {se in SERVERS_EAS} server_in_eas[s,se] == 1;
subject to unique_link_state {l in LINKS}: sum {le in LINKS_EAS} link_in_eas[l,le] == 1;

# Demand-to-link assignment must exist
# subject to unique_demand_in_link {d in DEMANDS}: #sum {l in LINKS} demand_in_link[d,l] >= 1;
# demand_tasks_used > 0 ==> sum {l in LINKS} demand_in_link[d,l] >= 1;

# VM-to-link assignment must exist ....
# subject to unique_vm_in_link {v in VMS, s in SERVERS}: 
	# sum {l in LINKS} vm_in_link[v,l] >= 1;
	# vm_server_assignment[v,s] == 1 ==> vm_in_server[v,s] == 1 else sum {l in LINKS} vm_in_link[v,l] >= 1;

# VM-to-server assignment must be unique
subject to unique_vm_in_server {v in VMS}: sum {s in SERVERS} vm_in_server[v,s] == 1;
# OR: (?)
# subject to vm_in_server_compliance:
# 	sum {v in VMS, s in SERVERS} (vm_server_assignment[v,s] - vm_in_server[v,s]) == 0;

######### Servers and VMs bindings #########
# Server efficiency must not be exceeded by running VMs
subject to server_efficiency_not_exceeded {s in SERVERS}:
	sum {v in VMS} (vm_required_efficiency[v] * vm_in_server[v,s]) <= sum {se in SERVERS_EAS} (server_efficiency_in_eas[s,se] * server_in_eas[s,se]);

# Each VM must be fully covered by assigned servers
subject to vm_efficiency_fulfilled {v in VMS}:
	vm_required_efficiency[v] <= sum {s in SERVERS, se in SERVERS_EAS} (server_efficiency_in_eas[s,se] * server_in_eas[s,se] * vm_in_server[v,s]);


######### Links and demands bindings #########
# # Link capacity must not be exceeded by processed demands
# subject to link_capacity_not_exceeded {l in LINKS}:
	# demand_tasks_used > 0 ==>
	# sum {d in DEMANDS} (demand_tasks[d] * demand_in_link[d,l]) <= sum {le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le]);

# Link capacity must not be exceeded by processed demands
subject to link_capacity_not_exceeded {l in LINKS}:
	sum {v in VMS} (vm_migration_required_link_capacity[v] * vm_in_link[v,l]) <= sum {le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le]);

subject to vm_migration_fulfilled {v in VMS, l in LINKS}:
	vm_in_link[v,l] == 1 ==> 
	vm_migration_required_link_capacity[v] <= sum {le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le] * vm_in_link[v,l]);

# # Each demand must be fully covered by assigned links
# subject to demand_fulfilled {d in DEMANDS, l in LINKS}:
# 	if (demand_tasks_used > 0)
# 		# && (server_output[l,member(demand_tasks_source_nodes[d], SERVERS)] == 1
# 		# || server_input[l,member(demand_tasks_destination_nodes[d], SERVERS)] == 1)
# 		# && demand_in_link_sum[d] != demand_tasks_route_length[d])
# 	then demand_tasks[d] <= sum {le in LINKS_EAS} (link_capacity[l,le] * link_in_eas[l,le] * demand_in_link[d,l]);

# We need to block the assignment of the demands to other possible links if other valid connection already covers it:
# subject to demand_fulfilled_2 {d in DEMANDS}:
# 	demand_tasks_used > 0 ==> demand_in_link_sum[d] == demand_tasks_route_length[d];


######### Flow rules for servers and routers #########
# subject to servers_flow_rule {d in DEMANDS, s in SERVERS}:
# 	demand_tasks_used > 0 ==>
# 	sum {l in LINKS} ((server_output[l,s] - server_input[l,s]) * demand_in_link[d,l]) == 
# 		sum {v in VMS} (vm_server_assignment[v,s] - vm_in_server[v,s]);

### concepts
# hardcoded 'vm_in_link' -> no effect on 'vm_in_server' (equal to 'vm_server_assignment')
# subject to servers_flow_rule {v in VMS, s in SERVERS}:
	# vm_server_assignment[v,s] == 1 ==>
	# 	sum {l in LINKS} (server_output[l,s] * vm_server_assignment[v,s] / 2) == vm_in_server[v,s] + sum {l in LINKS} (server_input[l,s] * vm_in_link[v,l] / 2);

# hardcoded 'vm_in_link' -> with effect on 'vm_in_server' (copy of 'vm_server_assignment', but with all proper VMs moved)
subject to servers_flow_rule {v in VMS, s in SERVERS}:
	sum {l in LINKS} ((server_output[l,s] - server_input[l,s]) * vm_in_link[v,l]) == vm_server_assignment[v,s] - vm_in_server[v,s];


subject to routers_flow_rule {v in VMS, r in ROUTERS}:
	sum {l in LINKS} (router_output[l,r] * vm_in_link[v,l]) - sum {l in LINKS} (router_input[l,r] * vm_in_link[v,l]) == 0;

######### Constraints for routers #########
subject to check_if_switch_input_used {v in VMS, r in ROUTERS}: 
	sum {l in LINKS} (router_input[l,r] * vm_in_link[v,l]) <= router_used[r];
subject to check_if_switch_output_used {v in VMS, r in ROUTERS}:
	sum {l in LINKS} (router_output[l,r] * vm_in_link[v,l]) <= router_used[r];


################## Objective function ##################
var server_costs = sum {s in SERVERS, se in SERVERS_EAS} (server_energetic_cost[s,se] * server_in_eas[s,se]);
var link_costs = sum {l in LINKS, le in LINKS_EAS} (link_energetic_cost[l,le] * link_in_eas[l,le]);
var router_costs = sum {r in ROUTERS} (router_usage_cost[r] * router_used[r]);
var total_cost = server_costs + link_costs + router_costs;

# subject to test_total_costs_upper_bound:
# 	total_cost <= old_server_costs;
# subject to test_total_costs_lower_bound:
# 	total_cost >= 1.0;
# subject to test_server_costs_lower_bound:
# 	server_costs >= 1.0;

# subject to test_vm_in_link:
# 	total_cost >= old_server_costs ==> sum {v in VMS, l in LINKS} vm_in_link[v,l] >= 1;

minimize TotalCost: total_cost;
minimize ServerCosts: server_costs;