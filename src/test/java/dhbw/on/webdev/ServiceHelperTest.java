package dhbw.on.webdev;

import dhbw.on.webdev.repository.TrainingSessionRepository;
import dhbw.on.webdev.service.helper.ServiceHelper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ServiceHelperTest {
    @Inject
    ServiceHelper serviceHelper;

    @Inject
    TrainingSessionRepository trainingSessionRepository;


}
