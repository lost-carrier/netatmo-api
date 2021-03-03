package losty.netatmo.model;

public class Room {

    public static final String SET_POINT_MODE_MANUAL = "manual";
    public static final String SET_POINT_MODE_MAX = "max";
    public static final String SET_POINT_MODE_OFF = "off";
    public static final String SET_POINT_MODE_SCHEDULE = "schedule";
    public static final String SET_POINT_MODE_AWAY = "away";
    public static final String SET_POINT_MODE_HG = "hg";

    private String id;
    private boolean reachable;
    private float therm_measured_temperature;
    private float therm_setpoint_temperature;
    private String therm_setpoint_mode;
    private Long therm_setpoint_start_time;
    private Long therm_setpoint_end_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public float getTherm_measured_temperature() {
        return therm_measured_temperature;
    }

    public void setTherm_measured_temperature(float therm_measured_temperature) {
        this.therm_measured_temperature = therm_measured_temperature;
    }

    public float getTherm_setpoint_temperature() {
        return therm_setpoint_temperature;
    }

    public void setTherm_setpoint_temperature(float therm_setpoint_temperature) {
        this.therm_setpoint_temperature = therm_setpoint_temperature;
    }

    public String getTherm_setpoint_mode() {
        return therm_setpoint_mode;
    }

    public void setTherm_setpoint_mode(String therm_setpoint_mode) {
        this.therm_setpoint_mode = therm_setpoint_mode;
    }

    public Long getTherm_setpoint_start_time() {
        return therm_setpoint_start_time;
    }

    public void setTherm_setpoint_start_time(Long therm_setpoint_start_time) {
        this.therm_setpoint_start_time = therm_setpoint_start_time;
    }

    public long getTherm_setpoint_end_time() {
        return therm_setpoint_end_time;
    }

    public void setTherm_setpoint_end_time(Long therm_setpoint_end_time) {
        this.therm_setpoint_end_time = therm_setpoint_end_time;
    }
}
