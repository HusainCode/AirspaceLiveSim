export type FlightStatus =
  | 'SCHEDULED'
  | 'BOARDING'
  | 'DEPARTED'
  | 'IN_FLIGHT'
  | 'LANDED'
  | 'CANCELLED'
  | 'DELAYED';

export interface Flight {
  flightId: string;
  flightNumber: string;
  callsign: string;

  departureAirportIcao: string;
  destinationAirportIcao: string;

  departureAirportName: string;
  destinationAirportName: string;

  scheduledDepartureTime: string;
  actualDepartureTime: string;

  scheduledArrivalTime: string;
  estimatedArrivalTime: string;

  latitude: number;
  longitude: number;
  altitude: number;
  speed: number;

  status: FlightStatus;
  lastUpdated: number;
}

export interface FlightPosition {
  latitude: number;
  longitude: number;
  altitude: number;
}
