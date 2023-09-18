package me.revoltix.moduleloader.exceptions;

import me.kaimu.hastebin.Hastebin;
import me.revoltix.moduleloader.ModuleLoader;

public class ErrorInLoadModuleClassException extends Exception {

    public ErrorInLoadModuleClassException(String errorMessage, Throwable error) {
        super(errorMessage, error);
        if(ModuleLoader.postOnHastebin) {
            System.out.println(new Hastebin().post("Error message:\n" + this.getMessage() + "\n\n\nStacktrace:\n" + this.getStackTrace(), false));
        }
    }

}
