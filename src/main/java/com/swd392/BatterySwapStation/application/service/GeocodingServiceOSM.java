package com.swd392.BatterySwapStation.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class GeocodingServiceOSM {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeocodingServiceOSM(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Lấy latitude và longitude từ địa chỉ bằng OpenStreetMap Nominatim
     * Nếu không tìm thấy, sẽ ném lỗi IllegalArgumentException
     */
    public double[] getLatLon(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống");
        }

        try {
            String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", address)
                    .queryParam("format", "json")
                    .queryParam("limit", 1)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "BatterySwapApp/1.0 (vitle093@gmail.com)");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            log.info("HTTP status: {}", response.getStatusCode());
            log.info("OSM JSON response: {}", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                NominatimResult[] results = objectMapper.readValue(response.getBody(), NominatimResult[].class);
                if (results != null && results.length > 0 && results[0].lat != null && results[0].lon != null) {
                    double lat = Double.parseDouble(results[0].lat);
                    double lon = Double.parseDouble(results[0].lon);
                    log.info("Geocoding success: {} -> lat={}, lon={}", address, lat, lon);
                    return new double[]{lat, lon};
                }
            }
        } catch (Exception e) {
            log.error("Lỗi khi geocoding địa chỉ: {}", address, e);
        }

        throw new IllegalArgumentException("Không tìm thấy tọa độ cho địa chỉ: " + address);
    }

    static class NominatimResult {
        public String lat;
        public String lon;
        public String display_name;
        public String[] boundingbox;
    }
}
