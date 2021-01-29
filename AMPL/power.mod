# Author: Mateusz Nawrot

######### Declarations #########
set SERVERS ordered;
set INPUT_SERVERS;
set OUTPUT_SERVERS;
set SERVER_EAS;
set VMS;
set VMS_DEMAND;
set SWITCHES;
set LINKS;
set LINK_EAS;

# Directed links
param switch_output {e in LINKS, r in SWITCHES};
param switch_input {e in LINKS, r in SWITCHES};
param server_output {e in LINKS, n in SERVERS};
param server_input {e in LINKS, n in SERVERS};

param network_traffic_demand {d in VMS_DEMAND};
param source_nodes {d in VMS_DEMAND};
param destination_nodes {d in VMS_DEMAND};
param server_efficiency_in_eas {n in SERVERS, q in SERVER_EAS};
param vm_required_efficiency {m in VMS};

param server_energetic_cost {n in SERVERS, q in SERVER_EAS};
param link_capacity {e in LINKS, k in LINK_EAS};
param link_energetic_cost {e in LINKS, k in LINK_EAS};
param const_router_cost {r in SWITCHES};

######### Variables #########
var vm_in_server {m in VMS, n in SERVERS} binary;
var server_in_eas {n in SERVERS, q in SERVER_EAS} binary;
var demand_in_link {d in VMS_DEMAND, e in LINKS} binary;
var link_eas_used {e in LINKS, k in LINK_EAS} binary;
var switch_used {r in SWITCHES} binary;

######### Subjects #########
# Directed links binary definition
subject to switch_output_binary_1 {e in LINKS, r in SWITCHES}:
			switch_output[e,r] >=0;
subject to switch_output_binary_2 {e in LINKS, r in SWITCHES}:
			switch_output[e,r] <=1;

subject to switch_input_binary_1 {e in LINKS, r in SWITCHES}:
			switch_input[e,r] >=0;
subject to switch_input_binary_2 {e in LINKS, r in SWITCHES}:
			switch_input[e,r] <=1;

subject to server_output_binary_1 {e in LINKS, n in SERVERS}:
			server_output[e,n] >=0;
subject to server_output_binary_2 {e in LINKS, n in SERVERS}:
			server_output[e,n] <=1;

subject to server_input_binary_1 {e in LINKS, n in SERVERS}:
			server_input[e,n] >=0;
subject to server_input_binary_2 {e in LINKS, n in SERVERS}:
			server_input[e,n] <=1;

#1
subject to max_capacity {e in LINKS}: sum {k in LINK_EAS} link_eas_used[e,k] <= 1;
#2
subject to unique_server_state {n in SERVERS}: sum {q in SERVER_EAS} server_in_eas[n,q] <= 1;
#3
subject to sum_vms_efficiency {n in SERVERS}:
			sum {m in VMS} (vm_required_efficiency[m] * vm_in_server[m,n]) <= 
				sum {q in SERVER_EAS} (server_energetic_cost[n,q] * server_in_eas[n,q]);
#4
subject to one_vm_per_server {m in VMS}: sum {n in SERVERS} vm_in_server[m,n] = 1;
#5
subject to check_if_switch_used_1 {d in VMS_DEMAND, r in SWITCHES}: 
			sum {e in LINKS} (switch_output[e,r] * demand_in_link[d,e]) <= switch_used[r];
#6
subject to check_if_switch_used_2 {d in VMS_DEMAND, r in SWITCHES}:
			sum {e in LINKS} (switch_input[e,r] * demand_in_link[d,e]) <= switch_used[r];

#7a
#subject to server_flow_rule_output {d in VMS_DEMAND, m in VMS, n in SERVERS}:
#			sum {e in LINKS} (server_output[e,n] * demand_in_link[d,e]) = vm_in_server[m,member(source_nodes[d], SERVERS)];
#7b
#subject to server_flow_rule_input {d in VMS_DEMAND, m in VMS, n in SERVERS}:
#			sum {e in LINKS} (server_input[e,n] * demand_in_link[d,e]) = vm_in_server[m,member(destination_nodes[d], SERVERS)];

#7
subject to server_flow_rule {d in VMS_DEMAND, m in VMS, n in SERVERS}:
			sum {e in LINKS} (server_output[e,n] * demand_in_link[d,e] - server_input[e,n] * demand_in_link[d,e]) = 0;
#					vm_in_server[m,member(source_nodes[d], SERVERS)] - vm_in_server[m,member(destination_nodes[d], SERVERS)];
#8
subject to switch_flow_rule {d in VMS_DEMAND, r in SWITCHES}:
			sum {e in LINKS} (switch_output[e,r] * demand_in_link[d,e]) - sum {e in LINKS} (switch_input[e,r] * demand_in_link[d,e]) = 0;
#9
subject to demand_sum_limit {e in LINKS}:
			sum {d in VMS_DEMAND} (network_traffic_demand[d] * demand_in_link[d,e]) <= sum {k in LINK_EAS} (link_capacity[e,k] * link_eas_used[e,k]);

######### Objective function #########
# Server costs
var server_costs = sum {n in SERVERS, q in SERVER_EAS} (server_energetic_cost[n,q] * server_in_eas[n,q]);
# Link costs
var link_costs = sum {e in LINKS, k in LINK_EAS} (link_energetic_cost[e,k] * link_eas_used[e,k]);
# Router costs
var router_costs = sum {r in SWITCHES} (const_router_cost[r] * switch_used[r]);

var total_cost = server_costs + link_costs + router_costs;

#maximize TotalCost: total_cost;
minimize TotalCost: total_cost;