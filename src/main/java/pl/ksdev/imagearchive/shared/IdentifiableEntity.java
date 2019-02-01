package pl.ksdev.imagearchive.shared;

import java.util.UUID;

public interface IdentifiableEntity {

    Long getId();

    UUID getUuid();
}
