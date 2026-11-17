package hu.rushhour;

import hu.rushhour.model.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VehicleTest {
    @Test
    void immutableShift() {
        Vehicle v = new Vehicle('A', 2, 3, 2, true);
        Vehicle moved = v.shifted(2);
        assertEquals(3, v.col());
        assertEquals(5, moved.col());
        assertEquals(2, moved.row());
    }

    @Test
    void verticalShift() {
        Vehicle v = new Vehicle('C', 1, 3, 3, false);
        assertEquals(3, v.shifted(2).row());
        assertEquals(3, v.shifted(2).col());
    }

    @Test
    void rejectsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Vehicle('A', -1, 0, 2, true));
        assertThrows(IllegalArgumentException.class, () -> new Vehicle('A', 0, 0, 1, true));
    }
}
