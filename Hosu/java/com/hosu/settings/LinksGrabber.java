package com.hosu.settings;

import java.util.List;

@FunctionalInterface
public interface LinksGrabber {
 public abstract List<String> getURL();
}
