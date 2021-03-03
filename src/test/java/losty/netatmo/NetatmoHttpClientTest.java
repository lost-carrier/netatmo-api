package losty.netatmo;

import losty.netatmo.model.*;
import losty.netatmo.model.Module;
import losty.netatmo.oauthtoken.OAuthTokenHandler;
import losty.netatmo.oauthtoken.OAuthTokenStore;
import losty.netatmo.oauthtoken.TransientOAuthTokenStore;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static losty.netatmo.model.Module.TYPE_INDOOR;
import static losty.netatmo.model.Module.TYPE_RAIN_GAUGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetatmoHttpClientTest {

    @Test
    public void testGetMeasures() throws IllegalAccessException, OAuthProblemException, OAuthSystemException {

        NetatmoHttpClient client = prepareToRespond("{\"body\":[{\"beg_time\":1505001741,\"step_time\":308,\"value\":[[12.7,null,84],[12.7,null,84],[12.7,null,84]]},{\"beg_time\":1505002663,\"step_time\":308,\"value\":[[12.7,null,84],[12.6,null,84],[12.7,null,84]]},{\"beg_time\":1505003586,\"step_time\":308,\"value\":[[12.7,null,83],[12.7,null,84]]},{\"beg_time\":1505004150,\"step_time\":308,\"value\":[[12.7,null,83],[12.7,null,83]]},{\"beg_time\":1505004765,\"step_time\":309,\"value\":[[12.7,null,83],[12.7,null,84]]},{\"beg_time\":1505005382,\"step_time\":307,\"value\":[[12.7,null,84],[12.7,null,85]]},{\"beg_time\":1505005997,\"step_time\":307,\"value\":[[12.7,null,84],[12.7,null,84]]},{\"beg_time\":1505006561,\"step_time\":306,\"value\":[[12.7,null,84],[12.7,null,84]]},{\"beg_time\":1505007175,\"step_time\":308,\"value\":[[12.6,null,84],[12.6,null,85]]},{\"beg_time\":1505007790,\"step_time\":308,\"value\":[[12.6,null,84],[12.6,null,84]]},{\"beg_time\":1505008405,\"step_time\":308,\"value\":[[12.6,null,84],[12.5,null,84]]},{\"beg_time\":1505008969,\"step_time\":309,\"value\":[[12.5,null,84],[12.5,null,84]]},{\"beg_time\":1505009586,\"step_time\":306,\"value\":[[12.5,null,84],[12.4,null,85]]},{\"beg_time\":1505010200,\"step_time\":308,\"value\":[[12.4,null,85],[12.4,null,85]]},{\"beg_time\":1505010815,\"step_time\":308,\"value\":[[12.4,null,85],[12.4,null,86]]},{\"beg_time\":1505011379,\"step_time\":308,\"value\":[[12.4,null,86],[12.5,null,86]]},{\"beg_time\":1505011994,\"step_time\":308,\"value\":[[12.5,null,85],[12.5,null,85],[12.5,null,85],[12.5,null,85],[12.5,null,85]]},{\"beg_time\":1505013532,\"step_time\":257,\"value\":[[12.5,null,85],[12.4,null,85]]},{\"beg_time\":1505014096,\"step_time\":308,\"value\":[[12.3,null,86],[12.3,null,86],[12.3,null,86],[12.3,null,87],[12.3,null,87]]},{\"beg_time\":1505015634,\"step_time\":308,\"value\":[[12.3,null,87],[12.3,null,87]]},{\"beg_time\":1505016198,\"step_time\":308,\"value\":[[12.3,null,87],[12.4,null,87]]},{\"beg_time\":1505016815,\"step_time\":307,\"value\":[[12.4,null,87],[12.4,null,86],[12.5,null,86]]},{\"beg_time\":1505017737,\"step_time\":307,\"value\":[[12.5,null,86],[12.4,null,86]]},{\"beg_time\":1505018352,\"step_time\":256,\"value\":[[12.4,null,86],[12.4,null,85]]},{\"beg_time\":1505018916,\"step_time\":307,\"value\":[[12.4,null,85],[12.4,null,85]]},{\"beg_time\":1505019531,\"step_time\":309,\"value\":[[12.4,null,85],[12.5,null,85]]},{\"beg_time\":1505020147,\"step_time\":307,\"value\":[[12.4,null,85],[12.4,null,86],[12.4,null,86]]},{\"beg_time\":1505021018,\"step_time\":307,\"value\":[[12.4,null,86],[12.4,null,86]]},{\"beg_time\":1505021633,\"step_time\":308,\"value\":[[12.4,null,86],[12.4,null,87]]},{\"beg_time\":1505022248,\"step_time\":308,\"value\":[[12.4,null,87],[12.5,null,87]]},{\"beg_time\":1505022863,\"step_time\":308,\"value\":[[12.5,null,86],[12.4,null,86]]},{\"beg_time\":1505023427,\"step_time\":308,\"value\":[[12.4,null,86],[12.4,null,86],[12.4,null,86]]},{\"beg_time\":1505024350,\"step_time\":308,\"value\":[[12.4,null,86],[12.4,null,86],[12.5,null,86]]},{\"beg_time\":1505025273,\"step_time\":308,\"value\":[[12.5,null,86],[12.5,null,86]]},{\"beg_time\":1505025888,\"step_time\":257,\"value\":[[12.6,null,86],[12.6,null,85]]},{\"beg_time\":1505026452,\"step_time\":308,\"value\":[[12.6,null,85],[12.6,null,85]]},{\"beg_time\":1505027067,\"step_time\":307,\"value\":[[12.6,null,85],[12.6,null,85]]},{\"beg_time\":1505027682,\"step_time\":307,\"value\":[[12.7,null,85],[12.7,null,84]]},{\"beg_time\":1505028247,\"step_time\":307,\"value\":[[12.7,null,84],[12.7,null,84]]},{\"beg_time\":1505028862,\"step_time\":308,\"value\":[[12.8,null,84],[12.9,null,84]]},{\"beg_time\":1505029477,\"step_time\":308,\"value\":[[13,null,83],[13.1,null,82]]},{\"beg_time\":1505030092,\"step_time\":308,\"value\":[[13.1,null,82],[13.1,null,82],[13.2,null,82]]},{\"beg_time\":1505030964,\"step_time\":308,\"value\":[[13.2,null,82],[13.3,null,82]]},{\"beg_time\":1505031579,\"step_time\":307,\"value\":[[13.3,null,82],[13.4,null,83]]},{\"beg_time\":1505032194,\"step_time\":308,\"value\":[[13.6,null,83],[13.7,null,81],[13.8,null,81]]},{\"beg_time\":1505033116,\"step_time\":257,\"value\":[[13.9,null,79],[14,null,79]]},{\"beg_time\":1505033681,\"step_time\":308,\"value\":[[14.2,null,80],[14.3,null,79]]},{\"beg_time\":1505034296,\"step_time\":307,\"value\":[[14.4,null,78],[14.5,null,78]]},{\"beg_time\":1505034912,\"step_time\":307,\"value\":[[14.7,null,78],[15.1,null,79]]},{\"beg_time\":1505035475,\"step_time\":307,\"value\":[[15.3,null,77],[15.7,null,75]]},{\"beg_time\":1505036091,\"step_time\":308,\"value\":[[15.8,null,74],[15.8,null,73]]},{\"beg_time\":1505036705,\"step_time\":308,\"value\":[[15.8,null,73],[15.9,null,73],[16,null,73],[16,null,71]]},{\"beg_time\":1505037936,\"step_time\":256,\"value\":[[16.4,null,73],[16.9,null,71]]},{\"beg_time\":1505038501,\"step_time\":307,\"value\":[[17.5,null,69],[18,null,68]]},{\"beg_time\":1505039116,\"step_time\":308,\"value\":[[18.3,null,66],[18.1,null,65]]},{\"beg_time\":1505039730,\"step_time\":308,\"value\":[[18,null,65],[18.3,null,66]]},{\"beg_time\":1505040345,\"step_time\":257,\"value\":[[19,null,67],[19.5,null,64]]},{\"beg_time\":1505040909,\"step_time\":308,\"value\":[[19.7,null,61],[20,null,63]]},{\"beg_time\":1505041526,\"step_time\":307,\"value\":[[20.5,null,60],[20.8,null,58],[20.8,null,58],[21.2,null,59]]},{\"beg_time\":1505042755,\"step_time\":256,\"value\":[[21.5,null,56],[21.3,null,55]]},{\"beg_time\":1505043320,\"step_time\":308,\"value\":[[21.4,null,56],[21.4,null,55]]},{\"beg_time\":1505043935,\"step_time\":308,\"value\":[[21.4,null,54],[21.5,null,53]]},{\"beg_time\":1505044549,\"step_time\":308,\"value\":[[21.4,null,53],[21.6,null,53],[21.8,null,53]]},{\"beg_time\":1505045421,\"step_time\":308,\"value\":[[21.7,null,52],[21.6,null,53]]},{\"beg_time\":1505046036,\"step_time\":308,\"value\":[[21.4,null,53],[21.3,null,53],[21.2,null,53]]},{\"beg_time\":1505046959,\"step_time\":308,\"value\":[[21,null,54],[20.9,null,53]]},{\"beg_time\":1505047574,\"step_time\":257,\"value\":[[20.8,null,55],[20.9,null,56]]},{\"beg_time\":1505048138,\"step_time\":308,\"value\":[[21.2,null,58],[21.3,null,54],[21.3,null,54]]},{\"beg_time\":1505049061,\"step_time\":308,\"value\":[[21,null,54],[20.7,null,55]]},{\"beg_time\":1505049676,\"step_time\":308,\"value\":[[20.7,null,55],[20.6,null,55]]},{\"beg_time\":1505050240,\"step_time\":308,\"value\":[[20.2,null,55],[20,null,55],[19.7,null,56]]},{\"beg_time\":1505051163,\"step_time\":308,\"value\":[[19.5,null,57],[19.3,null,57]]},{\"beg_time\":1505051778,\"step_time\":308,\"value\":[[19.3,null,58],[19.1,null,58],[19,null,58]]},{\"beg_time\":1505052650,\"step_time\":308,\"value\":[[18.8,null,59],[18.8,null,61]]},{\"beg_time\":1505053265,\"step_time\":308,\"value\":[[18.8,null,60],[18.9,null,60],[18.8,null,61]]},{\"beg_time\":1505054188,\"step_time\":308,\"value\":[[18.7,null,61],[18.6,null,61]]},{\"beg_time\":1505054803,\"step_time\":257,\"value\":[[18.6,null,62],[18.6,null,62]]},{\"beg_time\":1505055367,\"step_time\":308,\"value\":[[18.7,null,62],[18.7,null,62],[18.8,null,62]]},{\"beg_time\":1505056290,\"step_time\":308,\"value\":[[18.8,null,62],[18.8,null,60]]},{\"beg_time\":1505056905,\"step_time\":308,\"value\":[[18.7,null,60],[18.5,null,60]]},{\"beg_time\":1505057469,\"step_time\":308,\"value\":[[18.5,null,61],[18.4,null,62],[18.4,null,62]]},{\"beg_time\":1505058392,\"step_time\":308,\"value\":[[18.3,null,62],[18.3,null,63]]},{\"beg_time\":1505059007,\"step_time\":308,\"value\":[[18.2,null,62],[18.1,null,64],[18.1,null,63]]},{\"beg_time\":1505059879,\"step_time\":308,\"value\":[[18,null,64],[17.9,null,65]]},{\"beg_time\":1505060494,\"step_time\":308,\"value\":[[17.9,null,65],[17.8,null,65],[17.7,null,65]]},{\"beg_time\":1505061417,\"step_time\":308,\"value\":[[17.7,null,66],[17.6,null,66]]},{\"beg_time\":1505062031,\"step_time\":257,\"value\":[[17.6,null,66],[17.5,null,67]]},{\"beg_time\":1505062596,\"step_time\":308,\"value\":[[17.5,null,68],[17.5,null,69],[17.4,null,69]]},{\"beg_time\":1505063519,\"step_time\":103,\"value\":[[17.4,null,70],[17.4,null,70]]},{\"beg_time\":1505063929,\"step_time\":308,\"value\":[[17.4,null,71],[17.3,null,71],[17.2,null,71]]},{\"beg_time\":1505064801,\"step_time\":308,\"value\":[[17.1,null,72],[17.1,null,73]]},{\"beg_time\":1505065416,\"step_time\":307,\"value\":[[17,null,71],[16.9,null,72],[16.9,null,72]]},{\"beg_time\":1505066339,\"step_time\":308,\"value\":[[16.8,null,72],[16.7,null,72]]},{\"beg_time\":1505066954,\"step_time\":307,\"value\":[[16.6,null,72],[16.5,null,73]]},{\"beg_time\":1505067517,\"step_time\":309,\"value\":[[16.4,null,73],[16.3,null,73]]},{\"beg_time\":1505068133,\"step_time\":307,\"value\":[[16.3,null,73],[16.2,null,74]]},{\"beg_time\":1505068748,\"step_time\":308,\"value\":[[16.2,null,74],[16.1,null,74],[16.1,null,73],[16,null,74]]},{\"beg_time\":1505069928,\"step_time\":307,\"value\":[[15.9,null,74],[15.9,null,74],[15.8,null,75]]},{\"beg_time\":1505070850,\"step_time\":307,\"value\":[[15.7,null,75],[15.6,null,76]]},{\"beg_time\":1505071466,\"step_time\":308,\"value\":[[15.4,null,76],[15.3,null,76]]},{\"beg_time\":1505072081,\"step_time\":257,\"value\":[[15.3,null,77],[15.3,null,77]]},{\"beg_time\":1505072644,\"step_time\":308,\"value\":[[15.3,null,77],[15.2,null,77]]},{\"beg_time\":1505073259,\"step_time\":308,\"value\":[[15.1,null,78],[14.9,null,78],[14.8,null,78]]},{\"beg_time\":1505074182,\"step_time\":309,\"value\":[[14.6,null,79],[14.5,null,79]]},{\"beg_time\":1505074747,\"step_time\":308,\"value\":[[14.4,null,80],[14.4,null,79]]},{\"beg_time\":1505075362,\"step_time\":307,\"value\":[[14.4,null,80],[14.3,null,80]]},{\"beg_time\":1505075977,\"step_time\":256,\"value\":[[14.2,null,80],[14.1,null,80]]},{\"beg_time\":1505076592,\"step_time\":308,\"value\":[[14,null,81],[14,null,81]]},{\"beg_time\":1505077156,\"step_time\":308,\"value\":[[13.9,null,81],[13.9,null,81]]},{\"beg_time\":1505077771,\"step_time\":308,\"value\":[[13.8,null,81],[13.8,null,81]]},{\"beg_time\":1505078386,\"step_time\":308,\"value\":[[13.7,null,80],[13.6,null,81],[13.5,null,81]]},{\"beg_time\":1505079309,\"step_time\":257,\"value\":[[13.3,null,81],[13.2,null,82]]},{\"beg_time\":1505079873,\"step_time\":308,\"value\":[[13.2,null,82],[13.2,null,82]]},{\"beg_time\":1505080488,\"step_time\":308,\"value\":[[13.2,null,82],[13.1,null,82],[13,null,82]]},{\"beg_time\":1505081411,\"step_time\":308,\"value\":[[12.9,null,82],[12.9,null,82]]},{\"beg_time\":1505081975,\"step_time\":308,\"value\":[[12.8,null,82],[12.7,null,82]]},{\"beg_time\":1505082590,\"step_time\":308,\"value\":[[12.7,null,82],[12.6,null,83],[12.5,null,83]]},{\"beg_time\":1505083513,\"step_time\":308,\"value\":[[12.4,null,83],[12.4,null,83]]},{\"beg_time\":1505084077,\"step_time\":308,\"value\":[[12.3,null,83],[12.3,null,83]]},{\"beg_time\":1505084694,\"step_time\":307,\"value\":[[12.2,null,83],[12.2,null,83],[12.1,null,83],[12.1,null,83]]},{\"beg_time\":1505085923,\"step_time\":308,\"value\":[[12,null,83],[11.9,null,83]]},{\"beg_time\":1505086487,\"step_time\":308,\"value\":[[11.9,null,84],[11.8,null,84]]},{\"beg_time\":1505087102,\"step_time\":308,\"value\":[[11.7,null,84],[11.6,null,84]]},{\"beg_time\":1505087717,\"value\":[[11.5,null,84]]}],\"status\":\"ok\",\"time_exec\":0.085386037826538,\"time_server\":1528321956}\n");

        List<String> types = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_PRESSURE, Params.TYPE_HUMIDITY);
        Date dateBegin = DateTime.parse("2015-09-10T00Z").toDate();
        Date dateEnd = DateTime.parse("2015-09-11T00Z").toDate();
        Station station = new Station("My Station", "my-station-id");
        Module module = new Module("My Module", "my-module-id", TYPE_INDOOR);


        List<Measures> measures = client.getMeasures(station, module, types, Params.SCALE_MAX, dateBegin, dateEnd, null, null);


        assertNotNull(measures);
        assertEquals(287, measures.size());

        Measures firstMeasures = measures.get(0);
        assertEquals(1505001741000L, firstMeasures.getBeginTime());
        assertEquals(12.7, firstMeasures.getTemperature(), 0.0);
        assertEquals(Double.NaN, firstMeasures.getPressure(), 0.0);
        assertEquals(84, firstMeasures.getHumidity(), 0.0);

        Measures lastMeasures = measures.get(286);
        assertEquals(1505087717000L, lastMeasures.getBeginTime());
        assertEquals(11.5, lastMeasures.getTemperature(), 0.0);
        assertEquals(Double.NaN, lastMeasures.getPressure(), 0.0);
        assertEquals(84, lastMeasures.getHumidity(), 0.0);
    }

    @Test
    public void getStationsData() throws OAuthProblemException, OAuthSystemException, IllegalAccessException {

        NetatmoHttpClient client = prepareToRespond("{\"body\":{\"devices\":[{\"_id\":\"70:xx:xx:xx:xx:xx\",\"cipher_id\":\"enc:16:whatthefuckisthisshitinhere\",\"date_setup\":1366148563,\"last_setup\":1366148563,\"type\":\"NAMain\",\"last_status_store\":1528323455,\"module_name\":\"Innen\",\"firmware\":132,\"last_upgrade\":1439972537,\"wifi_status\":38,\"co2_calibrating\":false,\"station_name\":\"Station Name\",\"data_type\":[\"Temperature\",\"CO2\",\"Humidity\",\"Noise\",\"Pressure\"],\"place\":{\"altitude\":453,\"city\":\"Zurich\",\"country\":\"CH\",\"timezone\":\"Europe/Zurich\",\"location\":[8.4,47.4]},\"dashboard_data\":{\"time_utc\":1528323437,\"Temperature\":24.6,\"CO2\":751,\"Humidity\":58,\"Noise\":37,\"Pressure\":1014.9,\"AbsolutePressure\":961.6,\"min_temp\":24.6,\"max_temp\":24.6,\"date_min_temp\":1528322530,\"date_max_temp\":1528322530,\"temp_trend\":\"stable\",\"pressure_trend\":\"stable\"},\"modules\":[{\"_id\":\"02:xx:xx:xx:xx:xx\",\"type\":\"NAModule1\",\"module_name\":\"Außen\",\"data_type\":[\"Temperature\",\"Humidity\"],\"last_setup\":1366146161,\"dashboard_data\":{\"time_utc\":1489172240,\"Temperature\":8.3,\"Humidity\":100,\"min_temp\":8.3,\"max_temp\":33.2,\"date_min_temp\":1489172240,\"date_max_temp\":1489150204},\"firmware\":44,\"last_message\":1489172253,\"last_seen\":1489172240,\"rf_status\":91,\"battery_vp\":3744,\"battery_percent\":6},{\"_id\":\"05:xx:xx:xx:xx:xx\",\"type\":\"NAModule3\",\"module_name\":\"Niederschlagsmesser\",\"data_type\":[\"Rain\"],\"last_setup\":1447784487,\"dashboard_data\":{\"time_utc\":1476015169,\"Rain\":0.202,\"sum_rain_24\":0.7},\"firmware\":8,\"last_message\":1476015175,\"last_seen\":1476015169,\"rf_status\":63,\"battery_vp\":5270,\"battery_percent\":70},{\"_id\":\"03:xx:xx:xx:xx:xx\",\"type\":\"NAModule4\",\"module_name\":\"Schlafzimmer\",\"data_type\":[\"Temperature\",\"CO2\",\"Humidity\"],\"last_setup\":1459448955,\"dashboard_data\":{\"time_utc\":1528323426,\"Temperature\":23.3,\"CO2\":443,\"Humidity\":60,\"min_temp\":23.2,\"max_temp\":23.3,\"date_min_temp\":1528322504,\"date_max_temp\":1528323426,\"temp_trend\":\"up\"},\"firmware\":44,\"last_message\":1528323451,\"last_seen\":1528323426,\"rf_status\":75,\"battery_vp\":5116,\"battery_percent\":51}]}],\"user\":{\"mail\":\"netatmo@email.com\",\"administrative\":{\"country\":\"DE\",\"feel_like_algo\":0,\"lang\":\"de-DE\",\"pressureunit\":0,\"reg_locale\":\"de-CH\",\"unit\":0,\"windunit\":0}}},\"status\":\"ok\",\"time_exec\":0.18290090560913,\"time_server\":1528323598}");


        List<Station> stations = client.getStationsData(null, null);


        assertNotNull(stations);
        assertEquals(1, stations.size());
        Station station = stations.get(0);
        assertEquals("70:xx:xx:xx:xx:xx", station.getId());
        assertEquals("Station Name", station.getName());
        List<Module> modules = station.getModules();
        assertNotNull(modules);
        assertEquals(4, modules.size());
        assertEquals("70:xx:xx:xx:xx:xx", modules.get(0).getId());
        assertEquals("Innen", modules.get(0).getName());
        assertEquals(TYPE_INDOOR, modules.get(0).getType());
        assertEquals("03:xx:xx:xx:xx:xx", modules.get(3).getId());
        assertEquals("Schlafzimmer", modules.get(3).getName());
        assertEquals(TYPE_INDOOR, modules.get(3).getType());
    }

    @Test
    public void testGetMeasuresRainWithAggregation() throws OAuthSystemException, IllegalAccessException, OAuthProblemException {

        NetatmoHttpClient client = prepareToRespond("{\"body\":[{\"beg_time\":1571617800,\"step_time\":3600,\"value\":[[0],[0],[0.7070000000000001],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0.202],[0.202],[0],[0],[0],[0],[0],[0]]}],\"status\":\"ok\",\"time_exec\":0.08422398567199707,\"time_server\":1571781297}");

        List<String> types = Arrays.asList(Params.TYPE_RAIN_SUM);
        Date dateBegin = DateTime.parse("2019-10-21T00Z").toDate();
        Date dateEnd = DateTime.parse("2019-10-22T00Z").toDate();
        Station station = new Station("My Station", "70:ee:50:15:f5:dc");
        Module module = new Module("My Module", "05:00:00:02:a8:38", TYPE_RAIN_GAUGE);

        List<Measures> measures = client.getMeasures(station, module, types, Params.SCALE_ONE_HOUR, dateBegin, dateEnd, null, null);
        assertEquals(24, measures.size());
        assertEquals(0.0, measures.get(0).getSum_rain(), 0.0);
        assertEquals(0.0, measures.get(1).getSum_rain(), 0.0);
        assertEquals(0.707, measures.get(2).getSum_rain(), 0.00001d);
        assertEquals(0.0, measures.get(3).getSum_rain(), 0.0d);
    }


    @Test
    public void testGetMeasuresRainWithScaleMax() throws OAuthSystemException, IllegalAccessException, OAuthProblemException {

        NetatmoHttpClient client = prepareToRespond("{\"body\":[{\"beg_time\":1571623437,\"step_time\":307,\"value\":[[0],[0]]},{\"beg_time\":1571624039,\"step_time\":308,\"value\":[[0],[0]]},{\"beg_time\":1571624642,\"step_time\":307,\"value\":[[0],[0.101]]},{\"beg_time\":1571625244,\"step_time\":39,\"value\":[[0.101],[0.101]]},{\"beg_time\":1571625577,\"step_time\":308,\"value\":[[0.101],[0.101]]},{\"beg_time\":1571626179,\"step_time\":307,\"value\":[[0.101],[0.101]]},{\"beg_time\":1571626782,\"value\":[[0]]}],\"status\":\"ok\",\"time_exec\":0.06606698036193848,\"time_server\":1571782393}");

        List<String> types = Arrays.asList(Params.TYPE_RAIN);
        Date dateBegin = DateTime.parse("2019-10-21T02:00Z").toDate();
        Date dateEnd = DateTime.parse("2019-10-21T03:00Z").toDate();
        Station station = new Station("My Station", "70:ee:50:15:f5:dc");
        Module module = new Module("My Module", "05:00:00:02:a8:38", TYPE_RAIN_GAUGE);

        List<Measures> measures = client.getMeasures(station, module, types, Params.SCALE_MAX, dateBegin, dateEnd, null, null);
        assertEquals(13, measures.size());
        assertEquals(0.0, measures.get(0).getRain(), 0.0);
        assertEquals(0.0, measures.get(1).getRain(), 0.0);
        assertEquals(0.0, measures.get(4).getRain(), 0.0d);
        assertEquals(0.101, measures.get(5).getRain(), 0.0d);
        assertEquals(0.101, measures.get(6).getRain(), 0.0d);
        assertEquals(0.101, measures.get(7).getRain(), 0.0d);
    }


    @Test
    public void testGetPublicData() throws OAuthSystemException, IllegalAccessException, OAuthProblemException {

        NetatmoHttpClient client = prepareToRespond("{\"status\":\"ok\",\"time_server\":1571780902,\"time_exec\":0,\"body\":[{\"_id\":\"70:ee:50:02:79:7e\",\"place\":{\"location\":[8.543904,47.369443],\"timezone\":\"Europe/Zurich\",\"country\":\"CH\",\"altitude\":416,\"city\":\"Zurich\"},\"mark\":14,\"measures\":{\"02:00:00:01:f8:66\":{\"res\":{\"1571780697\":[13.4,86]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:02:79:7e\":{\"res\":{\"1571780730\":[1023.7]},\"type\":[\"pressure\"]},\"05:00:00:00:2e:d2\":{\"rain_60min\":0,\"rain_24h\":0.202,\"rain_live\":0,\"rain_timeutc\":1571780723}},\"modules\":[\"02:00:00:01:f8:66\",\"05:00:00:00:2e:d2\"],\"module_types\":{\"02:00:00:01:f8:66\":\"NAModule1\",\"05:00:00:00:2e:d2\":\"NAModule3\"}},{\"_id\":\"70:ee:50:02:91:50\",\"place\":{\"location\":[8.531449500000008,47.3772429],\"timezone\":\"Europe/Zurich\",\"country\":\"CH\",\"altitude\":411,\"city\":\"Zurich\",\"street\":\"Kanonengasse\"},\"mark\":14,\"measures\":{\"02:00:00:02:9d:c0\":{\"res\":{\"1571780396\":[13.9,93]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:02:91:50\":{\"res\":{\"1571780409\":[1022.6]},\"type\":[\"pressure\"]},\"05:00:00:01:27:64\":{\"rain_60min\":0,\"rain_24h\":0,\"rain_live\":0,\"rain_timeutc\":1571780409}},\"modules\":[\"05:00:00:01:27:64\",\"02:00:00:02:9d:c0\"],\"module_types\":{\"05:00:00:01:27:64\":\"NAModule3\",\"02:00:00:02:9d:c0\":\"NAModule1\"}},{\"_id\":\"70:ee:50:06:86:68\",\"place\":{\"location\":[8.541694000000007,47.3768866],\"timezone\":\"Europe/Zurich\",\"country\":\"CH\",\"altitude\":414,\"city\":\"Zurich\",\"street\":\"Bahnhofplatz\"},\"mark\":1,\"measures\":{\"02:00:00:2e:eb:14\":{\"res\":{\"1571780733\":[11.2,98]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:06:86:68\":{\"res\":{\"1571780749\":[1022.2]},\"type\":[\"pressure\"]},\"05:00:00:02:97:82\":{\"rain_60min\":0,\"rain_24h\":0,\"rain_live\":0,\"rain_timeutc\":1571780739}},\"modules\":[\"02:00:00:2e:eb:14\",\"05:00:00:02:97:82\"],\"module_types\":{\"02:00:00:2e:eb:14\":\"NAModule1\",\"05:00:00:02:97:82\":\"NAModule3\"}},{\"_id\":\"70:ee:50:00:a4:fa\",\"place\":{\"location\":[8.547313,47.381006],\"timezone\":\"Europe/Zurich\",\"country\":\"CH\",\"altitude\":470,\"city\":\"Zurich\",\"street\":\"Culmannstrasse\"},\"mark\":14,\"measures\":{\"02:00:00:00:9f:be\":{\"res\":{\"1571780586\":[13.6,86]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:00:a4:fa\":{\"res\":{\"1571780629\":[1022.2]},\"type\":[\"pressure\"]},\"05:00:00:02:41:1c\":{\"rain_60min\":0,\"rain_24h\":0,\"rain_live\":0,\"rain_timeutc\":1571780624},\"06:00:00:00:e9:88\":{\"wind_strength\":1,\"wind_angle\":270,\"gust_strength\":2,\"gust_angle\":270,\"wind_timeutc\":1571780625}},\"modules\":[\"02:00:00:00:9f:be\",\"05:00:00:02:41:1c\",\"06:00:00:00:e9:88\"],\"module_types\":{\"02:00:00:00:9f:be\":\"NAModule1\",\"05:00:00:02:41:1c\":\"NAModule3\",\"06:00:00:00:e9:88\":\"NAModule2\"}}]}");

        List<String> types = Arrays.asList(Params.TYPE_RAIN);

        List<Map.Entry<Station, Measures>> measures = client.getPublicData(47.38d, 8.55d, 47.37d, 8.54d, types, false);
        assertEquals(13, measures.size());
        assertEquals(0.0, measures.get(1).getValue().getRain(), 0.0);
        assertEquals(0.0, measures.get(1).getValue().getSum_rain_1(), 0.0);
        assertEquals(0.202, measures.get(1).getValue().getSum_rain_24(), 0.0);

    }

    @Test
    public void getPublicData() throws OAuthProblemException, OAuthSystemException, IllegalAccessException {

        NetatmoHttpClient client = prepareToRespond("{\"body\":[{\"_id\":\"70:ee:50:17:eb:b6\",\"place\":{\"location\":[8.7272458797622,41.913026000486],\"altitude\":6,\"timezone\":\"Europe/Paris\"},\"mark\":3,\"measures\":{\"02:00:00:17:d0:b2\":{\"res\":{\"1528826083\":[23,75]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:17:eb:b6\":{\"res\":{\"1528826084\":[1010.9]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:17:d0:b2\"],\"module_types\":{\"02:00:00:17:d0:b2\":\"NAModule1\"}},{\"_id\":\"70:ee:50:02:b3:2c\",\"place\":{\"location\":[8.7115285956954,41.909349035887],\"altitude\":11,\"timezone\":\"Europe/Paris\"},\"mark\":10,\"measures\":{\"02:00:00:02:d8:be\":{\"res\":{\"1528826231\":[24,66]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:02:b3:2c\":{\"res\":{\"1528826245\":[1012.4]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:02:d8:be\"],\"module_types\":{\"02:00:00:02:d8:be\":\"NAModule1\"}},{\"_id\":\"70:ee:50:02:be:88\",\"place\":{\"location\":[8.7395163329231,41.918458845976],\"altitude\":8,\"timezone\":\"Europe/Paris\"},\"mark\":5,\"measures\":{\"02:00:00:02:d1:9e\":{\"res\":{\"1528826101\":[24.1,72]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:02:be:88\":{\"res\":{\"1528826111\":[1012.6]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:02:d1:9e\"],\"module_types\":{\"02:00:00:02:d1:9e\":\"NAModule1\"}},{\"_id\":\"70:ee:50:01:49:9e\",\"place\":{\"location\":[8.7703885,41.971943],\"altitude\":169,\"timezone\":\"Europe/Paris\"},\"mark\":13,\"measures\":{\"02:00:00:01:3f:b8\":{\"res\":{\"1528826025\":[24.3,67]},\"type\":[\"temperature\",\"humidity\"]},\"05:00:00:00:15:36\":{\"rain_60min\":0,\"rain_24h\":0.303,\"rain_live\":0,\"rain_timeutc\":1528826057},\"06:00:00:02:35:ae\":{\"wind_strength\":9,\"wind_angle\":243,\"gust_strength\":19,\"gust_angle\":238,\"wind_timeutc\":1528826064},\"70:ee:50:01:49:9e\":{\"res\":{\"1528826068\":[1011.1]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:01:3f:b8\",\"05:00:00:00:15:36\",\"06:00:00:02:35:ae\"],\"module_types\":{\"02:00:00:01:3f:b8\":\"NAModule1\",\"05:00:00:00:15:36\":\"NAModule3\",\"06:00:00:02:35:ae\":\"NAModule2\"}},{\"_id\":\"70:ee:50:1e:10:d2\",\"place\":{\"location\":[8.740494,41.946683],\"timezone\":\"Europe/Paris\"},\"measures\":{\"02:00:00:1d:f4:26\":{\"res\":{\"1528826132\":[22.8,73]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:1e:10:d2\":{\"res\":{\"1528826170\":[1002.2]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:1d:f4:26\"],\"module_types\":{\"02:00:00:1d:f4:26\":\"NAModule1\"}},{\"_id\":\"70:ee:50:13:61:5c\",\"place\":{\"location\":[8.7245946037049,41.965660480989],\"altitude\":184,\"timezone\":\"Europe/Paris\"},\"measures\":{\"70:ee:50:13:61:5c\":{\"res\":{\"1528825916\":[1012.7]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:13:52:22\"],\"module_types\":{\"02:00:00:13:52:22\":\"NAModule1\"}},{\"_id\":\"70:ee:50:1a:fc:7e\",\"place\":{\"location\":[8.5974025655762,42.138857026067],\"altitude\":76,\"timezone\":\"Europe/Paris\"},\"mark\":0,\"measures\":{\"02:00:00:1a:f3:72\":{\"res\":{\"1528825843\":[24.8,66]},\"type\":[\"temperature\",\"humidity\"]},\"05:00:00:01:bb:a2\":{\"rain_60min\":0,\"rain_24h\":5.05,\"rain_live\":0,\"rain_timeutc\":1528825844},\"06:00:00:00:99:de\":{\"wind_strength\":1,\"wind_angle\":135,\"gust_strength\":3,\"gust_angle\":180,\"wind_timeutc\":1528825844},\"70:ee:50:1a:fc:7e\":{\"res\":{\"1528825844\":[1009.3]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:1a:f3:72\",\"05:00:00:01:bb:a2\",\"06:00:00:00:99:de\"],\"module_types\":{\"02:00:00:1a:f3:72\":\"NAModule1\",\"05:00:00:01:bb:a2\":\"NAModule3\",\"06:00:00:00:99:de\":\"NAModule2\"}},{\"_id\":\"70:ee:50:28:ac:d2\",\"place\":{\"location\":[8.5944775,42.1315259],\"altitude\":23,\"timezone\":\"Europe/Paris\"},\"mark\":0,\"measures\":{\"02:00:00:27:1d:4c\":{\"res\":{\"1528825993\":[23.9,72]},\"type\":[\"temperature\",\"humidity\"]},\"05:00:00:03:b5:ca\":{\"rain_60min\":0,\"rain_24h\":0,\"rain_live\":0,\"rain_timeutc\":1528825844},\"06:00:00:02:01:a6\":{\"wind_strength\":1,\"wind_angle\":321,\"gust_strength\":3,\"gust_angle\":260,\"wind_timeutc\":1528826019},\"70:ee:50:28:ac:d2\":{\"res\":{\"1528826021\":[1013.1]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:27:1d:4c\",\"06:00:00:02:01:a6\",\"05:00:00:03:b5:ca\"],\"module_types\":{\"02:00:00:27:1d:4c\":\"NAModule1\",\"06:00:00:02:01:a6\":\"NAModule2\",\"05:00:00:03:b5:ca\":\"NAModule3\"}},{\"_id\":\"70:ee:50:22:e8:c6\",\"place\":{\"location\":[8.68583,42.11846],\"altitude\":58,\"timezone\":\"Europe/Paris\"},\"mark\":14,\"measures\":{\"02:00:00:22:e7:d6\":{\"res\":{\"1528826375\":[20.2,96]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:22:e8:c6\":{\"res\":{\"1528826375\":[1008.7]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:22:e7:d6\"],\"module_types\":{\"02:00:00:22:e7:d6\":\"NAModule1\"}},{\"_id\":\"70:ee:50:2d:00:76\",\"place\":{\"location\":[8.7585094603271,42.563921350366],\"altitude\":0,\"timezone\":\"Europe/Paris\"},\"mark\":5,\"measures\":{\"02:00:00:2c:c0:ae\":{\"res\":{\"1528826150\":[23.5,69]},\"type\":[\"temperature\",\"humidity\"]},\"05:00:00:03:ce:e8\":{\"rain_60min\":0,\"rain_24h\":0,\"rain_live\":0,\"rain_timeutc\":1528826188},\"06:00:00:02:f0:46\":{\"wind_strength\":7,\"wind_angle\":233,\"gust_strength\":18,\"gust_angle\":234,\"wind_timeutc\":1528826195},\"70:ee:50:2d:00:76\":{\"res\":{\"1528826198\":[1015.1]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:2c:c0:ae\",\"05:00:00:03:ce:e8\",\"06:00:00:02:f0:46\"],\"module_types\":{\"02:00:00:2c:c0:ae\":\"NAModule1\",\"05:00:00:03:ce:e8\":\"NAModule3\",\"06:00:00:02:f0:46\":\"NAModule2\"}},{\"_id\":\"70:ee:50:2c:77:7a\",\"place\":{\"location\":[8.9091853,41.6992683],\"timezone\":\"Europe/Paris\"},\"measures\":{\"02:00:00:2c:76:4e\":{\"res\":{\"1528826255\":[24.7,90]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:2c:77:7a\":{\"res\":{\"1528826295\":[995.7]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:2c:76:4e\"],\"module_types\":{\"02:00:00:2c:76:4e\":\"NAModule1\"}},{\"_id\":\"70:ee:50:19:18:68\",\"place\":{\"location\":[8.8079093,41.8867502],\"altitude\":230,\"timezone\":\"Europe/Paris\"},\"mark\":12,\"measures\":{\"02:00:00:19:3d:d4\":{\"res\":{\"1528826285\":[24.2,69]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:19:18:68\":{\"res\":{\"1528826298\":[1033.2]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:19:3d:d4\"],\"module_types\":{\"02:00:00:19:3d:d4\":\"NAModule1\"}},{\"_id\":\"70:ee:50:28:ac:32\",\"place\":{\"location\":[8.8444082,41.9079819],\"altitude\":0,\"timezone\":\"Europe/Paris\"},\"mark\":5,\"measures\":{\"02:00:00:28:c0:d8\":{\"res\":{\"1528825679\":[25,69]},\"type\":[\"temperature\",\"humidity\"]},\"05:00:00:03:1c:2a\":{\"rain_60min\":0,\"rain_24h\":3.737,\"rain_live\":0,\"rain_timeutc\":1528825704},\"06:00:00:01:4f:38\":{\"wind_strength\":1,\"wind_angle\":11,\"gust_strength\":3,\"gust_angle\":0,\"wind_timeutc\":1528825711},\"70:ee:50:28:ac:32\":{\"res\":{\"1528825711\":[1011.4]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:28:c0:d8\",\"05:00:00:03:1c:2a\",\"06:00:00:01:4f:38\"],\"module_types\":{\"02:00:00:28:c0:d8\":\"NAModule1\",\"05:00:00:03:1c:2a\":\"NAModule3\",\"06:00:00:01:4f:38\":\"NAModule2\"}},{\"_id\":\"70:ee:50:01:f2:ae\",\"place\":{\"location\":[8.824787,41.904886],\"altitude\":46,\"timezone\":\"Europe/Paris\"},\"mark\":13,\"measures\":{\"02:00:00:01:c3:c4\":{\"res\":{\"1528826308\":[23.1,76]},\"type\":[\"temperature\",\"humidity\"]},\"05:00:00:00:9b:3c\":{\"rain_60min\":0,\"rain_24h\":0.404,\"rain_live\":0,\"rain_timeutc\":1528826308},\"70:ee:50:01:f2:ae\":{\"res\":{\"1528826315\":[1011.8]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:01:c3:c4\",\"05:00:00:00:9b:3c\"],\"module_types\":{\"02:00:00:01:c3:c4\":\"NAModule1\",\"05:00:00:00:9b:3c\":\"NAModule3\"}},{\"_id\":\"70:ee:50:2c:ef:2c\",\"place\":{\"location\":[3.752898,43.447014],\"timezone\":\"Europe/Paris\"},\"measures\":{\"02:00:00:2c:71:ec\":{\"res\":{\"1528826331\":[24.3,49]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:2c:ef:2c\":{\"res\":{\"1528826376\":[1000.5]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:2c:71:ec\"],\"module_types\":{\"02:00:00:2c:71:ec\":\"NAModule1\"}},{\"_id\":\"70:ee:50:29:08:a2\",\"place\":{\"location\":[3.7512656,43.4529247],\"altitude\":10,\"timezone\":\"Europe/Madrid\"},\"mark\":8,\"measures\":{\"02:00:00:29:5a:76\":{\"res\":{\"1528826305\":[26.4,49]},\"type\":[\"temperature\",\"humidity\"]},\"70:ee:50:29:08:a2\":{\"res\":{\"1528826345\":[1007.5]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:29:5a:76\"],\"module_types\":{\"02:00:00:29:5a:76\":\"NAModule1\"}},{\"_id\":\"70:ee:50:1f:5f:74\",\"place\":{\"location\":[4.430916,43.455831],\"timezone\":\"Europe/Paris\"},\"measures\":{\"02:00:00:1f:77:06\":{\"res\":{\"1528826350\":[24.4,74]},\"type\":[\"temperature\",\"humidity\"]},\"05:00:00:02:fd:10\":{\"rain_60min\":0,\"rain_24h\":0.101,\"rain_live\":0,\"rain_timeutc\":1528826357},\"70:ee:50:1f:5f:74\":{\"res\":{\"1528826369\":[1005.1]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:1f:77:06\",\"05:00:00:02:fd:10\"],\"module_types\":{\"02:00:00:1f:77:06\":\"NAModule1\",\"05:00:00:02:fd:10\":\"NAModule3\"}},{\"_id\":\"70:ee:50:2a:ea:04\",\"place\":{\"location\":[4.7250787,43.4643594],\"altitude\":3,\"timezone\":\"Europe/Paris\"},\"mark\":14,\"measures\":{\"02:00:00:2b:60:d2\":{\"res\":{\"1528826030\":[21.6,72]},\"type\":[\"temperature\",\"humidity\"]},\"05:00:00:04:73:8e\":{\"rain_60min\":0,\"rain_24h\":0,\"rain_live\":0,\"rain_timeutc\":1528826049},\"06:00:00:02:55:f0\":{\"wind_strength\":7,\"wind_angle\":217,\"gust_strength\":12,\"gust_angle\":216,\"wind_timeutc\":1528826056},\"70:ee:50:2a:ea:04\":{\"res\":{\"1528826062\":[1012.7]},\"type\":[\"pressure\"]}},\"modules\":[\"02:00:00:2b:60:d2\",\"05:00:00:04:73:8e\",\"06:00:00:02:55:f0\"],\"module_types\":{\"02:00:00:2b:60:d2\":\"NAModule1\",\"05:00:00:04:73:8e\":\"NAModule3\",\"06:00:00:02:55:f0\":\"NAModule2\"}}],\"status\":\"ok\",\"time_exec\":1.3139359951019,\"time_server\":1528826441}");


        List<Map.Entry<Station, Measures>> measures = client.getPublicData(43.1, 8.5, 42.6, 4.4, Arrays.asList("temperature","humidity"), false);


        assertEquals(49, measures.size());
        Map.Entry<Station, Measures> s1m0 = measures.get(0);
        assertEquals("70:ee:50:17:eb:b6", s1m0.getKey().getId());
        assertEquals("41.913026,8.727246", s1m0.getKey().getName());
        assertEquals(0, s1m0.getKey().getModules().size());
        assertEquals(1528826084, s1m0.getValue().getBeginTime());
        assertEquals(1010.9, s1m0.getValue().getPressure(),0.0);

        Map.Entry<Station, Measures> s1m1 = measures.get(1);
        assertEquals("70:ee:50:17:eb:b6", s1m1.getKey().getId());
        assertEquals("41.913026,8.727246", s1m1.getKey().getName());
        assertEquals(1, s1m1.getKey().getModules().size());
        assertEquals("02:00:00:17:d0:b2", s1m1.getKey().getModules().get(0).getId());
        assertEquals(null, s1m1.getKey().getModules().get(0).getName());
        assertEquals(Module.TYPE_OUTDOOR, s1m1.getKey().getModules().get(0).getType());
        assertEquals(1528826083, s1m1.getValue().getBeginTime());
        assertEquals(23.0, s1m1.getValue().getTemperature(),0.0);
        assertEquals(75.0, s1m1.getValue().getHumidity(),0.0);

        Map.Entry<Station, Measures> s24 = measures.get(24);
        assertEquals("70:ee:50:2d:00:76", s24.getKey().getId());
        assertEquals("42.563921,8.758509", s24.getKey().getName());
        assertEquals(1, s24.getKey().getModules().size());
        assertEquals("06:00:00:02:f0:46", s24.getKey().getModules().get(0).getId());
        assertEquals(null, s24.getKey().getModules().get(0).getName());
        assertEquals(Module.TYPE_WIND_GAUGE, s24.getKey().getModules().get(0).getType());
        assertEquals(1528826195, s24.getValue().getBeginTime());
        assertEquals(233.0, s24.getValue().getWindAngle(),0.0);
        assertEquals(7.0, s24.getValue().getWindStrength(),0.0);
        assertEquals(234.0, s24.getValue().getGustAngle(),0.0);
        assertEquals(18.0, s24.getValue().getGustStrength(),0.0);

        Map.Entry<Station, Measures> s26 = measures.get(26);
        assertEquals("70:ee:50:2d:00:76", s26.getKey().getId());
        assertEquals("42.563921,8.758509", s26.getKey().getName());
        assertEquals(1, s26.getKey().getModules().size());
        assertEquals("05:00:00:03:ce:e8", s26.getKey().getModules().get(0).getId());
        assertEquals(null, s26.getKey().getModules().get(0).getName());
        assertEquals(Module.TYPE_RAIN_GAUGE, s26.getKey().getModules().get(0).getType());
        assertEquals(1528826188, s26.getValue().getBeginTime());
        assertEquals(0.0, s26.getValue().getRain(),0.0);
        assertEquals(0.0, s26.getValue().getSum_rain_1(),0.0);
        assertEquals(0.0, s26.getValue().getSum_rain_24(),0.0);
    }

    @Test
    public void getHomesdata() throws OAuthProblemException, OAuthSystemException, IllegalAccessException {
        NetatmoHttpClient client = prepareToRespond("{\n" +
                "    \"body\": {\n" +
                "        \"homes\": [\n" +
                "            {\n" +
                "                \"id\": \"xxxxxxxxxxxxxxxxxxxxxxxx\",\n" +
                "                \"name\": \"Thermostat\",\n" +
                "                \"altitude\": 0,\n" +
                "                \"coordinates\": [\n" +
                "                    14.247799784851,\n" +
                "                    40.829912554443\n" +
                "                ],\n" +
                "                \"country\": \"IT\",\n" +
                "                \"timezone\": \"Europe/Rome\",\n" +
                "                \"rooms\": [\n" +
                "                    {\n" +
                "                        \"id\": \"xxxxxxxxx\",\n" +
                "                        \"name\": \"My room\",\n" +
                "                        \"type\": \"custom\",\n" +
                "                        \"module_ids\": [\n" +
                "                            \"xx:xx:xx:xx:xx:xx\"\n" +
                "                        ],\n" +
                "                        \"measure_offset_NAPlug_temperature\": 0,\n" +
                "                        \"measure_offset_NAPlug_estimated_temperature\": 0\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"modules\": [\n" +
                "                    {\n" +
                "                        \"id\": \"xx:xx:xx:xx:xx:xx\",\n" +
                "                        \"type\": \"NAPlug\",\n" +
                "                        \"name\": \"Thermostat\",\n" +
                "                        \"setup_date\": 1479455690,\n" +
                "                        \"modules_bridged\": [\n" +
                "                            \"xx:xx:xx:xx:xx:xx\"\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": \"xx:xx:xx:xx:xx:xx\",\n" +
                "                        \"type\": \"NATherm1\",\n" +
                "                        \"name\": \"Thermostat\",\n" +
                "                        \"setup_date\": 1479455691,\n" +
                "                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                        \"bridge\": \"xx:xx:xx:xx:xx:xx\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"therm_schedules\": [\n" +
                "                    {\n" +
                "                        \"zones\": [\n" +
                "                            {\n" +
                "                                \"type\": 0,\n" +
                "                                \"id\": 0,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 22\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"type\": 1,\n" +
                "                                \"id\": 1,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 20\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"type\": 5,\n" +
                "                                \"id\": 4,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 16\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"timetable\": [\n" +
                "                            {\n" +
                "                                \"m_offset\": 0,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 360,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 480,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 1200,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 1380,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 1800,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 1920,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 2640,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 2820,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 3240,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 3360,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 4080,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 4260,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 4680,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 4800,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 5520,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 5700,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 6120,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 6240,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 6960,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 7140,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 7560,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 8580,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 9000,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 10020,\n" +
                "                                \"zone_id\": 1\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"name\": \"Default\",\n" +
                "                        \"hg_temp\": 7,\n" +
                "                        \"away_temp\": 15,\n" +
                "                        \"id\": \"xxxxxxxxxxxxxxxxxxxxxxxx\",\n" +
                "                        \"type\": \"therm\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"timetable\": [\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 420\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 540\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 960\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 1290\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 1860\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 1980\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 2400\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 2730\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 3300\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 3420\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 3840\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 4170\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 4740\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 4860\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 5280\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 5610\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 6180\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 6300\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 6720\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 7050\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 7620\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 7740\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 8160\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 8490\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 9060\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 9180\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 9600\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 9930\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"zones\": [\n" +
                "                            {\n" +
                "                                \"name\": \"Night\",\n" +
                "                                \"id\": 1,\n" +
                "                                \"type\": 1,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 18\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"name\": \"Economy\",\n" +
                "                                \"id\": 4,\n" +
                "                                \"type\": 5,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 16\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"name\": \"Comfort\",\n" +
                "                                \"id\": 0,\n" +
                "                                \"type\": 0,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 20\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"name\": \"Morning\",\n" +
                "                                \"id\": 7044,\n" +
                "                                \"type\": 4,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 19\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"name\": \"Scheduling \",\n" +
                "                        \"default\": false,\n" +
                "                        \"away_temp\": 12,\n" +
                "                        \"hg_temp\": 7,\n" +
                "                        \"id\": \"xxxxxxxxxxxxxxxxxxxxxxxx\",\n" +
                "                        \"selected\": true,\n" +
                "                        \"type\": \"therm\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"therm_setpoint_default_duration\": 180,\n" +
                "                \"schedules\": [\n" +
                "                    {\n" +
                "                        \"zones\": [\n" +
                "                            {\n" +
                "                                \"type\": 0,\n" +
                "                                \"id\": 0,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 22\n" +
                "                                    }\n" +
                "                                ],\n" +
                "                                \"rooms\": [\n" +
                "                                    {\n" +
                "                                        \"id\": \"xxxxxxxxx\",\n" +
                "                                        \"therm_setpoint_temperature\": 22\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"type\": 1,\n" +
                "                                \"id\": 1,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 20\n" +
                "                                    }\n" +
                "                                ],\n" +
                "                                \"rooms\": [\n" +
                "                                    {\n" +
                "                                        \"id\": \"xxxxxxxxx\",\n" +
                "                                        \"therm_setpoint_temperature\": 20\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"type\": 5,\n" +
                "                                \"id\": 4,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 16\n" +
                "                                    }\n" +
                "                                ],\n" +
                "                                \"rooms\": [\n" +
                "                                    {\n" +
                "                                        \"id\": \"xxxxxxxxx\",\n" +
                "                                        \"therm_setpoint_temperature\": 16\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"timetable\": [\n" +
                "                            {\n" +
                "                                \"m_offset\": 0,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 360,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 480,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 1200,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 1380,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 1800,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 1920,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 2640,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 2820,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 3240,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 3360,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 4080,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 4260,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 4680,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 4800,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 5520,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 5700,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 6120,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 6240,\n" +
                "                                \"zone_id\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 6960,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 7140,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 7560,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 8580,\n" +
                "                                \"zone_id\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 9000,\n" +
                "                                \"zone_id\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"m_offset\": 10020,\n" +
                "                                \"zone_id\": 1\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"name\": \"Default\",\n" +
                "                        \"hg_temp\": 7,\n" +
                "                        \"away_temp\": 15,\n" +
                "                        \"id\": \"xxxxxxxxxxxxxxxxxxxxxxxx\",\n" +
                "                        \"type\": \"therm\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"timetable\": [\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 420\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 540\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 960\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 1290\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 1860\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 1980\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 2400\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 2730\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 3300\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 3420\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 3840\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 4170\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 4740\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 4860\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 5280\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 5610\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 6180\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 6300\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 6720\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 7050\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 7620\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 7740\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 8160\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 8490\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 9060\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 7044,\n" +
                "                                \"m_offset\": 9180\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 0,\n" +
                "                                \"m_offset\": 9600\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"zone_id\": 1,\n" +
                "                                \"m_offset\": 9930\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"zones\": [\n" +
                "                            {\n" +
                "                                \"name\": \"Night\",\n" +
                "                                \"id\": 1,\n" +
                "                                \"type\": 1,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 18\n" +
                "                                    }\n" +
                "                                ],\n" +
                "                                \"rooms\": [\n" +
                "                                    {\n" +
                "                                        \"id\": \"xxxxxxxxx\",\n" +
                "                                        \"therm_setpoint_temperature\": 18\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"name\": \"Economy\",\n" +
                "                                \"id\": 4,\n" +
                "                                \"type\": 5,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 16\n" +
                "                                    }\n" +
                "                                ],\n" +
                "                                \"rooms\": [\n" +
                "                                    {\n" +
                "                                        \"id\": \"xxxxxxxxx\",\n" +
                "                                        \"therm_setpoint_temperature\": 16\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"name\": \"Comfort\",\n" +
                "                                \"id\": 0,\n" +
                "                                \"type\": 0,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 20\n" +
                "                                    }\n" +
                "                                ],\n" +
                "                                \"rooms\": [\n" +
                "                                    {\n" +
                "                                        \"id\": \"xxxxxxxxx\",\n" +
                "                                        \"therm_setpoint_temperature\": 20\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"name\": \"Morning\",\n" +
                "                                \"id\": 7044,\n" +
                "                                \"type\": 4,\n" +
                "                                \"rooms_temp\": [\n" +
                "                                    {\n" +
                "                                        \"room_id\": \"xxxxxxxxx\",\n" +
                "                                        \"temp\": 19\n" +
                "                                    }\n" +
                "                                ],\n" +
                "                                \"rooms\": [\n" +
                "                                    {\n" +
                "                                        \"id\": \"xxxxxxxxx\",\n" +
                "                                        \"therm_setpoint_temperature\": 19\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"name\": \"Scheduling \",\n" +
                "                        \"default\": false,\n" +
                "                        \"away_temp\": 12,\n" +
                "                        \"hg_temp\": 7,\n" +
                "                        \"id\": \"xxxxxxxxxxxxxxxxxxxxxxxx\",\n" +
                "                        \"selected\": true,\n" +
                "                        \"type\": \"therm\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"therm_mode\": \"schedule\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"user\": {\n" +
                "            \"email\": \"user@email\",\n" +
                "            \"language\": \"it-IT\",\n" +
                "            \"locale\": \"it-IT\",\n" +
                "            \"feel_like_algorithm\": 0,\n" +
                "            \"unit_pressure\": 0,\n" +
                "            \"unit_system\": 0,\n" +
                "            \"unit_wind\": 0,\n" +
                "            \"id\": \"xxxxxxxxxxxxxxxxxxxxxxxx\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"status\": \"ok\",\n" +
                "    \"time_exec\": 0.039823055267334,\n" +
                "    \"time_server\": 1547448595\n" +
                "}");

        List<Home> homeList = client.getHomesdata();

        assertEquals(1, homeList.size());
        Home home = homeList.get(0);
        assertEquals("xxxxxxxxxxxxxxxxxxxxxxxx", home.getId());
        assertEquals("Thermostat", home.getName());
    }

    @Test
    public void getHomestatus() throws OAuthProblemException, OAuthSystemException, IllegalAccessException {
        NetatmoHttpClient client = prepareToRespond("{\n" +
                "    \"status\": \"ok\",\n" +
                "    \"time_server\": 1547560610,\n" +
                "    \"body\": {\n" +
                "        \"home\": {\n" +
                "            \"modules\": [\n" +
                "                {\n" +
                "                    \"id\": \"70:xx:xx:xx:xx:xx\",\n" +
                "                    \"type\": \"NAPlug\",\n" +
                "                    \"firmware_revision\": 174,\n" +
                "                    \"rf_strength\": 104,\n" +
                "                    \"wifi_strength\": 86\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"04:xx:xx:xx:xx:xx\",\n" +
                "                    \"reachable\": true,\n" +
                "                    \"type\": \"NATherm1\",\n" +
                "                    \"firmware_revision\": 65,\n" +
                "                    \"rf_strength\": 70,\n" +
                "                    \"battery_level\": 4400,\n" +
                "                    \"boiler_valve_comfort_boost\": false,\n" +
                "                    \"boiler_status\": false,\n" +
                "                    \"anticipating\": false,\n" +
                "                    \"bridge\": \"70:xx:xx:xx:xx:xx\",\n" +
                "                    \"battery_state\": \"full\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"rooms\": [\n" +
                "                {\n" +
                "                    \"id\": \"xxxxxxxxx\",\n" +
                "                    \"reachable\": true,\n" +
                "                    \"therm_measured_temperature\": 19,\n" +
                "                    \"therm_setpoint_temperature\": 19,\n" +
                "                    \"therm_setpoint_mode\": \"schedule\",\n" +
                "                    \"therm_setpoint_start_time\": 1547539200,\n" +
                "                    \"therm_setpoint_end_time\": 0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"id\": \"xxxxxxxxxxxxxxxxxxxxxxxx\"\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Home home = new Home();
        home.setId("xxxxxxxxxxxxxxxxxxxxxxxx");
        home.setName("Thermostat");
        home = client.getHomestatus(home);

        assertEquals(2, home.getModules().size());
        assertEquals(1, home.getRooms().size());

        Module naplug = home.getModules().get(0);
        assertEquals("70:xx:xx:xx:xx:xx", naplug.getId());
        assertEquals(Module.TYPE_NA_PLUG, naplug.getType());

        Module natherm1 = home.getModules().get(1);
        assertEquals("04:xx:xx:xx:xx:xx", natherm1.getId());
        assertEquals(Module.TYPE_NA_THERM_1, natherm1.getType());
        assertEquals(true, natherm1.isReachable());
        assertEquals(false, natherm1.isBoilerStatus());
    }

    @Test
    public void getHomestatusWithNulls() throws OAuthProblemException, OAuthSystemException, IllegalAccessException {
        NetatmoHttpClient client = prepareToRespond("{\n" +
                "    \"status\": \"ok\",\n" +
                "    \"time_server\": 1547560610,\n" +
                "    \"body\": {\n" +
                "        \"home\": {\n" +
                "            \"modules\": [\n" +
                "                {\n" +
                "                    \"id\": \"70:xx:xx:xx:xx:xx\",\n" +
                "                    \"type\": \"NAPlug\",\n" +
                "                    \"firmware_revision\": 174,\n" +
                "                    \"rf_strength\": 104,\n" +
                "                    \"wifi_strength\": 86\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"04:xx:xx:xx:xx:xx\",\n" +
                "                    \"reachable\": true,\n" +
                "                    \"type\": \"NATherm1\",\n" +
                "                    \"firmware_revision\": 65,\n" +
                "                    \"rf_strength\": 70,\n" +
                "                    \"battery_level\": 4400,\n" +
                "                    \"boiler_valve_comfort_boost\": false,\n" +
                "                    \"boiler_status\": false,\n" +
                "                    \"anticipating\": false,\n" +
                "                    \"bridge\": \"70:xx:xx:xx:xx:xx\",\n" +
                "                    \"battery_state\": \"full\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"rooms\": [\n" +
                "                {\n" +
                "                    \"id\": \"xxxxxxxxx\",\n" +
                "                    \"reachable\": true,\n" +
                "                    \"therm_measured_temperature\": 19,\n" +
                "                    \"therm_setpoint_temperature\": 19,\n" +
                "                    \"therm_setpoint_mode\": \"schedule\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"id\": \"xxxxxxxxxxxxxxxxxxxxxxxx\"\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Home home = new Home();
        home.setId("xxxxxxxxxxxxxxxxxxxxxxxx");
        home.setName("Thermostat");
        home = client.getHomestatus(home);

        assertEquals(2, home.getModules().size());
        assertEquals(1, home.getRooms().size());

        Module naplug = home.getModules().get(0);
        assertEquals("70:xx:xx:xx:xx:xx", naplug.getId());
        assertEquals(Module.TYPE_NA_PLUG, naplug.getType());

        Module natherm1 = home.getModules().get(1);
        assertEquals("04:xx:xx:xx:xx:xx", natherm1.getId());
        assertEquals(Module.TYPE_NA_THERM_1, natherm1.getType());
        assertEquals(true, natherm1.isReachable());
        assertEquals(false, natherm1.isBoilerStatus());
    }

    private static NetatmoHttpClient prepareToRespond(String string) throws OAuthProblemException, OAuthSystemException, IllegalAccessException {
        OAuthTokenStore oauthTokenStore = new TransientOAuthTokenStore();
        oauthTokenStore.setTokens("refreshToken", "accessToken", System.currentTimeMillis() + 20000);
        NetatmoHttpClient client = new NetatmoHttpClient("client_id", "client_secret", oauthTokenStore);

        OAuthResourceResponse response = mock(OAuthResourceResponse.class);
        when(response.getBody()).thenReturn(string);
        OAuthClient oAuthClient = mock(OAuthClient.class);
        when(oAuthClient.resource(any(OAuthClientRequest.class), eq(OAuth.HttpMethod.GET), eq(OAuthResourceResponse.class))).thenReturn(response);

        OAuthTokenHandler oAuthTokenHandler = (OAuthTokenHandler) FieldUtils.readField(client, "oAuthTokenHandler", true);
        FieldUtils.writeField(oAuthTokenHandler, "oAuthClient", oAuthClient, true);
        
        return client;
    }
}