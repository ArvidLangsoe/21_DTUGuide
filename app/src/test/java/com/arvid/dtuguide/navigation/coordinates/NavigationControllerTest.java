package com.arvid.dtuguide.navigation.coordinates;

import android.location.LocationManager;

import org.junit.Test;
import com.arvid.dtuguide.data.*;
import com.arvid.dtuguide.navigation.NavigationController;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NavigationControllerTest {

    private void print(String msg) {
        System.out.println(msg);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSearch() {
        LocationDAO dao = new LocationDAO();

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "Canteen 3",
                new GeoPoint(634342.032323, 2343434.242),
                0)
                .type(LocationDTO.MARKTYPE.CANTEEN)
                .description("This is a simple Canteen")
        ));


        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.80",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.81",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.82",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.83",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.84",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.85",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.86",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.87",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.88",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.89",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.90",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.91",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        NavigationController controller = new NavigationController(dao);

        try {
            print(controller.searchMatch("80")+"");
        } catch (LocationDAO.DAOException e) {
            e.printStackTrace();
        }

        try {
            controller.getLocation("X 1.80");
            controller.getLocation("X 1.81");
            controller.getLocation("X 1.82");
            controller.getLocation("X 1.83");
            controller.getLocation("X 1.84");
            controller.getLocation("X 1.85");
            controller.getLocation("X 1.86");
            controller.getLocation("X 1.87");
            controller.getLocation("X 1.88");
            controller.getLocation("X 1.89");
            controller.getLocation("X 1.90");
            controller.getLocation("X 1.91");
            controller.getLocation("X 1.85");
            controller.getLocation("X 1.90");
            print(controller.getHistoryList()+"");
            print(""+controller.getHistoryList().size());
        } catch (LocationDAO.DAOException e) {
            e.printStackTrace();
        }
    }
}