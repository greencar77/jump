package greencar77.jump.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import greencar77.jump.model.Model;

public abstract class Builder<S, M extends Model> {
    public static final String SPEC_METHOD_PREFIX = "spec";
    
    private S spec;

    public Builder() {}

    public Builder(S spec) {
        this.spec = spec;
    }
    
    public M build() {
        System.out.println(spec.getClass().getSimpleName());
        System.out.println(this.getClass().getSimpleName());
        setupDefault();
        validate();
        return buildModel();
    }

    protected abstract M buildModel();

    protected void setupDefault() {}

    protected void validate() {
        if (spec == null) {
            throw new ValidationException("spec is null");
        }
    }
    
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
    
    protected void invoke(String methodName) {
        try {
            Method m = getMethod(this.getClass(), methodName);
            if (m == null) {
                m = getMethod(this.getClass().getSuperclass(), methodName);
            }
            m.setAccessible(true);
            System.out.println("Invoke: " + m.getName());
            m.invoke(this);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Method getMethod(Class<?> clazz, String methodName) {
        try {
            return clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (SecurityException e) {
            return null;
        }
    }
    
    protected S getSpec() {
        return spec;
    }
    
    protected void setSpec(S spec) {
        this.spec = spec;
    }
}
