package org.genedb.top.chado.bulk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractIterator<E> implements Iterator<E> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractIterator.class);

    /* Implement the Java Iterator semantics in terms of the simpler getNext().
     * If hasNext() is called, we fetch the next element and store it in the
     * <code>current</code> member. This is then returned the next time <code>next()</code>
     * is called.
     */
    private E current = null;

    @Override
    public boolean hasNext() {
        if (current != null) {
            return true;
        }
        current = getNextAndLogErrors();
        return (current != null);
    }

    @Override
    public E next() {
        if (current != null) {
            E ret = current;
            current = null;
            return ret;
        }

        E ret = getNextAndLogErrors();
        if (ret == null) {
            closeIfNecessary();
            throw new NoSuchElementException();
        }
        return ret;
    }

    private E getNextAndLogErrors() {
        for(;;) {
            try {
                return getNext();
            }
            catch (DataIntegrityViolation e) {
                logger.error(e);
            }
            catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
    }

    /**
     * Get the next element.
     *
     * @return the next element if there is one, or <code>null</code> if not.
     */
    public abstract E getNext() throws SQLException, DataIntegrityViolation;

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private boolean closed = false;
    private void closeIfNecessary() {
        if (!closed) {
            try {
                close();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
            closed = true;
        }
    }

    public abstract void close() throws SQLException;
}
