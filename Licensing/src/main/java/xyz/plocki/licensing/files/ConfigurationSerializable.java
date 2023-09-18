package xyz.plocki.licensing.files;

import java.util.Map;

public interface ConfigurationSerializable {

    Map<String, Object> serialize();
}