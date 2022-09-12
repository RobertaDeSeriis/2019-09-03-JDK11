package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
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
	List<Portion> migliore;
	
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

	public List<Portion> getVertici() {
		return vertici;
	}
	
	public List<Adiacenza> getVicini(Portion e) {
		List<Adiacenza> vicini= new ArrayList<>();
		List<Portion> viciniId= Graphs.neighborListOf(this.grafo, e);
		
		for(Portion e1: viciniId) {
			vicini.add(new Adiacenza(e, e1,this.grafo.getEdgeWeight(grafo.getEdge(e1, e))));
		}
		return vicini;
	}
	
	public List<Portion> calcolaPercorso(Portion sorg, int p)
	{
		migliore = new LinkedList<Portion>();
		List<Portion> parziale = new LinkedList<>();
		parziale.add(sorg);
		cercaRicorsiva(parziale,p);
		return migliore;
	}

	private void cercaRicorsiva(List<Portion> parziale, int p) {
		 
				//condizione di terminazione
				if(parziale.size()==p)
				{
					int pesoParziale = pesoTot(parziale);
					if(pesoParziale > pesoTot(migliore))//la strada piú lunga é la migliore
					{
						migliore = new LinkedList<>(parziale);
					}
					return;
				}
				
				for(Portion v:Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) //scorro sui vicini dell'ultimo nodo sulla lista
				{
					if(!parziale.contains(v))
					{
						parziale.add(v);
						cercaRicorsiva(parziale,p);
						parziale.remove(parziale.size()-1);
					}
					
				}
		
	}

	private int pesoTot(List<Portion> parziale) {
		
		int peso = 0;
		
		for(int i=0; i<parziale.size()-1; i++) {
			peso+= grafo.getEdgeWeight(grafo.getEdge(parziale.get(i), parziale.get(i+1)));
		}
		return peso;
	}
	
}
