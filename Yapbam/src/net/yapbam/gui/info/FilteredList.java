package net.yapbam.gui.info;

import java.util.Iterator;
import java.util.List;

public abstract class FilteredList<T> implements Iterable<T> {
	protected List<T> physical;
	
	public FilteredList(List<T> news) {
		this.physical = news;
	}
	
	public int getPhysicalSize() {
		return physical.size();
	}
	
	public int size() {
		int result = 0;
		for (T element : physical) {
			if (isOk(element)) {
				result++;
			}
		}
		return result;
	}
	
	protected abstract boolean isOk(T element);
	
	public T get(int index) {
		int count = -1;
		for (T element : physical) {
			if (isOk(element)) {
				count++;
				if (count==index) {
					return element;
				}
			}
		}
		throw new ArrayIndexOutOfBoundsException(index);
	}
	
	public boolean isEmpty() {
		for (T info : physical) {
			if (isOk(info)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int index=0;
			@Override
			public boolean hasNext() {
				for (int i = index; i < physical.size(); i++) {
					if (isOk(physical.get(i))) {
						return true;
					}
				}
				return false;
			}

			@Override
			public T next() {
				T result = get(index);
				index++;
				return result;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
