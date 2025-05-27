package curioso.app;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import curioso.engine.Global;

@SpringBootTest
@ActiveProfiles("unit-tests")
public class BootstrapTests {

    @Test
    void testInitCallsLoadFilesAndLoadServices() throws IOException {
        assertFalse(Global.serviceFiles.isEmpty(), "Service files should be loaded");
    }

}