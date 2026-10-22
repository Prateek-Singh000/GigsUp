package com.gigsup.service;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

@Component
public class GeoUtil {

    // SRID 4326 = WGS84, the standard lat/lng coordinate system used by GPS
    private static final GeometryFactory FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    public Point toPoint(double lat, double lng) {
        // JTS Point takes (x=lng, y=lat) — easy to mix up!
        Point point = FACTORY.createPoint(new org.locationtech.jts.geom.Coordinate(lng, lat));
        point.setSRID(4326);
        return point;
    }
}
