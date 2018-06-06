package losty.netatmo;

import losty.netatmo.model.Measures;
import losty.netatmo.model.Module;
import losty.netatmo.model.Params;
import losty.netatmo.model.Station;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static losty.netatmo.model.Module.TYPE_INDOOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetatmoHttpClientTest {

    @Test
    public void testGetMeasures() throws IllegalAccessException, OAuthProblemException, OAuthSystemException {
        OAuthResourceResponse response = mock(OAuthResourceResponse.class);
        when(response.getBody()). thenReturn("{\"body\":[{\"beg_time\":1505001741,\"step_time\":308,\"value\":[[12.7,null,84],[12.7,null,84],[12.7,null,84]]},{\"beg_time\":1505002663,\"step_time\":308,\"value\":[[12.7,null,84],[12.6,null,84],[12.7,null,84]]},{\"beg_time\":1505003586,\"step_time\":308,\"value\":[[12.7,null,83],[12.7,null,84]]},{\"beg_time\":1505004150,\"step_time\":308,\"value\":[[12.7,null,83],[12.7,null,83]]},{\"beg_time\":1505004765,\"step_time\":309,\"value\":[[12.7,null,83],[12.7,null,84]]},{\"beg_time\":1505005382,\"step_time\":307,\"value\":[[12.7,null,84],[12.7,null,85]]},{\"beg_time\":1505005997,\"step_time\":307,\"value\":[[12.7,null,84],[12.7,null,84]]},{\"beg_time\":1505006561,\"step_time\":306,\"value\":[[12.7,null,84],[12.7,null,84]]},{\"beg_time\":1505007175,\"step_time\":308,\"value\":[[12.6,null,84],[12.6,null,85]]},{\"beg_time\":1505007790,\"step_time\":308,\"value\":[[12.6,null,84],[12.6,null,84]]},{\"beg_time\":1505008405,\"step_time\":308,\"value\":[[12.6,null,84],[12.5,null,84]]},{\"beg_time\":1505008969,\"step_time\":309,\"value\":[[12.5,null,84],[12.5,null,84]]},{\"beg_time\":1505009586,\"step_time\":306,\"value\":[[12.5,null,84],[12.4,null,85]]},{\"beg_time\":1505010200,\"step_time\":308,\"value\":[[12.4,null,85],[12.4,null,85]]},{\"beg_time\":1505010815,\"step_time\":308,\"value\":[[12.4,null,85],[12.4,null,86]]},{\"beg_time\":1505011379,\"step_time\":308,\"value\":[[12.4,null,86],[12.5,null,86]]},{\"beg_time\":1505011994,\"step_time\":308,\"value\":[[12.5,null,85],[12.5,null,85],[12.5,null,85],[12.5,null,85],[12.5,null,85]]},{\"beg_time\":1505013532,\"step_time\":257,\"value\":[[12.5,null,85],[12.4,null,85]]},{\"beg_time\":1505014096,\"step_time\":308,\"value\":[[12.3,null,86],[12.3,null,86],[12.3,null,86],[12.3,null,87],[12.3,null,87]]},{\"beg_time\":1505015634,\"step_time\":308,\"value\":[[12.3,null,87],[12.3,null,87]]},{\"beg_time\":1505016198,\"step_time\":308,\"value\":[[12.3,null,87],[12.4,null,87]]},{\"beg_time\":1505016815,\"step_time\":307,\"value\":[[12.4,null,87],[12.4,null,86],[12.5,null,86]]},{\"beg_time\":1505017737,\"step_time\":307,\"value\":[[12.5,null,86],[12.4,null,86]]},{\"beg_time\":1505018352,\"step_time\":256,\"value\":[[12.4,null,86],[12.4,null,85]]},{\"beg_time\":1505018916,\"step_time\":307,\"value\":[[12.4,null,85],[12.4,null,85]]},{\"beg_time\":1505019531,\"step_time\":309,\"value\":[[12.4,null,85],[12.5,null,85]]},{\"beg_time\":1505020147,\"step_time\":307,\"value\":[[12.4,null,85],[12.4,null,86],[12.4,null,86]]},{\"beg_time\":1505021018,\"step_time\":307,\"value\":[[12.4,null,86],[12.4,null,86]]},{\"beg_time\":1505021633,\"step_time\":308,\"value\":[[12.4,null,86],[12.4,null,87]]},{\"beg_time\":1505022248,\"step_time\":308,\"value\":[[12.4,null,87],[12.5,null,87]]},{\"beg_time\":1505022863,\"step_time\":308,\"value\":[[12.5,null,86],[12.4,null,86]]},{\"beg_time\":1505023427,\"step_time\":308,\"value\":[[12.4,null,86],[12.4,null,86],[12.4,null,86]]},{\"beg_time\":1505024350,\"step_time\":308,\"value\":[[12.4,null,86],[12.4,null,86],[12.5,null,86]]},{\"beg_time\":1505025273,\"step_time\":308,\"value\":[[12.5,null,86],[12.5,null,86]]},{\"beg_time\":1505025888,\"step_time\":257,\"value\":[[12.6,null,86],[12.6,null,85]]},{\"beg_time\":1505026452,\"step_time\":308,\"value\":[[12.6,null,85],[12.6,null,85]]},{\"beg_time\":1505027067,\"step_time\":307,\"value\":[[12.6,null,85],[12.6,null,85]]},{\"beg_time\":1505027682,\"step_time\":307,\"value\":[[12.7,null,85],[12.7,null,84]]},{\"beg_time\":1505028247,\"step_time\":307,\"value\":[[12.7,null,84],[12.7,null,84]]},{\"beg_time\":1505028862,\"step_time\":308,\"value\":[[12.8,null,84],[12.9,null,84]]},{\"beg_time\":1505029477,\"step_time\":308,\"value\":[[13,null,83],[13.1,null,82]]},{\"beg_time\":1505030092,\"step_time\":308,\"value\":[[13.1,null,82],[13.1,null,82],[13.2,null,82]]},{\"beg_time\":1505030964,\"step_time\":308,\"value\":[[13.2,null,82],[13.3,null,82]]},{\"beg_time\":1505031579,\"step_time\":307,\"value\":[[13.3,null,82],[13.4,null,83]]},{\"beg_time\":1505032194,\"step_time\":308,\"value\":[[13.6,null,83],[13.7,null,81],[13.8,null,81]]},{\"beg_time\":1505033116,\"step_time\":257,\"value\":[[13.9,null,79],[14,null,79]]},{\"beg_time\":1505033681,\"step_time\":308,\"value\":[[14.2,null,80],[14.3,null,79]]},{\"beg_time\":1505034296,\"step_time\":307,\"value\":[[14.4,null,78],[14.5,null,78]]},{\"beg_time\":1505034912,\"step_time\":307,\"value\":[[14.7,null,78],[15.1,null,79]]},{\"beg_time\":1505035475,\"step_time\":307,\"value\":[[15.3,null,77],[15.7,null,75]]},{\"beg_time\":1505036091,\"step_time\":308,\"value\":[[15.8,null,74],[15.8,null,73]]},{\"beg_time\":1505036705,\"step_time\":308,\"value\":[[15.8,null,73],[15.9,null,73],[16,null,73],[16,null,71]]},{\"beg_time\":1505037936,\"step_time\":256,\"value\":[[16.4,null,73],[16.9,null,71]]},{\"beg_time\":1505038501,\"step_time\":307,\"value\":[[17.5,null,69],[18,null,68]]},{\"beg_time\":1505039116,\"step_time\":308,\"value\":[[18.3,null,66],[18.1,null,65]]},{\"beg_time\":1505039730,\"step_time\":308,\"value\":[[18,null,65],[18.3,null,66]]},{\"beg_time\":1505040345,\"step_time\":257,\"value\":[[19,null,67],[19.5,null,64]]},{\"beg_time\":1505040909,\"step_time\":308,\"value\":[[19.7,null,61],[20,null,63]]},{\"beg_time\":1505041526,\"step_time\":307,\"value\":[[20.5,null,60],[20.8,null,58],[20.8,null,58],[21.2,null,59]]},{\"beg_time\":1505042755,\"step_time\":256,\"value\":[[21.5,null,56],[21.3,null,55]]},{\"beg_time\":1505043320,\"step_time\":308,\"value\":[[21.4,null,56],[21.4,null,55]]},{\"beg_time\":1505043935,\"step_time\":308,\"value\":[[21.4,null,54],[21.5,null,53]]},{\"beg_time\":1505044549,\"step_time\":308,\"value\":[[21.4,null,53],[21.6,null,53],[21.8,null,53]]},{\"beg_time\":1505045421,\"step_time\":308,\"value\":[[21.7,null,52],[21.6,null,53]]},{\"beg_time\":1505046036,\"step_time\":308,\"value\":[[21.4,null,53],[21.3,null,53],[21.2,null,53]]},{\"beg_time\":1505046959,\"step_time\":308,\"value\":[[21,null,54],[20.9,null,53]]},{\"beg_time\":1505047574,\"step_time\":257,\"value\":[[20.8,null,55],[20.9,null,56]]},{\"beg_time\":1505048138,\"step_time\":308,\"value\":[[21.2,null,58],[21.3,null,54],[21.3,null,54]]},{\"beg_time\":1505049061,\"step_time\":308,\"value\":[[21,null,54],[20.7,null,55]]},{\"beg_time\":1505049676,\"step_time\":308,\"value\":[[20.7,null,55],[20.6,null,55]]},{\"beg_time\":1505050240,\"step_time\":308,\"value\":[[20.2,null,55],[20,null,55],[19.7,null,56]]},{\"beg_time\":1505051163,\"step_time\":308,\"value\":[[19.5,null,57],[19.3,null,57]]},{\"beg_time\":1505051778,\"step_time\":308,\"value\":[[19.3,null,58],[19.1,null,58],[19,null,58]]},{\"beg_time\":1505052650,\"step_time\":308,\"value\":[[18.8,null,59],[18.8,null,61]]},{\"beg_time\":1505053265,\"step_time\":308,\"value\":[[18.8,null,60],[18.9,null,60],[18.8,null,61]]},{\"beg_time\":1505054188,\"step_time\":308,\"value\":[[18.7,null,61],[18.6,null,61]]},{\"beg_time\":1505054803,\"step_time\":257,\"value\":[[18.6,null,62],[18.6,null,62]]},{\"beg_time\":1505055367,\"step_time\":308,\"value\":[[18.7,null,62],[18.7,null,62],[18.8,null,62]]},{\"beg_time\":1505056290,\"step_time\":308,\"value\":[[18.8,null,62],[18.8,null,60]]},{\"beg_time\":1505056905,\"step_time\":308,\"value\":[[18.7,null,60],[18.5,null,60]]},{\"beg_time\":1505057469,\"step_time\":308,\"value\":[[18.5,null,61],[18.4,null,62],[18.4,null,62]]},{\"beg_time\":1505058392,\"step_time\":308,\"value\":[[18.3,null,62],[18.3,null,63]]},{\"beg_time\":1505059007,\"step_time\":308,\"value\":[[18.2,null,62],[18.1,null,64],[18.1,null,63]]},{\"beg_time\":1505059879,\"step_time\":308,\"value\":[[18,null,64],[17.9,null,65]]},{\"beg_time\":1505060494,\"step_time\":308,\"value\":[[17.9,null,65],[17.8,null,65],[17.7,null,65]]},{\"beg_time\":1505061417,\"step_time\":308,\"value\":[[17.7,null,66],[17.6,null,66]]},{\"beg_time\":1505062031,\"step_time\":257,\"value\":[[17.6,null,66],[17.5,null,67]]},{\"beg_time\":1505062596,\"step_time\":308,\"value\":[[17.5,null,68],[17.5,null,69],[17.4,null,69]]},{\"beg_time\":1505063519,\"step_time\":103,\"value\":[[17.4,null,70],[17.4,null,70]]},{\"beg_time\":1505063929,\"step_time\":308,\"value\":[[17.4,null,71],[17.3,null,71],[17.2,null,71]]},{\"beg_time\":1505064801,\"step_time\":308,\"value\":[[17.1,null,72],[17.1,null,73]]},{\"beg_time\":1505065416,\"step_time\":307,\"value\":[[17,null,71],[16.9,null,72],[16.9,null,72]]},{\"beg_time\":1505066339,\"step_time\":308,\"value\":[[16.8,null,72],[16.7,null,72]]},{\"beg_time\":1505066954,\"step_time\":307,\"value\":[[16.6,null,72],[16.5,null,73]]},{\"beg_time\":1505067517,\"step_time\":309,\"value\":[[16.4,null,73],[16.3,null,73]]},{\"beg_time\":1505068133,\"step_time\":307,\"value\":[[16.3,null,73],[16.2,null,74]]},{\"beg_time\":1505068748,\"step_time\":308,\"value\":[[16.2,null,74],[16.1,null,74],[16.1,null,73],[16,null,74]]},{\"beg_time\":1505069928,\"step_time\":307,\"value\":[[15.9,null,74],[15.9,null,74],[15.8,null,75]]},{\"beg_time\":1505070850,\"step_time\":307,\"value\":[[15.7,null,75],[15.6,null,76]]},{\"beg_time\":1505071466,\"step_time\":308,\"value\":[[15.4,null,76],[15.3,null,76]]},{\"beg_time\":1505072081,\"step_time\":257,\"value\":[[15.3,null,77],[15.3,null,77]]},{\"beg_time\":1505072644,\"step_time\":308,\"value\":[[15.3,null,77],[15.2,null,77]]},{\"beg_time\":1505073259,\"step_time\":308,\"value\":[[15.1,null,78],[14.9,null,78],[14.8,null,78]]},{\"beg_time\":1505074182,\"step_time\":309,\"value\":[[14.6,null,79],[14.5,null,79]]},{\"beg_time\":1505074747,\"step_time\":308,\"value\":[[14.4,null,80],[14.4,null,79]]},{\"beg_time\":1505075362,\"step_time\":307,\"value\":[[14.4,null,80],[14.3,null,80]]},{\"beg_time\":1505075977,\"step_time\":256,\"value\":[[14.2,null,80],[14.1,null,80]]},{\"beg_time\":1505076592,\"step_time\":308,\"value\":[[14,null,81],[14,null,81]]},{\"beg_time\":1505077156,\"step_time\":308,\"value\":[[13.9,null,81],[13.9,null,81]]},{\"beg_time\":1505077771,\"step_time\":308,\"value\":[[13.8,null,81],[13.8,null,81]]},{\"beg_time\":1505078386,\"step_time\":308,\"value\":[[13.7,null,80],[13.6,null,81],[13.5,null,81]]},{\"beg_time\":1505079309,\"step_time\":257,\"value\":[[13.3,null,81],[13.2,null,82]]},{\"beg_time\":1505079873,\"step_time\":308,\"value\":[[13.2,null,82],[13.2,null,82]]},{\"beg_time\":1505080488,\"step_time\":308,\"value\":[[13.2,null,82],[13.1,null,82],[13,null,82]]},{\"beg_time\":1505081411,\"step_time\":308,\"value\":[[12.9,null,82],[12.9,null,82]]},{\"beg_time\":1505081975,\"step_time\":308,\"value\":[[12.8,null,82],[12.7,null,82]]},{\"beg_time\":1505082590,\"step_time\":308,\"value\":[[12.7,null,82],[12.6,null,83],[12.5,null,83]]},{\"beg_time\":1505083513,\"step_time\":308,\"value\":[[12.4,null,83],[12.4,null,83]]},{\"beg_time\":1505084077,\"step_time\":308,\"value\":[[12.3,null,83],[12.3,null,83]]},{\"beg_time\":1505084694,\"step_time\":307,\"value\":[[12.2,null,83],[12.2,null,83],[12.1,null,83],[12.1,null,83]]},{\"beg_time\":1505085923,\"step_time\":308,\"value\":[[12,null,83],[11.9,null,83]]},{\"beg_time\":1505086487,\"step_time\":308,\"value\":[[11.9,null,84],[11.8,null,84]]},{\"beg_time\":1505087102,\"step_time\":308,\"value\":[[11.7,null,84],[11.6,null,84]]},{\"beg_time\":1505087717,\"value\":[[11.5,null,84]]}],\"status\":\"ok\",\"time_exec\":0.085386037826538,\"time_server\":1528321956}\n");
        OAuthJSONAccessTokenResponse token = mock(OAuthJSONAccessTokenResponse.class);
        OAuthClient oAuthClient = mock(OAuthClient.class);
        when(oAuthClient.resource(any(OAuthClientRequest.class), eq(OAuth.HttpMethod.GET), eq(OAuthResourceResponse.class))).thenReturn(response);

        NetatmoHttpClient client = new NetatmoHttpClient("client_id", "client_secret");
        FieldUtils.writeField(client, "oAuthClient", oAuthClient, true);

        List<String> types = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_PRESSURE, Params.TYPE_HUMIDITY);
        Date dateBegin = DateTime.parse("2015-09-10T00Z").toDate();
        Date dateEnd = DateTime.parse("2015-09-11T00Z").toDate();
        Station station = new Station("My Station", "my-station-id");
        Module module = new Module("My Module", "my-module-id", TYPE_INDOOR);


        List<Measures> measures = client.getMeasures(token, station, module, types, Params.SCALE_MAX, dateBegin, dateEnd, null, null);


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
        OAuthResourceResponse response = mock(OAuthResourceResponse.class);
        when(response.getBody()). thenReturn("{\"body\":{\"devices\":[{\"_id\":\"70:xx:xx:xx:xx:xx\",\"cipher_id\":\"enc:16:whatthefuckisthisshitinhere\",\"date_setup\":1366148563,\"last_setup\":1366148563,\"type\":\"NAMain\",\"last_status_store\":1528323455,\"module_name\":\"Innen\",\"firmware\":132,\"last_upgrade\":1439972537,\"wifi_status\":38,\"co2_calibrating\":false,\"station_name\":\"Station Name\",\"data_type\":[\"Temperature\",\"CO2\",\"Humidity\",\"Noise\",\"Pressure\"],\"place\":{\"altitude\":453,\"city\":\"Zurich\",\"country\":\"CH\",\"timezone\":\"Europe/Zurich\",\"location\":[8.4,47.4]},\"dashboard_data\":{\"time_utc\":1528323437,\"Temperature\":24.6,\"CO2\":751,\"Humidity\":58,\"Noise\":37,\"Pressure\":1014.9,\"AbsolutePressure\":961.6,\"min_temp\":24.6,\"max_temp\":24.6,\"date_min_temp\":1528322530,\"date_max_temp\":1528322530,\"temp_trend\":\"stable\",\"pressure_trend\":\"stable\"},\"modules\":[{\"_id\":\"02:xx:xx:xx:xx:xx\",\"type\":\"NAModule1\",\"module_name\":\"Außen\",\"data_type\":[\"Temperature\",\"Humidity\"],\"last_setup\":1366146161,\"dashboard_data\":{\"time_utc\":1489172240,\"Temperature\":8.3,\"Humidity\":100,\"min_temp\":8.3,\"max_temp\":33.2,\"date_min_temp\":1489172240,\"date_max_temp\":1489150204},\"firmware\":44,\"last_message\":1489172253,\"last_seen\":1489172240,\"rf_status\":91,\"battery_vp\":3744,\"battery_percent\":6},{\"_id\":\"05:xx:xx:xx:xx:xx\",\"type\":\"NAModule3\",\"module_name\":\"Niederschlagsmesser\",\"data_type\":[\"Rain\"],\"last_setup\":1447784487,\"dashboard_data\":{\"time_utc\":1476015169,\"Rain\":0.202,\"sum_rain_24\":0.7},\"firmware\":8,\"last_message\":1476015175,\"last_seen\":1476015169,\"rf_status\":63,\"battery_vp\":5270,\"battery_percent\":70},{\"_id\":\"03:xx:xx:xx:xx:xx\",\"type\":\"NAModule4\",\"module_name\":\"Schlafzimmer\",\"data_type\":[\"Temperature\",\"CO2\",\"Humidity\"],\"last_setup\":1459448955,\"dashboard_data\":{\"time_utc\":1528323426,\"Temperature\":23.3,\"CO2\":443,\"Humidity\":60,\"min_temp\":23.2,\"max_temp\":23.3,\"date_min_temp\":1528322504,\"date_max_temp\":1528323426,\"temp_trend\":\"up\"},\"firmware\":44,\"last_message\":1528323451,\"last_seen\":1528323426,\"rf_status\":75,\"battery_vp\":5116,\"battery_percent\":51}]}],\"user\":{\"mail\":\"netatmo@email.com\",\"administrative\":{\"country\":\"DE\",\"feel_like_algo\":0,\"lang\":\"de-DE\",\"pressureunit\":0,\"reg_locale\":\"de-CH\",\"unit\":0,\"windunit\":0}}},\"status\":\"ok\",\"time_exec\":0.18290090560913,\"time_server\":1528323598}");
        OAuthJSONAccessTokenResponse token = mock(OAuthJSONAccessTokenResponse.class);
        OAuthClient oAuthClient = mock(OAuthClient.class);
        when(oAuthClient.resource(any(OAuthClientRequest.class), eq(OAuth.HttpMethod.GET), eq(OAuthResourceResponse.class))).thenReturn(response);

        NetatmoHttpClient client = new NetatmoHttpClient("client_id", "client_secret");
        FieldUtils.writeField(client, "oAuthClient", oAuthClient, true);


        List<Station> stations = client.getStationsData(token);

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
}