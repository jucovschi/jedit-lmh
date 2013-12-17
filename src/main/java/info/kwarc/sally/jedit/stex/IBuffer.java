package info.kwarc.sally.jedit.stex;

public interface IBuffer {
	void select(int offsetBegin, int length);
	void replace(int offsetBegin, int length, String newText);
}
