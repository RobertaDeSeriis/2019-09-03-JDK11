package it.polito.tdp.food.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	FoodDao dao; 
	List<String> portionN;
	List<Portion> vertici;
	List<Adiacenza> archi;
	Graph<Portion, DefaultWeightedEdge> grafo; 
	Map<String, Portion> idMap;
	
	public Model() {
		this.dao= new FoodDao(); 
	}

	public List<String> getPortionN() {
		return dao.listAllPortions();
	}
	
	
	public String creaGrafo(int n) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap= new HashMap<>(); 
		vertici=dao.listVertici(n, idMap);
		archi= dao.listArchi(idMap);
		Graphs.addAllVertices(this.grafo, vertici);
		
		
		for (Adiacenza a: archi) {
			if(grafo.containsVertex(a.p1) && grafo.containsVertex(a.p2)) {
				Graphs.addEdge(this.grafo, a.p1, a.p2, a.peso);
			}
		}
		
		return "Grafo creato!\n# Vertici:"+ grafo.vertexSet().size()+"\n#Archi: "+grafo.edgeSet().size();
	}
	
	
}
