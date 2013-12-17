package info.kwarc.sally.jedit.mmt;

public class ResultListWrapper {
	ResultInstance [] result;
	int size;

	public int getSize() {
		return size;
	}

	public ResultListWrapper() {
	}

	public ResultInstance[] getResult() {
		return result;
	}
	
	public void setResult(ResultInstance[] result) {
		this.result = result;
	}
}
