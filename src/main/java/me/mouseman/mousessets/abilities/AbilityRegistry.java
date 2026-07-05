package me.mouseman.mousessets.abilities;

import me.mouseman.mousessets.definitions.AbilityDefinition;
import me.mouseman.mousessets.definitions.RegisteredAbility;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AbilityRegistry {

    private final JavaPlugin plugin;

    /*
     * Java implementations
     */
    private final Map<String, Ability> implementations = new HashMap<>();

    /*
     * Fully linked abilities
     */
    private final Map<String, RegisteredAbility> registry = new HashMap<>();

    public AbilityRegistry(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Ability ability) {

        String id = ability.getId().toLowerCase();

        if (implementations.containsKey(id)) {
            throw new IllegalStateException(
                    "Duplicate ability ID: " + id
            );
        }

        implementations.put(id, ability);
    }

    public void build(Map<String, AbilityDefinition> definitions) {

        registry.clear();

        for (AbilityDefinition definition : definitions.values()) {

            Ability implementation =
                    implementations.get(definition.getId().toLowerCase());

            if (implementation == null) {

                plugin.getLogger().warning(
                        "No Java implementation for ability '" +
                        definition.getId() + "'"
                );

                continue;
            }

            if (registry.containsKey(definition.getId().toLowerCase())) {
                plugin.getLogger().warning(
                        "Duplicate ability definition: " + definition.getId()
                );
                continue;
            }

            registry.put(
                    definition.getId().toLowerCase(),
                    new RegisteredAbility(
                            implementation,
                            definition
                    )
            );
        }

        /*
        * Warn about Java abilities
        * that aren't in abilities.yml
        */
        for (Ability ability : implementations.values()) {

            if (!registry.containsKey(
                    ability.getId().toLowerCase())) {

                plugin.getLogger().warning(
                        "Ability '" +
                        ability.getId() +
                        "' has no definition in abilities.yml"
                );
            }
        }
    }
    public RegisteredAbility get(String id) {

        return registry.get(id.toLowerCase());
    }
    public boolean contains(String id) {

        return registry.containsKey(id.toLowerCase());
    }

    public Collection<RegisteredAbility> getAbilities() {

        return registry.values();
    }

    public AbilityDefinition getDefinition(String id) {

        RegisteredAbility ability = get(id);

        return ability == null ? null : ability.getDefinition();
    }

    public Ability getImplementation(String id) {

        RegisteredAbility ability = get(id);

        return ability == null ? null : ability.getImplementation();
    }
}