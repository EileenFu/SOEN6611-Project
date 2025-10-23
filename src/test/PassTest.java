package test;

// src/test/java/model/PassTest.java
import model.Pass;
import org.junit.Test;
import utils.Enums.PassDuration;
import utils.Enums.ZoneType;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class PassTest {
    @Test
    void nightPassExpiresSixNextDay() {
        Pass p = new Pass(ZoneType.A, PassDuration.NIGHT, false);
        var start = p.getStartDateTime();
        assertEquals(start.toLocalDate().plusDays(1).atTime(LocalTime.of(6,0)), p.getExpiryDateTime());
    }
    @Test
    void dayPassExpiresEndOfDay() {
        Pass p = new Pass(ZoneType.A, PassDuration.DAY, false);
        var startDay = p.getStartDateTime().toLocalDate();
        assertEquals(startDay.atTime(23,59,59), p.getExpiryDateTime());
    }
    @Test
    void passBecomesInvalidAfterExpiry() throws Exception {
        Pass p = new Pass(ZoneType.A, PassDuration.DAY, false);
        Field f = Pass.class.getDeclaredField("expiryDateTime");
        f.setAccessible(true);
        f.set(p, LocalDateTime.now().minusMinutes(1));
        assertFalse(p.isValid());
    }
}
