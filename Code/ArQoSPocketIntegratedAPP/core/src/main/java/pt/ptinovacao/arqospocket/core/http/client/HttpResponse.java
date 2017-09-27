package pt.ptinovacao.arqospocket.core.http.client;

/**
 * Received response from the remote service.
 *
 * Created by Emílio Simões on 11-05-2017.
 */
public abstract class HttpResponse<T> {

    private final T entity;

    private final int code;

    protected HttpResponse(T entity, int code) {
        this.entity = entity;
        this.code = code;
    }

    public T getEntity() {
        return entity;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return code >= 200 && code <= 299;
    }
}
