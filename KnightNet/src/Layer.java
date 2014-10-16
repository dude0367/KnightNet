import java.util.ArrayList;


public class Layer {
	
	private ArrayList<Neuron> neurons = new ArrayList<Neuron>();
	private Layer previous;
	private Layer next;
	
	public Layer(int size, Layer last) {
		setPrevious(last);
		if(last != null) {
			last.setNext(this);
		}
		for(int i = 0; i < size; i++) {
			Neuron n = new Neuron(this);
			neurons.add(n);
		}
	}
	
	public void feedforward() {
		if(next == null) return;
	}

	public Layer getPrevious() {
		return previous;
	}

	public void setPrevious(Layer previous) {
		this.previous = previous;
	}

	public Layer getNext() {
		return next;
	}

	public void setNext(Layer next) {
		this.next = next;
	}

	public ArrayList<Neuron> getNeurons() {
		return neurons;
	}

	public void setNeurons(ArrayList<Neuron> neurons) {
		this.neurons = neurons;
	}

}
