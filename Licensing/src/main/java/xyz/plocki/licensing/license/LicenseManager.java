package xyz.plocki.licensing.license;

import org.apache.commons.lang3.RandomStringUtils;
import xyz.plocki.licensing.files.FileBuilder;

import java.util.ArrayList;
import java.util.List;

public class LicenseManager {

    private final FileBuilder fb;

    public LicenseManager() {
        fb = new FileBuilder("licenses.yml");
        if(!fb.getYaml().isSet("licenses")) {
            fb.getYaml().set("licenses", new ArrayList<String>());
            fb.save();
        }
    }

    public String createLicense() {
        List<String> licenses = fb.getYaml().getStringList("licenses");
        String license = RandomStringUtils.randomAlphabetic(12);
        licenses.add(license);
        fb.getYaml().set("licenses", licenses);
        fb.getYaml().set(license + ".activations", 0);
        fb.getYaml().set(license + ".lastActivation", System.currentTimeMillis());
        fb.save();
        return license;
    }

    public void deleteLicense(String license) {
        if(licenseExist(license)) {
            List<String> licenses = fb.getYaml().getStringList("licenses");
            licenses.remove(license);
            fb.getYaml().set("licenses", licenses);
            fb.getYaml().set(license + ".activations", null);
            fb.getYaml().set(license + ".lastActivation", null);
            fb.save();
        }
    }

    public void activateLicense(String license) {
        if(fb.getYaml().isSet(license + ".activations")) {
            fb.getYaml().set(license + ".activations", fb.getYaml().getInt(license + ".activations") + 1);
        } else {
            fb.getYaml().set(license + ".activations", 1);
        }
        fb.getYaml().set(license + ".lastActivation", System.currentTimeMillis());
        fb.save();
    }

    public boolean licenseExist(String license) {
        return fb.getYaml().getStringList("licenses").contains(license);
    }

    public List<String> getLicenses() {
        return fb.getYaml().getStringList("licenses");
    }

    public long getLastActivation(String license) {
        return fb.getYaml().getLong(license + ".lastActivation");
    }

}
