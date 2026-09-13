package dev.lrxh.neptune.game.kit.editor;

import dev.lrxh.neptune.game.kit.Kit;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EditorSession {
    private final UUID playerId;
    private final Kit kit;
    private final long openedAt;

    public EditorSession(UUID playerId, Kit kit) {
        this.playerId = playerId;
        this.kit = kit;
        this.openedAt = System.currentTimeMillis();
    }
}
