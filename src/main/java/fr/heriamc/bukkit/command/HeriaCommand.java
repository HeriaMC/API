package fr.heriamc.bukkit.command;

import fr.heriamc.api.user.rank.HeriaRank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeriaCommand {

    String name();

    HeriaRank power() default HeriaRank.ADMIN;

    String noPerm() default "§6§lHeriaMC §8┃ §cAction non autorisée.";

    String[] aliases() default {};

    String description() default "";

    String usage() default "";

    boolean inGameOnly() default false;
}
