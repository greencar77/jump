package greencar77.jump.builder;

import greencar77.jump.model.Model;

public interface Predefined<M extends Model> {
    M build(String specId);
}
