import java.util.ArrayList;


public class Network {
	
	private ArrayList<Layer> layers = new ArrayList<Layer>();
	
	public Network(int layers, int input, int output, int hiddensize) {
		Layer last = new Layer(input, null);
		getLayers().add(last);
		for(int i = 0; i < layers; i++) {
			last = new Layer(hiddensize, last);
			getLayers().add(last);
		}
		getLayers().add(new Layer(output, last));
	}
	
	public void process(double[] input) {
		for(Neuron n : layers.get(0).getNeurons()) {
			 
		}
	}

	public ArrayList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}

}
