package me.revoltix.moduleloader.exceptions;

import me.kaimu.hastebin.Hastebin;
import me.revoltix.moduleloader.ModuleLoader;

public class ErrorInPostHastebinException extends Exception {

    public ErrorInPostHastebinException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
        if(ModuleLoader.postOnHastebin) {
            System.out.println(new Hastebin().post("Error message:\n" + this.getMessage() + "\n\n\nStacktrace:\n" + this.getStackTrace(), false));
        }
    }

}
