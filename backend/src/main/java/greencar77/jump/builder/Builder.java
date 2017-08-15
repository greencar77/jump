package greencar77.jump.builder;

import java.lang.reflect.InvocationTargetException;

import greencar77.jump.model.Model;

public abstract class Builder<S, M extends Model> {
    private static final String SPEC_METHOD_PREFIX = "spec";
    
    private S spec;

    public Builder() {}

    public Builder(S spec) {
        this.spec = spec;
    }

    public abstract M build();
    
    @SuppressWarnings("unchecked")
    protected M generateModel(String specId) {
        String methodName = SPEC_METHOD_PREFIX + specId.substring(0, 1).toUpperCase() + specId.substring(1);
        Object o;
        try {
            o = this.getClass()
                    .getMethod(methodName)
                    .invoke(this);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        
        return (M) o;
    }
    
    protected S getSpec() {
        return spec;
    }
    
    protected void setSpec(S spec) {
        this.spec = spec;
    }
}
