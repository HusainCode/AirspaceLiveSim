import type { Flight } from '../types/flight';

const API_BASE_URL = 'http://localhost:8080/api';

export class FlightApi {
  static async getAllFlights(): Promise<Flight[]> {
    try {
      const response = await fetch(`${API_BASE_URL}/flights`);
      if (!response.ok) {
        throw new Error(`Failed to fetch flights: ${response.statusText}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error fetching flights:', error);
      throw error;
    }
  }

  static async getFlightById(flightId: string): Promise<Flight> {
    try {
      const response = await fetch(`${API_BASE_URL}/flights/${flightId}`);
      if (!response.ok) {
        throw new Error(`Failed to fetch flight: ${response.statusText}`);
      }
      return await response.json();
    } catch (error) {
      console.error(`Error fetching flight ${flightId}:`, error);
      throw error;
    }
  }

  static async searchFlights(query: string): Promise<Flight[]> {
    try {
      const response = await fetch(
        `${API_BASE_URL}/flights/search?q=${encodeURIComponent(query)}`
      );
      if (!response.ok) {
        throw new Error(`Failed to search flights: ${response.statusText}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error searching flights:', error);
      throw error;
    }
  }
}
