public class MyStack {
	private final int STACK_SIZE = 100;
	private int [] elements = new int [STACK_SIZE];
	private int size = 0;

	public void push(int element) {
		this.elements[size++] = element;
	}

	public int pop() {
		return this.elements[--size];
	}
}
