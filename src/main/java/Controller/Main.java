/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;

/**
 *
 * @author C. Levallois
 */
public class Main {

    static String filepath = "H:\\data\\datasets\\shape files\\Australia\\1259030001_ste11aaust_shape\\";
    static String filename = "STE11aAust.shp";
    static boolean testOneState = false;
    static double precision= 0.05;
    // the higher the number, the LESSER the precision

    public static void main(String[] args) throws Exception {
        File file = new File(filepath + filename);
        if (!file.exists() || !filename.endsWith(".shp")) {
            throw new Exception("Invalid shapefile filepath: " + filepath);
        }
        ShapefileDataStore dataStore = new ShapefileDataStore(file.toURL());
        ContentFeatureSource featureSource = dataStore.getFeatureSource();
        ContentFeatureCollection featureCollection = featureSource.getFeatures();
        
        CoordinatesExtractor extractor = new CoordinatesExtractor(featureCollection);
        extractor.extract(testOneState);
        
        CoordinatesWriter writer = new CoordinatesWriter();
        writer.write(precision);
        dataStore.dispose();

    }

}
