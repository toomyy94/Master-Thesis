package pt.ptinovacao.arqospocket.core.serialization.resolvers;

/**
 * Composed {@link InstanceResolver} that allows to get a "sub" {@link InstanceResolver}.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public abstract class SubInstanceResolver implements InstanceResolver {

    /**
     * Gets the {@link InstanceResolver} "sub-instance".
     *
     * @param subType the sub type identity.
     * @return the {@link InstanceResolver} "sub-instance".
     */
    public abstract InstanceResolver resolver(String subType);

    protected static InstanceResolver throwUnsupportedSubType(String subType, String type) {
        throw new UnsupportedOperationException(
                "Task sub type " + subType + " is not a supported sub type of task type " + type);
    }
}
