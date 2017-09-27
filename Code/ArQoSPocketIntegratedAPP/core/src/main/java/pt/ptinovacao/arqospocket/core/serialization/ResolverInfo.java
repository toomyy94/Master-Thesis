package pt.ptinovacao.arqospocket.core.serialization;

/**
 * Helper class to be used internally by the resolver to get an executable task from task data.
 * <p>
 * Created by Emílio Simões on 11-04-2017.
 */
public class ResolverInfo {

    private final String type;

    private final String subType;

    public ResolverInfo(String type) {
        this(type, null);
    }

    public ResolverInfo(String type, String subType) {
        this.type = type;
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }
}
