package com.flighttracker.common.dto;

import java.util.List;

public class FlightSearchResponse {

    private List<FlightResponse> flights;
    private int totalResults;
    private int page;
    private int pageSize;
    private String query;

    public FlightSearchResponse() {
    }

    public FlightSearchResponse(List<FlightResponse> flights, int totalResults, int page, int pageSize, String query) {
        this.flights = flights;
        this.totalResults = totalResults;
        this.page = page;
        this.pageSize = pageSize;
        this.query = query;
    }

    public List<FlightResponse> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightResponse> flights) {
        this.flights = flights;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean hasMoreResults() {
        return (page + 1) * pageSize < totalResults;
    }
}
