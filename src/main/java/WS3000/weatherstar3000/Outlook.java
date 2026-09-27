package WS3000.weatherstar3000;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URI;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import util.Utilities;

public class Outlook extends Thread {
	static Utilities utl = new Utilities();
	String month;
	String precipitation;
	String temperature;
	
	private static final GeometryFactory geometryFactory = new GeometryFactory();

	String getOutlook(String type, String lat, String lon) {//type prcp or temp
		try {
			String zipFile = "https://ftp.cpc.ncep.noaa.gov/GIS/us_tempprcpfcst/monthlyupdate/monthupd_" + type + "_latest.zip";

			try {
	            InputStream kmlStream = getKmlStreamFromUrl(zipFile);
	            if (kmlStream == null) {
	                System.err.println("No .kml file found inside the ZIP archive.");
	                return utl.ljust("NO REPORT", 17, " ");
	            }

	            List<NamedPolygon> polygons = parseKmlPolygons(kmlStream);
	            
	            Point userPoint = geometryFactory.createPoint(new Coordinate(Double.parseDouble(lon), Double.parseDouble(lat)));
	            
	            String matchedPolygonName = findContainingPolygon(polygons, userPoint);

	            if (matchedPolygonName != null) {
	                if (matchedPolygonName.contains("Above")) {
	                	return utl.ljust("ABOVE NORMAL", 17, " ");
	                } else if (matchedPolygonName.contains("Below")) {
	                	return utl.ljust("BELOW NORMAL", 17, " ");
	                } else {
	                	return utl.ljust("NORMAL", 17, " ");
	                }
	            } else {
	            	return utl.ljust("NO REPORT", 17, " ");
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            return utl.ljust("NO REPORT", 17, " ");
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return utl.ljust("NO REPORT", 17, " ");
		}
	}
	
	private InputStream getKmlStreamFromUrl(String zipUrl) {
		try {
			URL url = URI.create(zipUrl).toURL();
        
			ZipInputStream zis = new ZipInputStream(url.openStream());
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null) {
				if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".kml")) {
					return zis;
				}
			}
			
			return null;
			
		} catch (Exception e) {
			return null;
		}
    }
	
	private List<NamedPolygon> parseKmlPolygons(InputStream kmlStream) throws Exception {
	    List<NamedPolygon> result = new ArrayList<>();

	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(kmlStream);

	    NodeList placemarks = doc.getElementsByTagName("Placemark");

	    for (int i = 0; i < placemarks.getLength(); i++) {
	        Element placemark = (Element) placemarks.item(i);

	        NodeList nameNodes = placemark.getElementsByTagName("name");
	        String name = nameNodes.getLength() > 0 ? nameNodes.item(0).getTextContent().trim() : "Unnamed";

	        NodeList polygonNodes = placemark.getElementsByTagName("Polygon");

	        for (int j = 0; j < polygonNodes.getLength(); j++) {
	            Element polygonEl = (Element) polygonNodes.item(j);
	            NodeList coordsNodes = polygonEl.getElementsByTagName("coordinates");

	            if (coordsNodes.getLength() > 0) {
	                String rawCoords = coordsNodes.item(0).getTextContent();
	                Polygon polygon = buildJtsPolygon(rawCoords);
	                if (polygon != null) {
	                    result.add(new NamedPolygon(name, polygon));
	                }
	            }
	        }
	    }
	    return result;
	}
	
	private static Polygon buildJtsPolygon(String rawCoords) {
	    String[] pointsStr = rawCoords.trim().split("\\s+");
	    List<Coordinate> coordsList = new ArrayList<>();

	    for (String p : pointsStr) {
	        p = p.trim();
	        if (p.isEmpty()) continue;
	        
	        String[] parts = p.split(",");
	        if (parts.length >= 2) {
	            try {
	                double lon = Double.parseDouble(parts[0]);
	                double lat = Double.parseDouble(parts[1]);
	                coordsList.add(new Coordinate(lon, lat));
	            } catch (NumberFormatException e) {
	            	e.printStackTrace();
	            }
	        }
	    }

	    if (coordsList.size() < 3) return null;

	    if (!coordsList.get(0).equals2D(coordsList.get(coordsList.size() - 1))) {
	        coordsList.add(coordsList.get(0));
	    }

	    Coordinate[] coordsArray = coordsList.toArray(new Coordinate[0]);
	    return geometryFactory.createPolygon(coordsArray);
	}
	
	private String findContainingPolygon(List<NamedPolygon> polygons, Point point) {
        for (NamedPolygon np : polygons) {
            if (np.polygon.covers(point)) { 
                return np.name;
            }
        }
        return null;
    }
	
	private String getOutlookTitle() {
		try {
			ZonedDateTime today = ZonedDateTime.now(Main.timeZone);
			
			String[] monthsShorter = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC", "JAN"};
			String[] monthsShort = {"JAN", "FEB", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUG", "SEPT", "OCT", "NOV", "DEC", "JAN"};
			String[] monthsLong = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER", "JANUARY"};
			
			String zipFile = "https://ftp.cpc.ncep.noaa.gov/GIS/us_tempprcpfcst/monthlyupdate/monthupd_temp_latest.zip";
			
			int cMonthVal = ZonedDateTime.now(Main.timeZone).getMonthValue()-1;
			
			String currentMonth = monthsShorter[cMonthVal];
			String dataMonth = "";
			
			InputStream kmlStream = getKmlStreamFromUrl(zipFile);
            if (kmlStream == null) {
                System.err.println("No .kml file found inside the ZIP archive.");
                return utl.ljust("", 32, " ");
            }
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(kmlStream);
	    	Element root = doc.getDocumentElement();
	    	if (root.hasAttribute("id")) {
	        	dataMonth = parseMonth(root.getAttribute("id").toUpperCase());
	    	}
	        
	        if (today.isAfter(today.withDayOfMonth(1).with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.THURSDAY)))) {
	        	if (currentMonth.equals(dataMonth)) {
	        		return utl.cjust("MID " + monthsShort[cMonthVal] + " - MID " + monthsShort[cMonthVal+1], 32, " ");
	        	} else {
	        		return utl.cjust(monthsLong[cMonthVal+1], 32, " ");
	        	}
	        } else {
	        	return utl.cjust(monthsLong[cMonthVal], 32, " ");
	        }
	    } catch (Exception e) {
			return utl.ljust("", 32, " ");
		}
	}
	
	private String parseMonth(String text) {
		if (text == null) {
            return "";
        }

        Pattern pattern = Pattern.compile("Valid\\s+([A-Za-z]{3})", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).toUpperCase();
        }

        return "";
	}
	
	public void run() {
		String mainLat = Main.mainLat;
    	String mainLon = Main.mainLon;
		Main.outlook.month = getOutlookTitle();
		Main.outlook.temperature = getOutlook("temp", mainLat, mainLon);
		Main.outlook.precipitation = getOutlook("prcp", mainLat, mainLon);
	}
}

class NamedPolygon {
        String name;
        Polygon polygon;

        NamedPolygon(String name, Polygon polygon) {
            this.name = name;
            this.polygon = polygon;
        }
}
