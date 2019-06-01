package me.geekles.repaircost.utils.text;

public enum LanguageLibrary {

    PREFIX("&e〶&r "),
    START_HEADER("╼&m-----------&r MaxRepairCost %pv &m-----------╾"),
    CHECK_VERSION("Checking Server Version..."),
    HOOK_VERSION("Hooking into version %sv"),
    UNSUPPORTED_VERSION("Your server version, %sv, is not supported by this plugin!"),
    CRITICAL_ERROR("A critical error just occurred, please report the following as soon as possible!"),
    CONFIG_ERROR("An error just occurred while loading the config. Consider deleting the config and letting a new one generate."),
    LOADING_CONFIG("Deserializing config data..."),
    INITIALIZATION("Instantiating a few things..."),
    THANKYOU("Thank you for installing MaxRepairCost! If you're experiencing any issues please report it (see config for details)"),
    SUCCESS_FINAL("╼&m-----------&r Successfully Loaded &m-----------╾"),
    SHUTDOWN("Disabling..."),
    COMMAND_RELOAD("&2&l| " + PREFIX + "&aSuccessfully reloaded configuration!"),
    COMMAND_INVALID_ENTITY("&4&l| " + PREFIX + "&cYou must be a player to run that command!"),
    SUCCESS("Success!");

    private final String lang;
    LanguageLibrary(final String lang) {
        this.lang = T.C(lang);
    }

    @Override
    public String toString() {
        return P.R(lang);
    }

}
