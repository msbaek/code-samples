package fpij;

@FunctionalInterface
public interface UseInstance<T, S extends Exception> {
	void accept(T instance) throws S;
}
