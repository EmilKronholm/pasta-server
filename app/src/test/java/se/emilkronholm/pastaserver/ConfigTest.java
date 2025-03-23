package se.emilkronholm.pastaserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void testConfigBuilder() {
        int port = 5300;
        String ip = "0.0.0.0";
        

        // We let timeout be default value
        Config.Builder cb = new Config.Builder()
                            .setIp(ip)
                            .setPort(port);

        Config.loadShared(cb);

        assertEquals(Config.getShared().getPort(), port);
        assertEquals(Config.getShared().getIp(), ip);

        // Since timeout is default, make sure it has a value
        assertNotNull(Config.getShared().getTimeout());
        
    }
}
