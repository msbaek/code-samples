public class MyStack {
	private int STACK_SIZE = 100;
	private int [] element = new int[STACK_SIZE];
	private int index = 0;

	public void push(int element) {
		this.element[index++] = element;
	}

	public int pop() {
		return element[--index];
	}
}
