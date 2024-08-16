package Sequence.core;

import Sequence.SeqMod;
import arc.files.Fi;
import arc.struct.StringMap;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Unzipper {
    public static VersionController version;

    static {
        version = new VersionController();
    }

    public static Fi find(String name) {
        try {
            return SeqMod.MOD.root.findAll(f -> f.name().equals(name)).first();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public static Fi unzip(Fi file, String version) {
        if (file == null) throw new IllegalArgumentException("File is null.");
        Fi fi = SqConst.unzipDirectory.child(
                file.path()
                        .replace("/", "-")
                        .replace(".", "-") + "/");
        if (!fi.exists()) fi.mkdirs();
        fi = fi.child("c" + file.path().hashCode() % 1048576 + "/");
        if (!fi.exists()) fi.mkdirs();
        if (fi.child("version").exists()) {
            if (fi.child("version").readString().equals(version)) {
                SqLog.info("Version valid: @ (version @)", fi.absolutePath(), version);
                return fi.child(file.name());
            } else {
                for (Fi fi1 : fi.list()) {
                    fi1.delete();
                }
                SqLog.info("Replace @ to version: @", fi.absolutePath(), version);
            }
        }

        Fi a = fi.child(file.name());
        try (OutputStream write = a.write()) {
            write.write(file.readBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Fi b = fi.child("version");
        try (OutputStream write = b.write()) {
            write.write(version.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return a;
    }

    public static class VersionController {
        public static Fi version;
        private static boolean init = false;
        protected StringMap versions = new StringMap();

        public VersionController() {
            if (init) throw new IllegalStateException("Cannot invoke 'public VersionController()' again.");
            init = true;
            version = Unzipper.find("version.properties");

            String[] strings = version.readString().split("\\n");
            for (String string : strings) {
                String[] str = string.split("=");
                String key = str[0], value = str[1];
                versions.put(key, value);
                SqLog.info("@;@", key, value);
            }
        }

        public String get(String key) {
            if (versions.containsKey(key)) return versions.get(key);
            throw new RuntimeException("Don't have the key.");
        }
    }
}
