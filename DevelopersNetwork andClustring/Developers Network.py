#!/usr/bin/env python
# coding: utf-8

# In[2]:


# Developers simple network graph
import networkx as nx
import matplotlib.pyplot as plt

class Link:
    def __init__(self, _source, _target, _value):
        self.source = _source
        self.target = _target
        self.value  = _value


links = []
with open("PartnerDevelopersCount.txt", encoding="utf-8") as fp:
    for line in fp:
        line_elements = line.split(" ")
        link = Link(line_elements[0], line_elements[1], line_elements[2])
        links.append(link)

my_graph = nx.Graph()
for i in links:
    my_graph.add_edge(i.source, i.target, weight=int(i.value))
    

plt.figure(figsize = (5, 5))
plt.savefig("DevelopersNetwork.png")
nx.draw_circular(my_graph, with_labels=True, font_weight='bold', node_color="blue")


# In[4]:


import networkx as nx 
  
G = nx.read_weighted_edgelist('PartnerDevelopersCount.txt', delimiter =" ") 
population = { 
        'Vadim' : 628,
'Aarni' : 15,
'Umar' : 2,
'Alexander' : 11,
'Konstantin' : 2,
'Justin' : 1,
'Robert' : 48,
'Eli' : 1,
'Maxim' : 12,
'egor' : 3,
'MÃ¡ximo' : 2,
'Marcelo' : 1,
'Andrew' : 15,
'hnarasaki' : 1,
'Jim' : 1,
'Adam' : 1,
'Santiago' : 1,
'moncho' : 2,
'Jeff' : 1
 } 


# In[5]:


for i in list(G.nodes()): 
    G.nodes[i]['population'] = population[i] 
  
nx.draw_networkx(G, with_label = True) 


# In[6]:


# fixing the size of the figure 
plt.figure(figsize =(10, 7)) 
  
node_color = [G.degree(v) for v in G] 
# node colour is a list of degrees of nodes 
  
node_size = [10 * nx.get_node_attributes(G, 'population')[v] for v in G] 
# size of node is a list of population of cities 
  
edge_width = [0.4 * G[u][v]['weight'] for u, v in G.edges()] 
# width of edge is a list of weight of edges 
  
nx.draw_networkx(G, node_size = node_size,  
                 node_color = node_color, alpha = 0.7, 
                 with_labels = True, width = edge_width, 
                 edge_color ='.5', cmap = plt.cm.Blues) 
  
plt.axis('off') 
plt.tight_layout(); 
plt.savefig("developersNetworkwithWeighted.png")


# In[10]:



print("Random Layout:") 
  
node_color = [G.degree(v) for v in G] 
node_size = [10 * nx.get_node_attributes(G, 'population')[v] for v in G] 
edge_width = [0.4 * G[u][v]['weight'] for u, v in G.edges()] 
  
plt.figure(figsize =(10, 9)) 
pos = nx.random_layout(G) 

  
# demonstrating random layout 
nx.draw_networkx(G, pos, node_size = node_size,  
                 node_color = node_color, alpha = 0.7,  
                 with_labels = True, width = edge_width, 
                 edge_color ='.4', cmap = plt.cm.Blues) 
  
  
plt.figure(figsize =(10, 9)) 
pos = nx.circular_layout(G) 
print("Circular Layout:") 
  
# demonstrating circular layout 
nx.draw_networkx(G, pos, node_size = node_size,  
                 node_color = node_color, alpha = 0.7,  
                 with_labels = True, width = edge_width,  
                 edge_color ='.4', cmap = plt.cm.Blues) 
plt.savefig("DevelopersWeightedCircular.png")


# In[14]:


#colored 
import networkx as nx
G_fb = nx.read_edgelist('partnerDevelopers.txt', create_using = nx.Graph(), nodetype=str)
pos = nx.spring_layout(G_fb)
betCent = nx.betweenness_centrality(G_fb, normalized=True, endpoints=True)
node_color = [100 * G_fb.degree(v) for v in G_fb]
node_size =  [v * 10000 for v in betCent.values()]
plt.figure(figsize=(20,20))
nx.draw_networkx(G_fb, pos=pos, with_labels=True,
                 node_color=node_color,
                 node_size=node_size )

plt.savefig("DevelopersColored.png")


# In[ ]:




