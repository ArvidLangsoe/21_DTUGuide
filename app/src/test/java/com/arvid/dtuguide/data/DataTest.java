package com.arvid.dtuguide.data;

import org.junit.Test;
import com.arvid.dtuguide.data.*;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DataTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testLocationDAO(){
        LocationDAO dao = new LocationDAO();

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "Canteen 3",
                new GeoPoint(),
                0)
                .type(LocationDTO.MARKTYPE.CANTEEN)
                .description("This is a simple Canteen")
                ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.80",
                new GeoPoint(),
                0)
                .description("Aud X1.80")
        ));
    }

    }
