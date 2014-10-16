import java.util.HashMap;


public class Neuron {
	
	HashMap<Neuron, Double> weights = new HashMap<Neuron, Double>();
	Layer layer;
	
	public Neuron(Layer l) {
		layer = l;
		if(layer.getPrevious() != null) {
			for(Neuron n : layer.getPrevious().getNeurons()) {
				weights.put(n, (Math.random() - .5) * 2);
			}
		}
	}
}
