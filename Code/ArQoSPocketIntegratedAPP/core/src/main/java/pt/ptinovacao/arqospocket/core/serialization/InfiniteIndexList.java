package pt.ptinovacao.arqospocket.core.serialization;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Helper class to allow the task data and task result factories to easily ignore empty positions. This is a read only
 * list, no updates or transformations to the list are supported. If you need to transform the list use the original
 * wrapped list.
 *
 * @param <T> The type
 */
class InfiniteIndexList<T> implements List<T> {

    private List<T> wrappedList;

    private T defaultValue;

    private InfiniteIndexList(List<T> wrappedList, T defaultValue) {
        if (wrappedList == null) {
            throw new UnsupportedOperationException("Wrapped list cannot be null");
        }
        this.defaultValue = defaultValue;
        this.wrappedList = wrappedList;
    }

    /**
     * Creates a new instance of the {@link InfiniteIndexList} that wraps the provided {@link List}.
     *
     * @param toWrap the {@link List} to wrap.
     * @param defaultValue the value to return when the index does not match an existing position.
     * @param <T> the type of data contained in the list.
     * @return the created {@link InfiniteIndexList} instance.
     */
    static <T> List<T> fromList(List<T> toWrap, T defaultValue) {
        return new InfiniteIndexList<>(toWrap, defaultValue);
    }

    @Override
    public int size() {
        return wrappedList.size();
    }

    @Override
    public boolean isEmpty() {
        return wrappedList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return wrappedList.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @NonNull
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] a) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return wrappedList.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> c) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends T> c) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public T get(int index) {
        if (index >= wrappedList.size()) {
            return defaultValue;
        }
        return wrappedList.get(index);
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public int indexOf(Object o) {
        return wrappedList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return wrappedList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @NonNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Unsupported operation");
    }
}
