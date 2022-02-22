package com.poona.agrocart.ui.nav_addresses.map_view;

public interface SimplePlacePicker {

    String PACKAGE_NAME = "com.poona.agrocart";
    String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    String LOCATION_LAT_EXTRA = PACKAGE_NAME + ".LOCATION_lAT_EXTRA";
    String LOCATION_LNG_EXTRA = PACKAGE_NAME + ".LOCATION_LNG_EXTRA";
    String SELECTED_ADDRESS = PACKAGE_NAME + ".SELECTED_ADDRESS";
    String SELECTED_STATE = PACKAGE_NAME + ".SELECTED_STATE";
    String SELECTED_CITY = PACKAGE_NAME + ".SELECTED_CITY";
    String SELECTED_AREA = PACKAGE_NAME + ".SELECTED_AREA";
    String SELECTED_PIN_CODE = PACKAGE_NAME + ".SELECTED_PIN_CODE";
    String SELECTED_HOUSE_NUMBER = PACKAGE_NAME + ".SELECTED_HOUSE_NUMBER";
    String SELECTED_STREET = PACKAGE_NAME + ".SELECTED_STREET";
    String SELECTED_LANDMARK = PACKAGE_NAME + ".SELECTED_LANDMARK";
    String LANGUAGE = PACKAGE_NAME + ".LANGUAGE";

    String API_KEY = PACKAGE_NAME + ".API_KEY";
    String COUNTRY = PACKAGE_NAME + ".COUNTRY";
    String SUPPORTED_AREAS = PACKAGE_NAME + ".SUPPORTED_AREAS";

    int SUCCESS_RESULT = 0;
    int FAILURE_RESULT = 1;
    int SELECT_LOCATION_REQUEST_CODE = 22;
}