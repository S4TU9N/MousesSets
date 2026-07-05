package me.mouseman.mousessets.definitions;

import me.mouseman.mousessets.abilities.Ability;

public class RegisteredAbility {

    private final Ability implementation;
    private final AbilityDefinition definition;

    public RegisteredAbility(
            Ability implementation,
            AbilityDefinition definition
    ) {
        this.implementation = implementation;
        this.definition = definition;
    }

    public Ability getImplementation() {
        return implementation;
    }

    public AbilityDefinition getDefinition() {
        return definition;
    }
}