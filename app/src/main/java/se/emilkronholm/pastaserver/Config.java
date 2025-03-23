package se.emilkronholm.pastaserver;

import java.util.Optional;

class Config {

    // Public Static interface
    static void loadShared(Builder configBuilder) {
        shared = Optional.of(new Config(configBuilder));
    }

    static Config getShared() {
        if (shared.isEmpty()) {
            System.err.println("Config read before loading. Will use default config.");
            loadShared(new Builder()); // Build new shared instance with empty configBuilder => default config.
        }

        return shared.get();
    }




    // Private attributes (accessible through getters)
    private final int port;
    private final String ip;
    private final int timeout;

    // Singleton instance
    private static Optional<Config> shared = Optional.empty();

    // Private constructor
    private Config(Builder configBuilder) {
        this.port = configBuilder.port;
        this.ip = configBuilder.ip;
        this.timeout = configBuilder.timeout;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public int getTimeout() {
        return timeout;
    }

    // Public ConfigBuilder, used to cleanly build new configs :)
    public static class Builder {

        // Parameters
        private int port = 80;
        private String ip = "127.0.0.1";
        private int timeout = 10000;

        // Setters
        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
    }
}
