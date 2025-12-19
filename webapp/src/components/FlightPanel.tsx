import type { Flight } from '../types/flight';
import './FlightPanel.css';

interface FlightPanelProps {
  flights: Flight[];
  onFlightSelect?: (flight: Flight) => void;
}

export const FlightPanel: React.FC<FlightPanelProps> = ({
  flights,
  onFlightSelect,
}) => {
  return (
    <div className="flight-panel">
      <div className="flight-panel-header">
        <h2>Active Flights</h2>
        <span className="flight-count">{flights.length}</span>
      </div>
      <div className="flight-list">
        {flights.length === 0 ? (
          <div className="no-flights">No active flights</div>
        ) : (
          flights.map((flight) => (
            <div
              key={flight.flightId}
              className="flight-item"
              onClick={() => onFlightSelect?.(flight)}
            >
              <div className="flight-number">
                {flight.flightNumber || flight.callsign}
              </div>
              <div className="flight-route">
                <span className="airport">
                  {flight.departureAirportIcao}
                </span>
                <span className="arrow">→</span>
                <span className="airport">
                  {flight.destinationAirportIcao}
                </span>
              </div>
              <div className="flight-details">
                <span className="detail">
                  Alt: {Math.round(flight.altitude)}m
                </span>
                <span className="detail">
                  Speed: {Math.round(flight.speed)}km/h
                </span>
              </div>
              <div className={`flight-status status-${flight.status.toLowerCase()}`}>
                {flight.status}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};
