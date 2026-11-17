package dev.lrxh.neptune.feature.event;

import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.configs.impl.MessagesLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.game.match.MatchService;
import dev.lrxh.neptune.game.match.impl.participant.Participant;
import dev.lrxh.neptune.game.match.impl.participant.ParticipantColor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AutomatedEvent {
    private EventType type;
    private EventState state;
    private Kit kit;
    private List<UUID> participants;
    private UUID winner;
    private UUID starterUUID;

    // Bracket fields
    private List<UUID> remainingPlayers;
    private List<UUID> nextRoundPlayers;
    private int currentRound;
    private int activeMatches;

    public AutomatedEvent(EventType type, Kit kit) {
        this.type = type;
        this.state = EventState.WAITING;
        this.kit = kit;
        this.participants = new ArrayList<>();
        this.remainingPlayers = new ArrayList<>();
        this.nextRoundPlayers = new ArrayList<>();
        this.currentRound = 0;
        this.activeMatches = 0;
    }

    public boolean isEnded() {
        return state == EventState.ENDED;
    }

    public void nextRound(EventService service) {
        Collections.shuffle(remainingPlayers);
        int playersThisRound = remainingPlayers.size();
        nextRoundPlayers = new ArrayList<>();
        activeMatches = 0;

        // Odd player out gets a bye
        if (remainingPlayers.size() % 2 != 0) {
            UUID bye = remainingPlayers.remove(remainingPlayers.size() - 1);
            nextRoundPlayers.add(bye);
        }

        for (int i = 0; i < remainingPlayers.size() - 1; i += 2) {
            UUID uuidA = remainingPlayers.get(i);
            UUID uuidB = remainingPlayers.get(i + 1);

            Player playerA = Bukkit.getPlayer(uuidA);
            Player playerB = Bukkit.getPlayer(uuidB);

            if (playerA == null || playerB == null) {
                // skip disconnected players, advance the one who is online
                if (playerA != null) nextRoundPlayers.add(uuidA);
                if (playerB != null) nextRoundPlayers.add(uuidB);
                continue;
            }

            Participant pA = new Participant(playerA);
            Participant pB = new Participant(playerB);

            activeMatches++;
            final int round = currentRound;
            kit.getRandomArena().thenAccept(arena -> {
                if (arena == null) {
                    // no free arena, let both through instead of knocking them out
                    activeMatches--;
                    nextRoundPlayers.add(uuidA);
                    nextRoundPlayers.add(uuidB);
                    return;
                }
                Bukkit.getScheduler().runTask(Neptune.get(), () -> {
                    pA.setOpponent(pB);
                    pA.setColor(ParticipantColor.RED);
                    pB.setOpponent(pA);
                    pB.setColor(ParticipantColor.BLUE);
                    MatchService.get().startMatch(pA, pB, kit, arena, false, 1);
                });
            });
        }

        // nothing got started this round (everyone left or no arenas), move on so the event doesn't hang
        if (activeMatches <= 0) {
            if (nextRoundPlayers.size() <= 1) {
                service.endEvent(nextRoundPlayers.isEmpty() ? null : nextRoundPlayers.get(0));
            } else if (nextRoundPlayers.size() >= playersThisRound) {
                // nobody was eliminated, we'd just loop forever without arenas
                service.stopEvent();
            } else {
                remainingPlayers = new ArrayList<>(nextRoundPlayers);
                currentRound++;
                nextRound(service);
            }
            return;
        }

        // Broadcast round start
        for (UUID uuid : participants) {
            MessagesLocale.EVENT_STARTED.send(uuid,
                    Placeholder.unparsed("players", String.valueOf(remainingPlayers.size())));
        }
    }
}
