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

    private void print(String msg){
        System.out.println(msg);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testStartLocationDAO(){
        LocationDAO dao = new LocationDAO();
        print("DAO initialised");

    }

    @Test
    public void testLocationDAO(){
        LocationDAO dao = new LocationDAO();

        print(dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "Canteen 3",
                new GeoPoint(634342.032323, 2343434.242),
                0)
                .type(LocationDTO.MARKTYPE.CANTEEN)
                .description("This is a simple Canteen")
                ))
        +"");

        dao.saveLocation(new LocationDTO(new LocationDTO.LocationBuilder(
                "X 1.80",
                new GeoPoint(32332.32323, 991132.35556),
                0)
                .description("Aud X1.80")
        ));

        try {
            print(dao.getLocations()+"");
        } catch (LocationDAO.DAOException e) {
            e.printStackTrace();
        }

        try {
            print(dao.getLocation("X 1.80")+"");
        } catch (LocationDAO.DAOException e) {
            e.printStackTrace();
        }
        dao.deleteLocation("X 1.80");
        try {
            print(dao.getLocation("X 1.80")+"");
        } catch (LocationDAO.DAOException e) {
            e.printStackTrace();
        }
    }

    }
