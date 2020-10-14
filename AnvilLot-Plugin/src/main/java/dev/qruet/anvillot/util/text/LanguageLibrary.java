package dev.qruet.anvillot.util.text;

public enum LanguageLibrary {

    PREFIX("&e〶&r "),
    CHECK_VERSION("Checking Server Version..."),
    HOOK_VERSION("Hooking into version %sv"),
    UNSUPPORTED_VERSION("Your server version, %sv, is not supported by this plugin!"),
    CRITICAL_ERROR("A critical error just occurred, please report the following as soon as possible!"),
    CONFIG_ERROR("An error just occurred while loading the config. Consider deleting the config and letting a new one generate."),
    LOADING_CONFIG("Deserializing config data..."),
    INITIALIZATION("Instantiating a few things..."),
    THANKYOU("Thank you for installing AnvilLot! If you're experiencing any issues please report it (see config for details)"),
    SUCCESS_FINAL("╼&m-----------&r Successfully Loaded &m-----------╾"),
    SHUTDOWN("Disabling..."),
    SUCCESS("Success!"),

    START_HEADER(T.center(P.R("&8&m-----------&r AnvilLot %pv &8&m-----------╾"))),
    FOOTER(T.center("&8&m------------------------------------╾")),
    COMMAND_NO_PERMISSION(" &4&l| " + PREFIX + "&cYou do not have permission to do that."),
    COMMAND_RELOAD(" &2&l| " + PREFIX + "&aSuccessfully reloaded configuration in %tms!"),
    COMMAND_INVALID_ENTITY(" &4&l| " + PREFIX + "&cYou must be a player to run that command!"),

    RESOURCE_LINK("https://www.spigotmc.org/resources/anvillot-%E2%9C%AA-a-limitless-forge-experience-%E2%9C%AA.62905/"),
    DONATE_LINK("https://www.paypal.com/paypalme/qruet"),
    DISCORD_LINK("https://discord.com/invite/fx9gm7T");

    private final String lang;
    LanguageLibrary(final String lang) {
        this.lang = T.C(lang);
    }

    @Override
    public String toString() {
        return lang;
    }

}
