package org.wongoo.wongoo3.global.policy.file;

import java.util.Set;

public final class FilePolicy {
    private FilePolicy(){}

    public static final int MAX_FILE_COUNT = 5;
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final Set<String> ALLOWED_FILE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );
}
