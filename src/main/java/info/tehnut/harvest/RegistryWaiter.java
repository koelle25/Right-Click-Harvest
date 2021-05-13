// Daomephsta's registry listener
// https://gist.github.com/Daomephsta/c5c0ff50a5112bbefe01661da768c61e

package info.tehnut.harvest;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;

public class RegistryWaiter<T> implements RegistryEntryAddedCallback<T>
{
    private final Map<Identifier, T> waitingFor;
    private final T registryDefault;
    private final Consumer<Function<Identifier, T>> onObjectsAvailable;
    private final BiConsumer<Identifier, T> onObjectReplaced;

    private RegistryWaiter(Map<Identifier, T> waitingFor, T registryDefault, Consumer<Function<Identifier, T>> onObjectsAvailable, BiConsumer<Identifier, T> onObjectReplaced)
    {
        this.waitingFor = waitingFor;
        this.registryDefault = registryDefault;
        this.onObjectsAvailable = onObjectsAvailable;
        this.onObjectReplaced = onObjectReplaced;
    }

    /**
     * Waits in a non-blocking event-driven manner until {@code registry} contains values for all ids in {@code waitFor}
     * @param <T> type of registry object handled
     * @param onObjectsAvailable called when all specified registry objects are available
     * @param onObjectReplaced called when any specified registry objects is replaced. Recieves the id and new registry object.
     * @param registry the registry to watch
     * @param waitFor ids of registry objects to wait for
     * <br>
     * <pre>
     * Identifier barId = new Identifier("foo_mod:bar_block");
     * RegistryWaiter.waitFor(blocks ->
     * {
     *     Block barBlock = blocks.apply(barId);
     *     // Do stuff with barBlock
     * },
     * (id, replacement) ->
     * {
     *     // Update whatever was done with barBlock
     * },
     * Registry.BLOCK, barId);
     * <pre>
     */
    public static<T> void waitFor(Consumer<Function<Identifier, T>> onObjectsAvailable, BiConsumer<Identifier, T> onObjectReplaced, Registry<T> registry, Identifier... waitFor)
    {
        T registryDefault = registry instanceof DefaultedRegistry ? registry.get(((DefaultedRegistry<?>) registry).getDefaultId()) : null;
        Map<Identifier, T> initialState = Arrays.stream(waitFor).collect(toMap(
                id -> id,
                id -> registry.getOrEmpty(id).orElse(registryDefault),
                (u, v) -> {throw new IllegalArgumentException("Duplicate value " + v);},
                HashMap::new));
        RegistryWaiter<T> waiter = new RegistryWaiter<>(initialState, registryDefault, onObjectsAvailable, onObjectReplaced);
        // Register callback only if there are objects to wait for
        if (!waiter.tryExecuteAction())
            RegistryEntryAddedCallback.event(registry).register(waiter);
    }

    @Override
    public void onEntryAdded(int rawId, Identifier id, T object)
    {
        if (waitingFor.containsKey(id))
        {
            T oldValue = waitingFor.get(id);
            waitingFor.put(id, object);
            // Replacement
            if (oldValue != registryDefault)
                onObjectReplaced.accept(id, object);
                // Initial value
            else
                tryExecuteAction();
        }
    }

    /**
     * Executes the action if all registry objects are available
     * @return true if the action was executed
     */
    private boolean tryExecuteAction()
    {
        if (!waitingFor.values().contains(registryDefault))
        {
            onObjectsAvailable.accept(waitingFor::get);
            return true;
        }
        return false;
    }
}
